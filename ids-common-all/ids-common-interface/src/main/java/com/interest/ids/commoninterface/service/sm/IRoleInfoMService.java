package com.interest.ids.commoninterface.service.sm;

import java.util.List;
import java.util.Map;

import com.interest.ids.common.project.bean.sm.QueryRole;
import com.interest.ids.common.project.bean.sm.RoleInfo;
import com.interest.ids.common.project.bean.sm.RoleMAuthorizeM;

public interface IRoleInfoMService 
{
	//根据角色id查询角色信息
    RoleInfo selectRoleByPrimaryKey(Long id);
		
		//根据角色id删除角色信息
        boolean deleteRoleByPrimaryKey(Long id);
		
		//根据角色id更新角色信息
        int updateRoleByPrimaryKeySelective(RoleInfo role);
		
		//新建角色
        Long insertRole(RoleInfo role);
		
		//禁用或者启用角色
        boolean updateRoleMStatus(RoleInfo role);
		
		//添加角色的权限
        boolean insertRoleAuthorizeM(RoleMAuthorizeM roleMAuthorizeM);
		
		//删除角色下所有的权限
        boolean deleteRoleAuthorizeM(Long roleId);
		
		//查询用户下所有的角色
        List<RoleInfo> selectRolesByUserId(Map<String,Object> map);
		
		//分页查询所有的角色
        List<RoleInfo> selectRoleMByPage(QueryRole queryRole);
		
		//查询数据总条数
        int selectAllCount(QueryRole queryRole);

        /**
         * 统计当前角色下是否有用户，如果有则返回用户的登录名
         * @param id
         * @return
         */
        List<String> selectUserCountByRoleId(Long roleId);

        /**根据企业id查询所有的角色*/
		List<RoleInfo> selectRoleByEnterpriseId(Long id);
}
