//package ru.clothingstore.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.support.ResourceBundleMessageSource;
//import org.springframework.format.FormatterRegistry;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@EnableWebMvc
//public class MvcConfig implements WebMvcConfigurer {
//
//    @Value("${upload.path}")
//    private String uploadPath;
////    @Autowired
////    RoleFormatter roleFormatter;
////    @Autowired
////    CategoryFormatter categoryFormatter;
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/img/**")
//                .addResourceLocations("file://" + uploadPath + "/");
//
//
////        }
//            registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
//    }
//
////    @Override
////    public void addFormatters(FormatterRegistry registry) {
////        registry.addFormatter(roleFormatter);
////        registry.addFormatter(categoryFormatter);
////    }
//
////    @Bean
////    public ResourceBundleMessageSource messageSource() {
////        ResourceBundleMessageSource rb = new ResourceBundleMessageSource();
////        rb.setBasenames(new String[]{"validation/messages"});
////        return rb;
////    }
////
////    @Controller
////    static class FaviconController {
////        @RequestMapping("favicon.ico")
////        String favicon() {
////            return "forward:/resources/favicon.ico";
////        }
////    }
//}