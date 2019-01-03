package th.go.rd.rdepaymentservice.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DateHelperTest {

    private static Logger logger = LoggerFactory.getLogger(DateHelperTest.class);
    
    @Test
    public void testIsBeforeDate() throws ParseException {
        logger.info(" ================= Test isBeforeDate ================= ");
        
    	DateHelper dateHelper = new DateHelper();
    	SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
    	
    	String compareDateStr = "02-10-2018 10:47:11";
    	Date compareDate = format.parse(compareDateStr);
    	
    	String sameDateBeforeTimeStr = "02-10-2018 09:47:11";
    	Date sameDateBeforeTime = format.parse(sameDateBeforeTimeStr);
    	assertFalse(dateHelper.isBeforeDate(sameDateBeforeTime, compareDate));
    	
    	String sameDateAfterTimeStr = "02-10-2018 12:47:11";
    	Date sameDateAfterTime = format.parse(sameDateAfterTimeStr);
    	assertFalse(dateHelper.isBeforeDate(sameDateAfterTime, compareDate));
    	
    	String beforeDateStr = "02-9-2018 09:47:11";
    	Date beforeDate = format.parse(beforeDateStr);
    	assertTrue(dateHelper.isBeforeDate(beforeDate, compareDate));
    	
    	String afterDateStr = "02-11-2018 09:47:11";
    	Date afterDate = format.parse(afterDateStr);
    	assertFalse(dateHelper.isBeforeDate(afterDate, compareDate));
    }
    
    @Test
    public void testIsAfterDate() throws ParseException {
        logger.info(" ================= Test isAfterDate ================= ");
    	
    	DateHelper dateHelper = new DateHelper();
    	SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
    	
    	String compareDateStr = "02-10-2018 10:47:11";
    	Date compareDate = format.parse(compareDateStr);
    	
    	String sameDateBeforeTimeStr = "02-10-2018 09:47:11";
    	Date sameDateBeforeTime = format.parse(sameDateBeforeTimeStr);
    	assertFalse(dateHelper.isAfterDate(sameDateBeforeTime, compareDate));
    	
    	String sameDateAfterTimeStr = "02-10-2018 12:47:11";
    	Date sameDateAfterTime = format.parse(sameDateAfterTimeStr);
    	assertFalse(dateHelper.isAfterDate(sameDateAfterTime, compareDate));
    	
    	String beforeDateStr = "02-9-2018 09:47:11";
    	Date beforeDate = format.parse(beforeDateStr);
    	assertFalse(dateHelper.isAfterDate(beforeDate, compareDate));
    	
    	String afterDateStr = "02-11-2018 09:47:11";
    	Date afterDate = format.parse(afterDateStr);
    	assertTrue(dateHelper.isAfterDate(afterDate, compareDate));
    }
    
    @Test
    public void testIsBetweenDate() throws ParseException {
        logger.info(" ================= Test isBetweenDate ================= ");
    	
    	DateHelper dateHelper = new DateHelper();
    	SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
    	
    	String compareDateStr = "02-10-2018 10:47:11";
    	Date compareDate = format.parse(compareDateStr);
    	
    	/*------ case same start date ------*/
    	String startDateStr = "02-10-2018 09:47:11";
    	Date startDate = format.parse(startDateStr);
    	String endDateStr = "02-11-2018 10:47:11";
    	Date endDate = format.parse(endDateStr);
    	assertTrue(dateHelper.isBetweenDate(compareDate, startDate, endDate));
    	
    	/*------ case same end date -------*/
    	startDateStr = "02-9-2018 09:47:11";
    	startDate = format.parse(startDateStr);
    	endDateStr = "02-10-2018 12:47:11";
    	endDate = format.parse(endDateStr);
    	assertTrue(dateHelper.isBetweenDate(compareDate, startDate, endDate));
    	
    	/*------ case between date -------*/
    	startDateStr = "02-9-2018 09:47:11";
    	startDate = format.parse(startDateStr);
    	endDateStr = "02-11-2018 12:47:11";
    	endDate = format.parse(endDateStr);
    	assertTrue(dateHelper.isBetweenDate(compareDate, startDate, endDate));
    	
    	/*------- case not between date -----*/
    	startDateStr = "02-4-2018 09:47:11";
    	startDate = format.parse(startDateStr);
    	endDateStr = "02-3-2018 12:47:11";
    	endDate = format.parse(endDateStr);
    	assertFalse(dateHelper.isBetweenDate(compareDate, startDate, endDate));
    	
    }
}
