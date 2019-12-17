package com.interest.ids.common.project.mapper.kpi;

import com.interest.ids.common.project.bean.kpi.StatisticsScheduleTaskM;
import com.interest.ids.common.project.utils.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StatisticsScheduleMapper extends CommonMapper<StatisticsScheduleTaskM>{

    /**
     * 根据电站ID， 统计时间，统计类型查询计划任务
     * @param stationCodes
     * @param statTime
     * @param statType
     * @return
     */
    List<StatisticsScheduleTaskM> listTodoSchedulers(@Param("stationCodes") List<String> stationCodes,
                                                     @Param("statTime") Long statTime, @Param("statType") Integer statType);

    List<StatisticsScheduleTaskM> listTodoSchedulers(@Param("stationCodes")List<String> stationCodes,
                                                     @Param("statStartTime")Long statStartTime, @Param("statEndTime") Long statEndTime, @Param("statType") Integer statType);

    /**
     * 查询任务执行过程中出错的任务
     * @param stationCodes
     * @param statStartTime
     * @param statEndTime
     * @param statType 统计类型
     * @return
     */
    List<StatisticsScheduleTaskM> listErrorSchedulers(List<String> stationCodes, Long statStartTime, Long statEndTime, Integer statType);

    List<StatisticsScheduleTaskM> listErrorSchedulers(List<String> sIds, Long sTime, Long eTime);

    /**
     * 更新任务状态
     * @param list
     * @param status
     */
    void updateSchedulerStatus(List<StatisticsScheduleTaskM> list, int status);

    /**
     * 删除指定的任务序列
     * @param list
     */
    void deleteSchedulers(List<StatisticsScheduleTaskM> list);

}