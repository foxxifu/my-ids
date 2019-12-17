package com.interest.ids.biz.authorize.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.interest.ids.common.project.bean.sm.Department;
import com.interest.ids.common.project.bean.sm.Page;
import com.interest.ids.common.project.bean.sm.QueryUser;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.bean.sm.UserAuthorize;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.bean.sm.UserRole;
import com.interest.ids.commoninterface.dao.sm.IDepartmentMapper;
import com.interest.ids.commoninterface.dao.sm.UserInfoMapper;
import com.interest.ids.commoninterface.service.sm.IUserInfoService;
/**
 * 
 * @author xm
 *
 */
@Service("userInfoService")
public class UserInfoServiceImpl implements IUserInfoService {

    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
	private IDepartmentMapper departmentMapper;

    @Override
    public Map<String,Object> selectUserByPrimaryKey(Long id) {
    	Long departmentId = userInfoMapper.getDepartmentByUserId(id);
    	Map<String,Object> condition = new HashMap<>();
    	condition.put("id", id);
    	condition.put("departmentId", departmentId);
        return userInfoMapper.selectUserByPrimaryKey(condition);
    }

    @Override
    public int deleteUserByPrimaryKey(Long id) 
    {
    	int result = userInfoMapper.deleteUserByPrimaryKey(id);
    	if(result == 1)
    	{
    		userInfoMapper.deleteAllRole(id);
    		userInfoMapper.deleteUserAllAuthorize(id);
    	}
        return result;
    }

    @Override
    public int updateUserByPrimaryKeySelective(UserInfo user) {
        return userInfoMapper.updateUserByPrimaryKeySelective(user);
    }

    @Override
    public int insertUser(UserInfo user,String stationCodes,String roleIds,Long departmentId) {
        // 1. 插入用户
        Integer result = userInfoMapper.insertUser(user);
        if (1 == result) {
            // 2. 用户站点关联关系
        	if(null != stationCodes)
        	{
        		List<StationInfoM> list = new ArrayList<>();
        		String[] codes = stationCodes.split(",");
        		StationInfoM s = null;
        		for (String code : codes) {
        			s = new StationInfoM();
        			s.setStationCode(code);
        			s.setUserId(user.getId());
        			list.add(s);
        		}
        		// 批量插入关联关系
        		userInfoMapper.insertUserStation(list);
        	}
        	//3. 添加用户角色关联关系
        	if(null != roleIds)
        	{
        		List<UserRole> roles = new ArrayList<>();
        		String[] roleId = roleIds.split(",");
        		UserRole role = null;
        		for (String id : roleId) {
        			role = new UserRole();
        			role.setUserId(user.getId());
        			role.setRoleId(Long.parseLong(id));
        			roles.add(role);
				}
        		userInfoMapper.insertUserMRole(roles);
        	}
        	//4. 插入用户和部门之间的关联关系
        	if(null != departmentId)
        	{
        		Map<String,Object> data = new HashMap<String,Object>();
        		data.put("userId", user.getId());
        		data.put("departmentId", departmentId);
        		userInfoMapper.insertUserDepartment(data);
        	}
        }
        return result;
    }

    @Override
    public UserInfo selectUserMByUsernameAndPwd(UserInfo user) {
        return userInfoMapper.selectUserMByUsernameAndPwd(user);
    }

    @Override
    public boolean updateUserMStatus(UserInfo user) {
        return userInfoMapper.updateUserMStatus(user);
    }

    @Override
    public List<UserInfo> selectAllUser() {
        return userInfoMapper.selectAllUser();
    }

    @Override
    public List<Map<String,Object>> selectUserByConditions(QueryUser queryUser) {
    	List<Map<String,Object>> list = userInfoMapper.selectUserByConditions(queryUser);
    	if(null != list && list.size() > 0)
    	{
    		for (Map<String, Object> map : list) {
				if(null != map.get("id"))
				{
					List<String> roles = userInfoMapper.selectRolesByUserId(Long.parseLong(map.get("id").toString()));
					map.put("roleName",StringUtils.join(roles, ","));
					Department depatement = departmentMapper.getDepartmentByUserId(Long.parseLong(map.get("id").toString()));
					if(null != depatement) {
						map.put("department", depatement.getName());
					}else {
						map.put("department", map.get("enterprise"));
					}
					
				}
			}
    	}
        return list;
    }

    @Override
    public void deleteAllRole(Long id) {
        userInfoMapper.deleteAllRole(id);
    }

    @Override
    public void insertUserMRole(List<UserRole> userRoles) {
        userInfoMapper.insertUserMRole(userRoles);
    }

    @Override
    public void deleteUserAllAuthorize(Long id) {
        userInfoMapper.deleteUserAllAuthorize(id);
    }

    @Override
    public void insertUserAuthorize(UserAuthorize userAuthorize) {
        userInfoMapper.insertUserAuthorize(userAuthorize);
    }

    @Override
    public String selectUserPwd(UserInfo user) {
        return userInfoMapper.selectUserPwd(user);
    }

    @Override
    public List<UserInfo> selectUserByPage(Page<UserInfo> page) {
        return userInfoMapper.selectUserByPage(page);
    }

    @Override
    public int selectAllCount(QueryUser queryUser) {
        return userInfoMapper.selectAllCount(queryUser);
    }

    @Override
    public int updatePwd(UserInfo user) {
        return userInfoMapper.updatePwd(user);
    }

    @Override
    public List<UserInfo> selectUserByIds(Object[] ids) {
        return userInfoMapper.selectUserByIds(ids);
    }

    @Override
    public List<UserInfo> selectEnterpriseUser(Long userId) {
        return userInfoMapper.selectEnterpriseUser(userId);
    }

    @Override
    public UserInfo selectUserByDefectId(Long defectId) {
        return userInfoMapper.selectUserByDefectId(defectId);
    }

	@Override
	public void deleteUserStation(Long id) {
		userInfoMapper.deleteUserStation(id);
	}

	@Override
	public int insertUserStation(List<StationInfoM> stations) {
		return userInfoMapper.insertUserStation(stations);
	}

	@Override
	public Long checkLoginName(String loginName) {
		return userInfoMapper.checkLoginName(loginName);
	}

	@Override
	public List<UserInfo> selectUserByStationCode(String stationCode) {
		return userInfoMapper.selectUserByStationCode(stationCode);
	}

	@Override
	public void updateUserDepartment(Long id, Long departmentId) {
		Map<String,Object> map = new HashMap<>();
		map.put("id", id);
		map.put("departmentId", departmentId);
		userInfoMapper.updateUserDepartment(map);
	}

	@Override
	public void deleteUserDepartment(Long id) {
		userInfoMapper.deleteUserDepartment(id);
	}

	@Override
	public Map<String, Object> selectDepartmentByUserId(String userId) {
		return userInfoMapper.selectDepartmentByUserId(Long.parseLong(userId));
	}

	@Override
	public List<UserInfo> selectAll() {
		return userInfoMapper.selectAll();
	}
}
