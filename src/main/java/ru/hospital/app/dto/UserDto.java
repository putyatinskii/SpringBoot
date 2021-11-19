package ru.hospital.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @NotEmpty(message = "Поле обязательно для заполнения")
    @Size(min = 4, max = 30, message = "длина логина от 4 до 30 символов")
    private String login;

    @NotEmpty(message = "Поле обязательно для заполнения")
    @Size(min = 4, max = 60, message = "длина пароля от 4 до 30 символов")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotEmpty(message = "Поле обязательно для заполнения")
    @Size(min = 4, max = 50, message = "длина ФИО от 4 до 50 символов")
    private String name;

    @Pattern(regexp="([0-9]{11})", message = "Номер телефона представляет собой 11 цифр, начиная с восьмерки")
    @Size(min = 11, max = 11, message = "Введите номер телефона, начиная с восьмерки")
    private String number;

    @Email
    @NotEmpty(message = "Поле обязательно для заполнения")
    @Size(min = 5, max = 30)
    private String email;
}
