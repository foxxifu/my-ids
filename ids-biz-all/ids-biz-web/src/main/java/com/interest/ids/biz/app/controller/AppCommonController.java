package com.interest.ids.biz.app.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.interest.ids.biz.data.service.operation.IOperatorPositionService;
import com.interest.ids.common.project.bean.operation.OperatorPositionM;
import com.interest.ids.common.project.bean.sm.RoleInfo;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;

@RequestMapping("/app/common")
@Controller
public class AppCommonController {

    private static final Logger logger = LoggerFactory.getLogger(AppCommonController.class);

    @Resource
    private IOperatorPositionService operatorPositionService;

    /**
     * 报表查询
     * 
     * @return
     */
    @RequestMapping(value = "/realLocation", method = RequestMethod.POST)
    @ResponseBody
    public Response<String> realLocation(@RequestBody OperatorPositionM position, HttpSession session) {
        Response<String> response = new Response<>();

        logger.info("operator location: {}", position);

        try {
            if (position != null) {
                UserInfo user = (UserInfo)session.getAttribute("user");
                if (user == null) {
                    response.setCode(ResponseConstants.CODE_FAILED);
                    response.setMessage("can't get user from HttpSession");
                    return response;
                }
                
                OperatorPositionM oldPosition = operatorPositionService.queryOperatorPosition(user.getId());
                if (oldPosition == null) {
                    // 只有用户为运维人员此种角色，才保存位置信息
                    List<RoleInfo> roles = user.getRoles();
                    if (roles == null || roles.size() == 0) {
                        logger.warn("user is not an operator, no need store location.");
                    }
                    
                    for (RoleInfo role : roles) {
                        if (role.getId() == 3) {
                            position.setUserId(user.getId());
                            position.setUserName(user.getUserName());
                            position.setEnterpriseId(user.getEnterpriseId());
                            position.setLoginStatus((byte) 0);

                            operatorPositionService.saveOperatorPosition(position);
                            break;
                        }else {
                            logger.warn("user is not an operator, no need store location.");
                        }
                    }

                } else {
                    oldPosition.setLatitude(position.getLatitude());
                    oldPosition.setLongitude(position.getLongitude());

                    operatorPositionService.updateOperatorPosition(oldPosition);
                }

                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
            }
        } catch (Exception e) {
            logger.error("update operator location failed.", e);
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(e.getMessage());
        }

        return response;
    }
}
