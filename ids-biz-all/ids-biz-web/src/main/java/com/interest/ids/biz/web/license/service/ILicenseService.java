package com.interest.ids.biz.web.license.service;

import org.springframework.web.multipart.MultipartFile;

import com.interest.ids.biz.web.license.vo.LicenseInfoVo;
import com.interest.ids.common.project.bean.sm.UserInfo;

public interface ILicenseService {

    /**
     * 存储license文件
     * @param user
     * @param filePath
     * @param file
     * @return
     */
    boolean storeLicenseFile(UserInfo user, MultipartFile file);
    
    /**
     * 查询License基本信息
     * @return
     */
    LicenseInfoVo getLicenseInfo();
    
    /**
     * 验证文件类型是否制定License格式
     * @param fileName
     * @return
     */
    boolean validateFileType(String fileName);

    /**
     * 解析License, 获得闲置设备数及接入容量
     * @return
     */
    LicenseInfoVo parseLicenseFile();
    
    /**
     * 是否超过License可接入设备限制数
     * @param newDevNum 待接入的逆变器数
     * @return
     */
    boolean beyondDevNumLimit(int newDevNum);
    
    /**
     * 是否超过License可接入组串容量数
     * @param newCapacity 待配置的组串总容量
     * @return
     */
    boolean beyondCapacityLimit(double newCapacity);
}
