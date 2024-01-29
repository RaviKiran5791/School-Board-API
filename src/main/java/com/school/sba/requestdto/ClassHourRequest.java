package com.school.sba.requestdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClassHourRequest {
	

	private int userId;
	private int subjectId;
	private int roomNo;
	private int classHourId;

}
