package com.project.book.common.config.auth;

import org.apache.catalina.filters.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

//@Configuration
//public class CorsConfig {
//
//   @Bean
//   public CorsFilter corsFilter() {
//      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//      CorsConfiguration config = new CorsConfiguration();
//      config.addAllowedOriginPattern("*");
////      config.addAllowedOrigin("http://localhost:8081");
////      config.addAllowedOrigin("*");
//      config.addAllowedHeader("*");
//      config.addAllowedMethod("*");
//      config.setAllowCredentials(true);
//
//      source.registerCorsConfiguration("/**", config);
//      return source;
//   }
//
//
//}