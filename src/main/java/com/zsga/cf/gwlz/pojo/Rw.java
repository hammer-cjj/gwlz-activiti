package com.zsga.cf.gwlz.pojo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class Rw {
	//编号
	private Integer id;
	//任务标题
	private String rwTitle;
	//任务开始时间
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date startTime;
	//任务截至时间
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date endTime;
	//任务分配人Id
	private Integer rwFpId;
	//任务责任人Id
	private Integer rwZrId;
	//任务参与人Id
	private String rwCyId;
	//任务内容
	private String rwContent;
	//任务附件
	private String rwFj;
	//任务附件真实名称
	private String rwFjName;
	//任务发布时间
	private Date pubTime;
	//任务完成标志0：未签收；1：进行中；2：已完成
	private Integer state;
	//超期标志0：未超期；1：已超期
	private Integer cq;
	//任务分数，根据完成情况,较好：3（系数为1.5），一般：2（系数为1），部分完成（系数为0.5）：1，未完成：0（系数为0）
	private Integer completeQK;
	//任务完成时效 0：未完成；1：提前完成；2：按时完成；3：超期完成；
	private Integer completeSX;
	//任务完成日期
	private Date completeDate;
	//任务是否督办0：不督办；1：督办
	private Integer bwFlag;
	//任务难度 较难：5；困难：3；一般：1
	private Integer nandu;
	//任务重要性: 比较重要:3;重要:2;一般:1
	private Integer zhongyao;
	//任务等级: 特别紧急:3;紧急:2;一般:1
	private Integer dengji;
	//审核 0：未审核；1：已审核
	private Integer shenhe;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRwTitle() {
		return rwTitle;
	}
	public void setRwTitle(String rwTitle) {
		this.rwTitle = rwTitle;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Integer getRwFpId() {
		return rwFpId;
	}
	public void setRwFpId(Integer rwFpId) {
		this.rwFpId = rwFpId;
	}
	public Integer getRwZrId() {
		return rwZrId;
	}
	public void setRwZrId(Integer rwZrId) {
		this.rwZrId = rwZrId;
	}
	public String getRwCyId() {
		return rwCyId;
	}
	public void setRwCyId(String rwCyId) {
		this.rwCyId = rwCyId;
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
	public Date getPubTime() {
		return pubTime;
	}
	public void setPubTime(Date pubTime) {
		this.pubTime = pubTime;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getRwFjName() {
		return rwFjName;
	}
	public void setRwFjName(String rwFjName) {
		this.rwFjName = rwFjName;
	}
	public Integer getCq() {
		return cq;
	}
	public void setCq(Integer cq) {
		this.cq = cq;
	}
	public Integer getCompleteSX() {
		return completeSX;
	}
	public void setCompleteSX(Integer completeSX) {
		this.completeSX = completeSX;
	}
	public Date getCompleteDate() {
		return completeDate;
	}
	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}
	public Integer getBwFlag() {
		return bwFlag;
	}
	public void setBwFlag(Integer bwFlag) {
		this.bwFlag = bwFlag;
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
	public Integer getCompleteQK() {
		return completeQK;
	}
	public void setCompleteQK(Integer completeQK) {
		this.completeQK = completeQK;
	}
	public Integer getShenhe() {
		return shenhe;
	}
	public void setShenhe(Integer shenhe) {
		this.shenhe = shenhe;
	}
}
