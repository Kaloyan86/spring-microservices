package com.app.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Configuration
public class ApplicationBeanConfiguration {

    private static final Converter<String, LocalDate> LOCAL_DATE_CONVERTER = new Converter<>() {
        @Override
        public LocalDate convert(MappingContext<String, LocalDate> mappingContext) {
            return LocalDate.parse(mappingContext.getSource(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
    };

    private static final Converter<String, LocalDateTime> LOCAL_DATE_TIME_CONVERTER = new Converter<>() {
        @Override
        public LocalDateTime convert(MappingContext<String, LocalDateTime> mappingContext) {
            return LocalDateTime.parse(mappingContext.getSource(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
    };

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addConverter(LOCAL_DATE_CONVERTER);
        modelMapper.addConverter(LOCAL_DATE_TIME_CONVERTER);

        return modelMapper;
    }

}
