package pk.futurenostics.dtos;

import java.util.Map;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilterCondition {
	
	private String whereCondition;
	private Map<String, String> additionalJoinCondition;
	private Set<String> additionalGroupBy;

}
