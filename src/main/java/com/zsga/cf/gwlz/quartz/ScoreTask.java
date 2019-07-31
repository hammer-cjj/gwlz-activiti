package com.zsga.cf.gwlz.quartz;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.zsga.cf.gwlz.pojo.Rw;
import com.zsga.cf.gwlz.pojo.RwKh;
import com.zsga.cf.gwlz.pojo.UserExt;
import com.zsga.cf.gwlz.service.RwKhService;
import com.zsga.cf.gwlz.service.RwService;
import com.zsga.cf.gwlz.service.UserService;

/**
 * 定时统计考核总分
 * @author quadcopter
 *
 */
public class ScoreTask {

	@Autowired
	private UserService userService;
	@Autowired
	private RwService rwService;
	@Autowired
	private RwKhService rwKhService;
	
	/**
	 * 每天凌晨2点执行统计考核分数
	 */
	public void tjScore() {
		/**
		 * 统计考核分数
		 */
//		System.out.println("BBBB");
		//查询所有人员
		List<UserExt> userExtList = userService.list();
		List<RwKh> rwKhList = new ArrayList<RwKh>();
		for (UserExt userExt : userExtList) {
			int totalScore = 0;
			int zyTotal = 0;
			int ptTotal = 0;
			RwKh rwKh = new RwKh();
			//设置真实姓名
			rwKh.setRealName(userExt.getRealName());
			//设置用户Id
			rwKh.setUserId(userExt.getId());
			//查询有关用户的所有任务
			List<Rw> rwList = rwService.findMyRw(userExt);
			for (Rw rw : rwList) {
				totalScore += rw.getScore();
				if (rw.getZyFlag() == 1) {//重要任务
					zyTotal += 1;
				} else if (rw.getZyFlag() == 0) { //普通任务
					ptTotal += 1;
				}
			}
			//设置重要任务个数
			rwKh.setZyTotal(zyTotal);
			//设置普通任务个数
			rwKh.setPtTotal(ptTotal);
			//设置总分
			rwKh.setTotalScore(totalScore);
			//加入列表
			rwKhList.add(rwKh);
		}
		
		//更新任务考核总分
		rwKhService.updateTotalScore(rwKhList);
	}
}
