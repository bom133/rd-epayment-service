package th.go.rd.rdepaymentservice.dto;

import java.sql.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MasterReceiverUnitDto {

	private long masterReceiverUnitId;
	@JsonProperty("masterReceiverType")
	private MasterReceiverTypeDto masterReceiverTypeDto;
	@JsonProperty("masterStatus")
	private MasterStatusDto masterStatusDto;

	private Integer recFileNo;
	
	@NotBlank(message = "{app.val-resp.notblank}")
	@Size(max = 3,message = "{app.val-resp.size}")
	private String recCode;
	
	@NotBlank(message = "{app.val-resp.notblank}")
	@Size(max = 50,message = "{app.val-resp.size}")
	private String recShortNameTh;
	
	@NotBlank(message = "{app.val-resp.notblank}")
	@Size(max = 50,message = "{app.val-resp.size}")
	private String recShortNameEn;
	
	@NotBlank(message = "{app.val-resp.notblank}")
	@Size(max = 400,message = "{app.val-resp.size}")
	private String recNameTh;
	
	@NotBlank(message = "{app.val-resp.notblank}")
	@Size(max = 400,message = "{app.val-resp.size}")
	private String recNameEn;
	
	private String imagePath;
	public MasterReceiverUnitDto() {
		super();
	}
	public MasterReceiverUnitDto(long masterReceiverUnitId, MasterReceiverTypeDto masterReceiverTypeDto, MasterStatusDto masterStatusDto, String recCode,
			String recShortNameTh, String recShortNameEn, String recNameTh,
			String recNameEn, String imagePath, String createBy, Date createDate, String updateBy, Date updateDate,Integer recFileNo) {
		super();
		this.masterReceiverUnitId = masterReceiverUnitId;
		this.masterReceiverTypeDto = masterReceiverTypeDto;
		this.masterStatusDto = masterStatusDto;
		this.recCode = recCode;
		this.recShortNameTh = recShortNameTh;
		this.recShortNameEn = recShortNameEn;
		this.recNameTh = recNameTh;
		this.recNameEn = recNameEn;
		this.imagePath = imagePath;
		this.recFileNo = recFileNo;
	}
	public long getMasterReceiverUnitId() {
		return masterReceiverUnitId;
	}
	public void setMasterReceiverUnitId(long masterReceiverUnitId) {
		this.masterReceiverUnitId = masterReceiverUnitId;
	}
	public MasterReceiverTypeDto getMasterReceiverTypeDto() {
		return masterReceiverTypeDto;
	}
	public void setMasterReceiverTypeDto(MasterReceiverTypeDto masterReceiverTypeDto) {
		this.masterReceiverTypeDto = masterReceiverTypeDto;
	}
	public MasterStatusDto getMasterStatusDto() {
		return masterStatusDto;
	}
	public void setMasterStatusDto(MasterStatusDto masterStatusDto) {
		this.masterStatusDto = masterStatusDto;
	}
	public String getRecCode() {
		return recCode;
	}
	public void setRecCode(String recCode) {
		this.recCode = recCode;
	}
	public String getRecShortNameTh() {
		return recShortNameTh;
	}
	public void setRecShortNameTh(String recShortNameTh) {
		this.recShortNameTh = recShortNameTh;
	}
	public String getRecShortNameEn() {
		return recShortNameEn;
	}
	public void setRecShortNameEn(String recShortNameEn) {
		this.recShortNameEn = recShortNameEn;
	}
	public String getRecNameTh() {
		return recNameTh;
	}
	public void setRecNameTh(String recNameTh) {
		this.recNameTh = recNameTh;
	}
	public String getRecNameEn() {
		return recNameEn;
	}
	public void setRecNameEn(String recNameEn) {
		this.recNameEn = recNameEn;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public Integer getRecFileNo() {
		return this.recFileNo;
	}
	public void setRecFileNo(Integer recFileNo) {
		this.recFileNo = recFileNo;
	}
}
