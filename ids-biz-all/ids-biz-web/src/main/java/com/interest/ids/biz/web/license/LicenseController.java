package com.interest.ids.biz.web.license;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.interest.ids.biz.web.license.service.ILicenseService;
import com.interest.ids.biz.web.license.vo.LicenseInfoVo;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;

/**
 * License管理请求处理
 * 
 * @author zl
 * @date 2018-5-29
 */

@Controller
@RequestMapping("/license")
public class LicenseController {

    @Autowired
    private ILicenseService licenseService;

    @RequestMapping(value = "/licenseImport")
    @ResponseBody
    public Response<String> licenseImport(@RequestParam(value = "file") MultipartFile file, HttpSession session) {
        Response<String> response = new Response<>();

        if (!licenseService.validateFileType(file.getOriginalFilename())) {
            response.setCode(ResponseConstants.CODE_FILETYPE_NOTMATCH);
            response.setMessage(ResponseConstants.CODE_FILETYPE_NOTMATCH_VALUE);
            return response;
        }

        Object obj = session.getAttribute("user");
        if (obj != null) {
            UserInfo user = (UserInfo) obj;
            if (licenseService.storeLicenseFile(user, file)) {
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
            } else {
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            }
        }

        return response;
    }

    @RequestMapping(value = "/getLicenseInfo")
    @ResponseBody
    public Response<LicenseInfoVo> getLicenseInfo() {
        Response<LicenseInfoVo> response = new Response<>();

        response.setCode(ResponseConstants.CODE_SUCCESS);
        response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
        response.setResults(licenseService.getLicenseInfo());

        return response;
    }
}
