package th.go.rd.rdepaymentservice.util;

import java.util.Date;

import org.joda.time.DateTimeComparator;

public class DateHelper {
	
	private DateTimeComparator dateTimeComparator;

	public DateHelper() {
		super();
		this.dateTimeComparator = DateTimeComparator.getDateOnlyInstance();
	}

	public Boolean isBeforeDate(Date srcDate, Date compareDate) {
		return (this.dateTimeComparator.compare(srcDate, compareDate) < 0);
	}
	
	public Boolean isAfterDate(Date srcDate, Date compareDate) {
		return (this.dateTimeComparator.compare(srcDate, compareDate) > 0);
	}
	
	public Boolean isBetweenDate(Date srcDate, Date startDate, Date endDate) {
		return (!this.isBeforeDate(srcDate, startDate) && !this.isAfterDate(srcDate, endDate));
	}
}
