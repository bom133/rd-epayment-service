package th.go.rd.rdepaymentservice.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EpayReceiverPaymentLineDto {

	private long epayReceiverPaymentLineId;
	
	@JsonProperty("masterPaymentLine")
	@NotNull(message = "{app.val-resp.notblank}")
	private MasterPaymentLineDto masterPaymentLineDto;

	@JsonProperty("masterReceiverUnit")
	@NotNull(message = "{app.val-resp.notblank}")
	private MasterReceiverUnitDto masterReceiverUnitDto;

	@JsonProperty("masterStatus")
	@NotNull(message = "{app.val-resp.notblank}")
	private MasterStatusDto masterStatusDto;
	@Size(max = 500,message="{app.val-resp.max}")
	private String description;
	@Size(max = 50,message="{app.val-resp.max}")
	private String rdCertCode;
	@Size(max = 50,message="{app.val-resp.max}")
	private String recCertCode;
	@Size(max = 100,message="{app.val-resp.max}")
	private String recRedirectUrl;
	@Size(max = 100,message="{app.val-resp.max}")
	private String recDirectUrl;
	@Size(max = 100,message="{app.val-resp.max}")
	private String rdRedirectUrl;
	@Size(max = 100,message="{app.val-resp.max}")
	private String rdDirectUrl;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC+07:00")
	private Date startDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC+07:00")
	private Date endDate;
	@Size(max = 20,message="{app.val-resp.max}")
	private String terminalId;
	@Size(max = 20,message="{app.val-resp.max}")
	private String merchantId;

	public EpayReceiverPaymentLineDto() {
		super();
	}

	public EpayReceiverPaymentLineDto(long epayReceiverPaymentLineId, MasterPaymentLineDto masterPaymentLineDto,
			MasterReceiverUnitDto masterReceiverUnitDto, MasterStatusDto masterStatusDto, String description,
			String rdCertCode, String recCertCode, String recRedirectUrl, String recDirectUrl, String rdRedirectUrl,
			String rdDirectUrl, Date startDate, Date endDate, String terminalId, String merchantId) {
		super();
		this.epayReceiverPaymentLineId = epayReceiverPaymentLineId;
		this.masterPaymentLineDto = masterPaymentLineDto;
		this.masterReceiverUnitDto = masterReceiverUnitDto;
		this.masterStatusDto = masterStatusDto;
		this.description = description;
		this.rdCertCode = rdCertCode;
		this.recCertCode = recCertCode;
		this.recRedirectUrl = recRedirectUrl;
		this.recDirectUrl = recDirectUrl;
		this.rdRedirectUrl = rdRedirectUrl;
		this.rdDirectUrl = rdDirectUrl;
		this.startDate = startDate;
		this.endDate = endDate;
		this.terminalId = terminalId;
		this.merchantId = merchantId;
	}

	public long getEpayReceiverPaymentLineId() {
		return epayReceiverPaymentLineId;
	}

	public void setEpayReceiverPaymentLineId(long epayReceiverPaymentLineId) {
		this.epayReceiverPaymentLineId = epayReceiverPaymentLineId;
	}

	public MasterPaymentLineDto getMasterPaymentLineDto() {
		return masterPaymentLineDto;
	}

	public void setMasterPaymentLineDto(MasterPaymentLineDto masterPaymentLineDto) {
		this.masterPaymentLineDto = masterPaymentLineDto;
	}

	public MasterReceiverUnitDto getMasterReceiverUnitDto() {
		return masterReceiverUnitDto;
	}

	public void setMasterReceiverUnitDto(MasterReceiverUnitDto masterReceiverUnitDto) {
		this.masterReceiverUnitDto = masterReceiverUnitDto;
	}

	public MasterStatusDto getMasterStatusDto() {
		return masterStatusDto;
	}

	public void setMasterStatusDto(MasterStatusDto masterStatusDto) {
		this.masterStatusDto = masterStatusDto;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRdCertCode() {
		return rdCertCode;
	}

	public void setRdCertCode(String rdCertCode) {
		this.rdCertCode = rdCertCode;
	}

	public String getRecCertCode() {
		return recCertCode;
	}

	public void setRecCertCode(String recCertCode) {
		this.recCertCode = recCertCode;
	}

	public String getRecRedirectUrl() {
		return recRedirectUrl;
	}

	public void setRecRedirectUrl(String recRedirectUrl) {
		this.recRedirectUrl = recRedirectUrl;
	}

	public String getRecDirectUrl() {
		return recDirectUrl;
	}

	public void setRecDirectUrl(String recDirectUrl) {
		this.recDirectUrl = recDirectUrl;
	}

	public String getRdRedirectUrl() {
		return rdRedirectUrl;
	}

	public void setRdRedirectUrl(String rdRedirectUrl) {
		this.rdRedirectUrl = rdRedirectUrl;
	}

	public String getRdDirectUrl() {
		return rdDirectUrl;
	}

	public void setRdDirectUrl(String rdDirectUrl) {
		this.rdDirectUrl = rdDirectUrl;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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

	@Override
	public String toString() {
		return "EpayReceiverPaymentLineDto [epayReceiverPaymentLineId=" + epayReceiverPaymentLineId
				+ ", masterPaymentLineDto=" + masterPaymentLineDto + ", masterReceiverUnitDto=" + masterReceiverUnitDto
				+ ", masterStatusDto=" + masterStatusDto + ", description=" + description + ", rdCertCode=" + rdCertCode
				+ ", recCertCode=" + recCertCode + ", recRedirectUrl=" + recRedirectUrl + ", recDirectUrl="
				+ recDirectUrl + ", rdRedirectUrl=" + rdRedirectUrl + ", rdDirectUrl=" + rdDirectUrl + ", startDate="
				+ startDate + ", endDate=" + endDate + ", terminalId=" + terminalId + ", merchantId=" + merchantId
				+ "]";
	}

}
