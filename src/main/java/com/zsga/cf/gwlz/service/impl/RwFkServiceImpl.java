package com.zsga.cf.gwlz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zsga.cf.gwlz.mapper.RwFkMapper;
import com.zsga.cf.gwlz.pojo.RwFk;
import com.zsga.cf.gwlz.pojo.RwFkExt;
import com.zsga.cf.gwlz.service.RwFkService;

@Service
public class RwFkServiceImpl implements RwFkService {
	@Autowired
	private RwFkMapper rwFkMapper;

	@Override
	public int addRwFk(RwFk rwFk) {
		return rwFkMapper.addRwFk(rwFk);
	}

	@Override
	public List<RwFkExt> listRwFk(int rwId) {
		return rwFkMapper.listRwFk(rwId);
	}

	@Override
	public int delRwFk(int rwId) {
		return rwFkMapper.delRwFk(rwId);
	}

}
