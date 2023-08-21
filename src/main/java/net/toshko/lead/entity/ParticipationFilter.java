package net.toshko.lead.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class ParticipationFilter {

	List<Student> students;
	List<Teacher> teachers;
}
