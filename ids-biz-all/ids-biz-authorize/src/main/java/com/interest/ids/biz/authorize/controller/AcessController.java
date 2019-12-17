package com.interest.ids.biz.authorize.controller;

import com.interest.ids.biz.authorize.utils.AccessUtils;
import com.interest.ids.common.project.support.Response;
import com.interest.ids.common.project.support.Responses;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/auth")
public class AcessController {
    @Resource
    private AccessUtils accessUtils;

    @RequestMapping(value = "/check", method = RequestMethod.GET)
    @ResponseBody
    public Response access(@RequestParam String uri, @RequestParam String tokenId) {
        // WARING 未能定位出是接口异常还是权限有无,目前统一返回的boolean
        Boolean ok = accessUtils.access(uri, tokenId);
        if (ok) return Responses.SUCCESS();
        return Responses.FAILED();
    }
}
