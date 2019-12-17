package com.interest.ids.biz.authorize.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.interest.ids.common.project.bean.sm.ResourceM;
import com.interest.ids.commoninterface.dao.sm.ResourceMMapper;
import com.interest.ids.commoninterface.service.sm.IResourceMService;

@Service("resourceMService")
public class ResourceMServiceImpl implements IResourceMService {

    @Resource
    private ResourceMMapper resourceMMapper;

    @Override
    public int insertResourceM(ResourceM resource) {
        return resourceMMapper.insertResourceM(resource);
    }

    @Override
    public ResourceM selectResourceMById(Long id) {
        return resourceMMapper.selectResourceMById(id);
    }

    @Override
    public int deleteResourceMById(Long id) {
        return resourceMMapper.deleteResourceMById(id);
    }

    @Override
    public int updateResourceByid(ResourceM resource) {
        return resourceMMapper.updateResourceByid(resource);
    }

    @Override
    public List<ResourceM> selectResourceMsByAuthorizeId(Long authId) {
        return resourceMMapper.selectResourceMsByAuthorizeId(authId);
    }

    @Override
    public List<ResourceM> selectResourceMByConditions(ResourceM resource) {
        return resourceMMapper.selectResourceMByConditions(resource);
    }

    @Override
    public List<ResourceM> selectAllResourceM() {
        return resourceMMapper.selectAllResourceM();
    }

	@Override
	public List<ResourceM> selectAllResourceMByUserId(Long userId) {
		return resourceMMapper.selectAllResourceMByUserId(userId);
	}

}
