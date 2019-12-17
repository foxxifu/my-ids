package com.interest.ids.biz.kpicalc.kpi.dao.impl;

import com.interest.ids.biz.kpicalc.kpi.dao.IKpiStatisticDao;
import com.interest.ids.common.project.bean.kpi.*;
import com.interest.ids.commoninterface.dao.kpi.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Component("kpiStatisticDao")
public class KpiStatisticDaoImpl implements IKpiStatisticDao {

    private static final Logger logger = LoggerFactory.getLogger(KpiStatisticDaoImpl.class);

    @Autowired
    private KpiInverterHourMapper inverterHourMMapper;
    @Autowired
    private KpiInverterDayMapper inverterDayMMapper;
    @Autowired
    private KpiInverterMonthMapper inverterMonthMMapper;
    @Autowired
    private KpiInverterYearMapper inverterYearMMapper;
    @Autowired
    private KpiMeterHourMapper meterHourMMapper;
    @Autowired
    private KpiMeterDayMapper meterDayMMapper;
    @Autowired
    private KpiMeterMonthMapper meterMonthMMapper;
    @Autowired
    private KpiMeterYearMapper meterYearMMapper;
    @Autowired
    private KpiDcCombinerDayMapper combinerDCDayMMapper;
    @Autowired
    private KpiEmiHourMapper envMonitorHourMMapper;
    @Autowired
    private KpiStationHourMapper stationHourMMapper;
    @Autowired
    private KpiStationDayMapper stationDayMMapper;
    @Autowired
    private KpiStationMonthMapper stationMonthMMapper;
    @Autowired
    private KpiStationYearMapper stationYearMMapper;

    @Override
    @SuppressWarnings("unchecked")
    public <T> void saveOrUpdate(List<T> list) {

        if(list != null && list.size() > 0){
            T t = list.get(0);

            logger.info("start to save kpi statistics data: " + System.currentTimeMillis());
            
            if(t instanceof KpiInverterHourM){
                List<KpiInverterHourM> params = (List<KpiInverterHourM>)list;
                inverterHourMMapper.deleteByPrimaryKeyList(params);
                inverterHourMMapper.insertList(params);
            }else if(t instanceof KpiInverterDayM){
                List<KpiInverterDayM> params = (List<KpiInverterDayM>)list;
                inverterDayMMapper.deleteByPrimaryKeyList(params);
                inverterDayMMapper.insertList(params);
            }else if(t instanceof KpiInverterMonthM){
                List<KpiInverterMonthM> params = (List<KpiInverterMonthM>)list;
                inverterMonthMMapper.deleteByPrimaryKeyList(params);
                inverterMonthMMapper.insertList(params);
            }else if(t instanceof KpiInverterYearM){
                List<KpiInverterYearM> params = (List<KpiInverterYearM>)list;
                inverterYearMMapper.deleteByPrimaryKeyList(params);
                inverterYearMMapper.insertList(params);
            }else if(t instanceof KpiMeterHourM){
                
                List<KpiMeterHourM> params = (List<KpiMeterHourM>)list;
                meterHourMMapper.deleteByPrimaryKeyList(params);
                meterHourMMapper.insertList(params);
            }else if(t instanceof KpiMeterDayM){
                List<KpiMeterDayM> params = (List<KpiMeterDayM>)list;
                meterDayMMapper.deleteByPrimaryKeyList(params);
                meterDayMMapper.insertList(params);
            }else if(t instanceof KpiMeterMonthM){
                List<KpiMeterMonthM> params = (List<KpiMeterMonthM>)list;
                meterMonthMMapper.deleteByPrimaryKeyList(params);
                meterMonthMMapper.insertList(params);
            }else if(t instanceof KpiMeterYearM){
                List<KpiMeterYearM> params = (List<KpiMeterYearM>)list;
                meterYearMMapper.deleteByPrimaryKeyList(params);
                meterYearMMapper.insertList(params);
            }else if(t instanceof KpiDcCombinerDayM){

                List<KpiDcCombinerDayM> params = (List<KpiDcCombinerDayM>)list;
                combinerDCDayMMapper.deleteByPrimaryKeyList(params);
                combinerDCDayMMapper.insertList(params);

            }else if(t instanceof KpiEmiHourM){
                List<KpiEmiHourM> params = (List<KpiEmiHourM>)list;
                envMonitorHourMMapper.deleteByPrimaryKeyList(params);
                envMonitorHourMMapper.insertList(params);
            }else if(t instanceof KpiStationHourM){
                List<KpiStationHourM> params = (List<KpiStationHourM>)list;
                stationHourMMapper.deleteByPrimaryKeyList(params);
                stationHourMMapper.insertList(params);
            }else if(t instanceof KpiStationDayM){
                List<KpiStationDayM> params = (List<KpiStationDayM>)list;
                stationDayMMapper.deleteByPrimaryKeyList(params);
                stationDayMMapper.insertList(params);
            }else if(t instanceof KpiStationMonthM){
                List<KpiStationMonthM> params = (List<KpiStationMonthM>)list;
                stationMonthMMapper.deleteByPrimaryKeyList(params);
                stationMonthMMapper.insertList(params);
            }else if(t instanceof KpiStationYearM){
                List<KpiStationYearM> params = (List<KpiStationYearM>)list;
                stationYearMMapper.deleteByPrimaryKeyList(params);
                stationYearMMapper.insertList(params);
            }

            logger.info("saved " + t.getClass() + " for: " + list.size());
        }
    }

    
    public List<KpiDcCombinerDayM> getKpiDcCombinerDayByStationCode(String stationCode, long startTime,
			long endTime)
    {
    	return combinerDCDayMMapper.getKpiDcCombinerDayByStationCode(stationCode,startTime,endTime);
    }
    
    public List<KpiDcCombinerDayM> getTopKpiDcCombinerDayByStationCode(String stationCode,
    		long startTime, long endTime)
    {
    	return combinerDCDayMMapper.getTopKpiDcCombinerDayByStationCode(stationCode,startTime,endTime);
    }
}
