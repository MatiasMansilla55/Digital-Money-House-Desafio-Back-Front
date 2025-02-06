package com.example.api_activity.AppConfig;

import com.example.api_activity.dto.entry.Account;
import com.example.api_activity.dto.exit.AccountOutDTO;
import com.example.api_activity.dto.exit.ActivityOutDTO;
import com.example.api_activity.entities.Activity;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();


        // Configuraciones específicas
        modelMapper.addConverter(new LocalDateTimeToStringConverter());
        modelMapper.typeMap(AccountOutDTO.class, Account.class);

        modelMapper.typeMap(ActivityOutDTO.class, Activity.class);
        modelMapper.typeMap(Account.class, AccountOutDTO.class);
        modelMapper.typeMap(Activity.class, ActivityOutDTO.class)
                .addMappings(mapper -> {
                    mapper.map(Activity::getId, ActivityOutDTO::setId);
                    mapper.map(Activity::getDate, ActivityOutDTO::setDate);
                    mapper.map(Activity::getType, ActivityOutDTO::setType);
                    mapper.map(Activity::getAccountId, ActivityOutDTO::setAccountId);
                    mapper.map(Activity::getAmount, ActivityOutDTO::setAmount);
                    mapper.map(Activity::getDescription, ActivityOutDTO::setCvu);
                });

        return modelMapper;
    }
}
