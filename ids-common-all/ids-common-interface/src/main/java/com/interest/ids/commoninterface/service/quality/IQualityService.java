package com.interest.ids.commoninterface.service.quality;

import java.util.Collection;
import java.util.Map;

public interface IQualityService {

    /**
     * 获得signal code 和 order 的对应关系
     * @param devTypeId
     * @param domainId
     * @return
     */
    Map<String, Integer> getSignalCodeOrder(Integer devTypeId, Long domainId, Collection<String> cloumns);
}
