package com.zsga.cf.gwlz.pojo;

import java.util.List;
/**
 * 任务详细实体类,用于查看任务详细
 * @author quadcopter
 *
 */
public class RwDetail {
	//任务ID
	private Integer id;
	//责任人
	private String zrName;
	//分配人
	private String fpName;
	//参与人
	private List<User> cyUserList;
	//任务标题
	private String rwTitle;
	//任务开始日期
	private String startTimeStr;
	//任务结束日期
	private String endTimeStr;
	//任务内容
	private String rwContent;
	//附件
	private String rwFj;
	//附件真实名称
	private String rwFjName;
	//任务类别
	//private String categoryName;
	//任务状态
	private Integer state;
	//任务完成日期
	private String completeDate;
	//任务完成时效
	private Integer completeSX;
	//任务分数,按照完成情况分：较好：3，一般：2，未完成：0
	private Integer completeQK;
	//任务难度
	private Integer nandu;
	//任务重要性
	private Integer zhongyao;
	//任务等级
	private Integer dengji;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public List<User> getCyUserList() {
		return cyUserList;
	}
	public void setCyUserList(List<User> cyUserList) {
		this.cyUserList = cyUserList;
	}
	public String getRwContent() {
		return rwContent;
	}
	public void setRwContent(String rwContent) {
		this.rwContent = rwContent;
	}
	public String getRwFj() {
		return rwFj;
	}
	public void setRwFj(String rwFj) {
		this.rwFj = rwFj;
	}
	public String getZrName() {
		return zrName;
	}
	public void setZrName(String zrName) {
		this.zrName = zrName;
	}
	public String getFpName() {
		return fpName;
	}
	public void setFpName(String fpName) {
		this.fpName = fpName;
	}
	public String getRwTitle() {
		return rwTitle;
	}
	public void setRwTitle(String rwTitle) {
		this.rwTitle = rwTitle;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getStartTimeStr() {
		return startTimeStr;
	}
	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}
	public String getEndTimeStr() {
		return endTimeStr;
	}
	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}
	public String getRwFjName() {
		return rwFjName;
	}
	public void setRwFjName(String rwFjName) {
		this.rwFjName = rwFjName;
	}
	public String getCompleteDate() {
		return completeDate;
	}
	public void setCompleteDate(String completeDate) {
		this.completeDate = completeDate;
	}
	public Integer getCompleteSX() {
		return completeSX;
	}
	public void setCompleteSX(Integer completeSX) {
		this.completeSX = completeSX;
	}
	public Integer getCompleteQK() {
		return completeQK;
	}
	public void setCompleteQK(Integer completeQK) {
		this.completeQK = completeQK;
	}
	public Integer getNandu() {
		return nandu;
	}
	public void setNandu(Integer nandu) {
		this.nandu = nandu;
	}
	public Integer getZhongyao() {
		return zhongyao;
	}
	public void setZhongyao(Integer zhongyao) {
		this.zhongyao = zhongyao;
	}
	public Integer getDengji() {
		return dengji;
	}
	public void setDengji(Integer dengji) {
		this.dengji = dengji;
	}
}
