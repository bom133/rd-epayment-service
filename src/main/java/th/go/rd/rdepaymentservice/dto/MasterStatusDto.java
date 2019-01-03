package th.go.rd.rdepaymentservice.dto;

import javax.validation.constraints.Size;

public class MasterStatusDto {
	private String status;
	@Size(max = 100,message = "{app.val-resp.size}")
	private String statusNameTh;
	@Size(max = 100,message = "{app.val-resp.size}")
	private String statusNameEn;
	public MasterStatusDto() {
		super();
	}
	public MasterStatusDto(String status, String statusNameTh, String statusNameEn) {
		super();
		this.status = status;
		this.statusNameTh = statusNameTh;
		this.statusNameEn = statusNameEn;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusNameTh() {
		return statusNameTh;
	}
	public void setStatusNameTh(String statusNameTh) {
		this.statusNameTh = statusNameTh;
	}
	public String getStatusNameEn() {
		return statusNameEn;
	}
	public void setStatusNameEn(String statusNameEn) {
		this.statusNameEn = statusNameEn;
	}
	@Override
	public String toString() {
		return "MasterStatusDto [status=" + status + ", statusNameTh=" + statusNameTh + ", statusNameEn=" + statusNameEn + "]";
	}
	
	
}
