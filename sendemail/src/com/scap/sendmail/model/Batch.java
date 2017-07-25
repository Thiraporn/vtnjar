package com.scap.sendmail.model;

public class Batch {
	
	private String batchNo;
	private String hospitalCode;
    private String yyyy;
    private String mm;
    private String createDate;
    private String createTime;
    private String closeDate;
    private String closeTime; 
    private String createByUserId; 
    private String closeByUserId; 
    private String payment;
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
	public String getYyyy() {
		return yyyy;
	}
	public void setYyyy(String yyyy) {
		this.yyyy = yyyy;
	}
	public String getMm() {
		return mm;
	}
	public void setMm(String mm) {
		this.mm = mm;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCloseDate() {
		return closeDate;
	}
	public void setCloseDate(String closeDate) {
		this.closeDate = closeDate;
	}
	public String getCloseTime() {
		return closeTime;
	}
	public void setCloseTime(String closeTime) {
		this.closeTime = closeTime;
	}
	public String getCreateByUserId() {
		return createByUserId;
	}
	public void setCreateByUserId(String createByUserId) {
		this.createByUserId = createByUserId;
	}
	public String getCloseByUserId() {
		return closeByUserId;
	}
	public void setCloseByUserId(String closeByUserId) {
		this.closeByUserId = closeByUserId;
	}
	public String getPayment() {
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	} 
    
    

}
