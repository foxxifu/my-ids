package com.interest.ids.biz.kpicalc.kpi.util;

import com.interest.ids.biz.kpicalc.kpi.calc.AbstractKpiStatistic;
import com.interest.ids.common.project.spring.context.SystemContext;

import java.util.TimeZone;

/**
 * 创建统计Calculator实例
 */
public class KpiCalculatorFactory {

    /**
     * 创建统计类型实例
     * @param clazz
     * @param timeZone
     * @param <T>
     * @return
     */
    public static <T extends AbstractKpiStatistic> T getService(Class<T> clazz, TimeZone timeZone) {
        T serviceInstance = SystemContext.getBean(clazz);
        if(null != serviceInstance && null != timeZone){
            KpiStatisticUtil.setCurrentTheadKpiStatFlowVar(serviceInstance);
            serviceInstance.setTimeZone(timeZone);
        }
        return serviceInstance;
    }
}
