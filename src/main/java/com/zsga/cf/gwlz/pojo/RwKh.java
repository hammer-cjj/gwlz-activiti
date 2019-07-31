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
	private Integer totalScore;
	//重要任务个数
	private Integer zyTotal;
	//普通任务个数
	private Integer ptTotal;
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
	public Integer getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}
	public Integer getZyTotal() {
		return zyTotal;
	}
	public void setZyTotal(Integer zyTotal) {
		this.zyTotal = zyTotal;
	}
	public Integer getPtTotal() {
		return ptTotal;
	}
	public void setPtTotal(Integer ptTotal) {
		this.ptTotal = ptTotal;
	}
	
}
