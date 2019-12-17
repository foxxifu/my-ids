package com.interest.ids.gatekeeper.server.parse;

import io.netty.util.collection.IntObjectHashMap;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;
import com.interest.ids.common.project.buffer.UnSafeHeapBuffer;
import com.interest.ids.dev.api.utils.OsUtils;
import com.interest.ids.dev.api.utils.OsUtils.OsType;
import com.interest.ids.gatekeeper.server.utils.GateKeeperConstant;

/**
 * 穿网闸消息解析类
 * 
 * @author lhq
 *
 */
public class ProtocolParse {

	private static final Logger log = LoggerFactory.getLogger(ProtocolParse.class);

	private static final ProtocolParse parser = new ProtocolParse();

	/**
	 * 单例模式
	 * 
	 * @return ProtocolParse
	 */
	public static ProtocolParse getParser() {
		return parser;
	}

	/**
	 * id对应传输文件信息
	 */
	private IntObjectHashMap<DataFileInfoBean> files = new IntObjectHashMap<DataFileInfoBean>();

	/**
	 * 文件流传输过渡文件
	 */
	private static final String FILE_TMP_TAIL = ".tmp";

	// private final static ThreadPoolExecutor pool = new NatureThreadExecutor(1,
	// 10, 2048, new NamedThreadFactory("parse_file"));

	/**
	 * 解析消息
	 * 
	 * @param buffer
	 * 			UnSafeHeapBuffer
	 */
	public void parse(final UnSafeHeapBuffer buffer) {

		buffer.readUnsignedShort(); // 读取信息让读取游标增长 totalLength
		// 命令类型
		final byte cmd = buffer.readByte();
		// 创建文件的优先级较高，严格来说要按照优先级来排序
		if (cmd == 2) {
			createFile(buffer);
		} else {
			/** 可以后期考虑采用多线程解析，但必须考虑一个文件只能由一个线程顺序执行，保证文件的正确性 */
			// Runnable r = new Runnable() {
			//
			// @Override
			// public void run() {
			// try {
			parse0(buffer, cmd);
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// }
			// };
			// ThreadPoolUtil.getInstance().addRunnableTask(r);
		}
	}

	/**
	 * 根据第一次传递的消息来创建空文件
	 * 
	 * @param buffer
	 * 			UnSafeHeapBuffer
	 */
	private void createFile(final UnSafeHeapBuffer buffer) {

		// 数据类型
		byte dataType = buffer.readByte();
		buffer.readUnsignedShort();// 读取信息让读取游标增长 frameLength
		int sessionId = buffer.readUnsignedShort();
		buffer.readUnsignedInt();// 读取信息让读取游标增长 fileLength
		int fileNameLength = buffer.readUnsignedShort();
		byte[] fileNameData = new byte[fileNameLength];
		buffer.readBytes(fileNameData);
		String fileName = new String(fileNameData);
		// 创建文件
		File tmpFile = new File(getExportPath(fileName) + FILE_TMP_TAIL);
		DataFileInfoBean bean = new DataFileInfoBean(tmpFile.getAbsolutePath(),
				dataType, sessionId);
		log.info("creat file::" + fileName);
		if (!tmpFile.exists()) {
			try {
				tmpFile.createNewFile();

				files.put(sessionId, bean);
			} catch (IOException e) {
				log.error("create file error", e);
			}
		} else {
			files.put(sessionId, bean);
		}
	}

	/**
	 * 解析文件流消息
	 * 
	 * @param buffer
	 * 			UnSafeHeapBuffer
	 * @param cmd
	 * 			命令行
	 */
	private void parse0(UnSafeHeapBuffer buffer, byte cmd) {
		/*
		 * //数据长度 int totalLength = buffer.readUnsignedShort(); //命令类型 byte cmd
		 * = buffer.readByte();
		 */

		// socket数据
		if (cmd == 1) {
			// 数据类型
			byte dataType = buffer.readByte();
			int dataLength = buffer.readUnsignedShort(); // 数据长度
			byte[] data = new byte[dataLength];
			buffer.readBytes(data); // 数据内容
			parseSocketData(dataType, data);
			return;
		}
		// 数据传输命令
		if (cmd == 3) {
			// 数据类型
			byte dataType = buffer.readByte();
			// 当前帧数
			int frameNum = buffer.readUnsignedShort();
			// 总帧数
			int totalFrame = buffer.readUnsignedShort();
			// sessionId
			int sessionId = buffer.readUnsignedShort();
			// 数据长度
			int dataLen = buffer.readUnsignedShort();
			byte[] data = new byte[dataLen];
			buffer.readBytes(data);
			String fileName = getFile(sessionId);
			if (fileName == null) {
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (fileName != null) {
				try {
					File f = new File(fileName);
					boolean b = f.exists();
					if (b) {
						RandomAccessFile randomFile = new RandomAccessFile(f,
								"rw");
						randomFile.seek(f.length());
						randomFile.write(data);
						if (frameNum == totalFrame) {
							// 重命名文件
							String name = fileName.substring(0,
									fileName.lastIndexOf(".tmp"));
							File realFile = new File(name);
							Files.copy(f, realFile);
							randomFile.close();
							f.delete();
							// 解析文件
							parseFileData(dataType, realFile);
						} else {
							randomFile.close();
						}
					}
				} catch (Exception e) {
					log.error("write file error", e);
				}
			}
		}
	}

	/**
	 * 根据文件名称获取文件路径
	 * 
	 * @param fileName
	 * 			fileName
	 * @return String
	 */
	public static String getExportPath(String fileName) {
		OsType os = OsUtils.getOsType();
		if (os == OsType.LINUX) {
			String filePath = GateKeeperConstant.FILE_DIR + File.separator
					+ fileName;
			return filePath;
		} else {
			String filePath = System.getProperty("user.dir") + File.separator
					+ "tmp";
			File f = new File(filePath);
			if (!f.exists()) {
				synchronized (ProtocolParse.class) {
					if (!f.exists())
						f.mkdir();
				}
			}
			String path = filePath + File.separator + fileName;
			filePath = path.replace("\\", "/");
			return filePath;
		}
	}

	/**
	 * 解析socket数据
	 * 
	 * @param dataType
	 * 			byte
	 * @param data
	 * 			byte[]
	 */
	private void parseSocketData(byte dataType, byte[] data) {

		SocketDataParse parser = GateKeeperConstant.getParser(dataType);
		parser.parse(data);
	}

	/**
	 * 解析文件流数据
	 * 
	 * @param dataType
	 * 			byte
	 * @param file
	 * 			File
	 */
	private void parseFileData(byte dataType, File file) {
		if (dataType == GateKeeperConstant.SERVICEDATA_TYPE) {
			new ServiceDataParser().parse(file);
		} else {
			new DefaultFileParser(dataType).parse(file);
		}
	}

	/**
	 * 根据SESSIONID查找文件
	 * 
	 * @param sessionid
	 * 			sessionid
	 * @return String
	 */
	private String getFile(int sessionid) {

		String fileName = files.get(sessionid).getFileName();
		return fileName;
	}
}
