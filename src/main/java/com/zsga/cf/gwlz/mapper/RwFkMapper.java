package com.zsga.cf.gwlz.mapper;

import java.util.List;

import com.zsga.cf.gwlz.pojo.RwFk;
import com.zsga.cf.gwlz.pojo.RwFkExt;

public interface RwFkMapper {
	//保存反馈
	int addRwFk(RwFk rwFk);
	//查询反馈列表
	List<RwFkExt> listRwFk(int rwId);
	//删除反馈列表
	int delRwFk(int rwId);
}
