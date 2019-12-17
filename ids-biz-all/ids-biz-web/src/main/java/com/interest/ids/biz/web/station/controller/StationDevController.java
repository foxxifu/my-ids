package com.interest.ids.biz.web.station.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.interest.ids.biz.web.station.DTO.StationDevDto;
import com.interest.ids.common.project.bean.device.DeviceModuleQuery;
import com.interest.ids.common.project.bean.device.StationDev;
import com.interest.ids.common.project.bean.device.StationDevicePvModule;
import com.interest.ids.common.project.bean.device.StationPvModule;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.commoninterface.service.station.IStationDevService;
import com.interest.ids.commoninterface.service.station.StationInfoMService;

@Controller
public class StationDevController 
{
    @Resource
    private StationInfoMService stationInfoMService;
    
    @Resource
    private IStationDevService stationDevService;
    
    //设备接入 -- esn号校验设备是否存在
    @RequestMapping(value="/getDevExistByEsn",method=RequestMethod.POST)
    @ResponseBody
    public Response<StationDevDto> getDevExistByEsn(@RequestBody StationDevDto devDto)
    {
        Response<StationDevDto> response = new Response<>();
        if(null != devDto && null != devDto.getEsn())
        {
            Boolean result = stationDevService.selectDevExistByEsn(devDto.getEsn());
            if(result)
            {
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
            }else
            {
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            }
        }else
        {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
        }
        return response;
    }
    
    //查询所有设备的版本号 (根据企业号去查询)
    @RequestMapping(value="/getDevModelVersion",method=RequestMethod.POST)
    @ResponseBody
    public Response<List<String>> getDevModelVersion(@RequestBody StationDevDto devDto)
    {
        Response<List<String>> response = new Response<>();
        if(null != devDto && null != devDto.getEnterpriseId())
        {
            List<String> result = stationDevService.selectDevModelVersion(devDto.getEnterpriseId());
            if(null != result)
            {
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
                response.setResults(result);
            }else
            {
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            }
        }else
        {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
        }
        return response;
    }
    
    //设备接入保持
    @RequestMapping(value="/saveStationDev",method=RequestMethod.POST)
    @ResponseBody
    public Response<List<String>> saveStationDev(@RequestBody StationDevDto devDto)
    {
        Response<List<String>> response = new Response<>();
        if(null != devDto)
        {
            StationDev stationDev = new StationDev();
            BeanUtils.copyProperties(devDto, stationDev);
            int result = stationDevService.saveStationDev(stationDev);
            if(1 == result)
            {
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
            }else
            {
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            }
        }else
        {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
        }
        return response;
    }
    
    //查看电站下关联的设备
    @RequestMapping(value="/getStationDevsByStationCode",method=RequestMethod.POST)
    @ResponseBody
    public Response<List<DeviceInfo>> getStationDevsByStationCode(@RequestBody StationDevDto devDto)
    {
        Response<List<DeviceInfo>> response = new Response<>();
        if(null != devDto && null != devDto.getStationCode())
        {
            List<DeviceInfo> result = stationDevService.selectStationDevsByStationCode(devDto.getStationCode());
            if(null != result)
            {
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
                response.setResults(result);
            }else
            {
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            }
        }else
        {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
        }
        return response;
    }
    
    /**
     * 组串接入厂家信息
     *//*
    @RequestMapping(value="/getStationPvModule",method=RequestMethod.GET)
    @ResponseBody
    public Response<List<StationPvModule>> getStationPvModule()
    {
        Response<List<StationPvModule>> response = new Response<>();
        List<StationPvModule> result = stationDevService.selectStationPvModel();
        if(null != result)
        {
            response.setCode(ResponseConstants.CODE_SUCCESS);
            response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
            response.setResults(result);
        }else
        {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
        }
        return response;
    }*/
    
    /**
     * 根据厂家的id查询某一个厂家的详细信息
     */
    @RequestMapping(value="/getStationPvModuleDetail",method=RequestMethod.POST)
    @ResponseBody
    public Response<StationPvModule> getStationPvModuleDetail(@RequestBody StationDevDto devDto)
    {
        Response<StationPvModule> response = new Response<>();
        if(null != devDto && null != devDto.getId())
        {
            StationPvModule result = stationDevService.selectStationPvModuleDetail(devDto.getId());
            if(null != result)
            {
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
                response.setResults(result);
            }else
            {
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            }
        }else
        {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
        }
        return response;
    }
    
    /**
     * 给设备添加组串
     */
    @RequestMapping(value="/saveStationPvModule",method=RequestMethod.POST)
    @ResponseBody
    public Response<StationPvModule> saveStationPvModule(@RequestBody List<StationDevicePvModule> list)
    {
        Response<StationPvModule> response = new Response<>();
        if(null != list && list.size() > 0)
        {
            String result = stationDevService.insertStationPvModule(list);
            if(null != result && !result.equals("-1"))
            {
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
            }else
            {
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            }
        }else
        {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
        }
        
        return response;
    }
    
    /**
     * 查询所有的设备类型
     */
    @RequestMapping(value="/getAllDeviceType",method=RequestMethod.GET)
    @ResponseBody
    public Map<String,Integer> getAllDeviceType()
    {
        return DevTypeConstant.DEV_TYPE_I18N;
    }
    /**
     * 查询设备下的组串信息
     */
    @RequestMapping(value="/getModulesByStationCode",method=RequestMethod.POST)
    @ResponseBody
    public Response<List<Map<String,String>>> getModulesByEsn(@RequestBody DeviceModuleQuery deviceModuleQuery)
    {
        Response<List<Map<String,String>>> response = new Response<>();
        if(null != deviceModuleQuery && null != deviceModuleQuery.getEsn())
        {
            deviceModuleQuery.setStart(deviceModuleQuery.getPage()*deviceModuleQuery.getPageSize());
            List<Map<String,String>> result = stationDevService.selectModulesByEsn(deviceModuleQuery);
            if(null != result)
            {
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
            }else
            {
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            }
        }else
        {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
        }
        return response;
    }
    
    /**
     * 查询组件详情
     *//*
    @RequestMapping(value="/getDeviceModuleDetail",method=RequestMethod.POST)
    @ResponseBody
    public Response<Map<String,List>> getDeviceModuleDetail(@RequestBody StationDevDto devDto)
    {
        Map<String,List> result = new HashMap<String,List>();
        Response<Map<String,List>> response = new Response<>();
        
        if(null != devDto && null != devDto.getEsn())
        {
            //1. 查询所有的厂家信息
            List<StationPvModule> stationPvModule = stationDevService.selectStationPvModel();
            //2. 查询设备关联的组件
            List<StationDevicePvModule> devicePvModule = stationDevService.selectStationDevicePvModules(devDto.getEsn());
            if(null != stationPvModule)
            {
                result.put("stationPvModule", stationPvModule);
            }
            if(null != devicePvModule)
            {
                result.put("devicePvModule", devicePvModule);
            }
            response.setResults(result);
            response.setCode(ResponseConstants.CODE_SUCCESS);
            response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
        }else
        {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
        }
        return response;
    }*/
    
    /**
     * 更新设备组串信息
     */
    @RequestMapping(value="/updateDeviceModule",method=RequestMethod.POST)
    @ResponseBody
    public Response<StationDevicePvModule> updateDeviceModule(@RequestBody List<StationDevicePvModule> list)
    {
        Response<StationDevicePvModule> response = new Response<>();
        if(null != list && list.size() > 0)
        {
            String result = stationDevService.updateDeviceModule(list);
            if(null != result && !result.equals("-1"))
            {
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
            }else
            {
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            }
        }else
        {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
        }
        return response;
    }
}
