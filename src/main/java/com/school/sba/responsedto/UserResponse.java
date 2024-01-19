package com.school.sba.responsedto;

import java.util.List;

import com.school.sba.entity.AcademicProgram;
import com.school.sba.enums.USERROLE;

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
public class UserResponse {
	private int userId;
	private String userName;
	private String firstName;
	private String lastName;
	private long contactNo;
	private String email;
	private USERROLE userRole;
	private boolean isDeleted;
	private List<AcademicProgram> academicPrograms;

}
