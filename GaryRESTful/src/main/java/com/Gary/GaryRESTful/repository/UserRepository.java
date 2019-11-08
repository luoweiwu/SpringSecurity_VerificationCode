package com.Gary.GaryRESTful.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.Gary.GaryRESTful.domain.User;

public interface UserRepository extends CrudRepository<User,Long>{

	@Query(value = "select * from user where username = ?1",nativeQuery = true)
	User findUserByUsername(String username);
	
}
