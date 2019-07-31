package com.zsga.cf.gwlz.mapper;

import java.util.List;

import com.zsga.cf.gwlz.pojo.Yj;
import com.zsga.cf.gwlz.pojo.YjExt;

public interface YjMapper {
	//添加意见
	int addYj(Yj yj);
	//查询意见列表
	List<YjExt> listYj();
	//按照id查询意见
	Yj findYj(Integer id);
}
