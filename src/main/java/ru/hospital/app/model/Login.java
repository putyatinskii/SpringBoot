package ru.hospital.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table(name = "logins")
@AllArgsConstructor
@NoArgsConstructor
public class Login {
    @Id
    @GeneratedValue
    private UUID id;

    private String username;

    @Override
    public String toString() {
        return username;
    }
}
