package com.zsga.cf.gwlz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zsga.cf.gwlz.mapper.YjMapper;
import com.zsga.cf.gwlz.pojo.Yj;
import com.zsga.cf.gwlz.pojo.YjExt;
import com.zsga.cf.gwlz.service.YjService;

@Service
public class YjServiceImpl implements YjService {

	@Autowired
	private YjMapper yjMapper ;

	/**
	 * 添加意见
	 */
	@Override
	public int addYj(Yj yj) {
		return yjMapper.addYj(yj);
	}

	/**
	 * 查询意见列表
	 */
	@Override
	public List<YjExt> listYj() {
		return yjMapper.listYj();
	}

	/**
	 * 按照Id查询意见
	 */
	@Override
	public Yj findYj(Integer id) {
		return yjMapper.findYj(id);
	} 
	
	
}
