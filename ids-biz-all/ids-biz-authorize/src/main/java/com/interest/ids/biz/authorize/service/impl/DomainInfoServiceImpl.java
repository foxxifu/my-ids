package com.interest.ids.biz.authorize.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.interest.ids.common.project.bean.sm.DomainInfo;
import com.interest.ids.commoninterface.dao.sm.DomainInfoMapper;
import com.interest.ids.commoninterface.service.sm.IDomainInfoService;

@Service("domainInfoService")
public class DomainInfoServiceImpl implements IDomainInfoService {

    @Resource
    private DomainInfoMapper domainInfoMapper;

    @Override
    public int insertDomain(DomainInfo domain) {
        return domainInfoMapper.insertDomain(domain);
    }

    @Override
    public int updateDomain(DomainInfo domain) {
        return domainInfoMapper.updateDomain(domain);
    }

    @Override
    public int deleteDomainById(Long id) {
        return domainInfoMapper.deleteDomainById(id);
    }

    @Override
    public DomainInfo selectDomainById(Long id) {
        return domainInfoMapper.selectDomainById(id);
    }

    @Override
    public List<DomainInfo> selectAllDomain() {
        return domainInfoMapper.selectAllDomain();
    }

    @Override
    public List<DomainInfo> selectDomainsByParentId(Long domainId) {
        return domainInfoMapper.selectDomainsByParentId(domainId);
    }

    @Override
    public DomainInfo selectDomainByStationCode(String stationCode) {
        return domainInfoMapper.selectDomainByStationCode(stationCode);
    }

    @Override
    public List<DomainInfo> selectDomainsByEnter(Long enterpriseId) {
        return domainInfoMapper.selectDomainsByEnter(enterpriseId);
    }

    @Override
    public List<DomainInfo> selectTreeByParentId(Long domainId) {
        List<DomainInfo> list = this.selectDomainsByParentId(domainId);
        List<DomainInfo> result = new ArrayList<>();
        if (null != list && list.size() > 0) {
            result.addAll(list);
            for (DomainInfo domainInfo : list) {
                if (null != domainInfo) {
                    List<DomainInfo> subList = selectTreeByParentId(domainInfo.getId());
                    if (null != subList && subList.size() > 0) {
                        result.addAll(subList);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<DomainInfo> selectTreeByEnterpriseId(Long enterpriseId) {
        List<DomainInfo> list = domainInfoMapper.selectAllDomainsByEnter(enterpriseId);
        return list;
    }

    @Override
    public Integer countDomainByParentId(Long parentId) {
        return domainInfoMapper.countDomainByParentId(parentId);
    }

    @Override
    public List<DomainInfo> selectDoaminByIds(Collection<Long> domainIds) {
        return domainInfoMapper.selectDomainByIds(domainIds);
    }

    @Override
    public List<DomainInfo> getTree(Long id, String model) {
        if (model.equals("E")) {
            List<DomainInfo> domains = domainInfoMapper.selectDomainsByEnter(id);
            List<DomainInfo> children = null;
            for (DomainInfo domainInfo : domains) {
                children = getChildren(domainInfo.getId());
                domainInfo.setChildren(children);
            }
            return domains;
        } else {
            return getChildren(id);
        }

    }

    private List<DomainInfo> getChildren(Long id) {
        List<DomainInfo> list = domainInfoMapper.selectDomainsByParentId(id);
        if (null == list || (null != list && list.size() == 0)) {
            return list;
        } else {
            List<DomainInfo> children = null;
            for (DomainInfo domainInfo : list) {
                children = getChildren(domainInfo.getId());
                domainInfo.setChildren(children);
            }
        }
        return list;
    }

	@Override
	public Integer countStationByDomainId(Long id) {
		return domainInfoMapper.countStationByDomainId(id);
	}

}
