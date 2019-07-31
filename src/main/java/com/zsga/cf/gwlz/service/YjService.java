package com.zsga.cf.gwlz.service;

import java.util.List;

import com.zsga.cf.gwlz.pojo.Yj;
import com.zsga.cf.gwlz.pojo.YjExt;

public interface YjService {
	//添加意见
	int addYj(Yj yj);
	//查询意见列表
	List<YjExt> listYj();
	//按照Id查询意见
	Yj findYj(Integer id);
}
