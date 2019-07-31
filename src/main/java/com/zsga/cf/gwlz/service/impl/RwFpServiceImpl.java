package com.zsga.cf.gwlz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zsga.cf.gwlz.mapper.RwFpMapper;
import com.zsga.cf.gwlz.pojo.RwFp;
import com.zsga.cf.gwlz.service.RwFpService;

@Service
public class RwFpServiceImpl implements RwFpService {
	@Autowired
	private RwFpMapper rwFpMapper;

	@Override
	public int addRwFp(RwFp rwFp) {
		return rwFpMapper.addRwFp(rwFp);
	}

	@Override
	public int delRw(int rwId) {
		return rwFpMapper.delRwFp(rwId);
	}

}
