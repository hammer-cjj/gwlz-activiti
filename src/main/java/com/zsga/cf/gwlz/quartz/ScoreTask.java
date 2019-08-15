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
			double totalScore = 0;
			int count = 0;
			RwKh rwKh = new RwKh();
			//设置真实姓名
			rwKh.setRealName(userExt.getRealName());
			//设置用户Id
			rwKh.setUserId(userExt.getId());
			//查询有关用户的所有任务
			List<Rw> rwList = rwService.findMyRw(userExt);
			for (Rw rw : rwList) {
				int flag = rw.getCompleteQK();
				double xishu = 0;
				switch (flag) {
					case 0:      //0未完成：系数为0
						xishu = 0;
						break;
					case 1:      //1部分完成：系数为0.5
						xishu = 0.5;
						break;
					case 2:      //2一般：系数为1
						xishu = 1;
						break;
					case 3:      //3较好：系数为1.5
						xishu = 1.5;
						break;
					default:
						break;
				}
				double tempScore = xishu * (rw.getCompleteSX() + rw.getNandu() + rw.getDengji() + rw.getZhongyao());
				totalScore += tempScore;
				count += 1;
			}
			//设置重要任务个数
			rwKh.setCount(count);
			//设置总分
			rwKh.setTotalScore(totalScore);
			//加入列表
			rwKhList.add(rwKh);
		}
		
		//更新任务考核总分
		rwKhService.updateTotalScore(rwKhList);
	}
}
