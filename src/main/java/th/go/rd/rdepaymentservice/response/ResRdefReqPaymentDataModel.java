package th.go.rd.rdepaymentservice.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ResRdefReqPaymentDataModel {
	private String redirectURL;
	
	@JsonInclude(Include.NON_NULL)
	private String terminalId;
	
	@JsonInclude(Include.NON_NULL)
	private String merchantId;
	
	@JsonInclude(Include.NON_NULL)
	private String attachFile;
	
	private String dataFile;
	
	@JsonInclude(Include.NON_NULL)
	private String converId;
	
	@JsonInclude(Include.NON_NULL)
	private String rdTransNo;
	
	public ResRdefReqPaymentDataModel(String redirectURL, String terminalId, String merchartId, String attachFile,
			String dataFile) {
		super();
		this.redirectURL = redirectURL;
		this.terminalId = terminalId;
		this.merchantId = merchartId;
		this.attachFile = attachFile;
		this.dataFile = dataFile;
	}
	
	
	
	public ResRdefReqPaymentDataModel(String redirectURL, String terminalId, String merchartId, String attachFile,
			String dataFile, String converId, String rdTransNo) {
		super();
		this.redirectURL = redirectURL;
		this.terminalId = terminalId;
		this.merchantId = merchartId;
		this.attachFile = attachFile;
		this.dataFile = dataFile;
		this.converId = converId;
		this.rdTransNo = rdTransNo;
	}



	public String getConverId() {
		return converId;
	}



	public void setConverId(String converId) {
		this.converId = converId;
	}



	public String getRdTransNo() {
		return rdTransNo;
	}



	public void setRdTransNo(String rdTransNo) {
		this.rdTransNo = rdTransNo;
	}



	public String getRedirectURL() {
		return redirectURL;
	}

	public void setRedirectURL(String redirectURL) {
		this.redirectURL = redirectURL;
	}

	public ResRdefReqPaymentDataModel() {
		super();
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
	public String getAttachFile() {
		return attachFile;
	}
	public void setAttachFile(String attachFile) {
		this.attachFile = attachFile;
	}
	public String getDataFile() {
		return dataFile;
	}
	public void setDataFile(String dataFile) {
		this.dataFile = dataFile;
	}



	@Override
	public String toString() {
		return "ResRdefReqPaymentDataModel [redirectURL=" + redirectURL + ", terminalId=" + terminalId + ", merchartId="
				+ merchantId + ", attachFile=" + attachFile + ", dataFile=" + dataFile + ", converId=" + converId
				+ ", rdTransNo=" + rdTransNo + "]";
	}

	
	
	
}