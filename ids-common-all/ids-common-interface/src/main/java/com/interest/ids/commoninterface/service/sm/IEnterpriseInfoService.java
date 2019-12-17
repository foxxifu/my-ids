package com.interest.ids.commoninterface.service.sm;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.interest.ids.common.project.bean.sm.EnterpriseInfo;
import com.interest.ids.common.project.bean.sm.Page;
import com.interest.ids.common.project.bean.sm.QueryEnterprise;

public interface IEnterpriseInfoService {
	/**
	 * 添加企业
	 * 
	 * @param enterprise
	 * @return
	 */
	EnterpriseInfo insertEnterpriseM(EnterpriseInfo enterprise);

	/**
	 * 修改企业
	 * 
	 * @param enterprise
	 * @return
	 */
	int updateEnterpriseM(EnterpriseInfo enterprise);

	/**
	 * 通过id删除一个企业
	 * 
	 * @param id
	 * @return
	 */
	int deleteEnterpriseMById(Long id);

	/**
	 * 批量删除企业
	 * 
	 * @param ids
	 * @return
	 */
	int deleteEnterpriseMByIds(Long[] ids);

	/**
	 * 根据id查询企业
	 * 
	 * @param id
	 * @return
	 */
	EnterpriseInfo selectEnterpriseMById(Long id);

	/**
	 * 根据条件查询企业
	 * 
	 * @param queryEnterprise
	 * @return
	 */
	List<EnterpriseInfo> selectEnterpriseMByCondition(
			QueryEnterprise queryEnterprise);

	/**
	 * 查询数据总条数
	 * 
	 * @param enterprise
	 * @return
	 */
	int selectAllCount(QueryEnterprise queryEnterprise);

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @return
	 */
	@SuppressWarnings("rawtypes")
    List<EnterpriseInfo> selectEnterpriseMByPage(Page page);

	/**
	 * 根据stationCode查询企业信息
	 * 
	 * @param stationCode
	 * @return
	 */
	EnterpriseInfo selectEnterpriseMByStationCode(String stationCode);

	/**
	 * 根据用户的id查询企业
	 * 
	 * @param userId
	 * @return
	 */
	List<EnterpriseInfo> selectEnterpriseMByUserId(Map<String,Object> condition);
	
	/**
	 * 根据企业编号集查询企业
	 * @param enterpriseIds
	 * @return
	 */
	List<EnterpriseInfo> selectEnterpriseByIds(Collection<Long> enterpriseIds);

	/**
	 * 根据区域id查询企业信息
	 * 
	 * @param domainId
	 *            区域id
	 * @return EnterpriseInfo
	 */
	EnterpriseInfo selectEnterpriseMByDomainId(Long domainId);

	/**查询企业绑定的区域的名称*/
	List<String> selectDomainNameByEnterId(Long enterpriseId);

	/**查询企业绑定的用户*/
	List<String> selectUserNameByEnterId(Long enterpriseId);

	/**查询企业下绑定的电站*/
	List<String> selectStationNameByEnterId(Long enterpriseId);

	/**删除企业下面所有的角色*/
	void deleteRole(Long enterpriseId);
	/**
	 * 根据登录用户获取当前登录用户所在企业的id
	 * @param id
	 * @return
	 */
	String getLoginUserLogo(Long id);
}
