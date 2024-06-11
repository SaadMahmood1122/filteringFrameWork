package pk.futurenostics.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pk.futurenostics.enums.FilterType;
import pk.futurenostics.enums.JoinedTable;
import pk.futurenostics.enums.PrimaryOption;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilterConfig {
	private String filteredBy;
	private List<String> values;
	private FilterType type;
	@JsonIgnore
	private JoinedTable tableName;

}
