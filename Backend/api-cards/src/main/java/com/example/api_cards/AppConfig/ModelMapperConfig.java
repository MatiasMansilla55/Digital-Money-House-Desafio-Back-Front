package com.example.api_cards.AppConfig;

import com.example.api_cards.dto.entry.CreateCardEntryDTO;
import com.example.api_cards.dto.exit.CardOutDTO;
import com.example.api_cards.entities.Card;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        // Configuración de mappings
        modelMapper.typeMap(CreateCardEntryDTO.class, Card.class)
                .addMappings(mapper -> {
                    mapper.map(CreateCardEntryDTO::getNumber, Card::setNumber);
                    mapper.map(CreateCardEntryDTO::getCvc, Card::setCvc);
                    mapper.map(CreateCardEntryDTO::getExpiry, Card::setExpiry);
                    mapper.map(CreateCardEntryDTO::getName, Card::setName);
                    mapper.map(CreateCardEntryDTO::getAccountId, Card::setAccountId);
                });

        modelMapper.createTypeMap(Card.class, CardOutDTO.class)
                .addMappings(mapper -> {
                    mapper.map(Card::getNumber, CardOutDTO::setNumber);
                    mapper.map(Card::getName, CardOutDTO::setName);
                    mapper.map(Card::getExpiry, CardOutDTO::setExpiry);
                    mapper.map(Card::getCvc, CardOutDTO::setCvc);
                    mapper.map(Card::getAccountId, CardOutDTO::setAccountId);
                    mapper.map(Card::getId, CardOutDTO::setId);
                });
        // Configuraciones específicas
        modelMapper.addConverter(new LocalDateTimeToStringConverter());




        return modelMapper;
    }
}
