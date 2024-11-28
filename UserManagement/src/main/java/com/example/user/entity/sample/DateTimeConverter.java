package com.example.user.entity.sample;


import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

@Converter
public class DateTimeConverter implements AttributeConverter<ZonedDateTime, OffsetDateTime> {

    @Override
    public ZonedDateTime convertToEntityAttribute(OffsetDateTime arg0) {
        return arg0 == null ? null : arg0.toZonedDateTime();
    }

    @Override
    public OffsetDateTime convertToDatabaseColumn(ZonedDateTime arg0) {
        return arg0 == null ? null : arg0.toOffsetDateTime();
    }

}

