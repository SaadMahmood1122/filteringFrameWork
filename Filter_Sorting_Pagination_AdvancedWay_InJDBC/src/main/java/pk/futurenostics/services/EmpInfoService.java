package pk.futurenostics.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.data.querydsl.binding.QuerydslPredicateBuilder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import pk.futurenostics.dtos.EmployeeDto;
import pk.futurenostics.dtos.EmployeeRequestedDto;
import pk.futurenostics.dtos.EmployeeResponseDto;
import pk.futurenostics.dtos.FilterCondition;
import pk.futurenostics.dtos.FilterConfig;
import pk.futurenostics.entities.UtilityService;
import pk.futurenostics.enums.EmployeeTracker;
import pk.futurenostics.enums.QuiryBuilder;
import pk.futurenostics.enums.SortType;

@Service
@Slf4j
public class EmpInfoService {
	@Autowired
	private UtilityService utilityService;
	
	public EmployeeResponseDto getEmpRecords(EmployeeRequestedDto reqDto) {
		log.info("fetching emp-records from service with for request {} ", reqDto);
		 EmployeeResponseDto respDto = new EmployeeResponseDto();
		 respDto.setReqDto(reqDto);
		 
		 getEmployees(reqDto,respDto);
		 
		 log.info("complete fetching emp-records from service with for request {}",reqDto);
		
		return respDto;
	}
	
	
	
	
	
	
	private void getEmployees(EmployeeRequestedDto reqDto,EmployeeResponseDto respDto){
		var empList = fetchEmployeeRecords(reqDto);
		if(org.apache.commons.collections4.CollectionUtils.isEmpty(empList)) {
			respDto.setTotalRecords(0L);
			
		}
		
		respDto.setRecords(empList);
		
	}






	private List<EmployeeDto> fetchEmployeeRecords(EmployeeRequestedDto reqDto) {
		Map<String,Object> parameters = new HashedMap<>();
		parameters.put("pageSize", reqDto.getPageSize());
		parameters.put("pageIndex", reqDto.getPageIndex());
	    
		FilterCondition filterCondition = filterConfiguration(reqDto, parameters);
		 String sortedBy = populateSortedBy(reqDto,filterCondition);
		
		return null;
	
	}





	private String populateSortedBy(EmployeeRequestedDto reqDto, FilterCondition filterCondition) {
		if(reqDto.getSortConfig() != null && StringUtils.isNotBlank(reqDto.getSortConfig().getSortedBy())) {
			EmployeeTracker sortedAlias = EmployeeTracker.of(reqDto.getSortConfig().getSortedBy());
			if(!StringUtils.contains(QuiryBuilder.ORDER_BY.getQuery(), sortedAlias.getAlias())) {
				Set<String> additionalGroupBy = org.springframework.util.CollectionUtils.isEmpty(filterCondition.getAdditionalGroupBy())
						? new HashSet<>() :filterCondition.getAdditionalGroupBy();
				additionalGroupBy.add(sortedAlias.getFilterCondition());
				filterCondition.setAdditionalGroupBy(additionalGroupBy);
			}
			return sortedAlias.getSortedBy(reqDto.getSortConfig().getSortType());
		}
		return EmployeeTracker.EMPLOYEE_ID.getSortedBy(SortType.DESC);
	}






	private FilterCondition filterConfiguration(EmployeeRequestedDto reqDto, Map<String, Object> parameters) {
		var filters = Optional.ofNullable(reqDto.getFilterConfigs())  
		.orElse(new ArrayList<>())
		.stream()
		.filter(Objects::nonNull)
		.filter(f ->EmployeeTracker.of(f.getFilteredBy()) !=null)
		.filter(f -> org.apache.commons.collections4.CollectionUtils.isNotEmpty(f.getValues()))
		.collect(Collectors.toSet());
		
		Map<String,String> joinConditions = new HashMap<String, String>();
		
		if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(filters)) {
			List<String> conditions =new ArrayList<String>();
			
			filters
			.stream()
			.filter(Objects::nonNull)
			.filter(f -> org.apache.commons.collections4.CollectionUtils.isNotEmpty(f.getValues()))
			.forEach(
					filter ->{
						var filterType = filter.getType();
						var alias = EmployeeTracker.of(filter.getFilteredBy());
						var filterValues = filter.getValues();
						switch (filterType) {
						case MULTISELECT: {
							if(filterValues.contains("None")) {
								conditions.add(alias.getInConditionWithNoneOption());
							}
							else {
								conditions.add(alias.getInCondition());
							}
							
							parameters.put(alias.getAlias(), filterValues);
							break;
						}
							
			
						case DATE_RANGE: {
							  List<Date> dates = filterValues.stream()
									  .map(v ->{
										  if(StringUtils.isNotBlank(v)) {
											  try {
												return utilityService.StringToTimeStamp(v);
											} catch (Exception e) {
												log.error("Exception Coming from the StringToTImeStamp method");
												e.printStackTrace();
											}
										  }
										  return null;
									  }).map(date ->{
										  if(date == null) {
											  return null;
										  }
										  return Date.from(date.toInstant());
									  }).collect(Collectors.toList());
							          rangeValuesFilter(dates, alias, parameters, conditions);
							 break;
						}
						
						case PARTIAL: {
							
							conditions.add(alias.getLikeCondition());
							parameters.put(alias.getAlias(), StringUtils.join("%", StringUtils.upperCase(filterValues.get(0)),"%"));
							break;
						}
						case NUMBER_RANGE: {
							 List<Double> numbers = filterValues.stream().map(v -> {
								  if(v != null) {
									  return Double.valueOf(v);
								  }
								  return null;
							  }).collect(Collectors.toList());
							  rangeValuesFilter(numbers, alias, parameters, conditions);
							
							break;
						}
						default:
							break;
						}
						
					}
					
					);
			
			if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(conditions)) {
				return FilterCondition.builder()
						.additionalJoinCondition(joinConditions)
						.whereCondition(StringUtils.join(conditions, " AND "))
						.build();
			}
		}
		return FilterCondition.builder()
				.additionalJoinCondition(new HashMap<>())
				.whereCondition(StringUtils.EMPTY).build();
	}
	
	
	private <T> void rangeValuesFilter(List<T> values, EmployeeTracker tracker,
			Map<String, Object> parameters, List<String> conditions){
		
		if(org.apache.commons.collections4.CollectionUtils.isEmpty(values)) {
			return;
		}
		
		if(values.get(0) != null && StringUtils.isNotBlank(String.valueOf(values.get(0)))) {
			conditions.add(tracker.getStartCondition());
			parameters.put(tracker.getStartParam(), values.get(0));
		}
		
		if(values.size() >= 2 && values.get(1) != null && StringUtils.isNotBlank(String.valueOf(values.get(1)))) {
			conditions.add(tracker.getEndCondition());
			parameters.put(tracker.getEndParam(), values.get(1));
		}
		
	}
	
//	private void filterForCondition(FilterConfig filter, List<String> condition) {
//		
//	}

}
