package th.go.rd.rdepaymentservice.xml;

import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EpayStdPaymentInfoDetailModel {
	private String sysRefNo;
	
	private String departmentCode;
	
	private String payTo;
	
	private String formCode;
	
	private Double totalAmount;
	
	private String nid;
	
	private String nidBraNo;
	
	private String paymentMessage;

	@XmlElement(name="SystemRefNo")
	public String getSysRefNo() {
		return sysRefNo;
	}

	public void setSysRefNo(String sysRefNo) {
		this.sysRefNo = sysRefNo;
	}

	@XmlElement(name="DepartmentCode")
	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	@XmlElement(name="PayTo")
	public String getPayTo() {
		return payTo;
	}

	public void setPayTo(String payTo) {
		this.payTo = payTo;
	}

	@XmlElement(name="FormCode")
	public String getFormCode() {
		return formCode;
	}

	public void setFormCode(String formCode) {
		this.formCode = formCode;
	}

	@XmlElement(name="Amount")
	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	@XmlElement(name="Nid")
	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	@XmlElement(name="NidBrano")
	public String getNidBraNo() {
		return nidBraNo;
	}

	public void setNidBraNo(String nidBraNo) {
		this.nidBraNo = nidBraNo;
	}

	@XmlElement(name="PaymentMessage")
	public String getPaymentMessage() {
		return paymentMessage;
	}

	public void setPaymentMessage(String paymentMessage) {
		this.paymentMessage = paymentMessage;
	}

	@Override
	public String toString() {
		return "EpayStdPaymentInfoDetailModel [sysRefNo=" + sysRefNo + ", departmentCode=" + departmentCode + ", payTo="
				+ payTo + ", formCode=" + formCode + ", amount=" + totalAmount + ", nid=" + nid + ", nidBraNo=" + nidBraNo
				+ ", paymentMessage=" + paymentMessage + "]";
	}
	
	
}
