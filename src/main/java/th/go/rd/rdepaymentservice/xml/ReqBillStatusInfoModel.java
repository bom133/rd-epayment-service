package th.go.rd.rdepaymentservice.xml;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlElement;

import org.springframework.data.domain.AfterDomainEventPublication;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

public class ReqBillStatusInfoModel {
	@ApiModelProperty(value="รหัสหน่วยรับชำระ", required=true, example="002")
	@JsonProperty("BankCode")
	private String bankCode;
	
	@ApiModelProperty(value="รหัสประจำตัวผู้เสียภาษีอากร", required=true, example="1234567890123")
	@JsonProperty("Ref1")
	private String ref1;
	
	@ApiModelProperty(value="รหัสควบคุม", required=true, example="310009103573611")
	@JsonProperty("Ref2")
	private String ref2;
	
	@ApiModelProperty(value="ref3")
	@JsonProperty("Ref3")
	private String ref3;
	
	@ApiModelProperty(value="วันเวลาที่ส่งข้อมูลให้กรมสรรพากร", required=true, example="20180707121212")
	@JsonProperty("TransmitDate")
	private String transmitDate;
	
	@ApiModelProperty(value="ช่องทางการชำระเงิน", required=true)
	@JsonProperty("PaymentLine")
	private String paymentLine;

	@ApiModelProperty(value="จำนวนเงินรวมที่ต้องชำระ",required=true, example="500.50")
	@JsonProperty("TotalAmout")
	private BigDecimal totalAmout;
	
	@ApiModelProperty(value="หมายเลขที่หน่วยรับชำระกำหนด")
	@JsonProperty("TerminalID")
	private String terminalID;
	
	@ApiModelProperty(value="หมายเลขที่หน่วยรับชำระกำหนด")
	@JsonProperty("MerchantID")
	private String merchantID;


	@XmlElement(name="BankCode")
	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	@XmlElement(name="Ref1")
	public String getRef1() {
		return ref1;
	}

	public void setRef1(String ref1) {
		this.ref1 = ref1;
	}

	@XmlElement(name="Ref2")
	public String getRef2() {
		return ref2;
	}

	public void setRef2(String ref2) {
		this.ref2 = ref2;
	}

	@XmlElement(name="Ref3")
	public String getRef3() {
		return ref3;
	}

	public void setRef3(String ref3) {
		this.ref3 = ref3;
	}

	@XmlElement(name="TransmitDate")
	public String getTransmitDate() {
		return transmitDate;
	}

	public void setTransmitDate(String transmitDate) {
		this.transmitDate = transmitDate;
	}

	@XmlElement(name="TotalAmount")
	public BigDecimal getTotalAmout() {
		return totalAmout;
	}

	public void setTotalAmout(BigDecimal totalAmout) {
		this.totalAmout = totalAmout;
	}

	@XmlElement(name="TerminalID")
	public String getTerminalID() {
		return terminalID;
	}

	public void setTerminalID(String terminalID) {
		this.terminalID = terminalID;
	}

	@XmlElement(name="MerchantID")
	public String getMerchantID() {
		return merchantID;
	}

	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}
	
	@XmlElement(name="PaymentLine")
	public String getPaymentLine() {
		return paymentLine;
	}

	public void setPaymentLine(String paymentLine) {
		this.paymentLine = paymentLine;
	}

	@Override
	public String toString() {
		return "ReqBillPaymentInfomations [bankCode=" + bankCode + ", ref1="
				+ ref1 + ", ref2=" + ref2 + ", ref3=" + ref3 + ", transmitDate=" + transmitDate 
				+ ", totalAmout=" + totalAmout + ", terminalID=" + terminalID + ", merchantID=" + merchantID + "]";
	}
}
