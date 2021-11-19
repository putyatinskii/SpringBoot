package ru.hospital.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.hospital.app.model.Doctor;
import ru.hospital.app.model.Role;
import ru.hospital.app.model.User;
import ru.hospital.app.repository.DoctorRepository;
import ru.hospital.app.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    private final DoctorRepository doctorRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findUserByLoginUsername(login).orElse(null);
        if (user != null) {
            return new org.springframework.security.core.userdetails.User(
                    user.getLogin().getUsername(),
                    user.getPassword(),
                    List.of(Role.USER)
            );
        }
        else {
            Doctor doctor = doctorRepository.findDoctorByLoginUsername(login).orElseThrow(() -> new UsernameNotFoundException(login));
            return new org.springframework.security.core.userdetails.User(
                    doctor.getLogin().getUsername(),
                    doctor.getPassword(),
                    List.of(Role.DOCTOR)
            );
        }
    }
}
