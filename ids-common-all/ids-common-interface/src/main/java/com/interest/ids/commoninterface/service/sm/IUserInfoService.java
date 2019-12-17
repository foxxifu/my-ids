package com.interest.ids.commoninterface.service.sm;

import java.util.List;
import java.util.Map;

import com.interest.ids.common.project.bean.sm.Page;
import com.interest.ids.common.project.bean.sm.QueryUser;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.bean.sm.UserAuthorize;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.bean.sm.UserRole;

/**
 * 用户相关
 * 
 * @author xm
 *
 */
public interface IUserInfoService {

	/**
	 * 根据用户id查询用户
	 * 
	 * @param id
	 * @return
	 */
	Map<String,Object> selectUserByPrimaryKey(Long id);

	/**
	 * 根据用户id删除用户
	 * 
	 * @param id
	 * @return
	 */
	int deleteUserByPrimaryKey(Long id);

	/**
	 * 根据用户id跟新用户
	 * 
	 * @param user
	 * @return
	 */
	int updateUserByPrimaryKeySelective(UserInfo user);

	/**
	 * 添加用户
	 * 
	 * @param user
	 * @param stationCodes
	 * @param roleIds
	 * @return
	 */
	int insertUser(UserInfo user, String stationCodes, String roleIds,Long departmentId);

	/**
	 * 根据用户的用户名和密码查询用户
	 * 
	 * @param user
	 * @return
	 */
	UserInfo selectUserMByUsernameAndPwd(UserInfo user);

	/**
	 * 禁用或启用用户(用户状态：0:正常 1:禁用)
	 * 
	 * @param user
	 * @return
	 */
	boolean updateUserMStatus(UserInfo user);

	/**
	 * 删除用户角色管理关系
	 * 
	 * @param id
	 */
	void deleteAllRole(Long id);

	/**
	 * 添加用户角色关系
	 * 
	 * @param userRoles
	 */
	void insertUserMRole(List<UserRole> userRoles);

	/**
	 * 删除用户所有关联权限
	 * 
	 * @param id
	 */
	void deleteUserAllAuthorize(Long id);

	/**
	 * 新建用户权限关系
	 * 
	 * @param userAuthorize
	 */
	void insertUserAuthorize(UserAuthorize userAuthorize);

	/**
	 * 查询所有用户
	 * 
	 * @return
	 */
	List<UserInfo> selectAllUser();

	/**
	 * 根据用户的任意条件查询用户
	 * 
	 * @param queryUser
	 * @return
	 */
	List<Map<String,Object>> selectUserByConditions(QueryUser queryUser);

	/**
	 * 用户密码找回支持登录名/用户名/邮箱
	 * 
	 * @param user
	 * @return
	 */
	String selectUserPwd(UserInfo user);

	/**
	 * 分页显示用户
	 * 
	 * @param page
	 * @return
	 */
	List<UserInfo> selectUserByPage(Page<UserInfo> page);

	/**
	 * 查询数据总条数
	 * 
	 * @param user
	 * @return
	 */
	int selectAllCount(QueryUser queryUser);

	/**
	 * 修改密码
	 * 
	 * @param user
	 * @return
	 */
	int updatePwd(UserInfo user);

	/**
	 * 根据多个id查询用户的名字
	 * 
	 * @param array
	 * @return
	 */
	public List<UserInfo> selectUserByIds(Object[] ids);

	/**
	 * 查询和某一个用户同公司的所有的用户(只有id，和用户名)
	 * 
	 * @param userId
	 * @return
	 */
	List<UserInfo> selectEnterpriseUser(Long userId);

	/**
	 * 根据缺陷id查询缺陷的审核人
	 * 
	 * @param defectId
	 * @return
	 */
	UserInfo selectUserByDefectId(Long defectId);

	/**
	 * 删除用户电站关联关系
	 * 
	 * @param id
	 */
	void deleteUserStation(Long id);

	/**
	 * 插入用户电站关联关系
	 * 
	 * @param stations
	 * @return
	 */
	int insertUserStation(List<StationInfoM> stations);
	
	/**根据用户的登录名*/
	Long checkLoginName(String loginName);

	/**
	 * 根据电站的编码查询关联的人员信息
	 * @param stationCode
	 * @return
	 */
	List<UserInfo> selectUserByStationCode(String stationCode);

	/**更新用户部门关系*/
	void updateUserDepartment(Long id, Long departmentId);

	/**删除用户的时候删除用户部门关系*/
	void deleteUserDepartment(Long id);

	/**查询用户所在的部门*/
	Map<String, Object> selectDepartmentByUserId(String userId);

	List<UserInfo> selectAll();

}
