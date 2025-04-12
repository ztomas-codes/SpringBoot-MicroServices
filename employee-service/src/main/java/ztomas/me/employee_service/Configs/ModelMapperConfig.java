package ztomas.me.employee_service.Configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig  {

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    } 
}
