package com.interest.ids.dev.network.modbus.command;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.buffer.UnSafeHeapBuffer;
import com.interest.ids.dev.network.modbus.message.MessageBuilder;
import com.interest.ids.dev.network.modbus.message.MessageSender;
import com.interest.ids.dev.network.modbus.message.ModbusMessage;
import com.interest.ids.dev.network.modbus.utils.MD5;
import com.interest.ids.dev.network.modbus.utils.ModbusUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class FileTransferCmd {

    private static final Logger log = LoggerFactory.getLogger(FileTransferCmd.class);
    public static final byte FileTransfer_CMD = 0x41;
    public static final byte FileTransfer_CMD_PRE = 0x01;
    public static final byte FileTransfer_CMD_GET = 0x02;
    public static final byte FileTransfer_CMD_PRE_SEND = 0x03;
    public static final byte FileTransfer_CMD_SEND = 0x04;
    public static final byte FileTransfer_CMD_END_SEND = 0x05;

    /**
     * 准备取得文件
     *
     * @param dev
     * @param fileName
     */
    public static void fileGetPre(DeviceInfo dev, String fileName) {
        byte[] fileNameArray = fileName.getBytes();
        int size = fileNameArray.length;
        ModbusMessage msg = MessageBuilder.buildMessage(dev.getSnCode(), dev.getSecondAddress(), null, true);
        UnSafeHeapBuffer buffer = ModbusUtils.packageUnSafeHeapBuffer(3 + 2 + size + 1, dev.getSecondAddress(), FileTransfer_CMD, FileTransfer_CMD_PRE, dev);
        buffer.writeShort(size);
        buffer.writeBytes(fileNameArray);
        buffer.writeByte(0);
        msg.setRequest(buffer);
        msg.setTimeOut(30 * 1000);
        UnSafeHeapBuffer responce = MessageSender.sendSyncMsg(msg);
        responce.skipBytes(3);
        int fileNum = responce.readShort();
        int totalBytes = responce.readInt();
        int pageBytes = responce.readShort();
        int checkType = responce.readByte();
        int checkDateNum = responce.readShort();
        String checkInfo = new String(responce.readBytes(checkDateNum)).trim();
        log.info("checkInfo is :" + checkInfo + " total size is " + totalBytes + " page size is " + pageBytes + " fileNum is " + fileNum + " checkType is " + checkType);
        fileGet(dev, totalBytes, pageBytes, fileNum, fileName);
    }

    /**
     * 取得文件内容
     */
    public static void fileGet(DeviceInfo dev, int totalSize, int pageSize, int fileNum, String fileName) {
        int pageCount = (totalSize + pageSize - 1) / pageSize;
        byte[] totalBytes = new byte[totalSize];
        for (int i = 0; i < pageCount; i++) {
            int index = i * pageSize;
            ModbusMessage msg = MessageBuilder.buildMessage(dev.getSnCode(), dev.getSecondAddress(), null, true);
            UnSafeHeapBuffer buffer = ModbusUtils.packageUnSafeHeapBuffer(11, dev.getSecondAddress(), FileTransfer_CMD, FileTransfer_CMD_GET, dev);
            buffer.writeShort(fileNum);
            buffer.writeInt(index);
            if (index + pageSize > totalSize) {
                buffer.writeShort(totalSize - index);
            } else {
                buffer.writeShort(pageSize);
            }
            msg.setRequest(buffer);
            UnSafeHeapBuffer responce = MessageSender.sendSyncMsg(msg);
            responce.skipBytes(3);
            int length = responce.readShort();
            byte[] result = responce.readBytes(length);
            System.arraycopy(result, 0, totalBytes, index, result.length);
            String resultStr = new String(result).trim();
            log.info("result is :" + resultStr);
        }
        String filetPath = "/srv/fileManager/" + fileName;
        byteToFile(totalBytes, filetPath);
        log.info("get file sucess...");
    }

    /**
     * 准备发送文件
     *
     * @param dev
     * @param fileName
     */
    public static Integer sendFile(DeviceInfo dev, String fileName) {
        log.info("dev is .." + dev.getSnCode() + " fileName is :" + fileName);
        String path = fileName;
        String fileRealName=fileName.substring(fileName.lastIndexOf("/")+1);
        File file = new File(path);
        byte[] fileNameArray = fileRealName.getBytes();
        int size = fileNameArray.length;
        ModbusMessage msg = MessageBuilder.buildMessage(dev, null, true);
        UnSafeHeapBuffer buffer = ModbusUtils.packageUnSafeHeapBuffer(3 + 2 + size + 24, dev.getSecondAddress(), FileTransfer_CMD, FileTransfer_CMD_PRE_SEND, dev);
        buffer.writeShort(size + 1);
        buffer.writeBytes(fileNameArray);
        buffer.writeByte(0);
        buffer.writeInt(Integer.valueOf(String.valueOf(file.length())));
        buffer.writeByte(0);
        buffer.writeShort(16);
        buffer.writeBytes(MD5.getMD5Bytes(fileToBytes(path)));
        msg.setRequest(buffer);
        UnSafeHeapBuffer responce = MessageSender.sendSyncMsg(msg);
        responce.skipBytes(3);
        int number = responce.readShort();
        int transferMod = responce.readByte();
        int maxNoAnswer = responce.readShort();
        int pageSize = responce.readShort();
        log.info("file send ..." + number + " pageSize is " + pageSize);
        sendFileContent(dev, fileName, number, pageSize, transferMod == 1, maxNoAnswer);
        return number;
    }

    /**
     * 发送文件内容
     *
     * @param dev
     * @param fileName
     * @param number
     * @param pageSize
     * @param transferMod
     * @param maxNoAnswer
     */
    private static void sendFileContent(DeviceInfo dev, String fileName, int number, int pageSize, boolean transferMod, int maxNoAnswer) {
        log.info("file send ..." + number + " pageSize is " + pageSize + " transferMod is " + transferMod + " maxNoAnswer is " + maxNoAnswer);
        byte[] totalBytes = fileToBytes(fileName);
        int totalSize = totalBytes.length;
        int pageCount = (totalSize + pageSize - 1) / pageSize;
        for (int i = 0; i < pageCount; i++) {
            int index = i * pageSize;
            int count = pageSize;
            if (index + pageSize > totalSize) {
                count = totalSize - index;
            }
            log.info("count is..." + count);
            boolean needResponce = false;
            if (transferMod && maxNoAnswer != 0 && ((i + 1) % maxNoAnswer == 0 || i == pageCount - 1)) {
                needResponce = true;
            }

            ModbusMessage msg = MessageBuilder.buildMessage(dev, null, needResponce);
            UnSafeHeapBuffer buffer = ModbusUtils.packageUnSafeHeapBuffer(12 + count, dev.getSecondAddress(), FileTransfer_CMD, FileTransfer_CMD_SEND, dev);
            //是否需要应答
            buffer.writeByte(needResponce ? 1 : 0);
            buffer.writeShort(number);
            buffer.writeInt(index);
            buffer.writeShort(count);
            byte[] sendArray = new byte[count];
            System.arraycopy(totalBytes, index, sendArray, 0, count);
            buffer.writeBytes(sendArray);
            msg.setRequest(buffer);
            if (needResponce) {
                UnSafeHeapBuffer responce = MessageSender.sendSyncMsg(msg);
                responce.skipBytes(3);
                int reNumber = responce.readShort();
                int sendAddress = responce.readInt();
                log.info("file send ..." + reNumber + " pageSize is " + sendAddress);
            } else {
                MessageSender.sendAsynMsg(msg);
            }
        }
        endFileSend(dev, number);
    }

    /**
     * 结束发送文件
     *
     * @param dev
     * @param number
     */
    private static void endFileSend(DeviceInfo dev, int number) {
        log.info("end file dev is .." + dev.getSnCode());
        ModbusMessage msg = MessageBuilder.buildMessage(dev, null, true);
        UnSafeHeapBuffer buffer = ModbusUtils.packageUnSafeHeapBuffer(6, dev.getSecondAddress(), FileTransfer_CMD, FileTransfer_CMD_END_SEND, dev);
        buffer.writeShort(number);
        buffer.writeByte(0);
        msg.setRequest(buffer);
        MessageSender.sendSyncMsg(msg);
    }

    /**
     * 文件转换为字符串
     *
     * @param path
     * @return
     */
    private static byte[] fileToBytes(String path) {
        File file = new File(path);
        byte[] by = new byte[Integer.valueOf(String.valueOf(file.length()))];
        try {
            InputStream is = new FileInputStream(file);
            ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
            byte[] bb = new byte[2048];
            int ch;
            ch = is.read(bb);
            while (ch != -1) {
                bytestream.write(bb, 0, ch);
                ch = is.read(bb);
            }
            by = bytestream.toByteArray();
        } catch (Exception e) {
            log.error("file to byte error...", e);
        }
        return by;
    }


    /**
     * 二进制转化为文件
     *
     * @param contents
     * @param filePath
     */
    private static void byteToFile(byte[] contents, String filePath) {
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream output = null;
        try {
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(contents);
            bis = new BufferedInputStream(byteInputStream);
            File file = new File(filePath);
            // 获取文件的父路径字符串
            File path = file.getParentFile();
            if (!path.exists()) {
                log.info("文件夹不存在，创建。path={}", path);
                boolean isCreated = path.mkdirs();
                if (!isCreated) {
                    log.error("创建文件夹失败，path={}", path);
                }
            }
            fos = new FileOutputStream(file);
            // 实例化OutputString 对象
            output = new BufferedOutputStream(fos);
            byte[] buffer = new byte[1024];
            int length = bis.read(buffer);
            while (length != -1) {
                output.write(buffer, 0, length);
                length = bis.read(buffer);
            }
            output.flush();
        } catch (Exception e) {
            log.error("输出文件流时抛异常，filePath={}", filePath, e);
        } finally {
            try {
                bis.close();
                fos.close();
                output.close();
            } catch (IOException e0) {
                log.error("文件处理失败，filePath={}", filePath, e0);
            }
        }
    }


}

