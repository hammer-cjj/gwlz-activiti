package com.zsga.cf.gwlz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zsga.cf.gwlz.mapper.RwMapper;
import com.zsga.cf.gwlz.pojo.Rw;
import com.zsga.cf.gwlz.pojo.RwExt;
import com.zsga.cf.gwlz.pojo.User;
import com.zsga.cf.gwlz.service.RwService;

@Service
//@Transactional
public class RwServiceImpl implements RwService {
	
	@Autowired
	private  RwMapper rwMapper;

	@Override
	public int addRw(Rw rw) {
		return rwMapper.addRw(rw);
	}

	@Override
	public List<RwExt> listAllRw(Rw rw) {
		return rwMapper.listAllRw(rw);
	}

	@Override
	public int rwSign(int id) {
		return rwMapper.rwSign(id);
	}

	@Override
	public int rwComplete(int id) {
		return rwMapper.rwComplete(id);
	}

	/**
	 * 统计距离截止日期小等于3天的任务,责任人
	 */
	@Override
	public int countWeiCqZr(User user) {
		return rwMapper.countWeiCqZr(user);
	}
	
	/**
	 * 统计距离截止日期小等于3天的任务,参与人权限
	 */
	@Override
	public int countWeiCqCy(User user) {
		return rwMapper.countWeiCqCy(user);
	}
	
	/**
	 * 统计距离截止日期小等于3天的任务,处长权限
	 */
	@Override
	public int countWeiCq(User user) {
		return rwMapper.countWeiCq(user);
	}

	/**
	 * 根据任务Id查询
	 */
	@Override
	public Rw findRwById(int id) {
		return rwMapper.findRwById(id);
	}

	/**
	 * 更新任务
	 */
	@Override
	public int editRw(Rw rw) {
		return rwMapper.editRw(rw);
	}

	/**
	 * 删除任务
	 */
	@Override
	public int removeRw(int id) {
		return rwMapper.removeRw(id);
	}

	/**
	 * 查询属于我的任务
	 */
	@Override
	public List<Rw> findMyRw(User user) {
		return rwMapper.findMyRw(user);
	}

	/**
	 * 查询所有备忘任务
	 */
	@Override
	public List<RwExt> listAllRwBw(Rw rw) {
		return rwMapper.listAllRwBw(rw);
	}

	/**
	 * 备忘录中完成督办
	 */
	@Override
	public int completeDb(int id) {
		return rwMapper.completeDb(id);
	}

	@Override
	public List<RwExt> listAllRwGeRen(Rw rw) {
		return rwMapper.listAllRwGeRen(rw);
	}

	@Override
	public List<RwExt> listSh(Rw rw) {
		return rwMapper.listSh(rw);
	}

	@Override
	public List<RwExt> listAllSh(Rw rw) {
		return rwMapper.listAllSh(rw);
	}

	@Override
	public int completeSh(int id) {
		return rwMapper.completeSh(id);
	}

	/**
	 * 统计已经超期
	 */
	/*@Override
	public int countCq(User user) {
		return rwMapper.countCq(user);
	}*/

}
