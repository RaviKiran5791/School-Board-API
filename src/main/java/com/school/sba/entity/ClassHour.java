package com.school.sba.entity;

import java.time.LocalDateTime;

import com.school.sba.enums.CLASSSTATUS;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "classhours")
public class ClassHour {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int classHourId;
	private LocalDateTime beginsAt;
	private LocalDateTime endsAt;
	private int roomNo;
	@Enumerated(EnumType.STRING)
	private CLASSSTATUS classStatus;
	
	@ManyToOne
	private User user;
	
	@ManyToOne
	private AcademicProgram academicProgram;
	
	@ManyToOne
	private Subject subject;
	
	
	
		

}
