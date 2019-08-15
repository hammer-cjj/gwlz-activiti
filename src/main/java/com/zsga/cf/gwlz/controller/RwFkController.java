package com.zsga.cf.gwlz.controller;

import java.io.File;
import java.io.IOException;
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
import com.zsga.cf.gwlz.pojo.Rw;
import com.zsga.cf.gwlz.pojo.RwFk;
import com.zsga.cf.gwlz.pojo.RwFkExt;
import com.zsga.cf.gwlz.pojo.User;
import com.zsga.cf.gwlz.service.RwFkService;
import com.zsga.cf.gwlz.service.RwService;
import com.zsga.cf.gwlz.util.InterfaceUtil;

@Controller
@RequestMapping("/rwFk")
public class RwFkController {
	
	@Autowired
	private RwFkService rwFkService;
	@Autowired
	private RwService rwService;
	
	/**
	 * 查询任务反馈列表
	 * @param rwId
	 * @param request
	 * @param page
	 * @param rows
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/listRwFk")
	public Map<String, Object> list(@RequestParam("rwId") String rwId,HttpServletRequest request, String page, String rows) {
		Map<String, Object> modelMap = new HashMap<>();
		PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(rows));
		//获取session中的用户，即当前登入用户
//		User user = (User) request.getSession().getAttribute("currentUser");
		List<RwFkExt> rwFkList = rwFkService.listRwFk(Integer.parseInt(rwId));
		if (null != rwFkList) {
			PageInfo<RwFkExt> pageInfo = new PageInfo<>(rwFkList);
			modelMap.put("rows", pageInfo.getList());
			modelMap.put("total", pageInfo.getTotal());
		}
		return modelMap;
	}
	
	
	/**
	 * 添加反馈,批示
	 * @param rwFk
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveRwFk")
	@ResponseBody
	public Map<String, Object> saveRwFk(MultipartFile fj, RwFk rwFk, HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		int resultCode = 0;
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
			
			//可使用预览转码队列，将需要预览的文件url放入队列中，提前进行转码，
			//InterfaceUtil.doGet("http://127.0.0.1:8012/addTask?url=http://127.0.0.1:8080/gwlz-activiti/"+rwFk.getFkFj(), "utf-8");
		}
		
		//批示任务完成情况 0：未完成；1:部分完成；2：一般；3：较好
		String completeQK = request.getParameter("completeQK");
		if (null != completeQK) {
			int qkFlag = Integer.parseInt(completeQK);
			Rw rw = rwService.findRwById(rwFk.getRwId());
			int flag = 0;
			switch (qkFlag) {
				case 3:
					flag = 3; 
					break;
				case 2:
					flag = 2; 
					break;
				case 1:
					flag = 1; 
					break;
				default:
					flag = 0; 
					break;
			}
			rw.setCompleteQK(flag);
			rwService.editRw(rw); //更新任务分数
			
			//添加到考核表
		}
		
		rwFk.setUserId(user.getId());
		rwFk.setFkTime(new Date());
		resultCode = rwFkService.addRwFk(rwFk);
		if (resultCode > 0) {
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
		}
		return modelMap;
	}
}
