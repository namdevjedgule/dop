package com.example.dop.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.dop.Model.RoleMaster;
import com.example.dop.Model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByEmail(String email);

	Optional<User> findByEmail(String email);

	Optional<User> findByEmailAndRoleRoleId(String email, Long roleId);

	List<User> findByRoleRoleId(Long roleId);

	List<User> findByRole(RoleMaster role);

	long countByRole(RoleMaster role);

	List<User> findByRoleRoleIdAndStatus(Long roleId, String status);

	List<User> findByRoleRoleIdAndFirstNameContainingIgnoreCase(Long roleId, String firstName);

}
