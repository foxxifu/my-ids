package com.interest.ids.biz.web.poor.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.interest.ids.common.project.bean.sm.Page;
import com.interest.ids.common.project.bean.sm.PovertyReliefObjectT;
import com.interest.ids.common.project.bean.sm.QueryPovertyRelief;
import com.interest.ids.commoninterface.dao.poor.PovertyReliefMapper;
import com.interest.ids.commoninterface.service.poor.PovertyReliefService;

@Service("povertyReliefService")
public class PovertyReliefServiceImpl implements PovertyReliefService {

	@Resource
	private PovertyReliefMapper povertyReliefMapper;
	
	@Override
	public int insertPovertyRelief(PovertyReliefObjectT aid) {
		return povertyReliefMapper.insertPovertyRelief(aid);
	}

	@Override
	public int insertPovertyReliefByCollection(List<PovertyReliefObjectT> list) {
		return povertyReliefMapper.insertPovertyReliefByCollection(list);
	}

	@Override
	public PovertyReliefObjectT selectPovertyReliefById(Long id) {
		return povertyReliefMapper.selectPovertyReliefById(id);
	}

	@Override
	public int updatePovertyReliefById(PovertyReliefObjectT aid) {
		return povertyReliefMapper.updatePovertyReliefById(aid);
	}

	@Override
	public boolean deletePovertyReliefById(Long id) {
		return povertyReliefMapper.deletePovertyReliefById(id);
	}

	@Override
	public Integer deletePovertyReliefByIds(Long[] ids) {
		return povertyReliefMapper.deletePovertyReliefByIds(ids);
	}

	@Override
	public List<PovertyReliefObjectT> exportPovertyRelief(Long... ids) {
		return povertyReliefMapper.exportPovertyRelief(ids);
	}

	@Override
	public List<PovertyReliefObjectT> selectPovertyReliefByCondition(QueryPovertyRelief queryPovertyRelief) {
		return povertyReliefMapper.selectPovertyReliefByCondition(queryPovertyRelief);
	}

	@Override
	public Integer selectAllCount(QueryPovertyRelief queryPovertyRelief) {
		return povertyReliefMapper.selectAllCount(queryPovertyRelief);
	}

	@Override
	public List<PovertyReliefObjectT> selectPovertyReliefByPage(Page<?> page) {
		return povertyReliefMapper.selectPovertyReliefByPage(page);
	}

}
