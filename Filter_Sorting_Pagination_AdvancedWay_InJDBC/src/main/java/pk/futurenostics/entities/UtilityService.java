package pk.futurenostics.entities;

import java.sql.Timestamp;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UtilityService {
	
	private static final String PREV_DATE_FORMAT = "MM/dd/yyyy";
	private static final String EXCEPTION_WHILE_CONVERTING_STRING_TO_DATE_EXPECTED_FORMATE_IS_MM_DD_YYYY = "Exception while converting string to date. Expected formate is MM/dd/yyyy";
	
    // Converting String Date to Time stamp 
	public Timestamp StringToTimeStamp(String stringDate) throws Exception {
		DateFormat df = new SimpleDateFormat(PREV_DATE_FORMAT);
		try {
			Date date = df.parse(stringDate);
			long time = date.getTime();
			return new Timestamp(time);
		}catch(ParseException e) {
			log.error(EXCEPTION_WHILE_CONVERTING_STRING_TO_DATE_EXPECTED_FORMATE_IS_MM_DD_YYYY, e.getMessage());
			// also
			// throw new EmployeeServiceProblem 
			// i will made my own exception later
			throw new Exception();
		}
		
	}
	
	// Convert String date to Date
	public Date çonvertToDate(String stringDate) throws Exception {
		if(StringUtils.isBlank(stringDate)) {
			return null;
		}
		DateFormat df = new SimpleDateFormat(PREV_DATE_FORMAT);
		try {
			return df.parse(stringDate);
		}catch(ParseException e) {
			log.error(EXCEPTION_WHILE_CONVERTING_STRING_TO_DATE_EXPECTED_FORMATE_IS_MM_DD_YYYY, e.getMessage());
			// also
			// throw new EmployeeServiceProblem 
			// i will made my own exception later
			throw new Exception();
		}
		
	}
	
	// Convert to ZoneDateTime 
	public ZonedDateTime convertDateToZoneDateTime(Date value) {
		return ZonedDateTime.ofInstant(value.toInstant(), ZoneId.systemDefault());
	}
	
	

}
