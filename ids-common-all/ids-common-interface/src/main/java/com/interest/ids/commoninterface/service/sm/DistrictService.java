package com.interest.ids.commoninterface.service.sm;

import java.util.List;
import java.util.Map;

import com.interest.ids.common.project.bean.sm.District;
import com.interest.ids.common.project.dto.DomainTreeDto;

public interface DistrictService {
	/**
	 * 根据用户id和类型查询区域
	 * 
	 * @param userId
	 *            用户id
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return List<District>
	 */
    List<District> getAllDistrict(Long userId, String type_);

	/**
	 * 
	 * @param userId
	 * @param type_
	 * @return
	 */
    Map<String, Integer> selectDistrictCount(Long userId, String type_);

	/**
	 * 查询当前用户下的行政区域和区域下的电站
	 * @param id
	 * @param type_
	 * @param hasStation 是否子节点有电站内容 true：有电站  false：没有电站
	 * @return
	 */
    public List<District> getDistrictAndStation(Long id, String type_,boolean hasStation);
    /**
     * 查询当前登录用户的区域和区域下的电站信息
     * @param id 用户id
     * @param type_ 用户类型
     * @param hasStation 是否需要查询结果返回电站信息  true：有电站信息 false：不需要电站信息
     * @return
     */
	List<DomainTreeDto> getDomains(Long id, String type_, boolean hasStation);
}
