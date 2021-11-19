package ru.hospital.app.controller.impl;

import lombok.RequiredArgsConstructor;
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
import ru.hospital.app.controller.UserController;
import ru.hospital.app.dto.AppointmentDto;
import ru.hospital.app.dto.RecordDto;
import ru.hospital.app.dto.UserDto;
import ru.hospital.app.model.Speciality;
import ru.hospital.app.service.UserService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    @PostMapping(value = "/user-sign-up")
    public void signUpForUser(@Valid @RequestBody UserDto userDto) {
        userService.addUser(userDto);
    }

    @Override
    @GetMapping("/user/home")
    public UserDto getUserInfo() {
        return userService.findByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    @GetMapping("/user/home/all")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @Override
    @PutMapping("/user/home")
    public void updateUser(@Valid @RequestBody UserDto userDto) {
        userService.updateUser(userDto, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    @DeleteMapping("/user/home")
    public void deleteUser() {
        userService.deleteUser(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    @GetMapping("/user/home/records")
    public List<RecordDto> checkFreeTime(@RequestParam(required = false) String doctorName,
                                         @RequestParam(required = false) Speciality doctorType,
                                         @RequestParam Integer period) {
        return userService.checkFreeTime(doctorName, doctorType, period);
    }

    @Override
    @PostMapping("/user/home/records/{id}")
    public void createRecord(@PathVariable UUID id) {
        userService.createRecord(SecurityContextHolder.getContext().getAuthentication().getName(), id);
    }

    @Override
    @GetMapping("/user/home/future-appointments")
    public List<AppointmentDto> getFutureAppointments() {
        return userService.getFutureAppointments(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    @GetMapping("/user/home/past-appointments")
    public List<AppointmentDto> getPastAppointments(@RequestParam(required = false) String doctorName,
                                                 @RequestParam(required = false) String doctorType) {
        return userService.getPastAppointments(SecurityContextHolder.getContext().getAuthentication().getName(),
                doctorName,
                Speciality.valueOf(doctorType));
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
