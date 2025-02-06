package com.example.api_.transfers.AppConfig;

import org.modelmapper.AbstractConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeToStringConverter extends AbstractConverter<LocalDateTime, String> {
    @Override
    protected String convert(LocalDateTime source) {
        return source != null ? source.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
    }
}
