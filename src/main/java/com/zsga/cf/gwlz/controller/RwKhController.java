package com.zsga.cf.gwlz.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zsga.cf.gwlz.pojo.BingData;
import com.zsga.cf.gwlz.pojo.Rw;
import com.zsga.cf.gwlz.pojo.RwKh;
import com.zsga.cf.gwlz.pojo.User;
import com.zsga.cf.gwlz.pojo.UserExt;
import com.zsga.cf.gwlz.service.RwKhService;
import com.zsga.cf.gwlz.service.RwService;
import com.zsga.cf.gwlz.service.UserService;

@Controller
@RequestMapping("/rwKh")
public class RwKhController {

	@Autowired
	private RwKhService rwKhService;
	@Autowired
	private UserService userService;
	@Autowired
	private RwService rwService;
	
	/**
	 * 图像显示
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/score-name")
	public Map<String, Object> scorename() {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<RwKh> rwKhList = rwKhService.listRwKh();
		List<String> names = new ArrayList<>();
		List<Double> scores = new ArrayList<>();
		List<Integer> counts = new ArrayList<>();
		List<BingData> data = new ArrayList<>();
		for (RwKh rwKh : rwKhList) {
			names.add(rwKh.getRealName());
			scores.add(rwKh.getTotalScore());
			counts.add(rwKh.getCount());
			
			BingData d = new BingData();
			d.setName(rwKh.getRealName());
			d.setValue(rwKh.getCount());
			data.add(d);
		}
		modelMap.put("names", names);
		modelMap.put("scores", scores);
		modelMap.put("counts", counts);
		modelMap.put("data", data);
		return modelMap;
	}
	
	/**
	 * 查询任务考核表中的数据,每个人的考核分数
	 * @param request
	 * @param page
	 * @param rows
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/list")
	public Map<String, Object> list(HttpServletRequest request,String page, String rows) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(rows));
		List<RwKh> rwKhList = rwKhService.listRwKh();
		if (null != rwKhList) {
			PageInfo<RwKh> pageInfo = new PageInfo<>(rwKhList);
			modelMap.put("rows", pageInfo.getList());
			modelMap.put("total", pageInfo.getTotal());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
		}
		return modelMap;
	}
	
	/**
	 * 刷新考核分数
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/refreshKaohe")
	public Map<String, Object> refreshKaohe() {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		try {
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
			modelMap.put("success", true);
		} catch(Exception e) {
			modelMap.put("success", false);
		}
		return modelMap;
	}
	
}
