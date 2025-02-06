package com.example.demo.config.ModelMapperConfig;

import com.example.demo.dto.entry.UserEntryDto;
import com.example.demo.dto.exit.UserRegisterOutDto;
import com.example.demo.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Mapeo de UserEntryDto a User
        modelMapper.typeMap(UserEntryDto.class, User.class).addMappings(mapper -> {
            mapper.map(UserEntryDto::getFirstName, User::setFirstName);
            mapper.map(UserEntryDto::getLastName, User::setLastName);
            mapper.map(UserEntryDto::getDni, User::setDni);
            mapper.map(UserEntryDto::getPhone, User::setPhone);
            mapper.map(UserEntryDto::getEmail, User::setEmail);
            mapper.map(UserEntryDto::getPassword, User::setPassword);
        });

        // Mapeo de User a UserRegisterOutDto
        modelMapper.typeMap(User.class, UserRegisterOutDto.class).addMappings(mapper -> {
            mapper.map(User::getId, UserRegisterOutDto::setId);
            mapper.map(User::getFirstName, UserRegisterOutDto::setFirstName);
            mapper.map(User::getLastName, UserRegisterOutDto::setLastName);
            mapper.map(User::getDni, UserRegisterOutDto::setDni);
            mapper.map(User::getPhone, UserRegisterOutDto::setPhone);
            mapper.map(User::getEmail, UserRegisterOutDto::setEmail);
            mapper.map(User::getCvu, UserRegisterOutDto::setCvu);
            mapper.map(User::getAlias, UserRegisterOutDto::setAlias);
        });

        return modelMapper;
    }
}
