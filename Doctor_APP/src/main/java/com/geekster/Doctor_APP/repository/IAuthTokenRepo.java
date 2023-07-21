package com.geekster.Doctor_APP.repository;

import com.geekster.Doctor_APP.model.AuthenticationToken;
import com.geekster.Doctor_APP.model.Patient;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuthTokenRepo extends JpaRepository<AuthenticationToken,Long> {

   AuthenticationToken findFirstByTokenValue(String authTokenValue);

    AuthenticationToken findFirstByPatient(Patient patient);


}
