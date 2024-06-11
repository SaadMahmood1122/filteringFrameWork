package pk.futurenostics.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pk.futurenostics.enums.PrimaryOption;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRequestedDto {
	private PrimaryOption primaryOptionType;
	@JsonIgnore
	private int startIndex;
	private int pageIndex;
	private int pageSize;
	
	private SortConfig sortConfig;
	
	private List<FilterConfig> filterConfigs;
	
	public int getStartIndex() {
		return (pageIndex - 1) * pageSize;
	}

}
