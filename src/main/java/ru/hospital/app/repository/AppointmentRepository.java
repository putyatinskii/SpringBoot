package ru.hospital.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hospital.app.model.Appointment;
import ru.hospital.app.model.Speciality;

import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {


    List<Appointment> findAppointmentByRecordUserLoginUsernameAndIsClosedFalse(String login);

    List<Appointment> findAppointmentByRecordUserLoginUsernameAndIsClosedTrue(String login);

    List<Appointment> findAppointmentByRecordUserLoginUsernameAndRecordDoctorNameAndIsClosedTrue(String login, String doctorName);

    List<Appointment> findAppointmentByRecordUserLoginUsernameAndRecordDoctorSpecialityAndIsClosedTrue(String login, Speciality doctorSpeciality);
}
