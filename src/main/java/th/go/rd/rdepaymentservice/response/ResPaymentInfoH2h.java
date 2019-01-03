package th.go.rd.rdepaymentservice.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import th.go.rd.rdepaymentservice.component.JsonDecimalSerializer;

public class ResPaymentInfoH2h {

    private String agentId;
    private String controlCode;
	@JsonSerialize(using=JsonDecimalSerializer.class)
    private BigDecimal totalAmount;
	public ResPaymentInfoH2h() {
		super();
	}
	public ResPaymentInfoH2h(String agentId, String controlCode, BigDecimal totalAmount) {
		super();
		this.agentId = agentId;
		this.controlCode = controlCode;
		this.totalAmount = totalAmount;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getControlCode() {
		return controlCode;
	}
	public void setControlCode(String controlCode) {
		this.controlCode = controlCode;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	@Override
	public String toString() {
		return "PaymentInfoH2h [agentId=" + agentId + ", controlCode=" + controlCode + ", totalAmount=" + totalAmount + "]";
	}
    
    
}
