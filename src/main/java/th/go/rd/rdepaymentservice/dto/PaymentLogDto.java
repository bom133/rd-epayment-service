package th.go.rd.rdepaymentservice.dto;
// Generated Sep 17, 2018 10:48:55 AM by Hibernate Tools 4.3.5.Final

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PaymentLogDto {

	private long paymentLogId;
	@NotBlank(message = "{app.val-resp.notblank}")
	@Size(max = 36,message = "{app.val-resp.size}")
	private String uuid;
	@NotBlank(message = "{app.val-resp.notblank}")
	@Size(max = 20,message = "{app.val-resp.Size}")
	private String apiCode;
	@NotBlank(message = "{app.val-resp.notblank}")
	@Size(max = 26,message = "{app.val-resp.Size}")
	private Date transactionTime;
	@NotBlank(message = "{app.val-resp.notblank}")
	@Size(max = 20,message = "{app.val-resp.Size}")
	private String responseCode;
	@Size(max = 500,message = "{app.val-resp.Size}")
	private String responseMessage;
	@Size(max = 20,message = "{app.val-resp.Size}")
	private String ip;
	@NotBlank(message = "{app.val-resp.notblank}")
	@Size(max = 50,message = "{app.val-resp.Size}")
	private String userName;
	@NotBlank(message = "{app.val-resp.notblank}")
	@Size(max = 100,message = "{app.val-resp.Size}")
	private String actionName;
	@Size(max = 1000,message = "{app.val-resp.Size}")
	private String detailInfo;

	public PaymentLogDto() {
	}

	public PaymentLogDto(long paymentLogId, String uuid, String apiCode, Date transactionTime, String responseCode, String userName, String actionName) {
		this.paymentLogId = paymentLogId;
		this.uuid = uuid;
		this.apiCode = apiCode;
		this.transactionTime = transactionTime;
		this.responseCode = responseCode;
		this.userName = userName;
		this.actionName = actionName;
	}
	public PaymentLogDto(long paymentLogId, String uuid, String apiCode, Date transactionTime, String responseCode, String responseMessage, String ip, String userName, String actionName,
			String detailInfo) {
		this.paymentLogId = paymentLogId;
		this.uuid = uuid;
		this.apiCode = apiCode;
		this.transactionTime = transactionTime;
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
		this.ip = ip;
		this.userName = userName;
		this.actionName = actionName;
		this.detailInfo = detailInfo;
	}

	public long getPaymentLogId() {
		return paymentLogId;
	}

	public void setPaymentLogId(long paymentLogId) {
		this.paymentLogId = paymentLogId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getApiCode() {
		return apiCode;
	}

	public void setApiCode(String apiCode) {
		this.apiCode = apiCode;
	}

	public Date getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(Date transactionTime) {
		this.transactionTime = transactionTime;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getDetailInfo() {
		return detailInfo;
	}

	public void setDetailInfo(String detailInfo) {
		this.detailInfo = detailInfo;
	}

	@Override
	public String toString() {
		return "PaymentLogDto [paymentLogId=" + paymentLogId + ", uuid=" + uuid + ", apiCode=" + apiCode + ", transactionTime=" + transactionTime + ", responseCode=" + responseCode
				+ ", responseMessage=" + responseMessage + ", ip=" + ip + ", userName=" + userName + ", actionName=" + actionName + ", detailInfo=" + detailInfo + "]";
	}

	
}
