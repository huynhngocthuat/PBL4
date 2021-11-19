package com.edu.bkdn.config;


import com.edu.bkdn.dtos.Message.GetLastMessageDto;
import com.edu.bkdn.dtos.Message.GetMessageDto;
import com.edu.bkdn.dtos.User.GetUserDto;
import com.edu.bkdn.dtos.UserContact.CreateUserContactDto;
import com.edu.bkdn.models.Message;
import com.edu.bkdn.models.User;
import com.edu.bkdn.models.UserContact;
import com.edu.bkdn.utils.HelperUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        PropertyMap<UserContact, CreateUserContactDto> userContactMap = new PropertyMap<UserContact, CreateUserContactDto>() {
            @Override
            protected void configure() {
                map().setUserId(source.getUser().getId());
                map().setContactId(source.getContact().getId());
            }
        };

        PropertyMap<Message, GetMessageDto> messageDtoPropertyMap = new PropertyMap<Message, GetMessageDto>() {
            @Override
            protected void configure() {
                map().setUserId(source.getUser().getId());
            }
        };

        PropertyMap<Message, GetLastMessageDto> lastMessageDtoPropertyMap = new PropertyMap<Message, GetLastMessageDto>() {
            @Override
            protected void configure() {
                map().setUserId(source.getUser().getId());
            }
        };


//        PropertyMap<Conversation, GetConversationDto> conversationDtoPropertyMap = new PropertyMap<Conversation, GetConversationDto>() {
//            @Override
//            protected void configure() {
//                int lastIndex = source.getMessages().size() - 1;
//                map().setLastMessage(source.getMessages().get(lastIndex));
//            }
//        };
        modelMapper.addMappings(userContactMap);
        modelMapper.addMappings(lastMessageDtoPropertyMap);
        modelMapper.addMappings(messageDtoPropertyMap);
        return modelMapper;
    }
}
