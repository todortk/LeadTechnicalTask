package net.toshko.lead.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.toshko.lead.enums.CourseType;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
@Table(name = "courses")
public class Course {

	@Id
	String name;
	String description;
	CourseType type;
}
