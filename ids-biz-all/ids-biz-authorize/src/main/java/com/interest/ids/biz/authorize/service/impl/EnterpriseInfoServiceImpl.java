package com.interest.ids.biz.authorize.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.interest.ids.common.project.bean.sm.EnterpriseInfo;
import com.interest.ids.common.project.bean.sm.Page;
import com.interest.ids.common.project.bean.sm.QueryEnterprise;
import com.interest.ids.commoninterface.dao.sm.EnterpriseInfoMapper;
import com.interest.ids.commoninterface.service.sm.IEnterpriseInfoService;

@Service("enterpriseInfoMService")
public class EnterpriseInfoServiceImpl implements IEnterpriseInfoService {

    @Resource
    private EnterpriseInfoMapper enterpriseInfoMapper;

    @Override
    public EnterpriseInfo insertEnterpriseM(EnterpriseInfo enterprise)
    {
    	enterpriseInfoMapper.insertEnterpriseM(enterprise);
    	
        return enterprise;
    }

    @Override
    public int updateEnterpriseM(EnterpriseInfo enterprise) {
        return enterpriseInfoMapper.updateEnterpriseM(enterprise);
    }

    @Override
    public int deleteEnterpriseMById(Long id) {
        return enterpriseInfoMapper.deleteEnterpriseMById(id);
    }

    @Override
    public int deleteEnterpriseMByIds(Long[] ids) {
        return enterpriseInfoMapper.deleteEnterpriseMByIds(ids);
    }

    @Override
    public EnterpriseInfo selectEnterpriseMById(Long id) {
        return enterpriseInfoMapper.selectEnterpriseMById(id);
    }

    @Override
    public List<EnterpriseInfo> selectEnterpriseMByCondition(QueryEnterprise queryEnterprise) {
        return enterpriseInfoMapper.selectEnterpriseMByCondition(queryEnterprise);
    }

    @Override
    public int selectAllCount(QueryEnterprise queryEnterprise) {
        return enterpriseInfoMapper.selectAllCount(queryEnterprise);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<EnterpriseInfo> selectEnterpriseMByPage(Page page) {
        return enterpriseInfoMapper.selectEnterpriseMByPage(page);
    }

    @Override
    public EnterpriseInfo selectEnterpriseMByStationCode(String stationCode) {
        return enterpriseInfoMapper.selectEnterpriseMByStationCode(stationCode);
    }

    @Override
    public List<EnterpriseInfo> selectEnterpriseMByUserId(Map<String,Object> condition) {
        return enterpriseInfoMapper.selectEnterpriseMByUserId(condition);
    }

	@Override
	public EnterpriseInfo selectEnterpriseMByDomainId(Long domainId) {
		return this.enterpriseInfoMapper.selectEnterpriseMByDomainId(domainId);
	}

	@Override
	public List<String> selectDomainNameByEnterId(Long enterpriseId) {
		return enterpriseInfoMapper.selectDomainNameByEnterId(enterpriseId);
	}

	@Override
	public List<String> selectUserNameByEnterId(Long enterpriseId) {
		return enterpriseInfoMapper.selectUserNameByEnterId(enterpriseId);
	}

	@Override
	public List<String> selectStationNameByEnterId(Long enterpriseId) {
		return enterpriseInfoMapper.selectStationNameByEnterId(enterpriseId);
	}

	@Override
	public void deleteRole(Long enterpriseId) {
		enterpriseInfoMapper.deleteRole(enterpriseId);
	}

	
    @Override
    public List<EnterpriseInfo> selectEnterpriseByIds(Collection<Long> enterpriseIds) {
        
        return enterpriseInfoMapper.selectEnterpriseByIds(enterpriseIds);
    }

	@Override
	public String getLoginUserLogo(Long id) {
		return enterpriseInfoMapper.getLoginUserLogo(id);
	}

}
