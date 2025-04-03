package com.example.dop.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dop.Model.RoleMaster;

public interface RoleMasterRepository extends JpaRepository<RoleMaster, Long> {

	Optional<RoleMaster> findByRoleName(String roleName);
}
