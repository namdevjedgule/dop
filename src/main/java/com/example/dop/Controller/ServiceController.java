package com.example.dop.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.dop.Model.Admin;
import com.example.dop.Model.ServiceEntity;
import com.example.dop.Service.servicesService;

@Controller
@RequestMapping("/service")
public class ServiceController {

    @Autowired
    private servicesService serviceService;

    @GetMapping("/add")
    public String addServicePage() {
        return "ServiceAdd";
    }

    @GetMapping("/list")
    public String listServices(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "statusFilter", required = false) String statusFilter,
            Model model) {

        List<ServiceEntity> services;

        if (keyword != null && !keyword.trim().isEmpty() && statusFilter != null && !statusFilter.isEmpty()) {
            services = serviceService.searchServiceByStatus(keyword.trim(), statusFilter);
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            services = serviceService.searchService(keyword.trim(), keyword.trim());
        } else if (statusFilter != null && !statusFilter.isEmpty()) {
            services = serviceService.getServicesByStatus(statusFilter);
        } else {
            services = serviceService.getAllServices();
        }

        model.addAttribute("services", services);
        model.addAttribute("keyword", keyword);
        model.addAttribute("statusFilter", statusFilter);
        return "ServiceList";
    }

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/services";

    @PostMapping("/saveService")
    public String saveService(@ModelAttribute ServiceEntity service,
                              @RequestParam("profileImage") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                String uploadDir = System.getProperty("user.dir") + "/uploads/services/";
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(uploadDir, fileName);

                Files.write(filePath, file.getBytes());
                service.setProfilePhoto("/uploads/services/" + fileName); // Store relative path
            } catch (IOException e) {
                e.printStackTrace();
                return "redirect:/service/add?error=file_upload_failed";
            }
        }

        service.setStatus("Active");
        serviceService.saveService(service);
        return "redirect:/service/add?success";
    }

    @PostMapping("/deleteSelectedService")
    public String deleteSelected(@RequestParam(value = "selectedIds", required = false) List<Long> selectedIds,
                                 RedirectAttributes redirectAttributes) {
        if (selectedIds != null && !selectedIds.isEmpty()) {
            serviceService.deleteServices(selectedIds);
            redirectAttributes.addFlashAttribute("message", "Selected services deleted successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "No services selected for deletion.");
        }
        return "redirect:/service/list";
    }

    @GetMapping("/editService/{id}")
    public String editService(@PathVariable("id") Long id, Model model) {
    	ServiceEntity service = serviceService.editService(id);
        if (service == null) return "redirect:/service/list?error=ServiceNotFound";
        model.addAttribute("service", service);
        return "EditService";
    }

    @GetMapping("/delete/{id}")
    public String deleteService(@PathVariable("id") Long id) {
        serviceService.deleteService(id);
        return "redirect:/service/list";
    }
    @PostMapping("/UpdateService")
    public String updateService(@ModelAttribute("ap") ServiceEntity ap) {
        if (ap == null) {
            System.out.println("🚨 ap is NULL! Form data is not being received.");
            return "redirect:/service/list?error=FormNotSubmitted";
        }

        System.out.println("✅ Received data: " + ap);
        
        if (ap.getSid() == null) {
            return "redirect:/service/list?error=InvalidServiceId";
        }

        ServiceEntity existingService = serviceService.getServiceById(ap.getSid());

        if (existingService != null) { 
            System.out.println("🔄 Updating Service ID: " + ap.getSid());
            System.out.println("Old Name: " + existingService.getSname() + " -> New Name: " + ap.getSname());

            existingService.setSname(ap.getSname());
            existingService.setCategory(ap.getCategory());
            existingService.setAltTitle(ap.getAltTitle());
            existingService.setIcon(ap.getIcon());
            existingService.setDescription(ap.getDescription());

            existingService.setStatus(existingService.getStatus());

            serviceService.saveService(existingService);
        } else {
            System.out.println("🚨 Service ID not found: " + ap.getSid());
            return "redirect:/service/list?error=ServiceNotFound";
        }

        return "redirect:/service/list?success=ServiceUpdated";
    }


}
