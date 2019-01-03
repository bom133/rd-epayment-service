package th.go.rd.rdepaymentservice.dto;

import javax.persistence.Column;

public class EpayParameterSearchDto {
	
	private long epayParameterId;
	private String parameterType;
	private String paramCode;
	private String paramValue;
	private String description;
	
	@Column(name="EPAY_PARAMETER_ID")
	public long getEpayParameterId() {
		return epayParameterId;
	}
	public void setEpayParameterId(long epayParameterId) {
		this.epayParameterId = epayParameterId;
	}
	
	@Column(name="PARAM_TYPE")
	public String getParameterType() {
		return parameterType;
	}
	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
	}
	
	@Column(name="PARAM_CODE")
	public String getParamCode() {
		return paramCode;
	}
	public void setParamCode(String paramCode) {
		this.paramCode = paramCode;
	}
	
	@Column(name="PARAM_VALUE")
	public String getParamValue() {
		return paramValue;
	}
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	
	@Column(name="DESCRIPTION")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
