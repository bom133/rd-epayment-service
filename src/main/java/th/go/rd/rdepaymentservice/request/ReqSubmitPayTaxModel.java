package th.go.rd.rdepaymentservice.request;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import th.go.rd.rdepaymentservice.component.JsonDecimalSerializer;

import io.swagger.annotations.ApiModelProperty;
import th.go.rd.rdepaymentservice.util.validator.PresentOrFuture;

public class ReqSubmitPayTaxModel {

	// @ApiModelProperty(value = "Token ID สำหรับตรวจสอบสิทธิ์การใช้งาน")
	// private String tokenId;
	@ApiModelProperty(value = "Reference No. จากหน้าแบบ", required = true)
	@Size(max=13,message="{app.val-resp.size}")
	@NotBlank(message = "{app.val-resp.notblank}")
	private String refNo;
	
	@ApiModelProperty(value = "เลขประจำตัวผู้เสียภาษีอากรของ Agent", required = true)
	@Size(min=13, max=13,message="{app.val-resp.size}")
	@NotBlank(message = "{app.val-resp.notblank}")
	private String agentId;
	
	@ApiModelProperty(value = "ชื่อผู้เสียภาษีอากรของ Agent", required = true)
	@Size(max=400,message="{app.val-resp.size}")
	@NotBlank(message = "{app.val-resp.notblank}")
	private String agentName;
	
	@ApiModelProperty(value = "เลขที่สาขาของ Agent", required = true)
	@Size(max=8,message="{app.val-resp.size}")
	@NotBlank(message = "{app.val-resp.notblank}")
	private String agentBrano;
	
	@ApiModelProperty(value = "เดือนภาษี")
	@Size(max=6,message="{app.val-resp.size}")
	private String TaxMonthDisplay;
	
	@ApiModelProperty(value = "เป็นการชำระแบบปัดเศษสตางค์", allowableValues = "Y,N,R", required = true)
	@Size(min=1,max=1,message="{app.val-resp.size}")
	@NotBlank(message = "{app.val-resp.notblank}")
	@Pattern(regexp = "y|n|r", flags = Pattern.Flag.CASE_INSENSITIVE,message = "{app.val-resp.allowedValues}")
	private String isRound;
	
	@ApiModelProperty(value = "ประเภทแบบแสดงรายการภาษี หรือรวมรายการ", required = true)
	@Size(max=400,message="{app.val-resp.size}")
	@NotBlank(message = "{app.val-resp.notblank}")
	private String formCodeDisplay;
	
	
	@ApiModelProperty(value = "เงินภาษี", required = true)
	@PositiveOrZero(message = "{app.val-resp.positiveOrZero}")
	@JsonSerialize(using=JsonDecimalSerializer.class)
	private BigDecimal taxAmount;
	
	
	@ApiModelProperty(value = "จำนวนเงินรวม", required = true)
	@Positive(message = "{app.val-resp.positive}")
	@JsonSerialize(using=JsonDecimalSerializer.class)
	private BigDecimal totalAmount;
	
	// @FutureOrPresent(message = "{app.val-resp.futureOrPresent}")
	@ApiModelProperty(value = "วันที่สุดท้ายของการชำระเงิน", required = true)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@PresentOrFuture(message = "{app.val-resp.futureOrPresent}")
	@NotNull(message = "{app.val-resp.notblank}")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC+07:00")
	private Date expDate;
	
	@ApiModelProperty(value = "ช่องทางการชำระเงิน")
	@Size(max = 10, message = "{app.val-resp.size}")
	private String paymentLine;
	
	@ApiModelProperty(value = "วิธีการชำระเงิน", allowableValues = "CONF,AUTO")
	@Size(min=4,max=10,message="{app.val-resp.size}")
	@NotBlank(message = "{app.val-resp.notblank}")
	@Pattern(regexp = "CONF|AUTO", flags = Pattern.Flag.CASE_INSENSITIVE,message = "{app.val-resp.allowedValues}")
	private String paymentMethod;
	
	@ApiModelProperty(value = "หมายเลชบัญชี")
	@Size(max=20,message = "{app.val-resp.size}")
	private String accountNo;
	
	@ApiModelProperty(value = "วันที่โอนเงิน")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC+07:00")
	private Date orderTransferDate;
	
	@ApiModelProperty(value = "รหัสธนาคาร", required = true)
	@Size(max = 3, message = "{app.val-resp.size}")
	private String bankCode;

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
	
	@ApiModelProperty(value = "ยอดรวมที่ต้องชำระ")
	@PositiveOrZero(message = "{app.val-resp.positiveOrZero}")
	@JsonSerialize(using=JsonDecimalSerializer.class)
	private BigDecimal totalTaxAmount;
	
	@ApiModelProperty(value = "ค่าปรับอาญา")
	@Digits(fraction = 0,integer = 100, message = "{app.val-resp.number}")
	@PositiveOrZero(message = "{app.val-resp.positiveOrZero}")
	@JsonSerialize(using=JsonDecimalSerializer.class)
	private BigDecimal criminalFinesAmount;

	@Valid
	@NotNull(message = "{app.man-resp.invalid_field}")
	List<ReqSubmitPaymentDetailModel> paymentDetail;

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getAgentBrano() {
		return agentBrano;
	}

	public void setAgentBrano(String agentBrano) {
		this.agentBrano = agentBrano;
	}

	public String getTaxMonthDisplay() {
		return TaxMonthDisplay;
	}

	public void setTaxMonthDisplay(String taxMonthDisplay) {
		TaxMonthDisplay = taxMonthDisplay;
	}

	public String getIsRound() {
		return isRound;
	}

	public void setIsRound(String isRound) {
		this.isRound = isRound;
	}

	public String getFormCodeDisplay() {
		return formCodeDisplay;
	}

	public void setFormCodeDisplay(String formCodeDisplay) {
		this.formCodeDisplay = formCodeDisplay;
	}

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Date getExpDate() {
		return expDate;
	}

	public void setExpDate(Date expDate) {
		this.expDate = expDate;
	}

	public String getPaymentLine() {
		return paymentLine;
	}

	public void setPaymentLine(String paymentLine) {
		this.paymentLine = paymentLine;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public Date getOrderTransferDate() {
		return orderTransferDate;
	}

	public void setOrderTransferDate(Date orderTransferDate) {
		this.orderTransferDate = orderTransferDate;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public List<ReqSubmitPaymentDetailModel> getPaymentDetail() {
		return paymentDetail;
	}

	public void setPaymentDetail(List<ReqSubmitPaymentDetailModel> paymentDetail) {
		this.paymentDetail = paymentDetail;
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

	public void setPenaltyAmount(BigDecimal penaltyAmount) {
		this.penaltyAmount = penaltyAmount;
	}

	public BigDecimal getTotalTaxAmount() {
		return totalTaxAmount;
	}

	public void setTotalTaxAmount(BigDecimal totalTaxAmount) {
		this.totalTaxAmount = totalTaxAmount;
	}

	public BigDecimal getCriminalFinesAmount() {
		return criminalFinesAmount;
	}

	public void setCriminalFinesAmount(BigDecimal criminalFinesAmount) {
		this.criminalFinesAmount = criminalFinesAmount;
	}

	@Override
	public String toString() {
		return "ReqSubmitPayTaxModel [refNo=" + refNo + ", agentId=" + agentId + ", agentName=" + agentName
				+ ", agentBrano=" + agentBrano + ", TaxMonthDisplay=" + TaxMonthDisplay + ", isRound=" + isRound
				+ ", formCodeDisplay=" + formCodeDisplay + ", taxAmount=" + taxAmount + ", totalAmount=" + totalAmount
				+ ", expDate=" + expDate + ", paymentLine=" + paymentLine + ", paymentMethod=" + paymentMethod
				+ ", accountNo=" + accountNo + ", orderTransferDate=" + orderTransferDate + ", bankCode=" + bankCode
				+ ", netTaxAmount=" + netTaxAmount + ", surchargeAmount=" + surchargeAmount + ", penaltyAmount="
				+ penaltyAmount + ", totalTaxAmount=" + totalTaxAmount + ", criminalFinesAmount=" + criminalFinesAmount
				+ ", paymentDetail=" + paymentDetail + "]";
	}

}
