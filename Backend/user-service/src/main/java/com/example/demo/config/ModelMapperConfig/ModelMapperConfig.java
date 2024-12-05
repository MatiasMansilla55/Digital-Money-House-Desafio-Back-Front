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
        modelMapper.typeMap(UserEntryDto.class, User.class);
        modelMapper.typeMap(User.class, UserRegisterOutDto.class);
        return modelMapper;
    }
}
