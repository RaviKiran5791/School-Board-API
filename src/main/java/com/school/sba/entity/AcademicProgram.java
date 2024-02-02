package com.school.sba.entity;

import java.time.LocalDate;
import java.util.List;

import com.school.sba.enums.PROGRAMTYPE;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "academicprograms")
public class AcademicProgram {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int programId;
	
	@Enumerated(EnumType.STRING)
	private PROGRAMTYPE programtype;
	private String programName;
	private LocalDate beginsAt;
	private LocalDate endsAt;
	private boolean isDeleted;
	private boolean autoRepeateScheduled;
	
	@ManyToOne
	private School school;
	
	@ManyToMany
	private List<Subject> subjects;
	
	@ManyToMany
	private List<User> users;
	
	@OneToMany(mappedBy = "academicProgram")
	private List<ClassHour> classHours;

}
