package th.go.rd.rdepaymentservice.response;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import th.go.rd.rdepaymentservice.component.JsonDecimalSerializer;

public class ResCheckPayinSlipModel {
	private String agentId;
	@JsonSerialize(using=JsonDecimalSerializer.class)
	private BigDecimal totalAmount;
	private String controlCode;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC+07:00")
	private Date expireDate;
	private String payinSlipStatus;
	private String checkPayinStatus;
	
	
	
	public ResCheckPayinSlipModel() {
		super();
	}
	
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getControlCode() {
		return controlCode;
	}
	public void setControlCode(String controlCode) {
		this.controlCode = controlCode;
	}
	public Date getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	public String getPayinSlipStatus() {
		return payinSlipStatus;
	}
	public void setPayinSlipStatus(String payinSlipStatus) {
		this.payinSlipStatus = payinSlipStatus;
	}

	public String getCheckPayinStatus() {
		return checkPayinStatus;
	}

	public void setCheckPayinStatus(String checkPayinStatus) {
		this.checkPayinStatus = checkPayinStatus;
	}
	
	
	
}
