package ru.hospital.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.hospital.app.dto.AppointmentDto;
import ru.hospital.app.dto.DoctorDto;
import ru.hospital.app.dto.RecordDto;
import ru.hospital.app.exception.BadRequestException;
import ru.hospital.app.exception.NotFoundException;
import ru.hospital.app.exception.UniqueException;
import ru.hospital.app.model.Appointment;
import ru.hospital.app.model.Doctor;
import ru.hospital.app.model.Login;
import ru.hospital.app.model.Record;
import ru.hospital.app.repository.AppointmentRepository;
import ru.hospital.app.repository.DoctorRepository;
import ru.hospital.app.repository.LoginRepository;
import ru.hospital.app.repository.RecordRepository;
import ru.hospital.app.service.DoctorService;
import ru.hospital.app.util.mapper.impl.AppoitmentDtoMapper;
import ru.hospital.app.util.mapper.impl.DoctorDtoMapper;
import ru.hospital.app.util.mapper.impl.RecordDtoMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("dbDoctorService")
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    private final RecordRepository recordRepository;

    private final AppointmentRepository appointmentRepository;

    private final LoginRepository loginRepository;

    private final PasswordEncoder passwordEncoder;

    private final DoctorDtoMapper mapper;

    private final RecordDtoMapper recordDtoMapper;

    private final AppoitmentDtoMapper appoitmentDtoMapper;

    @Override
    public void addDoctor(DoctorDto doctorDto) {
        if (loginRepository.getLoginByUsername(doctorDto.getLogin()).isPresent()) {
            throw new UniqueException("Введенный логин занят придумайте другой");
        } else if (doctorRepository.findDoctorByNumberIs(doctorDto.getNumber()).isPresent()) {
            throw new UniqueException("Данный номер телефона уже используется");
        } else if (doctorRepository.findDoctorByEmailIs(doctorDto.getEmail()).isPresent()) {
            throw new UniqueException("Данный эмейл уже используется");
        } else {
            Login login = new Login(null, doctorDto.getLogin());
            doctorDto.setPassword(passwordEncoder.encode(doctorDto.getPassword()));
            Doctor doctor = mapper.toEntity(doctorDto).orElseThrow(() -> new BadRequestException("Ошибка пр маппине"));
            doctor.setLogin(login);
            doctorRepository.save(doctor);
        }
    }

    @Override
    public void updateDoctor(DoctorDto doctorDto, String login) {
        doctorDto.setPassword(passwordEncoder.encode(doctorDto.getPassword()));
        Doctor doctor = doctorRepository.findDoctorByLoginUsername(login).orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        if (!doctor.getLogin().getUsername().equals(doctorDto.getLogin()) &&
                loginRepository.getLoginByUsername(doctorDto.getLogin()).isPresent()) {
            throw new UniqueException("Введенный логин занят придумайте другой");
        } else if (!doctor.getNumber().equals(doctorDto.getNumber()) &&
                doctorRepository.findDoctorByNumberIs(doctorDto.getNumber()).isPresent()) {
            throw new UniqueException("Данный номер телефона уже используется");
        } else  if (!doctor.getEmail().equals(doctorDto.getEmail()) &&
                doctorRepository.findDoctorByEmailIs(doctorDto.getEmail()).isPresent()) {
            throw new UniqueException("Данный эмейл уже используется");
        } else {
            if (!doctor.getLogin().getUsername().equals(doctorDto.getLogin())) {
                doctor.setLogin(new Login(null, doctorDto.getLogin()));
            }
            doctor.setName(doctorDto.getName());
            doctor.setPassword(doctorDto.getPassword());
            doctor.setNumber(doctorDto.getNumber());
            doctor.setEmail(doctorDto.getEmail());
            doctor.setSpeciality(doctorDto.getSpeciality());
            doctor.setExperience(doctorDto.getExperience());
            doctorRepository.save(doctor);
        }
    }

    @Override
    public void deleteDoctor(String login) {
        Doctor doctor = doctorRepository.findDoctorByLoginUsername(login).orElseThrow();
        doctorRepository.deleteById(doctor.getId());
    }

    @Override
    public List<DoctorDto> getAllDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .map(doctorDto -> doctorDto.orElseThrow(() -> new BadRequestException("Ошибка при маппинге")))
                .collect(Collectors.toList());
    }

    @Override
    public DoctorDto findByLogin(String login) {
        return mapper.toDto(doctorRepository.findDoctorByLoginUsername(login).orElseThrow(() -> new NotFoundException("Пользователь не найден")))
                .orElseThrow(() -> new BadRequestException("Ошибка при маппинге"));
    }


    @Override
    public void createRecords(String login, LocalDateTime dateTime) {
        Doctor doctor = doctorRepository.findDoctorByLoginUsername(login).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Record record = recordRepository.save(new Record(null, null, doctor, dateTime, null));
        appointmentRepository.save(new Appointment(null, record, "", false, false, false));
    }

    @Override
    public void updateAppointment(UUID id, AppointmentDto appointmentDto) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new NotFoundException("Запись не найдена"));
        appointment.setDescription(appointmentDto.getDescription());
        appointment.setIsSick(appointmentDto.getIsSick());
        appointment.setIsClosed(appointmentDto.getIsClosed());
        appointment.setIsVisited(appointment.getIsVisited());
        appointmentRepository.save(appointment);
    }

    @Override
    public List<RecordDto> getTodayRecords(String login) {
        return recordRepository.findRecordByDoctorLoginUsernameAndTimeOfAppointmentBetween(login, LocalDateTime.now(), LocalDateTime.now().plusDays(1))
                .stream().
                map(recordDtoMapper::toDto)
                .map(doctorDto -> doctorDto.orElseThrow(() -> new BadRequestException("Ошибка при маппинге")))
                .collect(Collectors.toList());
    }

    @Override
    public List<RecordDto> getUserRecords(String login, UUID userId) {
        Doctor doctor = doctorRepository.findDoctorByLoginUsername(login).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        return recordRepository.findRecordByDoctorIdAndUserId(doctor.getId(), userId)
                .stream().map(recordDtoMapper::toDto)
                .map(doctorDto -> doctorDto.orElseThrow(() -> new BadRequestException("Ошибка при маппинге")))
                .collect(Collectors.toList());
    }
}
