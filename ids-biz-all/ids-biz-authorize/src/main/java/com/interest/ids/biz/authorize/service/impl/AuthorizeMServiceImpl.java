package com.interest.ids.biz.authorize.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.interest.ids.common.project.bean.sm.AuthorizeM;
import com.interest.ids.commoninterface.dao.sm.AuthorizeMMapper;
import com.interest.ids.commoninterface.service.sm.IAuthorizeMService;

@Service("authorizeMService")
public class AuthorizeMServiceImpl implements IAuthorizeMService {

    @Resource
    private AuthorizeMMapper authorizeMMapper;

    @Override
    public int insertAuthorizeM(AuthorizeM authorize) {
        return authorizeMMapper.insertAuthorizeM(authorize);
    }

    @Override
    public AuthorizeM selectAuthorizeMById(Long id) {
        return authorizeMMapper.selectAuthorizeMById(id);
    }

    @Override
    public int deleteAuthorizeMById(Long id) {
        return authorizeMMapper.deleteAuthorizeMById(id);
    }

    @Override
    public int updateAuthorizeMById(AuthorizeM authorize) {
        return authorizeMMapper.updateAuthorizeMById(authorize);
    }

    @Override
    public List<AuthorizeM> selectAuthByRoleId(Long roleId) {
        return authorizeMMapper.selectAuthByRoleId(roleId);
    }

    @Override
    public List<AuthorizeM> selectAuthByUserId(Long userId) {
        return authorizeMMapper.selectAuthByUserId(userId);
    }

    @Override
    public List<AuthorizeM> selectAllAuthorizeM() {
        return authorizeMMapper.selectAllAuthorizeM();
    }

    @Override
    public List<AuthorizeM> selectUserAuthorize(Long userId) {
        return authorizeMMapper.selectUserAuthorize(userId);
    }

}
