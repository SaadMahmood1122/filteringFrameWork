package pk.futurenostics.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
public enum QuiryBuilder {
	
	ORDER_BY("GROUP BY emp_id,  ");
	
	
	String query;
}
