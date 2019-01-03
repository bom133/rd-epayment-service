package th.go.rd.rdepaymentservice.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="TAXDATA")
public class EpayReqPaymentXMLModel {
	private String rdTransactionNo;
	private String refNo;
	private String mid;
	private String agentId;
	private String agentBraNo;
	private String formCode;
	private String taxMonth;
	private String Amount;
	private String refDate;
	private String expDate;
	private String paymentLine;
	private String rdRedirectUrl;
	private String rdDirectUrl;
	private String dataKey;
	private String terminalId;
	private String mechantId;
	
	@XmlElement(name="RDTransNo")
	public String getRdTransactionNo() {
		return rdTransactionNo;
	}
	public void setRdTransactionNo(String rdTransactionNo) {
		this.rdTransactionNo = rdTransactionNo;
	}
	
	@XmlElement(name="RefNo")
	public String getRefNo() {
		return refNo;
	}
	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}
	
	@XmlElement(name="Mid")
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	
	@XmlElement(name="Nid")
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	
	@XmlElement(name="Brano")
	public String getAgentBraNo() {
		return agentBraNo;
	}
	public void setAgentBraNo(String agentBraNo) {
		this.agentBraNo = agentBraNo;
	}
	
	@XmlElement(name="FormCode")
	public String getFormCode() {
		return formCode;
	}
	public void setFormCode(String formCode) {
		this.formCode = formCode;
	}
	
	@XmlElement(name="TaxMonth")
	public String getTaxMonth() {
		return taxMonth;
	}
	public void setTaxMonth(String taxMonth) {
		this.taxMonth = taxMonth;
	}
	
	@XmlElement(name="RefDate")
	public String getRefDate() {
		return refDate;
	}
	public void setRefDate(String refDate) {
		this.refDate = refDate;
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
	
	@XmlElement(name="BackURL")
	public String getRdRedirectUrl() {
		return rdRedirectUrl;
	}
	public void setRdRedirectUrl(String rdRedirectUrl) {
		this.rdRedirectUrl = rdRedirectUrl;
	}
	
	@XmlElement(name="RespURL")
	public String getRdDirectUrl() {
		return rdDirectUrl;
	}
	public void setRdDirectUrl(String rdDirectUrl) {
		this.rdDirectUrl = rdDirectUrl;
	}
	
	@XmlElement(name="DateKey")
	public String getDataKey() {
		return dataKey;
	}
	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}
	
	@XmlElement(name="TerminalID")
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	
	@XmlElement(name="MerchantID")
	public String getMechantId() {
		return mechantId;
	}
	public void setMechantId(String mechantId) {
		this.mechantId = mechantId;
	}
	
	@XmlElement(name="Amount")
	public String getAmount() {
		return Amount;
	}
	public void setAmount(String amount) {
		Amount = amount;
	}
	@Override
	public String toString() {
		return "EpayReqPaymentXMLModel [rdTransactionNo=" + rdTransactionNo + ", refNo=" + refNo + ", mid=" + mid
				+ ", agentId=" + agentId + ", agentBraNo=" + agentBraNo + ", formCode=" + formCode + ", taxMonth="
				+ taxMonth + ", Amount=" + Amount + ", refDate=" + refDate + ", expDate=" + expDate + ", paymentLine="
				+ paymentLine + ", rdRedirectUrl=" + rdRedirectUrl + ", rdDirectUrl=" + rdDirectUrl + ", dataKey="
				+ dataKey + ", terminalId=" + terminalId + ", mechantId=" + mechantId + "]";
	}
	
}
