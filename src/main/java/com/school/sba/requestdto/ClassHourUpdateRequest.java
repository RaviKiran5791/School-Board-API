package com.school.sba.requestdto;

import com.school.sba.enums.CLASSSTATUS;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClassHourUpdateRequest {

	private int userId;
	private int subjectId;
	private int roomNo;
	private int classHourId;
	private CLASSSTATUS classstatus;
}
