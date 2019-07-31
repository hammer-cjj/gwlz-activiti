package com.zsga.cf.gwlz.service;

import java.util.List;

import com.zsga.cf.gwlz.pojo.RwKh;

public interface RwKhService {
	void updateTotalScore(List<RwKh> rwKhList);
	List<RwKh> listRwKh();
}
