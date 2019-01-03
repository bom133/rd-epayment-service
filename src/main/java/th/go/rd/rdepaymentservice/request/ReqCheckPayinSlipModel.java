package th.go.rd.rdepaymentservice.request;

import java.math.BigDecimal;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import th.go.rd.rdepaymentservice.component.JsonDecimalSerializer;

import io.swagger.annotations.ApiModelProperty;

public class ReqCheckPayinSlipModel {
	
	@ApiModelProperty(value = "เลขประจำตัวผู้เสียภาษีอากรของ Agent", required = true)
	@Size(min=13, max=13,message="{app.val-resp.size}")
	private String agentId;
	@JsonSerialize(using=JsonDecimalSerializer.class)
	private BigDecimal totalAmount;
	private String controlCode;
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
	@Override
	public String toString() {
		return "ReqCheckPayinSlipModel [agentId=" + agentId + ", totalAmount=" + totalAmount + ", controlCode="
				+ controlCode + "]";
	}
}
