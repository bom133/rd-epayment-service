package th.go.rd.rdepaymentservice.exception;

import java.io.Serializable;

public class ApiFieldError implements Serializable {

	private static final long serialVersionUID = -7832231251923206811L;
	private String field;
	private String errorCode;
	private String errorMessage;
	private Object rejectedValue;

	public ApiFieldError(String field, String errorCode, String errorMessage, Object rejectedValue) {
		this.field = field;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.rejectedValue = rejectedValue;
	}

	public String getField() {
		return field;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public Object getRejectedValue() {
		return rejectedValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((errorCode == null) ? 0 : errorCode.hashCode());
		result = prime * result + ((errorMessage == null) ? 0 : errorMessage.hashCode());
		result = prime * result + ((field == null) ? 0 : field.hashCode());
		result = prime * result + ((rejectedValue == null) ? 0 : rejectedValue.hashCode());
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
		ApiFieldError other = (ApiFieldError) obj;
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
		if (field == null) {
			if (other.field != null)
				return false;
		} else if (!field.equals(other.field))
			return false;
		if (rejectedValue == null) {
			if (other.rejectedValue != null)
				return false;
		} else if (!rejectedValue.equals(other.rejectedValue))
			return false;
		return true;
	}

}
