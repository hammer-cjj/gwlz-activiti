package com.zsga.cf.gwlz.pojo;

import java.util.Date;

public class Msg {
	private Integer id;
	//短信内容
	private String content;
	//短信发送时间
	private String sendTime;
	//发送标志0：添加任务；1：即将超期提醒
	private Integer flag;
	//发送到哪个手机
	private String tel;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
}
