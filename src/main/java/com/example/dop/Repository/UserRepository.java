package com.example.dop.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.dop.Model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query(value = "CALL sp_user_login(:email)", nativeQuery = true)
	User loginUser(@Param("email") String email);

	boolean existsByEmail(String email);

	Optional<User> findByEmail(String email);

//	Optional<User> findByUserName(String userName);

}
