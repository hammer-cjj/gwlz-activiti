package com.zsga.cf.gwlz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zsga.cf.gwlz.mapper.RwKhMapper;
import com.zsga.cf.gwlz.pojo.RwKh;
import com.zsga.cf.gwlz.service.RwKhService;

@Service
public class RwKhServiceImpl implements RwKhService {

	@Autowired
	private RwKhMapper rwKhMapper;
	
	/**
	 * 更新任务总分
	 */
	@Override
	public void updateTotalScore(List<RwKh> rwKhList) {
		rwKhMapper.updateTotalScore(rwKhList);
	}

	/**
	 * 查询任务考核表的数据 
	 */
	@Override
	public List<RwKh> listRwKh() {
		return rwKhMapper.listRwKh();
	}

}
