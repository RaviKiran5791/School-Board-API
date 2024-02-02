package com.school.sba.responsedto;

import java.time.LocalDate;
import java.util.List;

import com.school.sba.entity.Subject;
import com.school.sba.enums.PROGRAMTYPE;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcademicProgramResponse {
	
	private int programId;
	private PROGRAMTYPE programtype;
	private String programName;
	private LocalDate beginsAt;
	private LocalDate endsAt;
	private List<String> subjects;
	private boolean autoRepeateScheduled;

}
