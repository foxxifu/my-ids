package com.interest.ids.biz.authorize.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.interest.ids.biz.authorize.dto.ResourceDto;
import com.interest.ids.common.project.bean.sm.ResourceM;
import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.commoninterface.service.sm.IResourceMService;

@Controller
@RequestMapping("/resource")
public class ResourceController {
    @Resource
    private IResourceMService resourceMService;

    private static final Logger log = LoggerFactory
			.getLogger(ResourceController.class);
    /**
     * 插入资源
     * 
     * @param resource
     * @return
     */
    @RequestMapping(value="/insertResource",method=RequestMethod.POST)
    @ResponseBody
    public Response<ResourceDto> insertResource(@RequestBody ResourceDto resourceDto) {
        Response<ResourceDto> response = new Response<ResourceDto>();
        
        if (null != resourceDto) {
            ResourceM resource = new ResourceM();
            BeanUtils.copyProperties(resourceDto, resource);
            Integer result = resourceMService.insertResourceM(resource);
            if(null != result && result.equals(1))
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
     * 根据id查询资源
     */
    @RequestMapping(value="/selectResourceMById",method=RequestMethod.POST)
    @ResponseBody
    public Response<ResourceDto> selectResourceMById(@RequestBody ResourceDto resourceDto) {
        Response<ResourceDto> response = new Response<ResourceDto>();
        
        if (null != resourceDto && null != resourceDto.getId()) {
            ResourceM resource = resourceMService.selectResourceMById(resourceDto.getId());
            if(null != resource)
            {
                ResourceDto dto = new ResourceDto();
                BeanUtils.copyProperties(resource, dto);
                response.setResults(dto);
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
                log.info("get resouce by id success, resouce id is " + resourceDto.getId());
            }else
            {
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
                log.error("get resouce by id fail, resouce id is " + resourceDto.getId());
            }
        }else
        {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            log.error("get resouce by id fail, resouce id is null");
        }
        return response;
    }

    /**
     * 根据id删除资源
     */
    @RequestMapping(value="/deleteResourceMById",method=RequestMethod.POST)
    @ResponseBody
    public Response<ResourceDto> deleteResourceMById(@RequestBody ResourceDto resourceDto) {
        Response<ResourceDto> response = new Response<ResourceDto>();
        
        if (null != resourceDto && null != resourceDto.getId()) {
            Integer result = resourceMService.deleteResourceMById(resourceDto.getId());
            if(null != result && result.equals(1))
            {
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
                log.info("delete resouce by id success, resouce id is " + resourceDto.getId());
            }else
            {
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
                log.error("delete resouce by id fail, resouce id is " + resourceDto.getId());
            }
        }else
        {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            log.error("delete resouce by id fail, resouce id is null");
        }

        return response;
    }

    /**
     * 根据id更新数据
     */
    @RequestMapping(value="/updateResourceByid",method=RequestMethod.POST)
    @ResponseBody
    public Response<ResourceDto> updateResourceByid(@RequestBody ResourceDto resourceDto) {
        Response<ResourceDto> response = new Response<ResourceDto>();
        
        if (null != resourceDto) {
            ResourceM resource = new ResourceM();
            BeanUtils.copyProperties(resourceDto, resource);
            Integer result = resourceMService.updateResourceByid(resource);
            if(null != result && result.equals(1))
            {
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
            }else
            {
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
            }
        }else
        {
            response.setCode(ResponseConstants.CODE_SUCCESS);
            response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
        }

        return response;
    }

    /**
     * 根据权限id查询所有的资源
     */
    @RequestMapping(value="/getResourceMsByAuthorizeId",method=RequestMethod.POST)
    @ResponseBody
    public Response<List<ResourceDto>> getResourceMsByAuthorizeId(@RequestBody ResourceDto resourceDto) {
        Response<List<ResourceDto>> response = new Response<>();
        
        if (null != resourceDto && null != resourceDto.getRoleId()) {
            List<ResourceM> result = resourceMService.selectResourceMsByAuthorizeId(resourceDto.getRoleId());
            if(null != result)
            {
                List<ResourceDto> list = new ArrayList<ResourceDto>();
                ResourceDto dto = null;
                for (ResourceM r : result) 
                {
                    dto = new ResourceDto();
                    BeanUtils.copyProperties(r, dto);
                    list.add(dto);
                }
                response.setResults(list);
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
                log.info("getResourceMsByAuthorizeId success, Authorize id is " + resourceDto.getId());
            }else
            {
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
                log.error("getResourceMsByAuthorizeId fail, Authorize id is " + resourceDto.getId());
            }
        }else
        {
            response.setCode(ResponseConstants.CODE_SUCCESS);
            response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
            log.error("getResourceMsByAuthorizeId success, Authorize id is null");
        }

        return response;
    }

    /**
     * 根据任意条件查询 - 暂时未使用
     */
    public Response<List<ResourceDto>> getResourceMByConditions(ResourceM resource) {
        Response<List<ResourceDto>> response = new Response<>();
        if (null != resource) 
        {
            List<ResourceM> result = resourceMService.selectResourceMByConditions(resource);
            if(null != result)
            {
                List<ResourceDto> list = new ArrayList<ResourceDto>();
                ResourceDto dto = null;
                for (ResourceM r : result) 
                {
                    dto = new ResourceDto();
                    BeanUtils.copyProperties(r, dto);
                    list.add(dto);
                }
                response.setResults(list);
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
            }else
            {
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
            }
        }else
        {
            response.setCode(ResponseConstants.CODE_SUCCESS);
            response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
        }

        return response;
    }
    
    /**
     * /查询所有的资源
     */
    @RequestMapping(value="/getAllResource",method=RequestMethod.GET)
    @ResponseBody
    public Response<List<ResourceDto>> getAllResource() 
    {
        Response<List<ResourceDto>> response = new Response<>();
        List<ResourceM> result = resourceMService.selectAllResourceM();
        if(null != result)
        {
            List<ResourceDto> list = new ArrayList<ResourceDto>();
            ResourceDto dto = null;
            for (ResourceM r : result) 
            {
                dto = new ResourceDto();
                BeanUtils.copyProperties(r, dto);
                list.add(dto);
            }
            response.setResults(list);
            response.setCode(ResponseConstants.CODE_SUCCESS);
            response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
        }else
        {
            response.setCode(ResponseConstants.CODE_SUCCESS);
            response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
        }
        
        return response;
    }
}
