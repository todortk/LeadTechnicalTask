package net.toshko.lead.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.toshko.lead.entity.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, String>{

}
