package com.interest.ids.commoninterface.dao.sm;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.interest.ids.common.project.bean.TreeModel;
import com.interest.ids.common.project.bean.sm.DomainInfo;
import com.interest.ids.common.project.dto.DomainTreeDto;

public interface DomainInfoMapper {
	/**
	 * 区域的添加
	 * 
	 * @param domain
	 * @return
	 */
	public int insertDomain(DomainInfo domain);

	/**
	 * 区域的修改
	 * 
	 * @param domain
	 * @return
	 */
	public int updateDomain(DomainInfo domain);

	/**
	 * 根据id删除区域
	 * 
	 * @param id
	 * @return
	 */
	public int deleteDomainById(Long id);

	/**
	 * 根据ID查询区域信息
	 * 
	 * @param id
	 * @return
	 */
	public DomainInfo selectDomainById(Long id);

	/**
	 * 查询所有的区域
	 * 
	 * @return
	 */
	public List<DomainInfo> selectAllDomain();

	/**
	 * 根据父id查询所有的子区域
	 * 
	 * @param parentId
	 * @return
	 */
	public List<DomainInfo> selectDomainsByParentId(Long domainId);

	/**
	 * 根据电站编码查询区域
	 * 
	 * @param stationCode
	 * @return
	 */
	public DomainInfo selectDomainByStationCode(String stationCode);

	/**
	 * 根据用户查询区域和企业树
	 * 
	 * @param queryParams
	 *            id、type_
	 * @return List<TreeModel>
	 */
	public List<TreeModel> getUserDomainTree(Map<String, Object> queryParams);

	/** 根据企业id查询直接子区域*/
	public List<DomainInfo> selectDomainsByEnter(Long enterpriseId);

	/**根据企业id查询所有的区域*/
	public List<DomainInfo> selectAllDomainsByEnter(Long enterpriseId);

	/**统计父区域下面的子区域个数 */
	public Integer countDomainByParentId(Long parentId);
	
	/**
	 * 根据区域编号集查询区域对象
	 * @param domainIds
	 * @return
	 */
	List<DomainInfo> selectDomainByIds(Collection<Long> domainIds);

	/**统计区域下的电站总数*/
	public Integer countStationByDomainId(Long id);
	/**
	 * 根据区域id查询他的path路径集合
	 * @param domainIds
	 * @return
	 */
	List<String> getDomainPathByIds(Set<Long> domainIds);
	/**
	 * 获取当前登录用户区域信息
	 * @param domainParentIds 父节点的id
	 * @param domainIds 区域id
	 * @return
	 */
	List<DomainTreeDto> getDomainByIds(Set<Long> domainParentIds);
}
