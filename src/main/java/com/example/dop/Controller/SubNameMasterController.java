//package com.example.dop.Controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.example.dop.Model.SubNameMaster;
//import com.example.dop.Repository.SubNameMasterRepository;
//
//@RestController
//@RequestMapping("/names")
//@CrossOrigin(origins = "*")
//public class SubNameMasterController {
//
//	@Autowired
//	private SubNameMasterRepository subNameMasterRepository;
//
//	@GetMapping("/all")
//	public List<SubNameMaster> getAllNames() {
//		return subNameMasterRepository.findAll();
//	}
//}
