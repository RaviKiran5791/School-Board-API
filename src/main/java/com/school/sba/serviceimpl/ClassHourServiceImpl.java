package com.school.sba.serviceimpl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.school.sba.entity.AcademicProgram;
import com.school.sba.entity.ClassHour;
import com.school.sba.entity.Schedule;
import com.school.sba.enums.CLASSSTATUS;
import com.school.sba.enums.USERROLE;
import com.school.sba.exception.ClassHourNotFoundByIdException;
import com.school.sba.exception.DataAlreadyExistException;
import com.school.sba.exception.DataNotExistException;
import com.school.sba.exception.IllegalRequestException;
import com.school.sba.exception.ProgramNotFoundByIdException;
import com.school.sba.exception.ScheduleNotFoundException;
import com.school.sba.exception.SubjectNotFoundByIdException;
import com.school.sba.exception.UserNotFoundByIdException;
import com.school.sba.repositary.AcademicProgramRepositary;
import com.school.sba.repositary.ClassHourRepositary;
import com.school.sba.repositary.SubjectRepositary;
import com.school.sba.repositary.UserRepositary;
import com.school.sba.requestdto.ClassHourRequest;
import com.school.sba.requestdto.ExcelRequestDto;
import com.school.sba.responsedto.ClassHourResponse;
import com.school.sba.service.ClassHourService;
import com.school.sba.utility.ResponseStructure;
@Service
public class ClassHourServiceImpl implements ClassHourService{


	@Autowired
	private ClassHourRepositary classHourRepo;
	@Autowired
	private AcademicProgramRepositary programRepo;
	@Autowired
	private SubjectRepositary subjectRepo;
	@Autowired
	private UserRepositary userRepo;


	private ClassHourResponse mapToResponse(ClassHour classHour) {
		return new ClassHourResponse().builder()
				.classHourId(classHour.getClassHourId())
				.beginsAt(classHour.getBeginsAt())
				.endsAt(classHour.getEndsAt())
				.roomNo(classHour.getRoomNo())
				.build();
	}

	private ClassHour mapToNewClassHour(ClassHour existingClassHour) {
		return ClassHour.builder()
				.user(existingClassHour.getUser())
				.academicProgram(existingClassHour.getAcademicProgram())
				.roomNo(existingClassHour.getRoomNo())
				.beginsAt(existingClassHour.getBeginsAt().plusDays(7))
				.endsAt(existingClassHour.getEndsAt().plusDays(7))
				.classStatus(existingClassHour.getClassStatus())
				.subject(existingClassHour.getSubject())
				.build();	
	}

	private ClassHour deleteSchedule(ClassHour classHour)
	{
		classHourRepo.delete(classHour);
		return classHour;
	}

	@Autowired
	private ResponseStructure<ClassHourResponse> structure;


	private LocalDateTime dateToDateTime(LocalDate date, LocalTime time){
		return LocalDateTime.of(date,time);
	}

	@Override
	public ResponseEntity<ResponseStructure<ClassHourResponse>> addClassHoursToAcademicProgram(int programId,ClassHourRequest request) {
		return programRepo.findById(programId)
				.map(program ->{
					Schedule schedule = program.getSchool().getSchedule();

					if(schedule == null) 
					{ throw new ScheduleNotFoundException("Failed to GENERATE Class Hour"); }

					if(program.getClassHours()==null || program.getClassHours().isEmpty())
					{
						List<ClassHour> perDayClasshour = new ArrayList<ClassHour>();
						LocalDate date = program.getBeginsAt();
						int end=6;

						DayOfWeek dayOfWeek = date.getDayOfWeek();

						if(!dayOfWeek.equals(DayOfWeek.MONDAY))
							end=end+(7-dayOfWeek.getValue());


						// for generating day
						for(int day=1; day<=end; day++) { 

							if(date.getDayOfWeek().equals(DayOfWeek.SUNDAY))
								date=date.plusDays(1);

							LocalTime currentTime = schedule.getOpensAt();
							LocalDateTime lasthour = null;

							// for generating class hours per day
							for(int entry=1; entry<=schedule.getClassHoursPerday(); entry++) { 
								ClassHour classhour = new ClassHour();

								if(currentTime.equals(schedule.getOpensAt())) { // first class hour of the day
									classhour.setBeginsAt(dateToDateTime(date,currentTime));
								}
								else if(currentTime.equals(schedule.getBreaktime())) {  // after break time
									lasthour = lasthour.plus(schedule.getBreakeLengthInMinutes());
									classhour.setBeginsAt(dateToDateTime(date, lasthour.toLocalTime()));
								}
								else if(currentTime.equals(schedule.getLunchTime())) {  // after lunch time
									lasthour = lasthour.plus(schedule.getLunchBreakLengthInMinutes());
									classhour.setBeginsAt(dateToDateTime(date, lasthour.toLocalTime()));
								}
								else { // rest class hours of that day
									classhour.setBeginsAt(dateToDateTime(date, lasthour.toLocalTime()));
								}
								classhour.setEndsAt(classhour.getBeginsAt().plus(schedule.getClassHoursLengthInMinutes()));
								classhour.setClassStatus(CLASSSTATUS.NOT_SCHEDULED);
								classhour.setAcademicProgram(program);

								perDayClasshour.add(classHourRepo.save(classhour));

								lasthour = perDayClasshour.get(entry-1).getEndsAt();

								currentTime = lasthour.toLocalTime();

								if(currentTime.equals(schedule.getClosesAt())) // school closing time
									break;

							}
							date = date.plusDays(1);
						}
						program.setClassHours(perDayClasshour);
						programRepo.save(program);

						structure.setStatus(programId);
						structure.setMessage("Classhour GENERATED for Program: "+program.getProgramName());
						structure.setData(null);

						return new ResponseEntity<ResponseStructure<ClassHourResponse>> (structure, HttpStatus.CREATED);
					}
					else
						throw new IllegalRequestException("Classhours Already Generated for :: "+program.getProgramName()+" of ID: "+program.getProgramId());

				})
				.orElseThrow(() -> new ProgramNotFoundByIdException("Failed to GENERATE Class Hour"));
	}


	//	public ResponseEntity<ResponseStructure<ClassHourResponse>> addClassHoursToAcademicProgram(int programId, ClassHourRequest request) {
	//	    return programRepo.findById(programId)
	//	            .map(program -> {
	//	                Schedule schedule = program.getSchool().getSchedule();
	//
	//	                if (schedule == null) {
	//	                    throw new ScheduleNotFoundException("Failed to GENERATE Class Hour");
	//	                }
	//
	//	                if (program.getClassHours() == null || program.getClassHours().isEmpty()) {
	//	                    List<ClassHour> perDayClasshour = new ArrayList<>();
	//	                    
	//	                    LocalDate startDate = program.getBeginsAt();
	//	                    LocalDate endDate = program.getEndsAt();
	//
	//	                    // Calculate the end date for the first week based on the program start date
	//	                    LocalDate firstWeekEndDate;
	//	                    
	//	                    if (startDate.getDayOfWeek() == DayOfWeek.MONDAY) {
	//	                        firstWeekEndDate = startDate.with(TemporalAdjusters.next(DayOfWeek.SATURDAY));
	//	                    } else {
	//	                        firstWeekEndDate = startDate.with(TemporalAdjusters.next(DayOfWeek.SUNDAY))
	//	                                .with(TemporalAdjusters.next(DayOfWeek.SATURDAY));
	//	                    }
	//
	//	                    while (startDate.isBefore(firstWeekEndDate) || startDate.isEqual(firstWeekEndDate)) {
	//	                        if (startDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
	//	                            LocalTime currentTime = schedule.getOpensAt();
	//	                            LocalDateTime lastHour = null;
	//
	//	                            // Generate class hours per day
	//	                            for (int entry = 1; entry <= schedule.getClassHoursPerday(); entry++) {
	//	                                ClassHour classHour = new ClassHour();
	//
	//	                                if (currentTime.equals(schedule.getOpensAt())) {
	//	                                    classHour.setBeginsAt(dateToDateTime(startDate, currentTime));
	//	                                } else if (currentTime.equals(schedule.getBreaktime())) {
	//	                                    lastHour = lastHour.plus(schedule.getBreakeLengthInMinutes());
	//	                                    classHour.setBeginsAt(dateToDateTime(startDate, lastHour.toLocalTime()));
	//	                                } else if (currentTime.equals(schedule.getLunchTime())) {
	//	                                    lastHour = lastHour.plus(schedule.getLunchBreakLengthInMinutes());
	//	                                    classHour.setBeginsAt(dateToDateTime(startDate, lastHour.toLocalTime()));
	//	                                } else {
	//	                                    classHour.setBeginsAt(dateToDateTime(startDate, lastHour.toLocalTime()));
	//	                                }
	//
	//	                                classHour.setEndsAt(classHour.getBeginsAt().plus(schedule.getClassHoursLengthInMinutes()));
	//	                                classHour.setClassStatus(CLASSSTATUS.NOT_SCHEDULED);
	//	                                classHour.setAcademicProgram(program);
	//
	//	                                perDayClasshour.add(classHourRepo.save(classHour));
	//
	//	                                lastHour = perDayClasshour.get(entry - 1).getEndsAt();
	//	                                currentTime = lastHour.toLocalTime();
	//
	//	                                if (currentTime.equals(schedule.getClosesAt())) {
	//	                                    break;
	//	                                }
	//	                            }
	//	                        }
	//	                        startDate = startDate.plusDays(1);
	//	                    }
	//
	//	                    program.setClassHours(perDayClasshour);
	//	                    programRepo.save(program);
	//
	//	                    structure.setStatus(programId);
	//	                    structure.setMessage("Classhour GENERATED for Program: " + program.getProgramName());
	//	                    structure.setData(null);
	//
	//	                    return new ResponseEntity<>(structure, HttpStatus.CREATED);
	//	                } else {
	//	                    throw new IllegalRequestException("Classhours Already Generated for :: " + program.getProgramName() + " of ID: " + program.getProgramId());
	//	                }
	//	            })
	//	            .orElseThrow(() -> new ProgramNotFoundByIdException("Failed to GENERATE Class Hour"));
	//	}




	@Override
	public ResponseEntity<ResponseStructure<List<ClassHourResponse>>> updateClassHour(
			List<ClassHourRequest> classhourequestlist) {


		List<ClassHourResponse> updatedClassHourResponses=new ArrayList<>();
		ResponseStructure<List<ClassHourResponse>> structure=new ResponseStructure<>();


		for(ClassHourRequest req : classhourequestlist)
		{
			return	userRepo.findById(req.getUserId()).map(user ->{

				return classHourRepo.findById(req.getClassHourId()).map(classHour->{

					return subjectRepo.findById(req.getSubjectId()).map(subject->{

						if(user.getUserRole().equals(USERROLE.TEACHER) && user.getSubject().equals(subject))
						{

							boolean isPresent=classHourRepo.existsByBeginsAtBetweenAndRoomNo(classHour.getBeginsAt(), classHour.getEndsAt(), req.getRoomNo());

							if(isPresent)
							{
								throw new DataAlreadyExistException("class room already assigned");
							}

							else 
							{
								classHour.setSubject(subject);
								classHour.setUser(user);
								classHour.setRoomNo(req.getRoomNo());

								classHourRepo.save(classHour);

								updatedClassHourResponses.add(mapToResponse(classHour));


								structure.setStatus(HttpStatus.OK.value());
								structure.setMessage("Updated");
								structure.setData(updatedClassHourResponses);

								return new ResponseEntity<ResponseStructure<List<ClassHourResponse>>>(structure,HttpStatus.OK);
							}
						}

						else 
							throw new IllegalRequestException("Unable to update teacher with given subject");


					}).orElseThrow(()->new SubjectNotFoundByIdException("subject not found "));

				}).orElseThrow(()->new ClassHourNotFoundByIdException("Class houn not present"));

			}).orElseThrow(()->new UserNotFoundByIdException("user not found"));
		}
		return null;

	}

	public void generateWeeklyClassHours()
	{

		List<AcademicProgram> programsToBeAutoRepeated = programRepo.findByAutoRepeateScheduledTrue();		{

			if(!programsToBeAutoRepeated.isEmpty())
			{
				programsToBeAutoRepeated.forEach(program->{


					int n=program.getSchool().getSchedule().getClassHoursPerday() * 6;
					// getting last week class hour
					List<ClassHour> lastWeekClassHours = classHourRepo.findLastNRecordsByAcademicProgram(program, n);

					if(!lastWeekClassHours.isEmpty())
					{
						for(int i=lastWeekClassHours.size()-1;i>=0;i--)
						{
							ClassHour existClassHour = lastWeekClassHours.get(i);
							classHourRepo.save(mapToNewClassHour(existClassHour));

						}

						System.out.println("this week data generated as per last week data");
					}
					System.out.println("No Last week data present");
				});
				System.out.println("Schedule Successfully Auto Repeated for the Upcoming WEEK.");
			}
			else
				System.out.println("Auto Repeat Schedule : OFF");
		}

	}

	@Override
	public ResponseEntity<ResponseStructure<String>>  writeIntoXlSheet(int programId,ExcelRequestDto excelRequestDto) {

		return programRepo.findById(programId).map(program->{
			if(!program.isDeleted())
			{
				LocalDateTime from=excelRequestDto.getFromDate().atTime(LocalTime.MIDNIGHT);
				LocalDateTime to=excelRequestDto.getToDate().atTime(LocalTime.MIDNIGHT).plusDays(1);
				List<ClassHour> classHours = classHourRepo.findAllByAcademicProgramAndBeginsAtBetween(program, from, to);

				if(!classHours.isEmpty())
				{
					XSSFWorkbook writeBook=new XSSFWorkbook();
					Sheet sheet=writeBook.createSheet();
					int rowNumber=0;
					Row header=sheet.createRow(rowNumber);
					header.createCell(0).setCellValue("Date");
					header.createCell(1).setCellValue("Begin Time");
					header.createCell(2).setCellValue("End Time");
					header.createCell(3).setCellValue("Subject");
					header.createCell(4).setCellValue("Teacher");
					header.createCell(5).setCellValue("Room No");

					DateTimeFormatter timeFormatter=DateTimeFormatter.ofPattern("HH-mm");
					DateTimeFormatter dateFormatter=DateTimeFormatter.ofPattern("YYYY-MM-dd");


					for(ClassHour classHour: classHours)
					{
						Row row=sheet.createRow(++rowNumber);
						row.createCell(0).setCellValue(dateFormatter.format(classHour.getBeginsAt()));
						row.createCell(1).setCellValue(timeFormatter.format(classHour.getBeginsAt()));
						row.createCell(2).setCellValue(timeFormatter.format(classHour.getEndsAt()));

						if(classHour.getSubject()==null)
							row.createCell(3).setCellValue("NOT AVAILABLE");
						else
							row.createCell(3).setCellValue(classHour.getSubject().getSubjectName());

						if(classHour.getUser()==null)
							row.createCell(4).setCellValue("NOT AVAILABLE");
						else
							row.createCell(4).setCellValue(classHour.getUser().getUserName());

						row.createCell(5).setCellValue(classHour.getRoomNo());	
					}

					try {
						writeBook.write(new FileOutputStream(excelRequestDto.getFilePath()+"\\Classhours"+excelRequestDto.getFromDate()+excelRequestDto.getToDate()+".xlsx"));
					} 
					catch (IOException e) {
						e.printStackTrace();
					}



					ResponseStructure<String> structure=new ResponseStructure<>();

					structure.setStatus(HttpStatus.CREATED.value());
					structure.setMessage("Excel Sheet Created Successfully");
					structure.setData("Excel for the program"+programId);



					return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.CREATED);

				}
				else
					throw new DataNotExistException("Data Not Present, No class Hours present");
			}
			else
				throw new DataNotExistException("Program  Already Deleted");

		}).orElseThrow(()->new ProgramNotFoundByIdException("Program not present for given program id"));


	}

	@Override
	public ResponseEntity<?> writeToXlSheet(int programId, LocalDate fromDate, LocalDate toDate,
			MultipartFile multipartFile){
		
		return programRepo.findById(programId).map(program->{
			if(!program.isDeleted())
			{
				LocalDateTime from=fromDate.atTime(LocalTime.MIDNIGHT);
				LocalDateTime to=toDate.atTime(LocalTime.MIDNIGHT).plusDays(1);
				List<ClassHour> classHours = classHourRepo.findAllByAcademicProgramAndBeginsAtBetween(program, from, to);
				
				
				DateTimeFormatter timeFormatter=DateTimeFormatter.ofPattern("HH-mm");
				DateTimeFormatter dateFormatter=DateTimeFormatter.ofPattern("YYYY-MM-dd");
				
				XSSFWorkbook writeBook = null;
				try {
					writeBook = new XSSFWorkbook(multipartFile.getInputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(!classHours.isEmpty())
				{
		
					writeBook.forEach(sheet->{
						int rowNumber=0;
						Row header=sheet.createRow(rowNumber);
						header.createCell(0).setCellValue("Date");
						header.createCell(1).setCellValue("Begin Time");
						header.createCell(2).setCellValue("End Time");
						header.createCell(3).setCellValue("Subject");
						header.createCell(4).setCellValue("Teacher");
						header.createCell(5).setCellValue("Room No");
						
						
						for(ClassHour classHour: classHours)
						{
							Row row=sheet.createRow(++rowNumber);
							row.createCell(0).setCellValue(dateFormatter.format(classHour.getBeginsAt()));
							row.createCell(1).setCellValue(timeFormatter.format(classHour.getBeginsAt()));
							row.createCell(2).setCellValue(timeFormatter.format(classHour.getEndsAt()));

							if(classHour.getSubject()==null)
								row.createCell(3).setCellValue("NOT AVAILABLE");
							else
								row.createCell(3).setCellValue(classHour.getSubject().getSubjectName());

							if(classHour.getUser()==null)
								row.createCell(4).setCellValue("NOT AVAILABLE");
							else
								row.createCell(4).setCellValue(classHour.getUser().getUserName());

							row.createCell(5).setCellValue(classHour.getRoomNo());	
						}
						
					});
					ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
					try {
						writeBook.write(outputStream);
						writeBook.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					byte[] byteData = outputStream.toByteArray();

					return ResponseEntity.ok().header("Content Disposition", "attachement; filename="+multipartFile.getOriginalFilename())
							.contentType(MediaType.APPLICATION_OCTET_STREAM)
							.body(byteData);
				}
				else
					throw new DataNotExistException("Data Not Present, No class Hours present");
			}
			else
				throw new DataNotExistException("Program  Already Deleted");

		}).orElseThrow(()->new ProgramNotFoundByIdException("Program not present for given program id"));
	}
}
