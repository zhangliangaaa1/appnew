package com.yili.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by ANDY on 2016/12/3.
 */
public class MobileClaimInfoVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long claimId;
	private Long pendingId;
	private String claimNo;
	private String itemName;
	private String applyUserName;
	private String applyGroupName;
	private String applyDate;
	private String orgName;
	private BigDecimal applyAmount;
	private BigDecimal payAmount;
	private String summary;
	private String wfstate;

	private String item2Name;           //业务类别
	private String expenseIssuerName;   //报销人名称
	
	private Long id;
	private String submitDate;
	
	private List<MobileProcessHistoryVo> processHistoryList;

	public Long getClaimId() {
		return claimId;
	}
	public void setClaimId(Long claimId) {
		this.claimId = claimId;
	}

	public Long getPendingId() {
		return pendingId;
	}
	public void setPendingId(Long pendingId) {
		this.pendingId = pendingId;
	}

	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getApplyUserName() {
		return applyUserName;
	}
	public void setApplyUserName(String applyUserName) {
		this.applyUserName = applyUserName;
	}

	public String getApplyGroupName() {
		return applyGroupName;
	}
	public void setApplyGroupName(String applyGroupName) {
		this.applyGroupName = applyGroupName;
	}

	public String getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}

	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public BigDecimal getApplyAmount() {
		return applyAmount;
	}
	public void setApplyAmount(BigDecimal applyAmount) {
		this.applyAmount = applyAmount;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getItem2Name() {
		return item2Name;
	}
	public void setItem2Name(String item2Name) {
		this.item2Name = item2Name;
	}

	public String getExpenseIssuerName() {
		return expenseIssuerName;
	}
	public void setExpenseIssuerName(String expenseIssuerName) {
		this.expenseIssuerName = expenseIssuerName;
	}
	public String getWfstate() {
		return wfstate;
	}
	public void setWfstate(String wfstate) {
		this.wfstate = wfstate;
	}
	public List<MobileProcessHistoryVo> getProcessHistoryList() {
		return processHistoryList;
	}
	public void setProcessHistoryList(
			List<MobileProcessHistoryVo> processHistoryList) {
		this.processHistoryList = processHistoryList;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSubmitDate() {
		return submitDate;
	}
	public void setSubmitDate(String submitDate) {
		this.submitDate = submitDate;
	}
	
}
