package com.zsga.cf.gwlz.controller;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zsga.cf.gwlz.exceptions.UserSessionException;
import com.zsga.cf.gwlz.mapper.MsgMapper;
import com.zsga.cf.gwlz.mapper.RwMapper;
import com.zsga.cf.gwlz.pojo.Msg;
import com.zsga.cf.gwlz.pojo.Rw;
import com.zsga.cf.gwlz.pojo.RwDetail;
import com.zsga.cf.gwlz.pojo.RwExt;
import com.zsga.cf.gwlz.pojo.RwFk;
import com.zsga.cf.gwlz.pojo.RwFp;
import com.zsga.cf.gwlz.pojo.RwKh;
import com.zsga.cf.gwlz.pojo.User;
import com.zsga.cf.gwlz.service.RwFkService;
import com.zsga.cf.gwlz.service.RwFpService;
import com.zsga.cf.gwlz.service.RwService;
import com.zsga.cf.gwlz.service.UserService;
import com.zsga.cf.gwlz.util.MessageUtil;

@Controller
@RequestMapping("/rw")
public class RwController {
	
	@Autowired
	private RwService rwService;
	@Autowired
	private RwFpService rwFpService;
	@Autowired
	private RwFkService rwFkService;
	@Autowired
	private UserService userService;
	
	@Autowired
	private RwMapper rwMapper;
	@Autowired
	private MsgMapper msgMapper;
	
	/**
	 * 查询有关于我的任务
	 * @param request
	 * @return
	 */
	@RequestMapping("/findMyRw")
	@ResponseBody
	public Map<String, Object> findMyRw(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String userId = request.getParameter("userId");
		User user = new User();
		user.setId(Integer.parseInt(userId));
		List<Rw> rwList = rwService.findMyRw(user);
		if (null != rwList) {
			PageInfo<Rw> pageInfo = new PageInfo<>(rwList);
			map.put("rows", pageInfo.getList());
			map.put("total", pageInfo.getTotal());
			map.put("success", true);
		} else {
			map.put("success", false);
		}
		return map;
	}
	
	
	/**
	 * 删除任务
	 * @param request
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Map<String, Object> deleteRw(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		//获取session中的用户，即当前登入用户
		User user = (User) request.getSession().getAttribute("currentUser");
		//只能单条删除
		String id=request.getParameter("id");
		/*
		List<String> list=new ArrayList<String>();
		String[] strs = id.split(",");
		for (String str : strs) {
			list.add(str);
		}
		*/
		try {
			//删除任务表
			rwService.removeRw(Integer.parseInt(id));
			//删除任务分配表
			rwFpService.delRw(Integer.parseInt(id));
			//删除任务反馈表
			rwFkService.delRwFk(Integer.parseInt(id));
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
		}
		
		return map;
	}
	
	/**
	 * 任务修改时查询任务
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping("/findRwById")
	@ResponseBody
	public Map<String, Object> findRwById(@RequestParam("id") String id, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Rw rw = rwService.findRwById(Integer.parseInt(id));
		//获取session中的用户，即当前登入用户
		User user = (User) request.getSession().getAttribute("currentUser");
		if (null != rw) {
			map.put("rw", rw);
			map.put("success", true);
			if (user.getRoleId() == 0) { //权限是管理者可以修改任意的任务
				map.put("isEdit", true);
			} else { //权限不是管理者只能修改自己分配的任务
				if (user.getId() == rw.getRwFpId()) { //当前登陆用户是否是该任务的分配人
					map.put("isEdit", true);
				} else {
					map.put("isEdit", false);
				}
			}
		} else {
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 统计超期，以及即将超期的任务
	 * @param request
	 * @return
	 */
	@RequestMapping("/tj")
	@ResponseBody
	public Map<String, Object> tj(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			//获取session中的用户，即当前登入用户
			User user = (User) request.getSession().getAttribute("currentUser");
			int resultWeiCqZr = 0;
			int resultWeiCqCy = 0;
			int resultWeiCqTotal = 0;
			if (user.getRoleId() == 1) {//处长权限
				map.put("flag", 1);
				resultWeiCqTotal = rwService.countWeiCq(user);
				map.put("weiCqTotal", resultWeiCqTotal);
			}else if (user.getRoleId() == 2) {//副处长权限
				map.put("flag", 2);
				//统计距离超期小等于3天的任务
				resultWeiCqZr = rwService.countWeiCqZr(user);
				resultWeiCqCy = rwService.countWeiCqCy(user);
				map.put("weiCqZr", resultWeiCqZr);
				map.put("weiCqCy", resultWeiCqCy);
			} else {
				map.put("flag", 3);
				//统计距离超期小等于3天的任务
				resultWeiCqZr = rwService.countWeiCqZr(user);
				resultWeiCqCy = rwService.countWeiCqCy(user);
				map.put("weiCqZr", resultWeiCqZr);
				map.put("weiCqCy", resultWeiCqCy);
			}
			//统计已经超期的任务
			//int resultCq = rwService.countCq(user);
			//map.put("cq", resultCq);
			
			map.put("success", true);
		} catch(Exception e) {
			map.put("success", false);
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 添加任务参与人
	 * @param rw
	 * @param request
	 * @return
	 */
	@RequestMapping("/addRwCy")
	@ResponseBody
	public Map<String, Object> addRwCy(Rw rw,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		int resultCode = 0;
		resultCode = rwMapper.addRwCy(rw);
		Rw resultRw = rwMapper.findRwById(rw.getId());
		if (resultCode > 0) {
			String[] rwCyId = (rw.getRwCyId()).split(",");
			//获取session中的用户，即当前登入用户
			User user = (User) request.getSession().getAttribute("currentUser");
			if (null != rwCyId) {
				for (int i=0; i<rwCyId.length; i++) {
					RwFp rwFp = new RwFp(); 
					rwFp.setReceiveId(Integer.parseInt(rwCyId[i]));
					rwFp.setRwId(rw.getId());
					rwFp.setSendId(user.getId());
					rwFpService.addRwFp(rwFp);
				}
				//需要发送短信的电话
				List<String> arrayTel = new ArrayList<String>();
				for (String s : rwCyId) {
					User cyUser = userService.findUserById(Integer.parseInt(s));
					arrayTel.add(cyUser.getTel());
				}
				Msg msg = new Msg();
				msg.setTel(arrayTel.toString());
				
				String[] destinationAddresses = new String[arrayTel.size()];
				for (int i=0; i<destinationAddresses.length; i++) {
					destinationAddresses[i] = arrayTel.get(i);
				}
				
				
				//发送时间
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String sendTime = sdf.format(new Date());
				msg.setSendTime(sendTime);
				
				//发送内容
				String message = "您有一条新的任务【"+resultRw.getRwTitle()+"】,请及时签收! " + sendTime;
				msg.setContent(message);
				
				//添加任务标志位
				msg.setFlag(0);
				
				//发送短信
				//MessageUtil.sendMessage(destinationAddresses, message);
				
				//添加短信
				msgMapper.addMsg(msg);
			} 
			map.put("success", true);
			map.put("id", rw.getId());
		} else {
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 任务完成
	 * @param fj
	 * @param rwFk
	 * @param request
	 * @return
	 */
	@RequestMapping("/complete")
	@ResponseBody
	public Map<String, Object> rwComplete(MultipartFile fj, RwFk rwFk,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		int resultCode = 0;
		resultCode = rwService.rwComplete(rwFk.getRwId());
		if (resultCode > 0) {
			//获取session中的用户，即当前登入用户
			User user = (User) request.getSession().getAttribute("currentUser");
			//上传附件
			String fileName = fj.getOriginalFilename();
			if (StringUtils.isNotBlank(fileName)) { //判断是否有文件上传
				String rootPath = request.getServletContext().getRealPath("upload");
				
				String suffix = fileName.substring(fileName.lastIndexOf("."));
				String tempFileName = UUID.randomUUID().toString() + suffix;
				File fileTemp = new File(rootPath);
				if (!fileTemp.exists()) {
					fileTemp.mkdir();
				}
				
				File file = new File(rootPath + "\\" + tempFileName);
				
				try {
					fj.transferTo(file);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				//存入相对路径
				rwFk.setFkFj("upload/" + tempFileName);
				//存入附件真实名称
				rwFk.setFkFjName(fileName);
			}
			
			rwFk.setUserId(user.getId());
			rwFk.setFkTime(new Date());
			
			rwFkService.addRwFk(rwFk);
			
			try {
				//获取任务
				Rw currentRw = rwService.findRwById(rwFk.getRwId());
				currentRw.setCompleteDate(rwFk.getFkTime()); //任务完成时间
				
				long between = (currentRw.getEndTime().getTime() + (24 * 3600 * 1000) - rwFk.getFkTime().getTime()) / 1000;
				long day = between / (24 * 3600);
				long remain = between % (24 * 3600);
				
				if (currentRw.getCq() == 1) { //超期完成
					currentRw.setCompleteSX(3);
				} else if ((day == 0 && remain > 0) || (day==1 && remain == 0)) { //按时完成
					currentRw.setCompleteSX(2);
				} else if (day >= 1) {
					currentRw.setCompleteSX(1); //提前完成
				}
				//默认任务完成情况为一般
				if (currentRw.getZyFlag() == 1) { //重要工作完成情况一般得2分
					currentRw.setScore(2);
				} else if (currentRw.getZyFlag() == 0){ //其它工作完成情况一般得1分
					currentRw.setScore(1);
				}
				
				//更新任务：分数，时效和完成时间
				rwService.editRw(currentRw);
				
				//添加到考核表
				
				map.put("success", true);
			} catch (Exception e) {
				e.printStackTrace();
				map.put("success", false);
			}
		} else {
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 任务签收
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping("/sign")
	@ResponseBody
	public Map<String, Object> sign(@RequestParam("id") String id,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		int resultCode = 0;
		resultCode = rwService.rwSign(Integer.parseInt(id));
		if (resultCode > 0) {
			//获取session中的用户，即当前登入用户
			User user = (User) request.getSession().getAttribute("currentUser");
			RwFk rwFk = new RwFk();
			rwFk.setRwId(Integer.parseInt(id));
			rwFk.setUserId(user.getId());
			rwFk.setFkTime(new Date());
			rwFk.setContent("任务已签收");
			rwFkService.addRwFk(rwFk);
			map.put("success", true);
		} else {
			map.put("success", false);
		}
		return map;
	}

	/**
	 * 上传图片,没在使用
	 * @param file
	 * @param request
	 * @return
	 */
	@RequestMapping("/upLoad")
	@ResponseBody
	public Map<String, Object> upLoadFile(MultipartFile file,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String fileName = file.getOriginalFilename();
		if (StringUtils.isNotBlank(fileName)) {
			String rootPath = request.getServletContext().getRealPath("upload");
			
			String suffix = fileName.substring(fileName.lastIndexOf("."));
			String tempFileName = UUID.randomUUID().toString() + suffix;
			File fileTemp = new File(rootPath);
			if (!fileTemp.exists()) {
				fileTemp.mkdir();
			}
			
			File targetfile = new File(rootPath + "\\" + tempFileName);
			
			try {
				file.transferTo(targetfile);
				map.put("error", 0);
				map.put("url","upload/"+tempFileName);
			} catch (IllegalStateException | IOException e) {
				map.put("error", 1);
				map.put("message", "文件上传失败，请稍后重新尝试！");
			}
		}
				
		return map;
	}
	
	/**
	 * 添加，修改任务
	 * @param rw
	 * @param reCyId
	 * @param fj
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/save")
	@ResponseBody
	public Map<String, Object> saveRw(Rw rw, String[] rwCyId, MultipartFile fj,HttpServletRequest request) throws Exception {
		int resultCode = 0;
		Map<String, Object> modelMap = new HashMap<>();
		
		//获取session中的用户，即当前登入用户
		User user = (User) request.getSession().getAttribute("currentUser");
		if (null == user) {
			//modelMap.put("errorMsg", "session失效!");
			//return modelMap;
			throw new UserSessionException("登陆超时，请重新登陆!");
		}
		//上传附件
		String fileName = fj.getOriginalFilename();
		if (StringUtils.isNotBlank(fileName)) { //判断是否有文件上传
			String rootPath = request.getServletContext().getRealPath("upload");
			
			String suffix = fileName.substring(fileName.lastIndexOf("."));
			String tempFileName = UUID.randomUUID().toString() + suffix;
			File fileTemp = new File(rootPath);
			if (!fileTemp.exists()) {
				fileTemp.mkdir();
			}
			
			File file = new File(rootPath + "\\" + tempFileName);
			
			try {
				fj.transferTo(file);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//存入相对路径
			rw.setRwFj("upload/" + tempFileName);
			//存入附件真实名称
			rw.setRwFjName(fileName);
		}
		
		//存入任务发布时间
		rw.setPubTime(new Date());
		//任务完成标志
		if (rw.getState() == null) {
			rw.setState(0);
		}
		//任务超期标志
		if (rw.getCq() == null) {
			rw.setCq(0);
		}
		//任务分数
		if (rw.getScore() == null) {
			rw.setScore(0);
		}
		//任务完成时效
		if (rw.getCompleteSX() == null) {
			rw.setCompleteSX(0);
		}
		//任务下发
		rw.setBwFlag(1);
		
		//是否是重要任务
		String zyFlag = request.getParameter("zyFlagStr");
		
		if ("on".equals(zyFlag)) {
			rw.setZyFlag(1);
		} else {
			rw.setZyFlag(0);
		}
		
		int sendMsgFlag = 0;//是否发送短信标志位：2：新增需要发送；1：修改责任人需要发送；0：不需要发送；3：参与人不同需要发送短信；4：责任人和参与人都不同
		
		if (null != rw.getId()) {
			//修改前的任务
			Rw orignalRw = rwService.findRwById(rw.getId());
			if (orignalRw.getRwZrId() != rw.getRwZrId() && orignalRw.getRwCyId() != rw.getRwCyId()) { //责任人和参与人不同
				sendMsgFlag = 4;
			} else if (orignalRw.getRwZrId() != rw.getRwZrId()) { //责任人不同需要发送
				sendMsgFlag = 1;
			} else if (orignalRw.getRwCyId() != rw.getRwCyId()) {  //参与人不同需要发送
				sendMsgFlag = 3;
			}
			//修改任务
			resultCode = rwService.editRw(rw);
		} else {
			//新增任务
			resultCode = rwService.addRw(rw);
			sendMsgFlag = 2;
		}
		
		if (0 == resultCode) { //插入任务表失败
			modelMap.put("errorMsg", "任务发布失败！");
		} else { //插入任务分配表
			//删除任务分配表中任务ID等于该任务的数据
			rwFpService.delRw(rw.getId());
			if (null != rwCyId) {
				for (int i=0; i<rwCyId.length; i++) {
					RwFp rwFp = new RwFp(); 
					rwFp.setReceiveId(Integer.parseInt(rwCyId[i]));
					rwFp.setRwId(rw.getId());
					rwFp.setSendId(user.getId());
					rwFpService.addRwFp(rwFp);
				}
			}
			modelMap.put("success", true);
			
			if (sendMsgFlag != 0) { //不等于零需要发送短信
				//需要发送短信的电话
				List<String> arrayTel = new ArrayList<String>();

				int cyFlag = 0; //参与人改变标志位 0：需要发送，1：不需要发送
				
				if (sendMsgFlag == 2 || sendMsgFlag == 4) { //新增任务或者责任人和参与人都不同需要发送短信
					//责任人
					User zrUser = userService.findUserById(rw.getRwZrId());
					arrayTel.add(zrUser.getTel());
					
					//任务参与人
					String cyIds = rw.getRwCyId();
					if (null != cyIds) {
						String[] cyIdsArray = cyIds.split(",");
						for (String s : cyIdsArray) {
							User cyUser = userService.findUserById(Integer.parseInt(s));
							arrayTel.add(cyUser.getTel());
						}
					}
				} else if (sendMsgFlag == 1) {  //责任人不同
					//责任人
					User zrUser = userService.findUserById(rw.getRwZrId());
					arrayTel.add(zrUser.getTel());
				} else if (sendMsgFlag == 3) { //参与人不同
					//任务参与人
					String cyIds = rw.getRwCyId();
					if (null != cyIds) {
						String[] cyIdsArray = cyIds.split(",");
						for (String s : cyIdsArray) {
							User cyUser = userService.findUserById(Integer.parseInt(s));
							arrayTel.add(cyUser.getTel());
						}
					} else {
						cyFlag = 1;
					}
				}
				
				if (cyFlag == 0) {
					Msg msg = new Msg();
					msg.setTel(arrayTel.toString());
					
					String[] destinationAddresses = new String[arrayTel.size()];
//				destinationAddresses[0] = zrUser.getTel();
					for (int i=0; i<destinationAddresses.length; i++) {
						destinationAddresses[i] = arrayTel.get(i);
					}
					
					
					//发送时间
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String sendTime = sdf.format(new Date());
					msg.setSendTime(sendTime);
					
					//发送内容
					String message = "您有一条新的任务【"+rw.getRwTitle()+"】,请及时签收! " + sendTime;
					msg.setContent(message);
					
					//添加任务标志位
					msg.setFlag(0);
					
					//发送短信
					//MessageUtil.sendMessage(destinationAddresses, message);
					
					//添加短信
					msgMapper.addMsg(msg);
				}
			}
		}
		
		return modelMap;
	}
	
	
	/**
	 * 添加，修改任务（存在备忘录中，暂时不下发）
	 * @param rw
	 * @param reCyId
	 * @param fj
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/saveBw")
	@ResponseBody
	public Map<String, Object> saveRwBw(Rw rw, String[] rwCyId, MultipartFile fj,HttpServletRequest request) throws Exception {
		int resultCode = 0;
		Map<String, Object> modelMap = new HashMap<>();
		
		//获取session中的用户，即当前登入用户
		User user = (User) request.getSession().getAttribute("currentUser");
		if (null == user) {
			throw new UserSessionException("登陆超时，请重新登陆!");
		}
		//上传附件
		String fileName = fj.getOriginalFilename();
		if (StringUtils.isNotBlank(fileName)) { //判断是否有文件上传
			String rootPath = request.getServletContext().getRealPath("upload");
			
			String suffix = fileName.substring(fileName.lastIndexOf("."));
			String tempFileName = UUID.randomUUID().toString() + suffix;
			File fileTemp = new File(rootPath);
			if (!fileTemp.exists()) {
				fileTemp.mkdir();
			}
			
			File file = new File(rootPath + "\\" + tempFileName);
			
			try {
				fj.transferTo(file);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//存入相对路径
			rw.setRwFj("upload/" + tempFileName);
			//存入附件真实名称
			rw.setRwFjName(fileName);
		}
		
		//存入任务发布时间
//		rw.setPubTime(null);
		//任务完成标志
		if (rw.getState() == null) {
			rw.setState(0);
		}
		//任务超期标志
		if (rw.getCq() == null) {
			rw.setCq(0);
		}
		//任务分数
		if (rw.getScore() == null) {
			rw.setScore(0);
		}
		//任务完成时效
		if (rw.getCompleteSX() == null) {
			rw.setCompleteSX(0);
		}
		//任务暂不下发
		rw.setBwFlag(0);
		
		//是否是重要任务
		String zyFlag = request.getParameter("zyFlagStr");
		
		if ("on".equals(zyFlag)) {
			rw.setZyFlag(1);
		} else {
			rw.setZyFlag(0);
		}
		
		if (null != rw.getId()) {
			//修改任务
			resultCode = rwService.editRw(rw);
		} else {
			//新增任务
			resultCode = rwService.addRw(rw);
		}
		
		if (0 == resultCode) { //插入任务表失败
			modelMap.put("errorMsg", "任务发布失败！");
		} else { //插入任务分配表
			//删除任务分配表中任务ID等于该任务的数据
			rwFpService.delRw(rw.getId());
			if (null != rwCyId) {
				for (int i=0; i<rwCyId.length; i++) {
					RwFp rwFp = new RwFp(); 
					rwFp.setReceiveId(Integer.parseInt(rwCyId[i]));
					rwFp.setRwId(rw.getId());
					rwFp.setSendId(user.getId());
					rwFpService.addRwFp(rwFp);
				}
			}
			modelMap.put("success", true);			
		}
		
		return modelMap;
	}
	
	/**
	 * 查询任务列表
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/list")
	public Map<String, Object> list(Rw rw,HttpServletRequest request, String page, String rows) {
		Map<String, Object> modelMap = new HashMap<>();
		PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(rows));
		//获取session中的用户，即当前登入用户
		User user = (User) request.getSession().getAttribute("currentUser");
		if (null != user) {
			if (user.getRoleId() == 1 || user.getRoleId() == 0) { //处长权限，可以查看所有任务
				List<RwExt> rwExtList = rwService.listAllRw(rw);
				if (null != rwExtList) {
					PageInfo<RwExt> pageInfo = new PageInfo<>(rwExtList);
					modelMap.put("rows", pageInfo.getList());
					modelMap.put("total", pageInfo.getTotal());
				}
			} 
		} else { //session失效
			modelMap.put("rows", new ArrayList());
			modelMap.put("total", 0);
			modelMap.put("errFlag", true);
		}
		return modelMap;
	}
	
	/**
	 * 查询备忘任务列表
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/listBw")
	public Map<String, Object> listBw(Rw rw,HttpServletRequest request, String page, String rows) {
		Map<String, Object> modelMap = new HashMap<>();
		PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(rows));
		//获取session中的用户，即当前登入用户
		User user = (User) request.getSession().getAttribute("currentUser");
		if (null != user) {
			if (user.getRoleId() == 1 || user.getRoleId() == 0) { //处长权限，可以查看所有任务
				List<RwExt> rwExtList = rwService.listAllRwBw(rw);
				if (null != rwExtList) {
					PageInfo<RwExt> pageInfo = new PageInfo<>(rwExtList);
					modelMap.put("rows", pageInfo.getList());
					modelMap.put("total", pageInfo.getTotal());
				}
			} 
		} else { //session失效
			modelMap.put("rows", new ArrayList());
			modelMap.put("total", 0);
			modelMap.put("errFlag", true);
		}
		return modelMap;
	}
	
	/**
	 * 查询我分配的任务列表
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/listFp")
	public Map<String, Object> listFp(Rw rw,HttpServletRequest request, String page, String rows) {
		Map<String, Object> modelMap = new HashMap<>();
		PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(rows));
		//获取session中的用户，即当前登入用户
		User user = (User) request.getSession().getAttribute("currentUser");
		if (null != user) {
			if (user.getRoleId() == 2) {  //副处长权限
				rw.setRwFpId(user.getId());
				List<RwExt> rwExtList = rwMapper.listFpRw(rw);
				if (null != rwExtList) {
					PageInfo<RwExt> pageInfo = new PageInfo<>(rwExtList);
					modelMap.put("rows", pageInfo.getList());
					modelMap.put("total", pageInfo.getTotal());
				}
			}
		} else {
			throw new UserSessionException("登陆超时，请重新登陆!");
		}
		return modelMap;
	}
	
	/**
	 * 查询我负责的任务
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/listZr")
	public Map<String, Object> listZr(Rw rw, HttpServletRequest request, String page, String rows) {
		Map<String, Object> modelMap = new HashMap<>();
		PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(rows));
		//获取session中的用户，即当前登入用户
		User user = (User) request.getSession().getAttribute("currentUser");
		if (null != user) {
			rw.setRwZrId(user.getId());
			List<RwExt> rwExtList = rwMapper.listZrRw(rw);
			if (null != rwExtList) {
				PageInfo<RwExt> pageInfo = new PageInfo<>(rwExtList);
				modelMap.put("rows", pageInfo.getList());
				modelMap.put("total", pageInfo.getTotal());
			}
		} else {
			throw new UserSessionException("登陆超时，请重新登陆!");
		}
		return modelMap;
	}
	
	
	/**
	 * 查询我参与的任务列表
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/listCy")
	public Map<String, Object> listCy(Rw rw,HttpServletRequest request, String page, String rows) {
		Map<String, Object> modelMap = new HashMap<>();
		PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(rows));
		//获取session中的用户，即当前登入用户
		User user = (User) request.getSession().getAttribute("currentUser");
		if (null != user) {
			rw.setRwCyId(Integer.toString(user.getId()));
			List<RwExt> rwExtList = rwMapper.listCyRw(rw);
			if (null != rwExtList) {
				PageInfo<RwExt> pageInfo = new PageInfo<>(rwExtList);
				modelMap.put("rows", pageInfo.getList());
				modelMap.put("total", pageInfo.getTotal());
			}
		} else {
			throw new UserSessionException("登陆超时，请重新登陆!");
		}
		return modelMap;
	}
	
	/**
	 * 查看任务详细情况
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping("/findRw")
	public String findRw(@RequestParam("id") String id,@RequestParam("flag") String flag, HttpServletRequest request) {
//		Map<String, Object> modelMap = new HashMap<>();
		RwDetail rwDetail = new RwDetail();
		/**
		 * 查询任务责任人，任务种类，任务起止日期，标题，内容，附件等
		 */
		RwExt rwExt = rwMapper.findRwExt(Integer.parseInt(id));
		//设置任务ID
		rwDetail.setId(rwExt.getId());
		//设置状态
		rwDetail.setState(rwExt.getState());
		//设置标题
		rwDetail.setRwTitle(rwExt.getRwTitle());
		//设置任务内容
		rwDetail.setRwContent(rwExt.getRwContent());
		//设置任务附件
		rwDetail.setRwFj(rwExt.getRwFj());
		//设置任务附件真实名称
		rwDetail.setRwFjName(rwExt.getRwFjName());
		//设置任务责任人
		rwDetail.setZrName(rwExt.getRealName());
		//设置任务起止日期
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		rwDetail.setStartTimeStr(sdf.format(rwExt.getStartTime()));
		rwDetail.setEndTimeStr(sdf.format(rwExt.getEndTime()));
		//设置任务完成日期
		if (null != rwExt.getCompleteDate()) {
			DateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			rwDetail.setCompleteDate(sdf2.format(rwExt.getCompleteDate()));
		}
		//设置任务完成时效
		rwDetail.setCompleteSX(rwExt.getCompleteSX());
		//设置任务分数
		rwDetail.setScore(rwExt.getScore());
		//设置是否重要
		rwDetail.setZyFlag(rwExt.getZyFlag());
		//设置任务类型
		rwDetail.setCategoryName(rwExt.getCategoryName());
		/**
		 * 查询任务分配人
		 */
		User rwFpUser = rwMapper.findRwFp(rwExt.getRwFpId());
		//设置任务分配人
		rwDetail.setFpName(rwFpUser.getRealName());
		/**
		 * 查询任务参与人
		 */
		String rwCyIds = rwExt.getRwCyId();
		if (null != rwCyIds) {
			String[] cyIds = rwCyIds.split(",");
			List<User> rwCyUserList = rwMapper.findRwCy(cyIds);
			//设置任务参与人
			rwDetail.setCyUserList(rwCyUserList);
		}
//		modelMap.put("rwDetail", rwDetail);
		request.setAttribute("rwDetail", rwDetail);
		request.setAttribute("flag", Integer.parseInt(flag));
		return "rwDetail";
//		return modelMap;
	}
	
	/**
	 * 备忘录中完成督办
	 * @param id
	 * @return
	 */
	@RequestMapping("/completeDb")
	@ResponseBody
	public Map<String, Object> completeDb(@RequestParam("id") String id) {
		Map<String, Object> modelMap = new HashMap<>();
		int code = rwService.completeDb(Integer.parseInt(id));
		if (code > 0) {
			//需要发送短信的电话
			List<String> arrayTel = new ArrayList<String>();
			
			Rw rw = rwService.findRwById(Integer.parseInt(id));
			
			//责任人
			User zrUser = userService.findUserById(rw.getRwZrId());
			arrayTel.add(zrUser.getTel());
			
			//任务参与人
			String cyIds = rw.getRwCyId();
			if (null != cyIds) {
				String[] cyIdsArray = cyIds.split(",");
				for (String s : cyIdsArray) {
					User cyUser = userService.findUserById(Integer.parseInt(s));
					arrayTel.add(cyUser.getTel());
				}
			}
			
			Msg msg = new Msg();
			msg.setTel(arrayTel.toString());
			
			String[] destinationAddresses = new String[arrayTel.size()];
			for (int i=0; i<destinationAddresses.length; i++) {
				destinationAddresses[i] = arrayTel.get(i);
			}
			
			
			//发送时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String sendTime = sdf.format(new Date());
			msg.setSendTime(sendTime);
			
			//发送内容
			String message = "您有一条新的任务【"+rw.getRwTitle()+"】,请及时签收! " + sendTime;
			msg.setContent(message);
			
			//添加任务标志位
			msg.setFlag(0);
			
			//发送短信
			//MessageUtil.sendMessage(destinationAddresses, message);
			
			//添加短信
			msgMapper.addMsg(msg);
			
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
		}
		return modelMap;
	}
}
