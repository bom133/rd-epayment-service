package th.go.rd.rdepaymentservice.util.validator;

import java.util.Calendar;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PresentOrFutureValidator implements ConstraintValidator<PresentOrFuture, Date> {

    @Override
    public boolean isValid(Date value, ConstraintValidatorContext context) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(value);
        cal.set(Calendar.SECOND, 1);
        value = cal.getTime();
        Date today = new Date();
        cal.setTime(today);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        today = cal.getTime();
        return !value.before(today) || value.after(today);
    }

    
}
