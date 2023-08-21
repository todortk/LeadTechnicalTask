package net.toshko.lead.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityNotFoundException;
import net.toshko.lead.entity.Course;
import net.toshko.lead.entity.Group;
import net.toshko.lead.entity.ParticipationFilter;
import net.toshko.lead.entity.Student;
import net.toshko.lead.entity.Teacher;
import net.toshko.lead.enums.CourseType;
import net.toshko.lead.repository.CourseRepository;
import net.toshko.lead.repository.GroupRepository;
import net.toshko.lead.repository.StudentRepository;
import net.toshko.lead.repository.TeacherRepository;

@RestController
public class StudentAPIController {
	
	@Autowired
	GroupRepository groupsRepo;
	
	@Autowired
	CourseRepository coursesRepo;
	
	@Autowired
	TeacherRepository teachersRepo;
	
	@Autowired
	StudentRepository studentsRepo;

	private static final String GROUP_NOT_FOUND = "Unable to find net.toshko.lead.entity.Group with id";
	private static final String COURSE_NOT_FOUND = "Unable to find net.toshko.lead.entity.Course with id";
	
	/**
	 * Add or update a student
	 * @param newStudent - the student to be added or updated
	 * @return
	 */
	@PostMapping("/students/")
	public ResponseEntity<String> addStudents(@RequestBody Student student){
		try {
			studentsRepo.save(student);
		} catch (JpaObjectRetrievalFailureException e) {
			return processInvalidGroupOrCourse(e);
		} catch (DataIntegrityViolationException e){
			return checkForDuplicateCourse(e);
		}
		return ResponseEntity.ok().build();
	}

	private ResponseEntity<String> checkForDuplicateCourse(DataIntegrityViolationException e) {
		if(e.getMessage().contains("duplicate key")) {
			return ResponseEntity.unprocessableEntity().body("You have at least one duplicate course");
		}
		throw e;
	}

	private ResponseEntity<String> processInvalidGroupOrCourse(JpaObjectRetrievalFailureException e) {
		String msg = e.getMessage();
		int groupMessageIndex = msg.indexOf(GROUP_NOT_FOUND);
		if(groupMessageIndex>=0) {
			return ResponseEntity.unprocessableEntity().body("Group with name ["+msg.substring(groupMessageIndex+GROUP_NOT_FOUND.length()+1)+"] does not exist.");
		}
		int courseMessageIndex = msg.indexOf(COURSE_NOT_FOUND);
		if(courseMessageIndex>=0) {
			return ResponseEntity.unprocessableEntity().body("Course with name ["+msg.substring(courseMessageIndex+COURSE_NOT_FOUND.length()+1)+"] does not exist.");
		}
		throw e;
	}
	
	@DeleteMapping("/students/")
	public ResponseEntity<String> removeStudent(@RequestParam("name") String name){
		if(studentsRepo.existsById(name)) {
			studentsRepo.deleteById(name);
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/students/")
	public ResponseEntity<List<Student>> listStudents(){
		return ResponseEntity.ok(studentsRepo.findAll());
	}
	
	@GetMapping("/students/count")
	public ResponseEntity<String> getStudentsCount(){
		return ResponseEntity.ok(""+studentsRepo.count());
	}
	
	@PostMapping("/teachers")
	public ResponseEntity<String> addTeacher(@RequestBody Teacher teacher){
		try {
			teachersRepo.save(teacher);
		} catch (JpaObjectRetrievalFailureException e) {
			return processInvalidGroupOrCourse(e);
		} catch (DataIntegrityViolationException e){
			return checkForDuplicateCourse(e);
		}
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/teachers")
	public ResponseEntity<String> removeTeacher(@RequestParam("name") String name){
		if(teachersRepo.existsById(name)) {
			teachersRepo.deleteById(name);
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/teachers")
	public ResponseEntity<List<Teacher>> listTeachers(){
		return ResponseEntity.ok(teachersRepo.findAll());
	}
	
	@GetMapping("/teachers/count")
	public ResponseEntity<String> getTeachersCount(){
		return ResponseEntity.ok(""+teachersRepo.count());
	}
	
	@PostMapping("/courses")
	public ResponseEntity<String> addCourse(Course course){
		coursesRepo.save(course);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/courses")
	public ResponseEntity<String> removeCourse(@RequestParam("name") String name){
		if(coursesRepo.existsById(name)) {
			try {
				coursesRepo.deleteById(name);
			} catch (DataIntegrityViolationException t) {
				if(t.getMessage().contains("violates foreign key constraint")) {
					return ResponseEntity.unprocessableEntity().body("The course ["+name+"] is still referenced by some students or teachers. Remove all references before deleting the course." );
				}
				throw t;
			}
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/courses")
	public ResponseEntity<List<Course>> listCourses(){
		return ResponseEntity.ok(coursesRepo.findAll());
	}

	@GetMapping("/courses/main/count")
	public ResponseEntity<String> getNumberOfMainCourses(){
		return ResponseEntity.ok(""+coursesRepo.countByType(CourseType.MAIN));
	}

	@GetMapping("/courses/secondary/count")
	public ResponseEntity<String> getNumberOfSecondaryCourses(){
		return ResponseEntity.ok(""+coursesRepo.countByType(CourseType.SECONDARY));
	}

	@PostMapping("/groups")
	public ResponseEntity<String> addGroup(@RequestBody Group group){
		groupsRepo.save(group);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/groups")
	public ResponseEntity<String> removeGroup(@RequestParam("name") String name){
		if(groupsRepo.existsById(name)) {
			try {
				groupsRepo.deleteById(name);
			} catch (DataIntegrityViolationException t) {
				if(t.getMessage().contains("violates foreign key constraint")) {
					return ResponseEntity.unprocessableEntity().body("The group ["+name+"] is still referenced by some students or teachers. Remove all references before deleting the group." );
				}
				throw t;
			}
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/groups")
	public ResponseEntity<List<Group>> listGroups(){
		return ResponseEntity.ok(groupsRepo.findAll());
	}
	
	
	@GetMapping("/reports/studentsincourse")
	public ResponseEntity<List<Student>> getStudentsInCourse(@RequestParam("course") String course){
		return ResponseEntity.ok(studentsRepo.findAllByCoursesContaining(course));
	}
	
	@GetMapping("/reports/studentsingroup")
	public ResponseEntity<List<Student>> getStudentsInGroup(@RequestParam("group") String group){
		return ResponseEntity.ok(studentsRepo.findAllByGroupName(group));
	}
	
	@GetMapping("/reports/participation")
	public ResponseEntity<ParticipationFilter> getStudentsAndTeachersByGroupAndCourse(@RequestParam("group") String group,@RequestParam("course") String course){
		List<Student> students = studentsRepo.findAllByCoursesContainingAndGroupName(course, group);
		List<Teacher> teachers = teachersRepo.findAllByCoursesContainingAndGroupName(course, group);
		ParticipationFilter result = new ParticipationFilter(students, teachers);
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/reports/age")
	public ResponseEntity<List<Student>> getStudentsByAgeAndCourse(@RequestParam("age") int age,@RequestParam("course") String course){
		return ResponseEntity.ok(studentsRepo.findAllByAgeGreaterThanAndCoursesContaining(age, course));
	}
	
	

}
