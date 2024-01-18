package com.school.sba.responsedto;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleResponse {
	
	private int scheduleId;
	private LocalTime opensAt;
	private LocalTime closesAt;
	private int classHoursPerday;
	private int classHoursLengthInMinutes;
	private LocalTime breaktime;
	private int breakeLengthInMinutes;
	private LocalTime lunchTime;
	private int lunchBreakLengthInMinutes;

}
