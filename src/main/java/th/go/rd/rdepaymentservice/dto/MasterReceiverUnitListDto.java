package th.go.rd.rdepaymentservice.dto;

import javax.persistence.Column;

public class MasterReceiverUnitListDto {
	
	private long masterReceiverUnitId;
	private String recCode;
	private String recShortName;
	private String recName;
	private String status;
	
	@Column(name="MASTER_RECEIVER_UNIT_ID")
	public long getMasterReceiverUnitId() {
		return masterReceiverUnitId;
	}
	public void setMasterReceiverUnitId(long masterReceiverUnitId) {
		this.masterReceiverUnitId = masterReceiverUnitId;
	}
	
	@Column(name="REC_CODE")
	public String getRecCode() {
		return recCode;
	}
	public void setRecCode(String recCode) {
		this.recCode = recCode;
	}
	
	@Column(name="REC_SHORT_NAME_EN")
	public String getRecShortName() {
		return recShortName;
	}
	public void setRecShortName(String recShortName) {
		this.recShortName = recShortName;
	}
	
	@Column(name="REC_NAME_EN")
	public String getRecName() {
		return recName;
	}
	public void setRecName(String recName) {
		this.recName = recName;
	}
	
	@Column(name="STATUS")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
