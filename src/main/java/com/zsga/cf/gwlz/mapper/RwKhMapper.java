package com.zsga.cf.gwlz.mapper;

import java.util.List;

import com.zsga.cf.gwlz.pojo.RwKh;

public interface RwKhMapper {
	//更新考核总分,重要任务个数，普通任务个数 
	void updateTotalScore(List<RwKh> rwKhList);
	//查询任务考核表的数据 
	List<RwKh> listRwKh();
}
