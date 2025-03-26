package com.example.dop.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.dop.Model.ServiceEntity;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
    
    List<ServiceEntity> findBySnameContainingIgnoreCase(String keyword);
    
    List<ServiceEntity> findBySnameContainingIgnoreCaseAndStatus(String keyword, String status);
    
    List<ServiceEntity> findByStatus(String status);

	List<ServiceEntity> findBySnameContainingIgnoreCaseOrCategoryContainingIgnoreCase(String keyword, String category);
}
