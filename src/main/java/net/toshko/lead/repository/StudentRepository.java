package net.toshko.lead.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.toshko.lead.entity.Student;

public interface StudentRepository extends JpaRepository<Student, String> {

	List<Student> findAllByGroupName(String group);

	@Query("select c from Student c join c.courses s where s.name=:course")
	List<Student> findAllByCoursesContaining(@Param("course") String course);

	@Query("select c from Student c join c.courses s where s.name=:course and c.group.name=:group")
	List<Student> findAllByCoursesContainingAndGroupName(@Param("course") String course,@Param("group")String group);
	
	@Query("select c from Student c join c.courses s where s.name=:course and c.age>:age")
	List<Student> findAllByAgeGreaterThanAndCoursesContaining(@Param("age") int age,@Param("course") String course);
}
