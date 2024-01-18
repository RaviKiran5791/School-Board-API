package com.school.sba.requestdto;

import java.time.LocalDate;

import com.school.sba.enums.PROGRAMTYPE;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcademicProgramRequest {
	
	private PROGRAMTYPE programtype;
	private String programName;
	private LocalDate beginsAt;
	private LocalDate endsAt;

}
