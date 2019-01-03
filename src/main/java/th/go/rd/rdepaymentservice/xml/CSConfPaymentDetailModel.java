package th.go.rd.rdepaymentservice.xml;

import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CSConfPaymentDetailModel {
	@JsonProperty("SystemRefNo")
	private String systemRefNo;
	
	@JsonProperty("Amount")
	private Double amount;
	
	@JsonProperty("Nid")
	private String nid;
	
	@JsonProperty("NidBrano")
	private String nidBrano;
	
	@XmlElement(name="SystemRefNo")
	public String getSystemRefNo() {
		return systemRefNo;
	}

	public void setSystemRefNo(String systemRefNo) {
		this.systemRefNo = systemRefNo;
	}

	@XmlElement(name="Amount")
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@XmlElement(name="Nid")
	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	@XmlElement(name="NidBrano")
	public String getNidBrano() {
		return nidBrano;
	}

	public void setNidBrano(String nidBrano) {
		this.nidBrano = nidBrano;
	}

	@Override
	public String toString() {
		return "EPYConfPaymentDetail [systemRefNo=" + systemRefNo + ", amount=" + amount + ", nid=" + nid
				+ ", nidBrano=" + nidBrano + "]";
	}
}
