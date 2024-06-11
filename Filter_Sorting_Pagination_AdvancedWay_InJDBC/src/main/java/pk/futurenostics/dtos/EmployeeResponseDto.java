package pk.futurenostics.dtos;

import java.util.Collections;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDto {
	private EmployeeRequestedDto reqDto;
	private Long totalRecords;
	private List<EmployeeDto> records;
	
	public List<EmployeeDto> getRecords(){
		return (CollectionUtils.isNotEmpty(records)) ? records : Collections.emptyList();
	}
	

}
