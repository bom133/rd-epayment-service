package th.go.rd.rdepaymentservice.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MasterPaymentLineDto {

	private long masterPaymentLineId;
	@JsonProperty("masterStatus")
	@NotNull(message = "{app.val-resp.notblank}")
	private MasterStatusDto masterStatusDto;
	@NotBlank
	@Size(max = 10,message = "{app.val-resp.size}")
	private String payLineCode;
	@NotBlank(message = "{app.val-resp.notblank}")
	@Size(max = 200,message = "{app.val-resp.size}")
	private String payLineNameTh;
	@NotBlank(message = "{app.val-resp.notblank}")
	@Size(max = 200,message = "{app.val-resp.size}")
	private String payLineNameEn;
	private String createBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC+07:00")
	private Date createDate;
	private String updateBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC+07:00")
	private Date updateDate;
	
	public MasterPaymentLineDto(){
		super();
	}
	
	public MasterPaymentLineDto(long masterPaymentLineId, MasterStatusDto masterStatusDto, String payLineCode, String payLineNameTh, String payLineNameEn, String createBy, Date createDate,
			String updateBy, Date updateDate) {
		super();
		this.masterPaymentLineId = masterPaymentLineId;
		this.masterStatusDto = masterStatusDto;
		this.payLineCode = payLineCode;
		this.payLineNameTh = payLineNameTh;
		this.payLineNameEn = payLineNameEn;
		this.createBy = createBy;
		this.createDate = createDate;
		this.updateBy = updateBy;
		this.updateDate = updateDate;
	}
	public long getMasterPaymentLineId() {
		return masterPaymentLineId;
	}
	public void setMasterPaymentLineId(long masterPaymentLineId) {
		this.masterPaymentLineId = masterPaymentLineId;
	}
	public MasterStatusDto getMasterStatusDto() {
		return masterStatusDto;
	}
	public void setMasterStatusDto(MasterStatusDto masterStatusDto) {
		this.masterStatusDto = masterStatusDto;
	}
	public String getPayLineCode() {
		return payLineCode;
	}
	public void setPayLineCode(String payLineCode) {
		this.payLineCode = payLineCode;
	}
	public String getPayLineNameTh() {
		return payLineNameTh;
	}
	public void setPayLineNameTh(String payLineNameTh) {
		this.payLineNameTh = payLineNameTh;
	}
	public String getPayLineNameEn() {
		return payLineNameEn;
	}
	public void setPayLineNameEn(String payLineNameEn) {
		this.payLineNameEn = payLineNameEn;
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
		return "MasterPaymentLineDto [masterPaymentLineId=" + masterPaymentLineId + ", masterStatusDto=" + masterStatusDto + ", payLineCode=" + payLineCode + ", payLineNameTh=" + payLineNameTh
				+ ", payLineNameEn=" + payLineNameEn + ", createBy=" + createBy + ", createDate=" + createDate + ", updateBy=" + updateBy + ", updateDate=" + updateDate + "]";
	}
	
	
}
