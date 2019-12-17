package com.interest.ids.commoninterface.dao.sm;

import java.util.List;

import com.interest.ids.common.project.bean.sm.District;

public interface DistrictMapper 
{
    /**根据id统计每个行政区域的市有多少电站*/
    List<District> selectDistrictByIds(Long[] ids);
    
    /**查询所有的行政区域*/
    List<District> selectTopDistrict();
    
    /**根据父id查询子区域 */
    List<District> selectChildDistrict(Long parentId);
    
    /**根据id查询当前id的区域信息*/
    District selectDistrictById(Long id);
    
    List<District> findDisListByAreaCodes(Object[] codeSet);
}
