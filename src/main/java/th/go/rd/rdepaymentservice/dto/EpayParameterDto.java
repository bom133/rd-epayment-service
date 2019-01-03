package th.go.rd.rdepaymentservice.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EpayParameterDto {

	private long epayParameterId;

	@NotNull(message = "{app.val-resp.notblank}")
	@JsonProperty("masterParamType")
	private MasterParamTypeDto masterParamTypeDto;

	@NotNull(message = "{app.val-resp.notblank}")
	@Size(max = 30,message="{app.val-resp.size}")
	private String paramCode;

	@NotNull(message = "{app.val-resp.notblank}")
	@Size(max = 100,message="{app.val-resp.size}")
	private String paramValue;

	@Size(max= 500,message="{app.val-resp.size}")
	private String description;
	private String createBy;
	private Date createDate;
	private String updateBy;
	private Date updateDate;
	
	public EpayParameterDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public EpayParameterDto(long epayParameterId, MasterParamTypeDto masterParamTypeDto, String paramCode, String paramValue, String description, String createBy, Date createDate, String updateBy,
			Date updateDate) {
		super();
		this.epayParameterId = epayParameterId;
		this.masterParamTypeDto = masterParamTypeDto;
		this.paramCode = paramCode;
		this.paramValue = paramValue;
		this.description = description;
		this.createBy = createBy;
		this.createDate = createDate;
		this.updateBy = updateBy;
		this.updateDate = updateDate;
	}
	public long getEpayParameterId() {
		return epayParameterId;
	}
	public void setEpayParameterId(long epayParameterId) {
		this.epayParameterId = epayParameterId;
	}
	public MasterParamTypeDto getMasterParamTypeDto() {
		return masterParamTypeDto;
	}
	public void setMasterParamTypeDto(MasterParamTypeDto masterParamTypeDto) {
		this.masterParamTypeDto = masterParamTypeDto;
	}
	public String getParamCode() {
		return paramCode;
	}
	public void setParamCode(String paramCode) {
		this.paramCode = paramCode;
	}
	public String getParamValue() {
		return paramValue;
	}
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	@Override
	public String toString() {
		return "EpayParameterDto [epayParameterId=" + epayParameterId + ", masterParamTypeDto=" + masterParamTypeDto + ", paramCode=" + paramCode + ", paramValue=" + paramValue + ", description="
				+ description + ", createBy=" + createBy + ", createDate=" + createDate + ", updateBy=" + updateBy + ", updateDate=" + updateDate + "]";
	}
	
	
}
