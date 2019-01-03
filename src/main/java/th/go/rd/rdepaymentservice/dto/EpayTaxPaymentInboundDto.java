package th.go.rd.rdepaymentservice.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import th.go.rd.rdepaymentservice.component.JsonDecimalSerializer;

public class EpayTaxPaymentInboundDto {

	private long epayTaxPaymentInboundId;
	@JsonProperty("epayReceiverPaymentLine")
	private EpayReceiverPaymentLineDto epayReceiverPaymentLineDto;
	@JsonProperty("masterPaymentStatus")
	private MasterPaymentStatusDto masterPaymentStatusDto;
	@JsonProperty("masterSendMethod")
	private MasterSendMethodDto masterSendMethodDto;
	@Size(max = 35,message="{app.val-resp.size}")
	private String rdTranNo;
	@Size(max = 35,message="{app.val-resp.size}")
	private String csTranNo;
	@Size(max = 20,message="{app.val-resp.size}")
	private String ref2;
	@Size(max = 20,message="{app.val-resp.size}")
	private String ref3;
	@Size(max = 20,message="{app.val-resp.size}")
	private String agentId;
	@Size(max = 6,message="{app.val-resp.size}")
	private String agentBraNo;
	@NotNull(message = "{app.val-resp.notblank}")
	private Date transferDate;
	@NotNull(message = "{app.val-resp.notblank}")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC+07:00")
	private Date transmitDate;
	@Digits(integer = 16, fraction = 2)
	@PositiveOrZero(message = "{app.val-resp.positiveOrZero}")
	@JsonSerialize(using=JsonDecimalSerializer.class)
	private BigDecimal totalAmount;
	@Size(max = 20,message="{app.val-resp.size}")
	private String terminalId;
	@Size(max = 20,message="{app.val-resp.size}")
	private String merchantId;
	@Size(max= 200,message="{app.val-resp.size}")
	private String payStatusMsg;
	@Size(max = 200,message="{app.val-resp.size}")
	private String attachFile;
	@Size(max = 20,message="{app.val-resp.size}")
	private String createBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC+07:00")
	private Date createDate;
	@Size(max = 20,message="{app.val-resp.size}")
	private String updateBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC+07:00")
	private Date updateDate;
	@Size(max = 20,message="{app.val-resp.size}")
	private String authorizeCode;
	@Size(max = 10,message="{app.val-resp.size}")
	private String responseCode;
	@Size(max = 500,message="{app.val-resp.size}")
	private String reponseMessage;

	public EpayTaxPaymentInboundDto(long epayTaxPaymentInboundId, EpayReceiverPaymentLineDto epayReceiverPaymentLineDto,
			MasterPaymentStatusDto masterPaymentStatusDto, MasterSendMethodDto masterSendMethodDto, String rdTranNo,
			String csTranNo, String ref2, String ref3, String agentId, String agentBraNo, Date transferDate,
			Date transmitDate, BigDecimal totalAmount, String terminalId, String merchantId, String payStatusMsg,
			String attachFile, String createBy, Date createDate, String updateBy, Date updateDate, String authorizeCode,
			String responseCode, String reponseMessage) {
		super();
		this.epayTaxPaymentInboundId = epayTaxPaymentInboundId;
		this.epayReceiverPaymentLineDto = epayReceiverPaymentLineDto;
		this.masterPaymentStatusDto = masterPaymentStatusDto;
		this.masterSendMethodDto = masterSendMethodDto;
		this.rdTranNo = rdTranNo;
		this.csTranNo = csTranNo;
		this.ref2 = ref2;
		this.ref3 = ref3;
		this.agentId = agentId;
		this.agentBraNo = agentBraNo;
		this.transferDate = transferDate;
		this.transmitDate = transmitDate;
		this.totalAmount = totalAmount;
		this.terminalId = terminalId;
		this.merchantId = merchantId;
		this.payStatusMsg = payStatusMsg;
		this.attachFile = attachFile;
		this.createBy = createBy;
		this.createDate = createDate;
		this.updateBy = updateBy;
		this.updateDate = updateDate;
		this.authorizeCode = authorizeCode;
		this.responseCode = responseCode;
		this.reponseMessage = reponseMessage;
	}

	public long getEpayTaxPaymentInboundId() {
		return epayTaxPaymentInboundId;
	}

	public void setEpayTaxPaymentInboundId(long epayTaxPaymentInboundId) {
		this.epayTaxPaymentInboundId = epayTaxPaymentInboundId;
	}

	public EpayReceiverPaymentLineDto getEpayReceiverPaymentLineDto() {
		return epayReceiverPaymentLineDto;
	}

	public void setEpayReceiverPaymentLineDto(EpayReceiverPaymentLineDto epayReceiverPaymentLineDto) {
		this.epayReceiverPaymentLineDto = epayReceiverPaymentLineDto;
	}

	public MasterPaymentStatusDto getMasterPaymentStatusDto() {
		return masterPaymentStatusDto;
	}

	public void setMasterPaymentStatusDto(MasterPaymentStatusDto masterPaymentStatusDto) {
		this.masterPaymentStatusDto = masterPaymentStatusDto;
	}

	public MasterSendMethodDto getMasterSendMethodDto() {
		return masterSendMethodDto;
	}

	public void setMasterSendMethodDto(MasterSendMethodDto masterSendMethodDto) {
		this.masterSendMethodDto = masterSendMethodDto;
	}

	public String getRdTranNo() {
		return rdTranNo;
	}

	public void setRdTranNo(String rdTranNo) {
		this.rdTranNo = rdTranNo;
	}

	public String getCsTranNo() {
		return csTranNo;
	}

	public void setCsTranNo(String csTranNo) {
		this.csTranNo = csTranNo;
	}

	public String getRef2() {
		return ref2;
	}

	public void setRef2(String ref2) {
		this.ref2 = ref2;
	}

	public String getRef3() {
		return ref3;
	}

	public void setRef3(String ref3) {
		this.ref3 = ref3;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getAgentBraNo() {
		return agentBraNo;
	}

	public void setAgentBraNo(String agentBraNo) {
		this.agentBraNo = agentBraNo;
	}

	public Date getTransferDate() {
		return transferDate;
	}

	public void setTransferDate(Date transferDate) {
		this.transferDate = transferDate;
	}

	public Date getTransmitDate() {
		return transmitDate;
	}

	public void setTransmitDate(Date transmitDate) {
		this.transmitDate = transmitDate;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getPayStatusMsg() {
		return payStatusMsg;
	}

	public void setPayStatusMsg(String payStatusMsg) {
		this.payStatusMsg = payStatusMsg;
	}

	public String getAttachFile() {
		return attachFile;
	}

	public void setAttachFile(String attachFile) {
		this.attachFile = attachFile;
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

	public String getAuthorizeCode() {
		return authorizeCode;
	}

	public void setAuthorizeCode(String authorizeCode) {
		this.authorizeCode = authorizeCode;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getReponseMessage() {
		return reponseMessage;
	}

	public void setReponseMessage(String reponseMessage) {
		this.reponseMessage = reponseMessage;
	}

	@Override
	public String toString() {
		return "EpayTaxPaymentInboundDto [epayTaxPaymentInboundId=" + epayTaxPaymentInboundId
				+ ", epayReceiverPaymentLineDto=" + epayReceiverPaymentLineDto + ", masterPaymentStatusDto="
				+ masterPaymentStatusDto + ", masterSendMethodDto=" + masterSendMethodDto + ", rdTranNo=" + rdTranNo
				+ ", csTranNo=" + csTranNo + ", ref2=" + ref2 + ", ref3=" + ref3 + ", agentId=" + agentId
				+ ", agentBraNo=" + agentBraNo + ", transferDate=" + transferDate + ", transmitDate=" + transmitDate
				+ ", totalAmount=" + totalAmount + ", terminalId=" + terminalId + ", merchantId=" + merchantId
				+ ", payStatusMsg=" + payStatusMsg + ", attachFile=" + attachFile + ", createBy=" + createBy
				+ ", createDate=" + createDate + ", updateBy=" + updateBy + ", updateDate=" + updateDate
				+ ", authorizeCode=" + authorizeCode + ", responseCode=" + responseCode + ", reponseMessage="
				+ reponseMessage + "]";
	}

}
