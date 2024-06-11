package pk.futurenostics.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pk.futurenostics.enums.SortType;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SortConfig {
	private String sortedBy;
	private SortType sortType;

}
