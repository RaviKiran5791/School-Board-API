package com.school.sba.requestdto;

import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class ScheduleRequest {

	private LocalTime opensAt;
	private LocalTime closesAt;
	private int classHoursPerday;
	private int classHoursLengthInMinutes;
	private LocalTime breaktime;
	private int breakeLengthInMinutes;
	private LocalTime lunchTime;
	private int lunchBreakLengthInMinutes;
}
