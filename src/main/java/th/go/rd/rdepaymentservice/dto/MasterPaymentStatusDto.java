package th.go.rd.rdepaymentservice.dto;

import javax.validation.constraints.Size;

public class MasterPaymentStatusDto {

	private int masterPaymentStatusId;
	@Size(max = 100,message = "{app.val-resp.size}")
	private String descriptionTh;
	@Size(max = 10,message = "{app.val-resp.size}")
	private String descriptionEn;
	public MasterPaymentStatusDto() {
		super();
	}
	public MasterPaymentStatusDto(int masterPaymentStatusId, String descriptionTh, String descriptionEn) {
		super();
		this.masterPaymentStatusId = masterPaymentStatusId;
		this.descriptionTh = descriptionTh;
		this.descriptionEn = descriptionEn;
	}
	public int getMasterPaymentStatusId() {
		return masterPaymentStatusId;
	}
	public void setMasterPaymentStatusId(int masterPaymentStatusId) {
		this.masterPaymentStatusId = masterPaymentStatusId;
	}
	public String getDescriptionTh() {
		return descriptionTh;
	}
	public void setDescriptionTh(String descriptionTh) {
		this.descriptionTh = descriptionTh;
	}
	public String getDescriptionEn() {
		return descriptionEn;
	}
	public void setDescriptionEn(String descriptionEn) {
		this.descriptionEn = descriptionEn;
	}
	@Override
	public String toString() {
		return "MasterPaymentStatusDto [masterPaymentStatusId=" + masterPaymentStatusId + ", descriptionTh=" + descriptionTh + ", descriptionEn=" + descriptionEn + "]";
	}
	
	
}
