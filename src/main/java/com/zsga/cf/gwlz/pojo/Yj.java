package com.zsga.cf.gwlz.pojo;

import java.util.Date;

/**
 * 意见建议
 * @author quadcopter
 *
 */
public class Yj {
	private Integer id;
	private String yjTitle;
	private String yjContent;
	private Date pubTime;
	private Integer userId;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getYjTitle() {
		return yjTitle;
	}
	public void setYjTitle(String yjTitle) {
		this.yjTitle = yjTitle;
	}
	public String getYjContent() {
		return yjContent;
	}
	public void setYjContent(String yjContent) {
		this.yjContent = yjContent;
	}
	public Date getPubTime() {
		return pubTime;
	}
	public void setPubTime(Date pubTime) {
		this.pubTime = pubTime;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
}
