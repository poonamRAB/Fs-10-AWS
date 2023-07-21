package com.geekster.Doctor_APP.service;

import com.geekster.Doctor_APP.model.Doctor;
import com.geekster.Doctor_APP.repository.IDoctorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    @Autowired
    IDoctorRepo doctorRepo;

    public void addDoctor(Doctor doctor) {
        doctorRepo.save(doctor);
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepo.findAll();
    }
}
