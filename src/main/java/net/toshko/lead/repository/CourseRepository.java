package net.toshko.lead.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.toshko.lead.entity.Course;
import net.toshko.lead.enums.CourseType;

public interface CourseRepository extends JpaRepository<Course, String> {
	int countByType(CourseType type);
}
