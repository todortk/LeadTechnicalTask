package net.toshko.lead.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
@Table(name = "students")
public class Student {
	
	@Id
	String name;

	int age;

	@ManyToOne
	@JoinColumn(name="studentgroup", referencedColumnName="name")
	Group group;

	@ManyToMany
	@JoinTable(name="studentcourses",joinColumns=
            {@JoinColumn(name="student_name", referencedColumnName="name")},
        inverseJoinColumns=
            {@JoinColumn(name="course_name", referencedColumnName="name")},
        uniqueConstraints = { @UniqueConstraint(name = "UniqueStudentAndCourse", columnNames = { "student_name", "course_name" }) }
	)
	List<Course> courses;

}
