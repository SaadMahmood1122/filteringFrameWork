package pk.futurenostics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pk.futurenostics.dtos.EmployeeRequestedDto;
import pk.futurenostics.dtos.EmployeeResponseDto;
import pk.futurenostics.services.EmpInfoService;

@RestController
@RequestMapping("/api/emp-records")
@Slf4j
public class EmployeeController {
	@Autowired
	private EmpInfoService empInfoService;
	@PostMapping("/fetch-emp-records")
	public EmployeeResponseDto getEmployeeRecords(@RequestBody EmployeeRequestedDto employeeRequestedDto) {
		
		log.info("Fetching Employee records.......");
		return empInfoService.getEmpRecords(employeeRequestedDto);
	}

}
