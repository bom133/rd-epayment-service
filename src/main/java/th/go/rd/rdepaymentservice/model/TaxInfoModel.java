package th.go.rd.rdepaymentservice.model;

import io.swagger.annotations.ApiModelProperty;

public class TaxInfoModel {
	@ApiModelProperty(value="เลขประจำตัวผู้เสียภาษีอากรของ Agent")
	private String agentId;
	@ApiModelProperty(value="Reference No. จากหน้าแบบ")
	private String refNo;
	@ApiModelProperty(value="ประเภทแบบ")
	private String formCode;
	@ApiModelProperty(value="วันสุดท้ายของการชำระภาษี")
	private String expDate;
	@ApiModelProperty(value="เดือนภาษี")
	private String taxMonth;
	@ApiModelProperty(value="จำนวนเงิน")
	private String totalAmount;
	@ApiModelProperty(value="ข้อความสำหรับแสดงผลกรณีการชำระภาษีแบบปัดเศษ")
	private String isRound;
	@ApiModelProperty(value="คำอธิบายเกี่ยวกับการปัดเศษ")
	private String isRoundMessage;
	@ApiModelProperty(value="ข้อความเพื่อกด Redirect หน้าจอกลับไปยังหน้าเลือกรายการชำระภาษี")
	private String redirectPageMessage;
	
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	
	public String getRefNo() {
		return refNo;
	}
	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}
	
	public Object getFormCode() {
		return formCode;
	}
	public void setFormCode(String formCode) {
		this.formCode = formCode;
	}
	
	public String getExpDate() {
		return expDate;
	}
	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}
	
	public String getTaxMonth() {
		return taxMonth;
	}
	public void setTaxMonth(String taxMonth) {
		this.taxMonth = taxMonth;
	}
	
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	public String getIsRound() {
		return isRound;
	}
	public void setIsRound(String isRound) {
		this.isRound = isRound;
	}
	
	
	
	public String getIsRoundMessage() {
		return isRoundMessage;
	}
	public void setIsRoundMessage(String isRoundMessage) {
		isRoundMessage = isRoundMessage;
	}
	public String getRedirectPageMessage() {
		return redirectPageMessage;
	}
	public void setRedirectPageMessage(String redirectPageMessage) {
		this.redirectPageMessage = redirectPageMessage;
	}
	public TaxInfoModel(String agentId, String refNo, String formCode, String expDate, String taxMonth,
			String totalAmount, String isRound, String isRoundMessage, String redirectPageMessage) {
		super();
		this.agentId = agentId;
		this.refNo = refNo;
		this.formCode = formCode;
		this.expDate = expDate;
		this.taxMonth = taxMonth;
		this.totalAmount = totalAmount;
		this.isRound = isRound;
		this.redirectPageMessage = redirectPageMessage;
		this.isRoundMessage = isRoundMessage;
	}
	public TaxInfoModel() {
		super();
	}
	
}
