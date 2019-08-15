package com.zsga.cf.gwlz.pojo;
/**
 * 任务考核实体类
 * @author quadcopter
 *
 */
public class RwKh {
	//编号
	private Integer id;
	//用户Id
	private Integer userId;
	//真实姓名
	private String realName;
	//总分
	private Double totalScore;
	//任务个数
	private Integer count;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public Double getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(Double totalScore) {
		this.totalScore = totalScore;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
}
