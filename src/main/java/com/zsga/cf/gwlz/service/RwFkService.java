package com.zsga.cf.gwlz.service;

import java.util.List;

import com.zsga.cf.gwlz.pojo.RwFk;
import com.zsga.cf.gwlz.pojo.RwFkExt;

public interface RwFkService {
	//添加反馈
	int addRwFk(RwFk rwFk);
	//查询反馈列表
	List<RwFkExt> listRwFk(int rwId);
	//删除
	int delRwFk(int rwId);
}
