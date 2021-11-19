package ru.hospital.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hospital.app.model.Login;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LoginRepository extends JpaRepository<Login, UUID> {

    Optional<Login> getLoginByUsername(String login);
}
