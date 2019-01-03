package th.go.rd.rdepaymentservice.model;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;

public class AppValidatorModel {
	
//	@NotNull – validates that the annotated property value is not null
	@NotNull(message="{app.val-resp.notblank}")
	private String isNotNull;
	
//	@AssertTrue – validates that the annotated property value is true
	@AssertFalse(message = "{app.val-resp.istrue}")
	private boolean isTrue;
	
//	@Size – validates that the annotated property value has a size between the attributes min and max; can be applied to String, Collection, Map, and array properties
	@Size(min = 2, max = 4,message="{app.val-resp.size}")//, message="E0001::About Me must be between 2 and 4 characters"
	private String size;
	
	
//	@Min – vValidates that the annotated property has a value no smaller than the value attribute
	@Min(value = 1, message = "{app.val-resp.min}")
	@Max(value = 12, message = "{app.val-resp.max}")
	@Digits(fraction = 0,integer = 2, message = "{app.val-resp.number}")
	private String month;
	
//	@Email – validates that the annotated property is a valid email address
	@Email(message = "{app.val-resp.email}")
	private String email;
	
//	@NotEmpty – validates that the property is not null or empty; can be applied to String, Collection, Map or Array values
//	@NotBlank – can be applied only to text values and validated that the property is not null or whitespace
	@NotEmpty
	private List<@NotBlank String> preferences;

//	@Positive and @PositiveOrZero – apply to numeric values and validate that they are strictly positive, or positive including 0
//	@Negative and @NegativeOrZero – apply to numeric values and validate that they are strictly negative, or negative including 0
//	@Past and @PastOrPresent – validate that a date value is in the past or the past including the present; can be applied to date types including those added in Java 8
//	@Future and @FutureOrPresent – validates that a date value is in the future, or in the future including the present
	
	@Autowired
	@Valid
	private SubAppValidatorModel sub;
	
	public String getIsNotNull() {
		return isNotNull;
	}

	public void setIsNotNull(String isNotNull) {
		this.isNotNull = isNotNull;
	}

	public boolean isTrue() {
		return isTrue;
	}

	public void setTrue(boolean isTrue) {
		this.isTrue = isTrue;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getPreferences() {
		return preferences;
	}

	public void setPreferences(List<String> preferences) {
		this.preferences = preferences;
	}

	public SubAppValidatorModel getSub() {
		return sub;
	}

	public void setSub(SubAppValidatorModel sub) {
		this.sub = sub;
	}
}

class SubAppValidatorModel implements Serializable {

	@Email(message = "{app.val-resp.email}")
	private String subEmail;

	public String getSubEmail() {
		return subEmail;
	}

	public void setSubEmail(String subEmail) {
		this.subEmail = subEmail;
	}
}
