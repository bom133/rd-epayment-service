package th.go.rd.rdepaymentservice.exception;

import java.io.Serializable;

public class ApiGlobalError implements Serializable {
	private static final long serialVersionUID = 13651100765199026L;
	private String errorCode;
	private String errorMessage;
	private String errorStackTrace;

	public ApiGlobalError(String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public ApiGlobalError(String errorCode, String errorMessage, String errorStackTrace) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.errorStackTrace = errorStackTrace;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getErrorStackTrace() {
		return errorStackTrace;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((errorCode == null) ? 0 : errorCode.hashCode());
		result = prime * result + ((errorMessage == null) ? 0 : errorMessage.hashCode());
		result = prime * result + ((errorStackTrace == null) ? 0 : errorStackTrace.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ApiGlobalError other = (ApiGlobalError) obj;
		if (errorCode == null) {
			if (other.errorCode != null)
				return false;
		} else if (!errorCode.equals(other.errorCode))
			return false;
		if (errorMessage == null) {
			if (other.errorMessage != null)
				return false;
		} else if (!errorMessage.equals(other.errorMessage))
			return false;
		if (errorStackTrace == null) {
			if (other.errorStackTrace != null)
				return false;
		} else if (!errorStackTrace.equals(other.errorStackTrace))
			return false;
		return true;
	}

}
