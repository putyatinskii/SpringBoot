package ru.hospital.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hospital.app.model.Record;
import ru.hospital.app.model.Speciality;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface RecordRepository extends JpaRepository<Record, UUID> {

    List<Record> findRecordByDoctorIdAndUserId(UUID doctorId, UUID userId);

    List<Record> findRecordByDoctorLoginUsernameAndTimeOfAppointmentBetween(String login, LocalDateTime firstDate, LocalDateTime secondDate);

    List<Record> findRecordByUserIsNullAndTimeOfAppointmentBefore(LocalDateTime localDateTime);

    List<Record> findRecordByDoctorNameAndUserIsNullAndTimeOfAppointmentBefore(String name, LocalDateTime localDateTime);

    List<Record> findRecordByDoctorSpecialityAndUserIsNullAndTimeOfAppointmentBefore(Speciality speciality, LocalDateTime localDateTime);
}
