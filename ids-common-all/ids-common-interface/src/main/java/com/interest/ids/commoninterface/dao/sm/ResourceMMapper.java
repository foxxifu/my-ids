package com.interest.ids.commoninterface.dao.sm;

import java.util.List;

import com.interest.ids.common.project.bean.sm.ResourceM;

public interface ResourceMMapper 
{
	//添加新的资源
    int insertResourceM(ResourceM resource);
	
	//根据id查询资源
    ResourceM selectResourceMById(Long id);
	
	//根据id删除资源
    int deleteResourceMById(Long id);
	
	//根据id修改资源
    int updateResourceByid(ResourceM resource);
	
	//根据权限id查询所有的资源
    List<ResourceM> selectResourceMsByAuthorizeId(Long roleId);
	
	//根据表单任意字段查询资源
    List<ResourceM> selectResourceMByConditions(ResourceM resource);
	
	//查询所有的资源
    List<ResourceM> selectAllResourceM();

    /**根据用户的id查询所有的资源*/
	List<ResourceM> selectAllResourceMByUserId(Long userId);
}
