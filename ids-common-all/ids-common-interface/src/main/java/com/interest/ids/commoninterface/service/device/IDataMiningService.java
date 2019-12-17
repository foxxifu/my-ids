package com.interest.ids.commoninterface.service.device;

import java.util.List;
import java.util.Map;

import com.interest.ids.common.project.bean.device.DataMining;

/**
 * 数据补采的service层
 * @author wq
 *
 */
public interface IDataMiningService {

	/**
	 * 新增数据补采的数据
	 * @param dm
	 */
	void insertData(DataMining dm);
	/**
	 * 更新数据
	 * @param dm
	 */
	void updateData(DataMining dm);
	/**
	 * 根据id获取一条补采数据的信息
	 * @param id
	 * @return
	 */
	DataMining getByID(Long id);
	/**
	 * 根据条件查询补采的分页数据
	 * @param params
	 * @return
	 */
	List<DataMining> findPageInfo(Map<String, Object> params);
	/**
	 * 根据任务ID删除任务
	 * @param taskId
	 * @return
	 */
	int deleteTaskById(Long taskId);
	/**
	 * 根据补采任务id执行补采任务
	 * @param taskId
	 */
	boolean executeReloadData(Long taskId);
}
