package th.go.rd.rdepaymentservice.request;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import th.go.rd.rdepaymentservice.component.JsonDecimalSerializer;

import io.swagger.annotations.ApiModelProperty;
public class ReqSubmitPaymentDetailModel {
	
	@ApiModelProperty(value="เลขอ้างอิงการยื่นแบบฯ", required=true)
	@Size(min=13, max=13,message="{app.val-resp.size}")
	@NotBlank(message = "{app.val-resp.notblank}")
	private String systemRefNo;
	
	@ApiModelProperty(value="ประเภทแบบฯ/รหัสเอกสาร", required=true)
	@Size(min=1, max=20,message="{app.val-resp.size}")
	@NotBlank(message = "{app.val-resp.notblank}")
	private String formCode;
	
	@ApiModelProperty(value="หมายเลขประจำตัวผู้เสียภาษี", required=true)
	@Size(min=13, max=13,message="{app.val-resp.size}")
	@NotBlank(message = "{app.val-resp.notblank}")
	private String nid;
	
	@ApiModelProperty(value="เลขที่สาขาของหมายเลขประจำตัวผู้เสียภาษี", required=true)
	@Size(min=1, max=6,message="{app.val-resp.size}")
	@NotBlank(message = "{app.val-resp.notblank}")
	private String nidBrano;
	
	@ApiModelProperty(value="เดือนปีภาษี", required=true, example="062550")
	private String taxMonth;
	
	@ApiModelProperty(value = "ภาษีที่ต้องชำระ (ภาษีสุทธิ)")
	@PositiveOrZero(message = "{app.val-resp.positiveOrZero}")
	@JsonSerialize(using=JsonDecimalSerializer.class)
	private BigDecimal netTaxAmount;
	
	@ApiModelProperty(value = "เงินเพิ่ม")
	@PositiveOrZero(message = "{app.val-resp.positiveOrZero}")
	@JsonSerialize(using=JsonDecimalSerializer.class)
	private BigDecimal surchargeAmount;
	
	@ApiModelProperty(value = "เบี้ยปรับ")
	@PositiveOrZero(message = "{app.val-resp.positiveOrZero}")
	@JsonSerialize(using=JsonDecimalSerializer.class)
	private BigDecimal penaltyAmount;
	
	@ApiModelProperty(value = "ยอดรวมที่ต้องชำระ (ก่อนปัดเศษ)")
	@PositiveOrZero(message = "{app.val-resp.positiveOrZero}")
	@JsonSerialize(using=JsonDecimalSerializer.class)
	private BigDecimal totalTaxAmount;
	
	@ApiModelProperty(value = "รวมภาษีที่ต้องชำระ")
	@PositiveOrZero(message = "{app.val-resp.positiveOrZero}")
	@JsonSerialize(using=JsonDecimalSerializer.class)
	private BigDecimal taxAmount;
	
	@ApiModelProperty(value = "ค่าปรับอาญา")
	@Digits(fraction = 0,integer = 100, message = "{app.val-resp.number}")
	@PositiveOrZero(message = "{app.val-resp.positiveOrZero}")
	@JsonSerialize(using=JsonDecimalSerializer.class)
	private BigDecimal criminalFinesAmount;
	
	@ApiModelProperty(value = "ยอดรวมที่ต้องชำระ", required = true)
	@Positive(message = "{app.val-resp.positive}")
	@JsonSerialize(using=JsonDecimalSerializer.class)
	private BigDecimal totalAmount;
	
	@ApiModelProperty(value = "เป็นการชำระแบบปัดเศษสตางค์", allowableValues = "Y,N")
	@Size(min=1,max=1,message="{app.val-resp.size}")
	@NotBlank(message = "{app.val-resp.notblank}")
	@Pattern(regexp = "y|n", flags = Pattern.Flag.CASE_INSENSITIVE,message = "{app.val-resp.allowedValues}")
	private String isRound;
	
	public String getTaxMonth() {
		return taxMonth;
	}
	public void setTaxMonth(String taxMonth) {
		this.taxMonth = taxMonth;
	}
	public String getSystemRefNo() {
		return systemRefNo;
	}
	public void setSystemRefNo(String systemRefNo) {
		this.systemRefNo = systemRefNo;
	}
	public String getFormCode() {
		return formCode;
	}
	public void setFormCode(String formCode) {
		this.formCode = formCode;
	}
	
	public BigDecimal getNetTaxAmount() {
		return netTaxAmount;
	}
	public void setNetTaxAmount(BigDecimal netTaxAmount) {
		this.netTaxAmount = netTaxAmount;
	}
	public BigDecimal getSurchargeAmount() {
		return surchargeAmount;
	}
	public void setSurchargeAmount(BigDecimal surchargeAmount) {
		this.surchargeAmount = surchargeAmount;
	}
	public BigDecimal getPenaltyAmount() {
		return penaltyAmount;
	}
	public void setPanaltyAmount(BigDecimal penaltyAmount) {
		this.penaltyAmount = penaltyAmount;
	}
	public BigDecimal getTotalTaxAmount() {
		return totalTaxAmount;
	}
	public void setTotalTaxAmount(BigDecimal totalTaxAmount) {
		this.totalTaxAmount = totalTaxAmount;
	}
	public BigDecimal getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}
	public BigDecimal getCriminalFinesAmount() {
		return criminalFinesAmount;
	}
	public void setCriminalFinesAmount(BigDecimal criminalFinesAmount) {
		this.criminalFinesAmount = criminalFinesAmount;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getIsRound() {
		return isRound;
	}
	public void setIsRound(String isRound) {
		this.isRound = isRound;
	}
	public String getNid() {
		return nid;
	}
	public void setNid(String nid) {
		this.nid = nid;
	}
	public String getNidBrano() {
		return nidBrano;
	}
	public void setNidBrano(String nidBrano) {
		this.nidBrano = nidBrano;
	}
	@Override
	public String toString() {
		return "ReqSubmitPaymentDetailModel [systemRefNo=" + systemRefNo + ", formCode=" + formCode + ", nid=" + nid
				+ ", nidBrano=" + nidBrano + ", taxMonth=" + taxMonth + ", netTaxAmount=" + netTaxAmount
				+ ", surchargeAmount=" + surchargeAmount + ", penaltyAmount=" + penaltyAmount + ", totalTaxAmount="
				+ totalTaxAmount + ", taxAmount=" + taxAmount + ", criminalFineaAmount=" + criminalFinesAmount
				+ ", totalAmount=" + totalAmount + ", isRound=" + isRound + "]";
	}
}
