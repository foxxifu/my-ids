package com.interest.ids.commoninterface.dao.sm;

import java.util.List;

import com.interest.ids.common.project.bean.sm.AuthorizeM;

public interface AuthorizeMMapper 
{
	//插入权限
    int insertAuthorizeM(AuthorizeM authorize);
	
	//根据id查询权限
    AuthorizeM selectAuthorizeMById(Long id);
	
	//根据id删除权限
    int deleteAuthorizeMById(Long id);
	
	//根据id更新权限
    int updateAuthorizeMById(AuthorizeM authorize);
	
	//通过角色id查询所有的权限
    List<AuthorizeM> selectAuthByRoleId(Long roleId);
	
	/*//添加权限下面的资源
	public int insertAuthorizeMResource(AuthorizeMResource authorizeMResource);
	
	//删除权限下所有的资源
	public int deleteAuthorizeMResourceByAuthId(Long authId);*/
	
	//根据用户id查询用户的权限
    List<AuthorizeM> selectAuthByUserId(Long userId);
	
	//查询所有的权限
    List<AuthorizeM> selectAllAuthorizeM();

	//查询用户的权限
    List<AuthorizeM> selectUserAuthorize(Long userId);
}
