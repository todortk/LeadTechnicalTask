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
	 * @param newStudent - the student to be added or updated. Only the "name" part of groups and courses is used and needed.
	 * @return Ok if the student was successfully created or "unprocessable entity" if a course is duplicated or some of the courses or groups do not exist.
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

	/**
	 * This method checks if the teacher or the student had specified the same course more than once
	 * @param e - the exception that was raised by the store method.
	 * @return "unprocessable entity" error if the problem was that the course was duplicated. Rethrows the exception if the problem is not because of duplicate course.
	 */
	private ResponseEntity<String> checkForDuplicateCourse(DataIntegrityViolationException e) {
		if(e.getMessage().contains("duplicate key")) {
			return ResponseEntity.unprocessableEntity().body("You have at least one duplicate course");
		}
		throw e;
	}

	/**
	 * This method checks if the teacher or the student had specified non-existing course or group.
	 * @param e - the exception that was raised by the store method.
	 * @return "unprocessable entity" error if the problem was that a course or group does not exist in the database. Rethrows the exception if we couldn't recognize the problem out of the message.
	 */
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
	
	/**
	 * Deletes a single student from the database
	 * @param name the name of the student
	 * @return "ok" if the student was successfully deleted and "not found" error if there was no such student in the database.
	 */
	@DeleteMapping("/students/")
	public ResponseEntity<String> removeStudent(@RequestParam("name") String name){
		if(studentsRepo.existsById(name)) {
			studentsRepo.deleteById(name);
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	/**
	 * Returns a JSon containing a list of all students in the database
	 * @return
	 */
	@GetMapping("/students/")
	public ResponseEntity<List<Student>> listStudents(){
		return ResponseEntity.ok(studentsRepo.findAll());
	}
	
	/**
	 * Return the number of students in the database.
	 * @return
	 */
	@GetMapping("/students/count")
	public ResponseEntity<String> getStudentsCount(){
		return ResponseEntity.ok(""+studentsRepo.count());
	}
	
	/**
	 * Add or update a teacher
	 * @param teacher the teacher to be added or updated. Only the "name" part of groups and courses is used and needed.
	 * @return Ok if the teacher was successfully created or "unprocessable entity" if a course is duplicated or some of the courses or groups do not exist.
	 */
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
	
	
	/**
	 * Deletes a single teacher from the database
	 * @param name the name of the teacher
	 * @return "ok" if the teacher was successfully deleted and "not found" error if there was no such teacher in the database.
	 */
	@DeleteMapping("/teachers")
	public ResponseEntity<String> removeTeacher(@RequestParam("name") String name){
		if(teachersRepo.existsById(name)) {
			teachersRepo.deleteById(name);
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	/**
	 * Returns a JSon containing a list of all teachers in the database
	 * @return
	 */
	@GetMapping("/teachers")
	public ResponseEntity<List<Teacher>> listTeachers(){
		return ResponseEntity.ok(teachersRepo.findAll());
	}
	
	/**
	 * Return the number of teachers in the database.
	 * @return
	 */
	@GetMapping("/teachers/count")
	public ResponseEntity<String> getTeachersCount(){
		return ResponseEntity.ok(""+teachersRepo.count());
	}
	
	/**
	 * Add or update a course
	 * @param course the course to be added or updated.
	 * @return Ok response as there is no reason for this request to fail (invalid json is handled before this method is called)
	 */
	@PostMapping("/courses")
	public ResponseEntity<String> addCourse(Course course){
		coursesRepo.save(course);
		return ResponseEntity.ok().build();
	}
	
	/**
	 * Deletes a single course from the database
	 * @param name the name of the course
	 * @return "ok" if the course was successfully deleted, "not found" error if there was no such course in the database and "unprocessable entity" error if the course is still referred by some students or teachers.
	 */
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
	
	/**
	 * Returns a JSon containing a list of all courses in the database
	 * @return
	 */
	@GetMapping("/courses")
	public ResponseEntity<List<Course>> listCourses(){
		return ResponseEntity.ok(coursesRepo.findAll());
	}

	/**
	 * Returns the number of "main" courses in the database
	 * @return
	 */
	@GetMapping("/courses/main/count")
	public ResponseEntity<String> getNumberOfMainCourses(){
		return ResponseEntity.ok(""+coursesRepo.countByType(CourseType.MAIN));
	}

	/**
	 * Returns the number of "secondary" courses in the database
	 * @return
	 */
	@GetMapping("/courses/secondary/count")
	public ResponseEntity<String> getNumberOfSecondaryCourses(){
		return ResponseEntity.ok(""+coursesRepo.countByType(CourseType.SECONDARY));
	}

	/**
	 * Add or update a group
	 * @param group the course to be added or updated.
	 * @return Ok response as there is no reason for this request to fail (invalid json is handled before this method is called)
	 */
	@PostMapping("/groups")
	public ResponseEntity<String> addGroup(@RequestBody Group group){
		groupsRepo.save(group);
		return ResponseEntity.ok().build();
	}
	
	/**
	 * Deletes a single group from the database
	 * @param name the name of the group
	 * @return "ok" if the group was successfully deleted, "not found" error if there was no such group in the database and "unprocessable entity" error if the group is still referred by some students or teachers.
	 */
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
	
	/**
	 * Returns a JSon containing a list of all groups in the database
	 * @return
	 */
	@GetMapping("/groups")
	public ResponseEntity<List<Group>> listGroups(){
		return ResponseEntity.ok(groupsRepo.findAll());
	}
	
	/**
	 * Returns a JSon containing all the students in given course
	 * @param course the name of the course
	 * @return an "ok" response as there is no reason for this request to fail. If there are no students in the course or the course does not exist an empty list will be returned.
	 */
	@GetMapping("/reports/studentsincourse")
	public ResponseEntity<List<Student>> getStudentsInCourse(@RequestParam("course") String course){
		return ResponseEntity.ok(studentsRepo.findAllByCoursesContaining(course));
	}
	
	/**
	 * Returns a JSon containing all the students in given group
	 * @param course the name of the group
	 * @return an "ok" response as there is no reason for this request to fail. If there are no students in the group or the group does not exist an empty list will be returned.
	 */
	@GetMapping("/reports/studentsingroup")
	public ResponseEntity<List<Student>> getStudentsInGroup(@RequestParam("group") String group){
		return ResponseEntity.ok(studentsRepo.findAllByGroupName(group));
	}
	
	/**
	 * Returns all students and teachers attending the specified course and group
	 * @param group the group name
	 * @param course the name of the course
	 * @return an "ok" response as there is no reason for this request to fail. If there are no students  or teachers attending the specified combination of group and course or if the course or the group do not exist, an empty teachers and/or students list will be returned.
	 */
	@GetMapping("/reports/participation")
	public ResponseEntity<ParticipationFilter> getStudentsAndTeachersByGroupAndCourse(@RequestParam("group") String group,@RequestParam("course") String course){
		List<Student> students = studentsRepo.findAllByCoursesContainingAndGroupName(course, group);
		List<Teacher> teachers = teachersRepo.findAllByCoursesContainingAndGroupName(course, group);
		ParticipationFilter result = new ParticipationFilter(students, teachers);
		return ResponseEntity.ok(result);
	}

	/**
	 * Returns all students older than the specified age who participate in the given course
	 * @param age the age in years
	 * @param course the name of the course
	 * @return an "ok" response as there is no reason for this request to fail. Will return an empty list if the course does not exist or if there are no students in the filter.
	 */
	@GetMapping("/reports/age")
	public ResponseEntity<List<Student>> getStudentsByAgeAndCourse(@RequestParam("age") int age,@RequestParam("course") String course){
		return ResponseEntity.ok(studentsRepo.findAllByAgeGreaterThanAndCoursesContaining(age, course));
	}
	

}
