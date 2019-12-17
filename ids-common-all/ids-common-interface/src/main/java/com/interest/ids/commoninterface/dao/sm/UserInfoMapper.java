package com.interest.ids.commoninterface.dao.sm;

import java.util.List;
import java.util.Map;

import com.interest.ids.common.project.bean.sm.Page;
import com.interest.ids.common.project.bean.sm.QueryUser;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.bean.sm.UserAuthorize;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.bean.sm.UserRole;
/**
 * 
 * @author lhq
 * 可以在此类定义自定义查询
 *
 */
public interface UserInfoMapper {
	
	
	//根据用户id查询用户
	Map<String,Object> selectUserByPrimaryKey(Map<String,Object> condition);
	
	//根据用户id删除用户
    int deleteUserByPrimaryKey(Long id);
	
	//根据用户id跟新用户
    int updateUserByPrimaryKeySelective(UserInfo user);
	
	//添加用户
    int insertUser(UserInfo user);
	
	//根据用户的用户名和密码查询用户
    UserInfo selectUserMByUsernameAndPwd(UserInfo user);
	
	//禁用或启用用户(用户状态：0:正常 1:禁用)
    boolean updateUserMStatus(UserInfo user);
	
	//删除用户角色管理关系
    void deleteAllRole(Long id);
	
	//添加用户角色关系
    void insertUserMRole(List<UserRole> roles);
	
	//删除用户所有关联权限
    void deleteUserAllAuthorize(Long id);
	
	//新建用户权限关系
    void insertUserAuthorize(UserAuthorize userAuthorize);
	
	//查询所有用户
    List<UserInfo> selectAllUser();
	
	//根据用户的任意条件查询用户
    List<Map<String,Object>> selectUserByConditions(QueryUser queryUser);
	
	//用户密码找回支持登录名/用户名/邮箱
    String selectUserPwd(UserInfo user);
	
	//分页显示用户
    List<UserInfo> selectUserByPage(Page page);
	
	//查询数据总条数
    int selectAllCount(QueryUser queryUser);
	
	//插入用户电站关联关系
    int insertUserStation(List<StationInfoM> stations);

	//修改密码
    int updatePwd(UserInfo user);

    /**
     * 根据多个id查询用户的名字
     * @author xm
     * @param ids
     * @return
     */
    public List<UserInfo> selectUserByIds(Object[] ids);

    /**
     * 查询和某一个用户同公司的所有的用户(只有id，和用户名)
     * @param userId
     * @return
     */
    List<UserInfo> selectEnterpriseUser(Long userId);

    /**
     * 根据缺陷的id查询缺陷的审核人
     * @param defectId
     * @return
     */
    UserInfo selectUserByDefectId(Long defectId);

    /**删除用户电站关联关系*/
	void deleteUserStation(Long id);
	
	/**根据用户的登录名*/
	Long checkLoginName(String loginName);

	/**
	 * 根据电站的编码查询关联的人员信息
	 * @param stationCode
	 * @return
	 */
	List<UserInfo> selectUserByStationCode(String stationCode);

	/**
	 * 插入用户部门关联关系
	 * @param id
	 * @param departmentId
	 */
	void insertUserDepartment(Map<String,Object> data);

	/**查询用户的角色名称*/
	List<String> selectRolesByUserId(long parseLong);

	/**更新用户部门关联关系*/
	void updateUserDepartment(Map<String,Object> map);

	/**删除用户部门管理关系*/
	void deleteUserDepartment(Long id);

	/**根据用户的id查询用户的部门*/
	Long getDepartmentByUserId(Long id);

	/**查询用户所在的部门*/
	Map<String, Object> selectDepartmentByUserId(Long userId);

	List<UserInfo> selectAll();
}
