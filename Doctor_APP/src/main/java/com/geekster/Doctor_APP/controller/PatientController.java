package com.geekster.Doctor_APP.controller;

import com.geekster.Doctor_APP.model.Appointment;
import com.geekster.Doctor_APP.model.Patient;
import com.geekster.Doctor_APP.model.dto.SignInInput;
import com.geekster.Doctor_APP.model.dto.SignUpOutput;
import com.geekster.Doctor_APP.service.AuthenticationService;
import com.geekster.Doctor_APP.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PatientController {
    @Autowired
    PatientService patientService;

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("patient/signup")
    public SignUpOutput signUpPatient(@RequestBody Patient patient){
        return patientService.SignUpPatient(patient);
    }

    @PostMapping("patient/signIn")
    public String signInPatient(@RequestBody @Valid SignInInput signInInput){
        return patientService.signInPatient(signInInput);
    }

    @DeleteMapping("patient/signOut")
    public String signOutPatient(String email, String token){
        if(authenticationService.authenticate(email, token)){
            return patientService.signOutPatient(email);
        }
        else{
            return "sign out not allowed for non authenticated users!!!";
        }
    }


    @GetMapping("patients")
    List<Patient> getAllPatients(){
        return patientService.getAllPatients();
    }

    @PostMapping("appointment/schedule")
    public String scheduleAppointment(@RequestBody Appointment appointment, String email, String token){

        if(authenticationService.authenticate(email,token)){
            boolean status = patientService.scheduleAppointment(appointment);
            return status ? "appointment schedule" : "error occurred during scheduling appointment";
        }
        else{
            return "scheduling failed because of invalid authetication.";
        }
    }

    @DeleteMapping("appointment/cancel")
    public String cancelAppointment(String email, String token){

        if(authenticationService.authenticate(email, token)){
            patientService.cancelAppointment(email);
            return "Appointment canceled successfully!!";
        }
        else{
            return "scheduling failed due to invalid authentication!!";
        }
    }


}
