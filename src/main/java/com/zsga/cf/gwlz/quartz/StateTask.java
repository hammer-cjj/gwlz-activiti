package com.zsga.cf.gwlz.quartz;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.zsga.cf.gwlz.mapper.RwMapper;
import com.zsga.cf.gwlz.pojo.Rw;

/**
 * 定时统计任务超期
 * @author quadcopter
 *
 */
public class StateTask {
	@Autowired
	private RwMapper rwMapper;
	
	/**
	 * 每天凌晨1点统计超期任务
	 */
	public void executeTask() {
//		System.out.println("AAAAAAAAAAAA");
		try {
			List<Rw> list = rwMapper.listState_0_1();
			Calendar endTime = Calendar.getInstance();
			for (Rw rw : list) {
				endTime.setTime(rw.getEndTime());
				endTime.add(Calendar.DAY_OF_MONTH, 1);
				if (new Date().after(endTime.getTime())) {  //已超期有问题
					rw.setCq(1);
					rwMapper.updateRwState(rw);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
