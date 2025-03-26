package com.example.dop.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.dop.Model.ServiceEntity;
import com.example.dop.Repository.ServiceRepository;

@Service
public class servicesService {

    @Autowired
    private ServiceRepository serviceRepository;

    
    public void saveService(ServiceEntity service) 
    {
    service.setCreatedOn(LocalDate.now());
        serviceRepository.save(service);
    }

    
    public List<ServiceEntity> getAllServices() {
        return serviceRepository.findAll();
    }

    
    public ServiceEntity getServiceById(Long id) {
        Optional<ServiceEntity> optionalService = serviceRepository.findById(id);
        return optionalService.orElse(null);
    }

    
    public List<ServiceEntity> searchService(String keyword , String category) {
        return serviceRepository.findBySnameContainingIgnoreCaseOrCategoryContainingIgnoreCase(keyword,category);
    }

    
    public List<ServiceEntity> searchServiceByStatus(String keyword, String status) {
        return serviceRepository.findBySnameContainingIgnoreCaseAndStatus(keyword, status);
    }

    
    public List<ServiceEntity> getServicesByStatus(String status) {
        return serviceRepository.findByStatus(status);
    }

    
    public void deleteServices(List<Long> selectedIds) {
        serviceRepository.deleteAllById(selectedIds);
    }

    
    public void deleteService(Long id) {
        serviceRepository.deleteById(id);
    }

    
    public ServiceEntity editService(Long id) {
        return serviceRepository.findById(id).orElse(null);
    }


	
}
