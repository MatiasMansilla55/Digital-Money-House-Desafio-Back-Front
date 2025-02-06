package com.example.demo.dto.entry;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
public class UserEntryDto {
        //@NotNull(message = "El username del usuario no puede ser nulo")
        //@NotBlank(message = "Debe especificarse el username del usuario")
        //private String userName;
        @NotNull(message = "El nombre del usuario no puede ser nulo")
        @NotBlank(message = "Debe especificarse el nombre del usuario")
        private String firstName;
        @NotNull(message = "El apellido del usuario no puede ser nulo")
        @NotBlank(message = "Debe especificarse el apellido del usuario")
        private String lastName;
        @NotNull(message = "El DNI del usuario no puede ser nulo")
        @NotBlank(message = "Debe especificarse el DNI del usuario")
        private String dni;
        @NotNull(message = "El telefono del usuario no puede ser nulo")
        @NotBlank(message = "Debe especificarse el telefono del usuario")
        private String phone;
        @NotNull(message = "El email del usuario no puede ser nulo")
        @NotBlank(message = "Debe especificarse el email del usuario")
        private String email;
        @NotNull(message = "La contraseña del usuario no puede ser nulo")
        @NotBlank(message = "Debe especificarse la contraseña del usuario")
        private String password;
        //private Set<String> roles;

        public String getFirstName() {
                return firstName;
        }

        public void setFirstName(String firstName) {
                this.firstName = firstName;
        }

        public String getLastName() {
                return lastName;
        }

        public void setLastName(String lastName) {
                this.lastName = lastName;
        }

        public String getDni() {
                return dni;
        }

        public void setDni(String dni) {
                this.dni = dni;
        }

        public String getPhone() {
                return phone;
        }

        public void setPhone(String phone) {
                this.phone = phone;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }


}
