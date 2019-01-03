package th.go.rd.rdepaymentservice.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import th.go.rd.rdepaymentservice.component.JsonDecimalSerializer;

public class EpayTaxPaymentInfoDto {

	private long epayTaxPaymentInfoId;
	@JsonProperty("epayTaxPaymentInbound")
	private EpayTaxPaymentInboundDto epayTaxPaymentInboundDto;
	@NotNull(message = "{app.val-resp.notblank}")
	@JsonProperty("masterStatus")
	private MasterStatusDto masterStatusDto;
	@NotBlank(message = "{app.val-resp.notblank}")
	@Size(max = 20,message ="{app.val-resp.size}")
	private String refNo;
	@NotBlank(message = "{app.val-resp.notblank}")
	@Size(max = 20,message ="{app.val-resp.size}")
	private String agentId;
	@NotBlank(message = "{app.val-resp.notblank}")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC+07:00")
	private Date expDate;
	@Size(max = 3,message ="{app.val-resp.size}")
	private String recCode;
	@NotBlank(message = "{app.val-resp.notblank}")
	@Size(max = 10,message ="{app.val-resp.size}")
	private String paymentMethod;
	@Size(max = 20,message ="{app.val-resp.size}")
	private String accountNo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC+07:00")
	private Date orderTranDate;
	private String createBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC+07:00")
	private Date createDate;
	private String updateBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC+07:00")
	private Date updateDate;
	@Size(max = 6,message ="{app.val-resp.size}")
	private String taxMonthDisplay;
	@NotBlank(message = "{app.val-resp.notblank}")
	@Size(max = 6,message ="{app.val-resp.size}")
	private String agentBraNo;
	@NotBlank(message = "{app.val-resp.notblank}")
	@Size(max = 200,message ="{app.val-resp.size}")
	private String agentName;
	@NotBlank(message = "{app.val-resp.notblank}")
	@Size(max = 1,message ="{app.val-resp.size}")
	private char isRound;
	@NotBlank(message = "{app.val-resp.notblank}")
	@Size(max = 200,message ="{app.val-resp.size}")
	private String formCodeDisplay;
	@NotNull(message = "{app.val-resp.notblank}")
	@Digits(integer = 16, fraction = 2)
	@PositiveOrZero(message = "{app.val-resp.positiveOrZero}")
	@JsonSerialize(using=JsonDecimalSerializer.class)
	private BigDecimal taxAmount;
	@NotNull(message = "{app.val-resp.notblank}")
	@Digits(integer = 16, fraction = 2)
	@PositiveOrZero(message = "{app.val-resp.positiveOrZero}")
	@JsonSerialize(using=JsonDecimalSerializer.class)
	private BigDecimal totalAmount;
	@NotNull(message = "{app.val-resp.notblank}")
	@Digits(integer = 16, fraction = 2)
	@PositiveOrZero(message = "{app.val-resp.positiveOrZero}")
	@JsonSerialize(using=JsonDecimalSerializer.class)
	private BigDecimal criminalFinesAmount;
	@NotNull(message = "{app.val-resp.notblank}")
	@Digits(integer = 16, fraction = 2)
	@PositiveOrZero(message = "{app.val-resp.positiveOrZero}")
	@JsonSerialize(using=JsonDecimalSerializer.class)
	private BigDecimal surchargeAmount;
	@Size(max = 10,message = "{app.val-resp.size}")
	private String payLineCode;

	public EpayTaxPaymentInfoDto(long epayTaxPaymentInfoId, EpayTaxPaymentInboundDto epayTaxPaymentInboundDto,
			MasterStatusDto masterStatusDto, String refNo, String agentId, Date expDate, String recCode,
			String paymentMethod, String accountNo, Date orderTranDate, String createBy, Date createDate,
			String updateBy, Date updateDate, String taxMonthDisplay, String agentBraNo, String agentName, char isRound,
			String formCodeDisplay, BigDecimal taxAmount, BigDecimal totalAmount, BigDecimal criminalFinesAmount,
			BigDecimal surchargeAmount, String payLineCode) {
		super();
		this.epayTaxPaymentInfoId = epayTaxPaymentInfoId;
		this.epayTaxPaymentInboundDto = epayTaxPaymentInboundDto;
		this.masterStatusDto = masterStatusDto;
		this.refNo = refNo;
		this.agentId = agentId;
		this.expDate = expDate;
		this.recCode = recCode;
		this.paymentMethod = paymentMethod;
		this.accountNo = accountNo;
		this.orderTranDate = orderTranDate;
		this.createBy = createBy;
		this.createDate = createDate;
		this.updateBy = updateBy;
		this.updateDate = updateDate;
		this.taxMonthDisplay = taxMonthDisplay;
		this.agentBraNo = agentBraNo;
		this.agentName = agentName;
		this.isRound = isRound;
		this.formCodeDisplay = formCodeDisplay;
		this.taxAmount = taxAmount;
		this.totalAmount = totalAmount;
		this.criminalFinesAmount = criminalFinesAmount;
		this.surchargeAmount = surchargeAmount;
		this.payLineCode = payLineCode;
	}

	public long getEpayTaxPaymentInfoId() {
		return epayTaxPaymentInfoId;
	}

	public void setEpayTaxPaymentInfoId(long epayTaxPaymentInfoId) {
		this.epayTaxPaymentInfoId = epayTaxPaymentInfoId;
	}

	public EpayTaxPaymentInboundDto getEpayTaxPaymentInboundDto() {
		return epayTaxPaymentInboundDto;
	}

	public void setEpayTaxPaymentInboundDto(EpayTaxPaymentInboundDto epayTaxPaymentInboundDto) {
		this.epayTaxPaymentInboundDto = epayTaxPaymentInboundDto;
	}

	public MasterStatusDto getMasterStatusDto() {
		return masterStatusDto;
	}

	public void setMasterStatusDto(MasterStatusDto masterStatusDto) {
		this.masterStatusDto = masterStatusDto;
	}

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

	public Date getExpDate() {
		return expDate;
	}

	public void setExpDate(Date expDate) {
		this.expDate = expDate;
	}

	public String getRecCode() {
		return recCode;
	}

	public void setRecCode(String recCode) {
		this.recCode = recCode;
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

	public Date getOrderTranDate() {
		return orderTranDate;
	}

	public void setOrderTranDate(Date orderTranDate) {
		this.orderTranDate = orderTranDate;
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

	public String getTaxMonthDisplay() {
		return taxMonthDisplay;
	}

	public void setTaxMonthDisplay(String taxMonthDisplay) {
		this.taxMonthDisplay = taxMonthDisplay;
	}

	public String getAgentBraNo() {
		return agentBraNo;
	}

	public void setAgentBraNo(String agentBraNo) {
		this.agentBraNo = agentBraNo;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public char getIsRound() {
		return isRound;
	}

	public void setIsRound(char isRound) {
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

	public BigDecimal getCriminalFinesAmount() {
		return criminalFinesAmount;
	}

	public void setCriminalFinesAmount(BigDecimal criminalFinesAmount) {
		this.criminalFinesAmount = criminalFinesAmount;
	}

	public BigDecimal getSurchargeAmount() {
		return surchargeAmount;
	}

	public void setSurchargeAmount(BigDecimal surchargeAmount) {
		this.surchargeAmount = surchargeAmount;
	}

	public String getPayLineCode() {
		return payLineCode;
	}

	public void setPayLineCode(String payLineCode) {
		this.payLineCode = payLineCode;
	}

	@Override
	public String toString() {
		return "EpayTaxPaymentInfoDto [epayTaxPaymentInfoId=" + epayTaxPaymentInfoId + ", epayTaxPaymentInboundDto="
				+ epayTaxPaymentInboundDto + ", masterStatusDto=" + masterStatusDto + ", refNo=" + refNo + ", agentId="
				+ agentId + ", expDate=" + expDate + ", recCode=" + recCode + ", paymentMethod=" + paymentMethod
				+ ", accountNo=" + accountNo + ", orderTranDate=" + orderTranDate + ", createBy=" + createBy
				+ ", createDate=" + createDate + ", updateBy=" + updateBy + ", updateDate=" + updateDate
				+ ", taxMonthDisplay=" + taxMonthDisplay + ", agentBraNo=" + agentBraNo + ", agentName=" + agentName
				+ ", isRound=" + isRound + ", formCodeDisplay=" + formCodeDisplay + ", taxAmount=" + taxAmount
				+ ", totalAmount=" + totalAmount + ", criminalFinesAmount=" + criminalFinesAmount + ", surchargeAmount="
				+ surchargeAmount + ", payLineCode=" + payLineCode + "]";
	}

}
