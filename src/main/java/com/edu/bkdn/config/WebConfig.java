package com.edu.bkdn.config;


import com.edu.bkdn.dtos.Conversation.GetConversationDto;
import com.edu.bkdn.dtos.Message.GetMessageDto;
import com.edu.bkdn.dtos.Participant.GetParticipantDto;
import com.edu.bkdn.models.Conversation;
import com.edu.bkdn.models.Message;
import com.edu.bkdn.utils.ObjectMapperUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@EnableWebMvc
@ComponentScan
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(
                "/images/**",
                "/css/**",
                "/js/**")
                .addResourceLocations(
                        "classpath:/static/images/",
                        "classpath:/static/css/",
                        "classpath:/static/js/");
    }
//
//    @Bean
//    public ModelMapper modelMapper() {
//        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//
//        PropertyMap<Message, GetMessageDto> messageMap = new PropertyMap<Message, GetMessageDto>() {
//            @Override
//            protected void configure() {
//                map().setSenderId(source.getUser().getId());
//            }
//        };
//        modelMapper.addMappings(messageMap);
//        return modelMapper;
//    }
}
