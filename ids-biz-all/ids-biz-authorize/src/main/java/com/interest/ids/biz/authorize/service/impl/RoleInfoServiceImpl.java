package com.interest.ids.biz.authorize.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.interest.ids.common.project.bean.sm.AuthorizeM;
import com.interest.ids.common.project.bean.sm.QueryRole;
import com.interest.ids.common.project.bean.sm.RoleInfo;
import com.interest.ids.common.project.bean.sm.RoleMAuthorizeM;
import com.interest.ids.commoninterface.dao.sm.AuthorizeMMapper;
import com.interest.ids.commoninterface.dao.sm.RoleInfoMapper;
import com.interest.ids.commoninterface.service.sm.IRoleInfoMService;

@Service("roleInfoService")
public class RoleInfoServiceImpl implements IRoleInfoMService {
    @Resource
    private RoleInfoMapper roleInfoMapper;
    @Resource
    private AuthorizeMMapper authorizeMMapper;

    @Override
    public RoleInfo selectRoleByPrimaryKey(Long id) {
    	RoleInfo role = roleInfoMapper.selectRoleByPrimaryKey(id);
    	if(null != role && role.getId() != null)
    	{
    		List<AuthorizeM> list =  authorizeMMapper.selectAuthByRoleId(role.getId());
    		role.setAuthorizeMs(list);
    	}
        return role; 
    }

    @Override
    public boolean deleteRoleByPrimaryKey(Long id) {
        return roleInfoMapper.deleteRoleByPrimaryKey(id);
    }

    @Override
    public int updateRoleByPrimaryKeySelective(RoleInfo role) {
        return roleInfoMapper.updateRoleByPrimaryKeySelective(role);
    }

    @Override
    public Long insertRole(RoleInfo role) {
    	roleInfoMapper.insertRole(role);
        return role.getId();
    }

    @Override
    public boolean updateRoleMStatus(RoleInfo role) {
        return roleInfoMapper.updateRoleMStatus(role);
    }

    @Override
    public boolean insertRoleAuthorizeM(RoleMAuthorizeM roleMAuthorizeM) {
        return roleInfoMapper.insertRoleAuthorizeM(roleMAuthorizeM);
    }

    @Override
    public boolean deleteRoleAuthorizeM(Long roleId) {
        return roleInfoMapper.deleteRoleAuthorizeM(roleId);
    }

    @Override
    public List<RoleInfo> selectRolesByUserId(Map<String,Object> map) {
        return roleInfoMapper.selectRolesByUserId(map);
    }

    @Override
    public List<RoleInfo> selectRoleMByPage(QueryRole queryRole) {
        return roleInfoMapper.selectRoleMByPage(queryRole);
    }

    @Override
    public int selectAllCount(QueryRole queryRole) {
        return roleInfoMapper.selectAllCount(queryRole);
    }

    @Override
    public List<String> selectUserCountByRoleId(Long roleId) {
        return roleInfoMapper.selectUserCountByRoleId(roleId);
    }

	@Override
	public List<RoleInfo> selectRoleByEnterpriseId(Long id) {
		return roleInfoMapper.selectRoleByEnterpriseId(id);
	}
}
