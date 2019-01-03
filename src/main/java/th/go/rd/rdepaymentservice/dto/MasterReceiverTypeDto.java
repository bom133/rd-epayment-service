package th.go.rd.rdepaymentservice.dto;

import javax.validation.constraints.Size;

public class MasterReceiverTypeDto {

	private int receiverType;
	@Size(max = 100,message = "{app.val-resp.size}")
	private String descriptionTh;
	@Size(max  = 100,message = "{app.val-resp.size}")
	private String descriptionEn;
	public MasterReceiverTypeDto() {
		super();
	}
	public MasterReceiverTypeDto(int receiverType, String descriptionTh, String descriptionEn) {
		super();
		this.receiverType = receiverType;
		this.descriptionTh = descriptionTh;
		this.descriptionEn = descriptionEn;
	}
	public int getReceiverType() {
		return receiverType;
	}
	public void setReceiverType(int receiverType) {
		this.receiverType = receiverType;
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
	
	
}
