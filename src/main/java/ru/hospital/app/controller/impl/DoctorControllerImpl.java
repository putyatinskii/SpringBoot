package ru.hospital.app.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.hospital.app.controller.DoctorController;
import ru.hospital.app.dto.AppointmentDto;
import ru.hospital.app.dto.DoctorDto;
import ru.hospital.app.dto.RecordDto;
import ru.hospital.app.exception.BadRequestException;
import ru.hospital.app.service.DoctorService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class DoctorControllerImpl implements DoctorController {

    private final DoctorService doctorService;

    @Override
    @PostMapping(value = "/doctor-sign-up")
    public void signUpForDoctor(@Valid @RequestBody DoctorDto doctorDto) {
        doctorService.addDoctor(doctorDto);
    }

    @Override
    @GetMapping("/doctor/home")
    public DoctorDto getDoctorInfo() {
        return doctorService.findByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    @GetMapping("/doctor/home/all")
    public List<DoctorDto> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @Override
    @PutMapping("/doctor/home")
    public void updateDoctor(@Valid @RequestBody DoctorDto doctorDto) {
        doctorService.updateDoctor(doctorDto, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    @DeleteMapping("doctor/home")
    public void deleteDoctor() {
        doctorService.deleteDoctor(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    @GetMapping("doctor/home/today-records")
    public List<RecordDto> getTodayRecords() {
        return doctorService.getTodayRecords(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    @GetMapping("doctor/home/appointments-of-user/{id}")
    public List<RecordDto> getUserRecords(@PathVariable("id") UUID userId) {
        return doctorService.getUserRecords(SecurityContextHolder.getContext().getAuthentication().getName(), userId);
    }

    @Override
    @PutMapping("doctor/home/update-appointment/{id}")
    public void updateAppointment(@PathVariable UUID id, @RequestBody AppointmentDto appointmentDto) {
        doctorService.updateAppointment(id, appointmentDto);
    }

    @Override
    @PostMapping("doctor/home/create-record")
    public void createRecords(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam("date-time") LocalDateTime dateTime) {
        if (LocalDateTime.now().isAfter(dateTime)) {
            throw new BadRequestException("Дата и время не могут предшествовать сегодняшней");
        } else {
            doctorService.createRecords(SecurityContextHolder.getContext().getAuthentication().getName(), dateTime);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
