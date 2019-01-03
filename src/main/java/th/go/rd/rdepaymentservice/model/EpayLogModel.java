package th.go.rd.rdepaymentservice.model;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;

import th.go.rd.rdepaymentservice.exception.ApiFieldError;
import th.go.rd.rdepaymentservice.exception.ApiGlobalError;
import th.go.rd.rdepaymentservice.manager.RestManager;

public class EpayLogModel {

	@NotNull
	private int logLevel;
	@NotNull
	private int logType;
	@NotBlank
	private String apiCode;
	private Date requestTime = new Date();
	private Date responseTime = new Date();
	private String httpResponseCode;
	@NotBlank
	private String responseCode;
	private String responseMessage;
	private String ip;
	@NotBlank
	private String username;
	private String description;

	public EpayLogModel(@NotNull int logLevel, @NotNull int logType, @NotBlank String apiCode, Date requestTime,
			Date responseTime, String httpResponseCode, @NotBlank String responseCode, String responseMessage,
			String ip, @NotBlank String username, String description) {
		super();
		this.logLevel = logLevel;
		this.logType = logType;
		this.apiCode = apiCode;
		this.requestTime = requestTime;
		this.responseTime = responseTime;
		this.httpResponseCode = httpResponseCode;
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
		this.ip = ip;
		this.username = username;
		this.description = description;
	}

	public EpayLogModel(@NotNull int logLevel, @NotNull int logType, @NotBlank String apiCode,
			String username, String description) {
		this.logLevel = logLevel;
		this.logType = logType;
		this.apiCode = apiCode;
		this.username = username;
		this.description = description;
		this.setSuccess();
	}

	public void setSuccess() {
		this.responseCode = "I07000";
		this.httpResponseCode = HttpStatus.OK.toString();
		this.responseMessage = "Success";
		this.requestTime = new Date();
	}

	public void setError(String code, String msg) {
		this.httpResponseCode = HttpStatus.INTERNAL_SERVER_ERROR.toString();
		this.responseCode = code;
		this.responseMessage = msg;
	}

	public void setError(RestManager restManager) {
		List<ApiGlobalError> apiGlobalErrors = restManager.getApiGlobalErrors();
		if (!apiGlobalErrors.isEmpty()) {
			this.httpResponseCode = HttpStatus.INTERNAL_SERVER_ERROR.toString();
			this.responseCode = apiGlobalErrors.get(0).getErrorCode();
			this.responseMessage = apiGlobalErrors.get(0).getErrorMessage();
		} else {
			List<ApiFieldError> apiFieldErrors = restManager.getApiFieldErrors();
			if (!apiFieldErrors.isEmpty()) {
				this.responseCode = "400";
				this.httpResponseCode = HttpStatus.BAD_REQUEST.toString();
				this.responseMessage = apiFieldErrors.get(0).getErrorMessage();
			}
		}
	}

	public int getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(int logLevel) {
		this.logLevel = logLevel;
	}

	public int getLogType() {
		return logType;
	}

	public void setLogType(int logType) {
		this.logType = logType;
	}

	public String getApiCode() {
		return apiCode;
	}

	public void setApiCode(String apiCode) {
		this.apiCode = apiCode;
	}

	public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	public Date getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}

	public String getHttpResponseCode() {
		return httpResponseCode;
	}

	public void setHttpResponseCode(String httpResponseCode) {
		this.httpResponseCode = httpResponseCode;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
