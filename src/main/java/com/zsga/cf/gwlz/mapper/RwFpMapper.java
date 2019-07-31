package com.zsga.cf.gwlz.mapper;

import com.zsga.cf.gwlz.pojo.RwFp;

public interface RwFpMapper {
	//保存分配任务
	int addRwFp(RwFp rwFp);
	//删除任务分配
	int delRwFp(int rwId);
}
