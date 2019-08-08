package com.zsga.cf.gwlz.mapper;

import java.util.List;

import com.zsga.cf.gwlz.pojo.Rw;
import com.zsga.cf.gwlz.pojo.RwExt;
import com.zsga.cf.gwlz.pojo.User;

public interface RwMapper {
	//保存任务
	int addRw(Rw rw);
	//更新任务
	int editRw(Rw rw);
	//根据任务Id查询任务
	Rw findRwById(int id);
	//删除任务
	int removeRw(int id);
	//查询所有任务(处长和admin)
	List<RwExt> listAllRw(Rw rw);
	//查询所有备忘任务(处长和admin)
	List<RwExt> listAllRwBw(Rw rw);
	//查询我分配的任务(副处长)
	List<RwExt> listFpRw(Rw rw);
	//查询我负责的任务
	List<RwExt> listZrRw(Rw rw);
	//查询我参与的任务
	List<RwExt> listCyRw(Rw rw);
	//查询任务责任人，任务种类，任务起止日期，标题，内容，附件等
	RwExt findRwExt(int id);
	//查询任务分配人
	User findRwFp(int id);
	//查询任务参与人
	List<User> findRwCy(String[] cyIds);
	//查询属于我的任务
	List<Rw> findMyRw(User user);
	//任务签收
	int rwSign(int id);
	//任务完成
	int rwComplete(int id);
	//添加任务参与人
	int addRwCy(Rw rw);
	//查询未签收，进行中的任务
	List<Rw> listState_0_1();
	//改变任务状态
	int updateRwState(Rw rw);
	//统计距离截止日期小等于3天的任务,处长权限
	int countWeiCq(User user);
	//统计距离截止日期小等于3天的任务,责任人
	int countWeiCqZr(User user);
	//统计距离截止日期小等于3天的任务,参与人
	int countWeiCqCy(User user);
	//统计已超期
	//int countCq(User user);
	//备忘录中完成督办
	int completeDb(int id);
}
