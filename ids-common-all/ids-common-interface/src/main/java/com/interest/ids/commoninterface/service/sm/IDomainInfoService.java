package com.interest.ids.commoninterface.service.sm;

import java.util.Collection;
import java.util.List;

import com.interest.ids.common.project.bean.sm.DomainInfo;

public interface IDomainInfoService {
	/**
	 * 区域的添加
	 * 
	 * @param domain
	 * @return
	 */
    int insertDomain(DomainInfo domain);

	/**
	 * 区域的修改
	 * 
	 * @param domain
	 * @return
	 */
    int updateDomain(DomainInfo domain);

	/**
	 * 根据id删除区域
	 * 
	 * @param id
	 * @return
	 */
    int deleteDomainById(Long id);

	/**
	 * 根据ID查询区域信息
	 * 
	 * @param id
	 * @return
	 */
    DomainInfo selectDomainById(Long id);

	/**
	 * 查询所有的区域
	 * 
	 * @return
	 */
    List<DomainInfo> selectAllDomain();

	/**
	 * 根据父id查询所有的子区域
	 * 
	 * @param parentId
	 * @return
	 */
    List<DomainInfo> selectDomainsByParentId(Long domainId);

	/**
	 * 根据电站编码查询区域
	 * 
	 * @param stationCode
	 * @return
	 */
    DomainInfo selectDomainByStationCode(String stationCode);

    /**根据企业id查询直接子区域*/
	List<DomainInfo> selectDomainsByEnter(Long enterpriseId);

	/**根据区域的父id查询所有子区域*/
	List<DomainInfo> selectTreeByParentId(Long domainId);

	/**根据企业id查询所有子区域*/
	List<DomainInfo> selectTreeByEnterpriseId(Long enterpriseId);

	/**统计父区域下面的子区域个数 */
	Integer countDomainByParentId(Long parentId);
	
	/**
	 * 根据区域编号集查询区域
	 * @param domainIds
	 * @return
	 */
	List<DomainInfo> selectDoaminByIds(Collection<Long> domainIds);

	List<DomainInfo> getTree(Long id, String model);

	/**查询区域下的电站数量*/
	Integer countStationByDomainId(Long id);
}
