package com.zsga.cf.gwlz.pojo;

import java.util.Date;

public class RwFk {
	private Integer id;
	//任务Id
	private Integer rwId;
	//反馈内容
	private String content;
	//反馈时间
	private Date fkTime;
	//反馈者Id
	private Integer userId;
	//反馈附件
	private String fkFj;
	//反馈附件真实名称
	private String fkFjName;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getRwId() {
		return rwId;
	}
	public void setRwId(Integer rwId) {
		this.rwId = rwId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getFkTime() {
		return fkTime;
	}
	public void setFkTime(Date fkTime) {
		this.fkTime = fkTime;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getFkFj() {
		return fkFj;
	}
	public void setFkFj(String fkFj) {
		this.fkFj = fkFj;
	}
	public String getFkFjName() {
		return fkFjName;
	}
	public void setFkFjName(String fkFjName) {
		this.fkFjName = fkFjName;
	}
	
}
