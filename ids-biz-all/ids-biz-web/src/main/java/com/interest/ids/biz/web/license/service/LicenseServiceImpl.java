package com.interest.ids.biz.web.license.service;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.interest.ids.biz.web.license.vo.LicenseInfoVo;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.common.project.utils.MathUtil;
import com.interest.ids.commoninterface.service.device.IDevPVCapacityService;
import com.interest.ids.commoninterface.service.device.IDeviceInfoService;
import com.interest.ids.decrypt.DESDecrypter;
import com.interest.ids.decrypt.RunEnvTool;

@Service
public class LicenseServiceImpl implements ILicenseService {

    public static final String KEY = "INTEREST@IDS@2017-09";

    public static final int LICENSE_CORE_NUM = 3;

    public static final String VALIDE_SUFF = ".dat";

    public static final String FILENAME_SEPERATOR = "_";

    public static final int FILENAME_LENTH = 6;

    private static final Logger logger = LoggerFactory.getLogger(LicenseServiceImpl.class);

    @Autowired
    private IDeviceInfoService devService;

    @Autowired
    private IDevPVCapacityService devPvCapService;

    @Override
    public boolean storeLicenseFile(UserInfo user, MultipartFile mFile) {
        // 比要条件不满足时，返回导入失败标识
        if (user == null || mFile == null || mFile.getSize() <= 0) {
            return false;
        }

        // 先判断给定存放目录是否存在，不存在则创建
        String filePath = getLicenseBasePath();
        File baseDir = new File(filePath);
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }

        String tempFilePath = baseDir.getAbsolutePath() + File.separator + mFile.getOriginalFilename();
        File tempFile = new File(tempFilePath);

        // 临时存入License文件
        try {
            String existedLicense = getExistedLicenseFilePath();

            mFile.transferTo(tempFile);

            // 解析临时License文件

            DESDecrypter decrypter = new DESDecrypter(tempFilePath, KEY);
            List<String> licenseCore = decrypter.doDecrypt();
            if (licenseCore != null && licenseCore.size() == LICENSE_CORE_NUM) {
                String macAddr = licenseCore.get(0);
                if (!RunEnvTool.macAddrExist(macAddr)) {
                    logger.warn("Mac address[{}] doesn't match.", macAddr);
                    return false;
                }

                Integer totalDevNum = MathUtil.formatInteger(licenseCore.get(1), 0);
                Double totalCapacity = MathUtil.formatDouble(licenseCore.get(2), 0d);
                if (totalDevNum <= 0 || totalCapacity <= 0d) {
                    logger.warn("limitted deviceNum:{} or capacity:{} is not correct.", totalDevNum, totalCapacity);
                    return false;
                }

                // 获取系统已接入逆变器数量
                int installedDevNum = getInstalledDevNum();
                // 已接入的容量
                Double installedCap = devPvCapService.countAllInstalledCapacity();
                
                // 如果license已经导入过，判断新导入的license 限制数是否小于已接入的数量及容量
                if (existedLicense != null) {
                    String[] licenseInfo = existedLicense.substring(0, existedLicense.lastIndexOf(".")).split(
                            FILENAME_SEPERATOR);
                    // 文件名格式：
                    // imporDate_importUser_totalDevNum_installedDevNum_totalCapacity_installedCapacity
                    if (licenseInfo.length == FILENAME_LENTH) {
                        if (installedDevNum >= totalDevNum || installedCap >= totalCapacity) {
                            logger.warn(
                                    "the installed devices number:{} or capacity:{} is larger than new license limitted.",
                                    installedDevNum, installedCap);
                            return false;
                        }
                        
                        new File(existedLicense).delete();
                    }
                }

                List<String> fileNamePiece = new ArrayList<>();
                fileNamePiece.add(String.valueOf(System.currentTimeMillis()));
                fileNamePiece.add(user.getUserName());
                fileNamePiece.add(String.valueOf(totalDevNum));
                fileNamePiece.add(String.valueOf(installedDevNum));
                fileNamePiece.add(String.valueOf(totalCapacity));
                fileNamePiece.add(String.valueOf(installedCap));
                String licenseFileName = StringUtils.join(fileNamePiece, FILENAME_SEPERATOR) + VALIDE_SUFF;

                File finalFile = new File(baseDir.getAbsolutePath() + File.separator + licenseFileName);
                tempFile.renameTo(finalFile);
                tempFile = null;
                return true;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            // 删除临时文件
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }

        return false;
    }

    @Override
    public LicenseInfoVo getLicenseInfo() {
        LicenseInfoVo licenseInfoVo = null;

        if (getExistedLicenseFilePath() != null) {
            File license = new File(getExistedLicenseFilePath());
            if (license.exists() && license.isFile()) {
                String fileName = license.getName();
                fileName = fileName.substring(0, fileName.lastIndexOf("."));
                String[] namePieces = fileName.split(FILENAME_SEPERATOR);
                if (namePieces != null && namePieces.length == 6) {
                    licenseInfoVo = new LicenseInfoVo();
                    licenseInfoVo.setImportDate(MathUtil.formatLong(namePieces[0]));
                    licenseInfoVo.setImportUser(namePieces[1]);
                    licenseInfoVo.setTotalDevNum(MathUtil.formatInteger(namePieces[2]));
                    
                    // 获取系统已接入逆变器数量
                    int installedDevNum = getInstalledDevNum();
                    // 已接入的容量
                    Double installedCap = devPvCapService.countAllInstalledCapacity();
                    
                    licenseInfoVo.setInstalledDevNum(installedDevNum);
                    licenseInfoVo.setTotalCapacity(MathUtil.formatDouble(namePieces[4]));
                    licenseInfoVo.setInstalledCapacity(installedCap);
                }
            }
        }

        return licenseInfoVo;
    }

    @Override
    public boolean validateFileType(String fileName) {
        boolean result = false;

        if (CommonUtil.isNotEmpty(fileName) && fileName.length() > 4) {
            String suff = fileName.substring(fileName.length() - 4, fileName.length());
            result = suff.equals(VALIDE_SUFF);
        }

        return result;
    }

    /**
     * license 文件存放路径
     * 
     * @return
     */
    private String getLicenseBasePath() {
        String filePath = "/srv/fileManager/license";// 默认文件在linux系统上的存储路劲
        if ("win".equals(CommonUtil.whichSystem())) {
            filePath = "C:/fileManager/license";
        }

        return filePath;
    }

    /**
     * 获取已导入的License文件完整路径
     * 
     * @return
     */
    private String getExistedLicenseFilePath() {
        File licenseFile = null;

        String basePath = getLicenseBasePath();
        File dir = new File(basePath);
        if (dir.exists()) {
            File[] existedLicense = dir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String fileName) {

                    return validateFileType(fileName);
                }
            });

            if (existedLicense != null && existedLicense.length == 1) {
                licenseFile = existedLicense[0];
            }
        }

        return licenseFile == null ? null : licenseFile.getAbsolutePath();
    }

    @Override
    public LicenseInfoVo parseLicenseFile() {
        LicenseInfoVo result = new LicenseInfoVo();

        String existedLicense = getExistedLicenseFilePath();
        DESDecrypter decrypter = new DESDecrypter(existedLicense, KEY);
        List<String> licenseCore = decrypter.doDecrypt();
        if (licenseCore != null && licenseCore.size() == LICENSE_CORE_NUM) {
            Integer totalDevNum = MathUtil.formatInteger(licenseCore.get(1), 0);
            Double totalCapacity = MathUtil.formatDouble(licenseCore.get(2), 0d);
            result.setTotalDevNum(totalDevNum);
            result.setTotalCapacity(totalCapacity);
        } else {
            result.setTotalDevNum(0);
            result.setTotalCapacity(0d);
        }

        return result;
    }

    @Override
    public boolean beyondDevNumLimit(int newDevNum) {
        // 获取系统已接入逆变器数量
        int installedDevNum = getInstalledDevNum();
        int expectInstalledDevNum = installedDevNum + newDevNum;
        LicenseInfoVo licenseInfo = parseLicenseFile();

        return expectInstalledDevNum > licenseInfo.getTotalDevNum();
    }

    @Override
    public boolean beyondCapacityLimit(double newCapacity) {
        // 获取系统已接入组串容量
        Double installedCap = devPvCapService.countAllInstalledCapacity();
        double expectInstalledCap = installedCap + newCapacity;
        LicenseInfoVo licenseInfo = parseLicenseFile();

        return expectInstalledCap > licenseInfo.getTotalCapacity();
    }

    /**
     * 获取系统已接入的设备数量
     * 
     * @return
     */
    private int getInstalledDevNum() {
        List<DeviceInfo> allDevice = devService.queryAllDeviceInfoMs();
        int installedDevNum = 0;
        for (DeviceInfo device : allDevice) {
            if (device.getDevTypeId() == DevTypeConstant.INVERTER_DEV_TYPE
                    || device.getDevTypeId() == DevTypeConstant.CENTER_INVERT_DEV_TYPE
                    || device.getDevTypeId() == DevTypeConstant.HOUSEHOLD_INVERTER_DEV_TYPE) {
                installedDevNum++;
            }
        }

        return installedDevNum;
    }
}
