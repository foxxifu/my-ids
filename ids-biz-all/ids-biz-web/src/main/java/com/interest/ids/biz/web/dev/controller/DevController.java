package com.interest.ids.biz.web.dev.controller;

import com.interest.ids.biz.web.dev.service.DevService;
import com.interest.ids.common.project.support.Response;
import com.interest.ids.common.project.support.Responses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: sunbjx
 * @Description: 设备数据
 * @Date: Created in 上午10:36 18-2-2
 * @Modified By:
 */
@RestController
@RequestMapping("/dev")
public class DevController {

    @Autowired
    private DevService devService;

    @RequestMapping(value = "/getDevData", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Response getDevData(@RequestParam long id, @RequestBody Set<String> signalKeys) {

        Map<String, Object> result = devService.getDevDataByCache(id, signalKeys);
        return Responses.SUCCESS().setResults(result);
    }

    @RequestMapping(value = "/removeDev", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Response removeDev(@RequestBody List<Long> ids) {

        List<String> modbusSns = devService.removeByIds(ids);
        if (null != modbusSns) return Responses.SUCCESS().setResults(modbusSns);
        return Responses.FAILED();
    }
}
