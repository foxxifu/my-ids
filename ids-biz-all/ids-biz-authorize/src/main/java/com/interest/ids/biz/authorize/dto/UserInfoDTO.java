package com.interest.ids.biz.authorize.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;

import com.interest.ids.common.project.bean.sm.EnterpriseInfo;
import com.interest.ids.common.project.bean.sm.RoleInfo;
import com.interest.ids.common.project.bean.sm.UserInfo;

public class UserInfoDTO implements Serializable 
{
    private static final long serialVersionUID = -4523863954194998529L;

    private Long id;

    private String loginName;
    private String password;
    private String userName;
    private String qq;
    private String email;
    private String gender;
    private String phone;
    private Byte status;
    private Byte userType;
    private Long enterpriseId;
    private Long createUserId;
    private Date createDate;
    private Long modifyUserId;
    private Date modifyDate;
    private String type_;
    private String stationCodes;
    private EnterpriseInfo enterprise;
    private String stationCode;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 登陆用户的密码
     */
    private String loginUserPassword;
    /**
     * 角色的id
     */
    private Set<Long> roleList;
    private Set<String> stationList;
    /**角色名称*/
    private String roleName;
    /**角色id*/
    private String roleIds;
    /**权限id*/
    private String authIds;
    /**区域名称*/
    private String domainName;
    /**
     * 分页的数据 分别是 页数 和 每页显示条数 start
     */
    private Integer index;
    private Integer pageSize;
    /** 
     * 分页的数据 分别是 页数 和 每页显示条数 end
     */
    /**用户所属部门的id*/
    private Long departmentId;
    
    public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Long getId() {
        return id;
    }

    public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public EnterpriseInfo getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(EnterpriseInfo enterprise) {
		this.enterprise = enterprise;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getLoginUserPassword() {
		return loginUserPassword;
	}

	public void setLoginUserPassword(String loginUserPassword) {
		this.loginUserPassword = loginUserPassword;
	}

	public Set<Long> getRoleList() {
		return roleList;
	}

	public void setRoleList(Set<Long> roleList) {
		this.roleList = roleList;
	}

	public Set<String> getStationList() {
		return stationList;
	}

	public void setStationList(Set<String> stationList) {
		this.stationList = stationList;
	}

	public String getStationCodes() {
		return stationCodes;
	}

	public void setStationCodes(String stationCodes) {
		this.stationCodes = stationCodes;
	}

	public String getType_() {
		return type_;
	}

	public void setType_(String type_) {
		this.type_ = type_;
	}

	public String getAuthIds() {
        return authIds;
    }

    public void setAuthIds(String authIds) {
        this.authIds = authIds;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }
    public void setLoginName(String loginName) {
        this.loginName = loginName == null ? null : loginName.trim();
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq == null ? null : qq.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender == null ? null : gender.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getUserType() {
        return userType;
    }
    public void setUserType(Byte userType) {
        this.userType = userType;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(Long modifyUserId) {
        this.modifyUserId = modifyUserId;
    }
    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }
    
    
    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    public static UserInfoDTO userToDto(UserInfo user){
        UserInfoDTO dto = new UserInfoDTO();
        BeanUtils.copyProperties(user, dto);
        List<RoleInfo> roles = user.getRoles();
        if(roles!=null && roles.size()>0){
            dto.setRoleName(roles.get(0).getName()); 
        }
       //TODO 确定区域的值设置 domain
        return dto;
    }
    
}
