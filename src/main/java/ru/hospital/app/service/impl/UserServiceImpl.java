package ru.hospital.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hospital.app.dto.AppointmentDto;
import ru.hospital.app.dto.RecordDto;
import ru.hospital.app.dto.UserDto;
import ru.hospital.app.exception.BadRequestException;
import ru.hospital.app.exception.NotFoundException;
import ru.hospital.app.exception.UniqueException;
import ru.hospital.app.model.Appointment;
import ru.hospital.app.model.Login;
import ru.hospital.app.model.Record;
import ru.hospital.app.model.Speciality;
import ru.hospital.app.model.User;
import ru.hospital.app.repository.AppointmentRepository;
import ru.hospital.app.repository.LoginRepository;
import ru.hospital.app.repository.RecordRepository;
import ru.hospital.app.repository.UserRepository;
import ru.hospital.app.service.UserService;
import ru.hospital.app.util.mapper.impl.AppoitmentDtoMapper;
import ru.hospital.app.util.mapper.impl.RecordDtoMapper;
import ru.hospital.app.util.mapper.impl.UserDtoMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("dbUserService")
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final AppointmentRepository appointmentRepository;

    private final RecordRepository recordRepository;

    private final PasswordEncoder passwordEncoder;

    private final LoginRepository loginRepository;

    private final UserDtoMapper mapper;

    private final RecordDtoMapper recordDtoMapper;

    private final AppoitmentDtoMapper appoitmentDtoMapper;

    @Override
    public void addUser(UserDto userDto) {
        if (loginRepository.getLoginByUsername(userDto.getLogin()).isPresent()) {
            throw new UniqueException("Введенный логин занят придумайте другой");
        } else if (userRepository.findUserByNumberIs(userDto.getNumber()).isPresent()) {
            throw new UniqueException("Данный номер телефона уже используется");
        } else  if (userRepository.findUserByEmailIs(userDto.getEmail()).isPresent()) {
            throw new UniqueException("Данный эмейл уже используется");
        } else {
            Login login = new Login(null, userDto.getLogin());
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            User user = mapper.toEntity(userDto).orElseThrow(() -> new BadRequestException("Ошибка при маппинге"));
            user.setLogin(login);
            userRepository.save(user);
        }
    }

    @Override
    public UserDto findByLogin(String login) {
        return mapper.toDto(userRepository.findUserByLoginUsername(login).orElseThrow(() -> new BadRequestException("Пользователь не найден")))
                .orElseThrow(() -> new BadRequestException("Ошибка при маппинге"));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .map(userDto -> userDto.orElseThrow(() -> new BadRequestException("Ошибка при маппинге")))
                .collect(Collectors.toList());
    }

    @Override
    public void updateUser(UserDto userDto, String login) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user = userRepository.findUserByLoginUsername(login).orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        if (!user.getLogin().getUsername().equals(userDto.getLogin()) &&
                loginRepository.getLoginByUsername(userDto.getLogin()).isPresent()) {
            throw new UniqueException("Введенный логин занят придумайте другой");
        } else if (!user.getNumber().equals(userDto.getNumber()) &&
                userRepository.findUserByNumberIs(userDto.getNumber()).isPresent()) {
            throw new UniqueException("Данный номер телефона уже используется");
        } else  if (!user.getEmail().equals(userDto.getEmail()) &&
                userRepository.findUserByEmailIs(userDto.getEmail()).isPresent()) {
            throw new UniqueException("Данный эмейл уже используется");
        } else {
            if (!user.getLogin().getUsername().equals(userDto.getLogin())) {
                user.setLogin(new Login(null, userDto.getLogin()));
            }
            user.setName(userDto.getName());
            user.setPassword(userDto.getPassword());
            user.setNumber(userDto.getNumber());
            user.setEmail(userDto.getEmail());
            userRepository.save(user);
        }
    }

    @Override
    public void deleteUser(String login) {
        userRepository.deleteUserByLoginUsername(login);
    }


    @Override
    public List<RecordDto> checkFreeTime(String doctorName, Speciality doctorType, Integer period) {
        List<Record> recordList;
        if (doctorName != null) {
            recordList = recordRepository.findRecordByDoctorNameAndUserIsNullAndTimeOfAppointmentBefore(doctorName, LocalDateTime.now().plusDays(period));
        } else if (doctorType != null) {
            recordList = recordRepository.findRecordByDoctorSpecialityAndUserIsNullAndTimeOfAppointmentBefore(doctorType, LocalDateTime.now().plusDays(period));
        } else {
            recordList = recordRepository.findRecordByUserIsNullAndTimeOfAppointmentBefore(LocalDateTime.now().plusDays(period));
        }
        return recordList
                .stream()
                .map(recordDtoMapper::toDto)
                .map(doctorDto -> doctorDto.orElseThrow(() -> new BadRequestException("Ошибка при маппинге")))
                .collect(Collectors.toList());
    }

    @Override
    public void createRecord(String login, UUID recordId) {
        Record record = recordRepository.findById(recordId).orElseThrow(() -> new NotFoundException("Запись не найдена"));
        record.setUser(userRepository.findUserByLoginUsername(login).orElseThrow(() -> new NotFoundException("Пользователь не найден")));
        recordRepository.save(record);
    }

    @Override
    public List<AppointmentDto> getFutureAppointments(String login) {
        return appointmentRepository.findAppointmentByRecordUserLoginUsernameAndIsClosedFalse(login)
                .stream()
                .map(appoitmentDtoMapper::toDto)
                .map(doctorDto -> doctorDto.orElseThrow(() -> new BadRequestException("Ошибка при маппинге")))
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDto> getPastAppointments(String login, String doctorName, Speciality doctorType) {
        List<Appointment> appointmentList;
        if (doctorName != null) {
             appointmentList = appointmentRepository.findAppointmentByRecordUserLoginUsernameAndRecordDoctorNameAndIsClosedTrue(login, doctorName);
        } else if (doctorType != null){
             appointmentList = appointmentRepository.findAppointmentByRecordUserLoginUsernameAndRecordDoctorSpecialityAndIsClosedTrue(login,
                    doctorType);
        } else {
            appointmentList = appointmentRepository.findAppointmentByRecordUserLoginUsernameAndIsClosedTrue(login);
        }
        return appointmentList
                .stream()
                .map(appoitmentDtoMapper::toDto)
                .map(doctorDto -> doctorDto.orElseThrow(() -> new BadRequestException("Ошибка при маппинге")))
                .collect(Collectors.toList());
    }
}
