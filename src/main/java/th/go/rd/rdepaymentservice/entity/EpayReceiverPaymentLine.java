package th.go.rd.rdepaymentservice.entity;
// Generated Jul 17, 2018 10:14:52 AM by Hibernate Tools 4.3.5.Final

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * EpayReceiverPaymentLine generated by hbm2java
 */
@Entity
@Table(name = "EPAY_RECEIVER_PAYMENT_LINE")
public class EpayReceiverPaymentLine implements java.io.Serializable {

	private long epayReceiverPaymentLineId;
	private MasterPaymentLine masterPaymentLine;
	private MasterReceiverUnit masterReceiverUnit;
	private MasterStatus masterStatus;
	private String recRedirectUrl;
	private String recDirectUrl;
	private String rdRedirectUrl;
	private String rdDirectUrl;
	private Date startDate;
	private Date endDate;
	private String createBy;
	private Date createDate;
	private String updateBy;
	private Date updateDate;
	private String rdCertCode;
	private String recCertCode;
	private String terminalId;
	private String merchantId;
	private String description;
	private List<EpayTaxPaymentInbound> epayTaxPaymentInbounds = new ArrayList<>();
	private List<EpayTaxPaymentOutbound> epayTaxPaymentOutbounds = new ArrayList<>();

	public EpayReceiverPaymentLine() {
	}

	public EpayReceiverPaymentLine(long epayReceiverPaymentLineId, MasterPaymentLine masterPaymentLine, MasterReceiverUnit masterReceiverUnit, MasterStatus masterStatus, String createBy,
			Date createDate, String updateBy, Date updateDate) {
		this.epayReceiverPaymentLineId = epayReceiverPaymentLineId;
		this.masterPaymentLine = masterPaymentLine;
		this.masterReceiverUnit = masterReceiverUnit;
		this.masterStatus = masterStatus;
		this.createBy = createBy;
		this.createDate = createDate;
		this.updateBy = updateBy;
		this.updateDate = updateDate;
	}
	public EpayReceiverPaymentLine(long epayReceiverPaymentLineId, MasterPaymentLine masterPaymentLine, MasterReceiverUnit masterReceiverUnit, MasterStatus masterStatus, String recRedirectUrl,
			String recDirectUrl, String rdRedirectUrl, String rdDirectUrl, Date startDate, Date endDate, String createBy, Date createDate, String updateBy, Date updateDate, String rdCertCode,
			String recCertCode, String terminalId, String merchantId, String description, List<EpayTaxPaymentInbound> epayTaxPaymentInbounds, List<EpayTaxPaymentOutbound> epayTaxPaymentOutbounds) {
		this.epayReceiverPaymentLineId = epayReceiverPaymentLineId;
		this.masterPaymentLine = masterPaymentLine;
		this.masterReceiverUnit = masterReceiverUnit;
		this.masterStatus = masterStatus;
		this.recRedirectUrl = recRedirectUrl;
		this.recDirectUrl = recDirectUrl;
		this.rdRedirectUrl = rdRedirectUrl;
		this.rdDirectUrl = rdDirectUrl;
		this.startDate = startDate;
		this.endDate = endDate;
		this.createBy = createBy;
		this.createDate = createDate;
		this.updateBy = updateBy;
		this.updateDate = updateDate;
		this.rdCertCode = rdCertCode;
		this.recCertCode = recCertCode;
		this.terminalId = terminalId;
		this.merchantId = merchantId;
		this.description = description;
		this.epayTaxPaymentInbounds = epayTaxPaymentInbounds;
		this.epayTaxPaymentOutbounds = epayTaxPaymentOutbounds;
	}

	@Id
	@SequenceGenerator(name="EPAY_RECEIVER_PAYMENT_LINE_ID_SEQ", sequenceName="EPAY_RECEIVER_PAYMENT_LINE_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="EPAY_RECEIVER_PAYMENT_LINE_ID_SEQ")
	@Column(name = "EPAY_RECEIVER_PAYMENT_LINE_ID", unique = true, nullable = false)
	public long getEpayReceiverPaymentLineId() {
		return this.epayReceiverPaymentLineId;
	}

	public void setEpayReceiverPaymentLineId(long epayReceiverPaymentLineId) {
		this.epayReceiverPaymentLineId = epayReceiverPaymentLineId;
	}

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MASTER_PAYMENT_LINE_ID", nullable = false)
	public MasterPaymentLine getMasterPaymentLine() {
		return this.masterPaymentLine;
	}

	public void setMasterPaymentLine(MasterPaymentLine masterPaymentLine) {
		this.masterPaymentLine = masterPaymentLine;
	}

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MASTER_RECEIVER_UNIT_ID", nullable = false)
	public MasterReceiverUnit getMasterReceiverUnit() {
		return this.masterReceiverUnit;
	}

	public void setMasterReceiverUnit(MasterReceiverUnit masterReceiverUnit) {
		this.masterReceiverUnit = masterReceiverUnit;
	}

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATUS", nullable = false)
	public MasterStatus getMasterStatus() {
		return this.masterStatus;
	}

	public void setMasterStatus(MasterStatus masterStatus) {
		this.masterStatus = masterStatus;
	}

	@Column(name = "REC_REDIRECT_URL", length = 200)
	public String getRecRedirectUrl() {
		return this.recRedirectUrl;
	}

	public void setRecRedirectUrl(String recRedirectUrl) {
		this.recRedirectUrl = recRedirectUrl;
	}

	@Column(name = "REC_DIRECT_URL", length = 200)
	public String getRecDirectUrl() {
		return this.recDirectUrl;
	}

	public void setRecDirectUrl(String recDirectUrl) {
		this.recDirectUrl = recDirectUrl;
	}

	@Column(name = "RD_REDIRECT_URL", length = 200)
	public String getRdRedirectUrl() {
		return this.rdRedirectUrl;
	}

	public void setRdRedirectUrl(String rdRedirectUrl) {
		this.rdRedirectUrl = rdRedirectUrl;
	}

	@Column(name = "RD_DIRECT_URL", length = 200)
	public String getRdDirectUrl() {
		return this.rdDirectUrl;
	}

	public void setRdDirectUrl(String rdDirectUrl) {
		this.rdDirectUrl = rdDirectUrl;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "START_DATE", length = 10)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "END_DATE", length = 10)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "CREATE_BY", nullable = false, length = 20)
	public String getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_DATE", nullable = false, length = 26)
	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "UPDATE_BY", nullable = false, length = 20)
	public String getUpdateBy() {
		return this.updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_DATE", nullable = false, length = 26)
	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Column(name = "RD_CERT_CODE", length = 50)
	public String getRdCertCode() {
		return this.rdCertCode;
	}

	public void setRdCertCode(String rdCertCode) {
		this.rdCertCode = rdCertCode;
	}

	@Column(name = "REC_CERT_CODE", length = 50)
	public String getRecCertCode() {
		return this.recCertCode;
	}

	public void setRecCertCode(String recCertCode) {
		this.recCertCode = recCertCode;
	}

	@Column(name = "TERMINAL_ID", length = 20)
	public String getTerminalId() {
		return this.terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	@Column(name = "MERCHANT_ID", length = 20)
	public String getMerchantId() {
		return this.merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	@Column(name = "DESCRIPTION", length = 1000)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "epayReceiverPaymentLine",cascade=CascadeType.ALL)
	public List<EpayTaxPaymentInbound> getEpayTaxPaymentInbounds() {
		return this.epayTaxPaymentInbounds;
	}

	public void setEpayTaxPaymentInbounds(List<EpayTaxPaymentInbound> epayTaxPaymentInbounds) {
		this.epayTaxPaymentInbounds = epayTaxPaymentInbounds;
	}

	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "epayReceiverPaymentLine",cascade = CascadeType.ALL)
	public List<EpayTaxPaymentOutbound> getEpayTaxPaymentOutbounds() {
		return this.epayTaxPaymentOutbounds;
	}

	public void setEpayTaxPaymentOutbounds(List<EpayTaxPaymentOutbound> epayTaxPaymentOutbounds) {
		this.epayTaxPaymentOutbounds = epayTaxPaymentOutbounds;
	}

}