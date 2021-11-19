package ru.hospital.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hospital.app.model.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findUserByLoginUsername(String username);

    Optional<User> findUserByNumberIs(String number);

    Optional<User> findUserByEmailIs(String email);

    void deleteUserByLoginUsername(String username);

}
