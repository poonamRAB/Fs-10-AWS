package com.geekster.Doctor_APP.service;

import com.geekster.Doctor_APP.model.Appointment;
import com.geekster.Doctor_APP.model.AuthenticationToken;
import com.geekster.Doctor_APP.model.Patient;
import com.geekster.Doctor_APP.model.dto.SignInInput;
import com.geekster.Doctor_APP.model.dto.SignUpOutput;
import com.geekster.Doctor_APP.repository.IAppointmentRepo;
import com.geekster.Doctor_APP.repository.IAuthTokenRepo;
import com.geekster.Doctor_APP.repository.IDoctorRepo;
import com.geekster.Doctor_APP.repository.IPatientRepo;
import com.geekster.Doctor_APP.service.utility.emailUtility.EmailHandler;
import com.geekster.Doctor_APP.service.utility.hashingUtility.PasswordEncrypter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    @Autowired
    IPatientRepo patientRepo;

    @Autowired
    IDoctorRepo doctorRepo;

    @Autowired
    IAuthTokenRepo authTokenRepo;

    @Autowired
    IAppointmentRepo appointmentRepo;

    @Autowired
    AppointmentService appointmentService;


    public SignUpOutput SignUpPatient(Patient patient) {
        boolean signUpStatus = true;
        String signUpMsg = null;

        String newEmail = patient.getPatientEmail();

        if( newEmail == null){
            signUpMsg = "Invalid email";
            signUpStatus = false;
            return new SignUpOutput(signUpStatus,signUpMsg);
        }

        //checking if patient mail-Id already exists ??
        Patient existingPatient = patientRepo.findFirstByPatientEmail(newEmail);

        if(existingPatient != null){
            signUpMsg = "This email is already registered!!";
            signUpStatus = false;
            return new SignUpOutput(signUpStatus,signUpMsg);
        }

        // hashing the password : encrypt the password
        try{
            String encryptedPassword = PasswordEncrypter.encryptPassword(patient.getPatientPassword());

            //save appointment the patient with new encrypted password

            patient.setPatientPassword(encryptedPassword);
            patientRepo.save(patient);

            return new SignUpOutput(signUpStatus,"Patient registered successfully!!");
        }
        catch(Exception e){
            signUpMsg = "Internal error occurred during signUp !!!";
            signUpStatus = false;
            return new SignUpOutput(signUpStatus,signUpMsg);
        }
    }



    public String signOutPatient(String email) {
        Patient patient = patientRepo.findFirstByPatientEmail(email);

        authTokenRepo.delete(authTokenRepo.findFirstByPatient(patient));

        return "Patient Signed Out Successfully!!!";
    }

    public List<Patient> getAllPatients() {
        return patientRepo.findAll();
    }

    public String signInPatient(SignInInput signInInput) {
        String signInMsg = null;
        String signInEmail = signInInput.getEmail();

        if(signInEmail == null ){
            signInMsg = "Invalid Email!!";
            return signInMsg;
        }

        //checking for mail already exists or not
        Patient existingPatient = patientRepo.findFirstByPatientEmail(signInEmail);

        if(existingPatient == null){
            signInMsg = "This Email is not registered!!";
            return signInMsg;
        }


        try{
            String encryptedPassword = PasswordEncrypter.encryptPassword(signInInput.getPassword());

            if(existingPatient.getPatientPassword().equals(encryptedPassword)){
                AuthenticationToken authenticationToken = new AuthenticationToken(existingPatient);
                authTokenRepo.save(authenticationToken);

                EmailHandler.sendEmail(signInEmail,"email testing",authenticationToken.getTokenValue());
                return "Token sent to your email!!";
            }
            else{
                signInMsg = "Invalid credentials!!";
                return signInMsg;
            }
        }
        catch(Exception e){
            signInMsg = "Internal error occurred during sign In!!";
            return signInMsg;
        }
    }

    public boolean scheduleAppointment(Appointment appointment) {
        Long doctorId = appointment.getDoctor().getDoctorId();
        boolean isDoctorValid = doctorRepo.existsById(doctorId);

        Long patientId = appointment.getPatient().getPatientId();
        boolean isPatientValid = patientRepo.existsById(patientId);

        if(isDoctorValid && isPatientValid){
            appointmentService.saveAppointmentForPatient(appointment);

            return true;
        }
        else{
            return false;
        }
    }

    public void cancelAppointment(String email) {
        Patient patient = patientRepo.findFirstByPatientEmail(email);

        Appointment appointment = appointmentService.getAppointmentForPatient(patient);

        appointmentService.cancelAppointment(appointment);
    }
}
