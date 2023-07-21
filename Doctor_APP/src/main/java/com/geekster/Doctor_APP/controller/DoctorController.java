package com.geekster.Doctor_APP.controller;

import com.geekster.Doctor_APP.model.Doctor;
import com.geekster.Doctor_APP.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DoctorController {
    @Autowired
    DoctorService doctorService;

    @PostMapping("doctor")
    void addDoctor(@RequestBody Doctor doctor){
        doctorService.addDoctor(doctor);
    }

    @GetMapping("doctors")
    List<Doctor> getAllDoctors(){
        return doctorService.getAllDoctors();
    }

}
