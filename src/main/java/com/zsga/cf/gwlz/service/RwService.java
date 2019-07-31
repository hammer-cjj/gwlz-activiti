package com.zsga.cf.gwlz.service;

import java.util.List;

import com.zsga.cf.gwlz.pojo.Rw;
import com.zsga.cf.gwlz.pojo.RwExt;
import com.zsga.cf.gwlz.pojo.User;

public interface RwService {
	int addRw(Rw rw);
	int editRw(Rw rw);
	Rw findRwById(int id);
	int removeRw(int id);
	List<Rw> findMyRw(User user);
	List<RwExt> listAllRw(Rw rw);
	int rwSign(int id);
	int rwComplete(int id);
	int countWeiCqZr(User user);
	int countWeiCq(User user);
	int countWeiCqCy(User user);
	//int countCq(User user);
}
