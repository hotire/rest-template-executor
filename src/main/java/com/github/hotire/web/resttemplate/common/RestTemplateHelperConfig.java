package com.github.hotire.web.resttemplate.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate / AsyncRestTemplate 설정
 *
 * ObjectCaster 설정
 */
@Order
@Configuration
public class RestTemplateHelperConfig {

  @Bean
  @ConditionalOnMissingBean(RestTemplate.class)
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  @ConditionalOnMissingBean(AsyncRestTemplate.class)
  public AsyncRestTemplate asyncRestTemplate() {
    return new AsyncRestTemplate();
  }

  @Bean
  public ObjectCaster objectCaster(ObjectMapper objectMapper) {
    ObjectCastHelper.setObjectMapper(objectMapper);
    return new ObjectCaster(objectMapper);
  }
}
