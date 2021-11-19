package ru.hospital.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hospital.app.model.Doctor;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

    Optional<Doctor> findDoctorByLoginUsername(String username);

    Optional<Doctor> findDoctorByNumberIs(String number);

    Optional<Doctor> findDoctorByEmailIs(String email);

    void deleteDoctorByLoginUsername(String username);
}
