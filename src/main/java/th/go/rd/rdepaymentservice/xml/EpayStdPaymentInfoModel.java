package th.go.rd.rdepaymentservice.xml;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class EpayStdPaymentInfoModel {
	private String mid;
	
	private String rdTransactionNo;
	
	private String refNo;
	
	private String agentId;
	
	private String agentBraNo;
	
	private String transmitDate;
	
	private String expDate;
	
	private String paymentLine;
	
	private Double totalAmount;
	
	private String paymentMethod;
	
	private String accountNo;
	
	private String orderTranDate;
	
	private String bankCode;
	
	private String terminalId;
	
	private String merchantId;
	
	@JacksonXmlProperty(localName = "PaymentDetail")
    @JacksonXmlElementWrapper(useWrapping = false)
	private EpayStdPaymentInfoDetailModel[] paymentDetail;


	@XmlElement(name="Mid")
	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}


	@XmlElement(name="RDTransactionNo")
	public String getRdTransactionNo() {
		return rdTransactionNo;
	}

	public void setRdTransactionNo(String rdTransactionNo) {
		this.rdTransactionNo = rdTransactionNo;
	}

	@XmlElement(name="AgentID")
	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	@XmlElement(name="AgentBrano")
	public String getAgentBraNo() {
		return agentBraNo;
	}

	public void setAgentBraNo(String agentBraNo) {
		this.agentBraNo = agentBraNo;
	}

	@XmlElement(name="TransmitDate")
	public String getTransmitDate() {
		return transmitDate;
	}

	public void setTransmitDate(String transmitDate) {
		this.transmitDate = transmitDate;
	}

	@XmlElement(name="ExpDate")
	public String getExpDate() {
		return expDate;
	}

	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}


	@XmlElement(name="PaymentLine")
	public String getPaymentLine() {
		return paymentLine;
	}

	public void setPaymentLine(String paymentLine) {
		this.paymentLine = paymentLine;
	}


	@XmlElement(name="TotalAmount")
	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}


	@XmlElement(name="PaymentMethod")
	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}


	@XmlElement(name="AccountNo")
	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}


	@XmlElement(name="OrderTransferDate")
	public String getOrderTranDate() {
		return orderTranDate;
	}

	public void setOrderTranDate(String orderTranDate) {
		this.orderTranDate = orderTranDate;
	}


	@XmlElement(name="BankCode")
	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	@XmlElement(name="TerminalID")
	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId==null ? "" : terminalId;
	}


	@XmlElement(name="MerchantID")
	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId == null ? "": merchantId;
	}


	@XmlElement(name="PaymentDetail")
	public EpayStdPaymentInfoDetailModel[] getPaymentDetail() {
		return paymentDetail;
	}

	public void setPaymentDetail(EpayStdPaymentInfoDetailModel[] paymentDetail) {
		this.paymentDetail = paymentDetail;
	}


	@XmlElement(name="PaymentID")
	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	@Override
	public String toString() {
		return "EpayStdPaymentInfoModel [mid=" + mid + ", rdTransactionNo=" + rdTransactionNo + ", paymentID="
				+ refNo + ", agentId=" + agentId + ", agentBraNo=" + agentBraNo + ", transmitDate=" + transmitDate
				+ ", expDate=" + expDate + ", paymentLine=" + paymentLine + ", totalAmount=" + totalAmount
				+ ", paymentMethod=" + paymentMethod + ", accountNo=" + accountNo + ", orderTranDate=" + orderTranDate
				+ ", bankCode=" + bankCode + ", terminalID=" + terminalId + ", merchantID=" + merchantId
				+ ", paymentDetail=" + Arrays.toString(paymentDetail) + "]";
	}
	
}
