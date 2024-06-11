package pk.futurenostics.enums;

import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
@Getter
public enum EmployeeTracker {
	EMPLOYEE_ID("empId","emp.employee_id"),
	JOB("jobName","emp.job_id");
	
	private String alias;
	private String filterCondition;
	
	public static EmployeeTracker of(String aliasName){
		return Stream.of(EmployeeTracker.values())
				.filter(p ->StringUtils.equalsAnyIgnoreCase(p.alias, aliasName))
				.findFirst()
				.orElse(null);
	}
	
	// for In condition
	public String getInCondition() {return String.format("%s::text IN (:%s)", filterCondition, alias);}
	
	public String getInConditionWithNoneOption() {return String.format("(%s is null or %s::text IN (:%s))", filterCondition,filterCondition, alias);}
	
	// for sorting 
	
	public String getSortedBy(SortType sortType) { return String.format("order by %s %s", filterCondition, sortType.name());}
	
	public String getStartCondition() {return String.format(" %s >= :start%s ", filterCondition,alias);}
	
	public String getEndCondition() {return String.format(" %s >= :end%s ", filterCondition,alias);}
	
	public String getLikeCondition() {return String.format("UPPER(%s)::text LIKE :%s ", filterCondition,alias);}
	
	public String getStartParam() {return String.format("start%s", alias);}

	public String getEndParam() {return String.format("end%s", alias);}

}
