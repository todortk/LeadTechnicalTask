package net.toshko.lead.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.toshko.lead.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, String> {

	@Query("select c from Teacher c join c.courses s where s.name=:course and c.group.name=:group")
	List<Teacher> findAllByCoursesContainingAndGroupName(@Param("course") String course,@Param("group")String group);
	
}
