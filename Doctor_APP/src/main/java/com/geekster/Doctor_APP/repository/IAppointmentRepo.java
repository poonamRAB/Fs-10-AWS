package com.geekster.Doctor_APP.repository;

import com.geekster.Doctor_APP.model.Appointment;
import com.geekster.Doctor_APP.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAppointmentRepo extends JpaRepository<Appointment,Long> {

    Appointment findFirstByPatient(Patient patient);
}
