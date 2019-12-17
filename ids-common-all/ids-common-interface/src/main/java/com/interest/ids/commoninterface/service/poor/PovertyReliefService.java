package com.interest.ids.commoninterface.service.poor;

import java.util.List;

import com.interest.ids.common.project.bean.sm.Page;
import com.interest.ids.common.project.bean.sm.PovertyReliefObjectT;
import com.interest.ids.common.project.bean.sm.QueryPovertyRelief;

public interface PovertyReliefService {
	/**
	 * 新建扶贫用户
	 * 
	 * @param aid
	 * @return
	 */
	int insertPovertyRelief(PovertyReliefObjectT aid);

	/**
	 * 批量导入扶贫用户
	 * 
	 * @param list
	 * @return
	 */
	int insertPovertyReliefByCollection(List<PovertyReliefObjectT> list);

	/**
	 * 根据id查询扶贫用户
	 * 
	 * @param id
	 * @return
	 */
	PovertyReliefObjectT selectPovertyReliefById(Long id);

	/**
	 * 根据id修改扶贫用户
	 * 
	 * @param aid
	 * @return
	 */
	int updatePovertyReliefById(PovertyReliefObjectT aid);

	/**
	 * 根据id删除扶贫用户
	 * 
	 * @param id
	 * @return
	 */
	boolean deletePovertyReliefById(Long id);

	/**
	 * 根据多个id批量删除扶贫用户
	 * 
	 * @param ids
	 * @return
	 */
	Integer deletePovertyReliefByIds(Long[] ids);

	/**
	 * 导出扶贫用户（导出所有和导出选中）
	 * 
	 * @param ids
	 * @return
	 */
	List<PovertyReliefObjectT> exportPovertyRelief(Long... ids);

	/**
	 * 根据任何条件查询扶贫用户
	 * 
	 * @param queryPovertyRelief
	 * @return
	 */
	List<PovertyReliefObjectT> selectPovertyReliefByCondition(
			QueryPovertyRelief queryPovertyRelief);

	/**
	 * 查询扶贫用户总条数
	 * 
	 * @param queryPovertyRelief
	 * @return
	 */
	Integer selectAllCount(QueryPovertyRelief queryPovertyRelief);

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @return
	 */
	List<PovertyReliefObjectT> selectPovertyReliefByPage(Page<?> page);
}
