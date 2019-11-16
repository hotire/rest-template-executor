package com.github.hotire.web.resttemplate.common;

import static org.assertj.core.api.Java6Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import org.junit.Test;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

public class RestTemplateHelperConfigTest {

  @Test
  public void restTemplate() {
    // Given
    final RestTemplateHelperConfig config = new RestTemplateHelperConfig();

    // When
    final RestTemplate restTemplate = config.restTemplate();

    // Then
    assertThat(restTemplate).isNotNull();
  }

  @Test
  public void asyncRestTemplate() {
    // Given
    final RestTemplateHelperConfig config = new RestTemplateHelperConfig();

    // When
    final AsyncRestTemplate asyncRestTemplate = config.asyncRestTemplate();

    // Then
    assertThat(asyncRestTemplate).isNotNull();
  }

  @Test
  public void objectCaster() throws NoSuchFieldException, IllegalAccessException {
    // Given
    final ObjectMapper objectMapper = new ObjectMapper();
    final RestTemplateHelperConfig config = new RestTemplateHelperConfig();

    // When
    final ObjectCaster objectCaster = config.objectCaster(objectMapper);
    final Object result = Arrays.stream(ObjectCastHelper.class.getDeclaredFields())
      .filter(field -> "objectMapper".equals(field.getName()))
      .findAny()
      .map(field -> {
        field.setAccessible(true);
        try {
          return field.get(null);
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      })
      .orElseThrow(() -> new RuntimeException("not found objectMapper field"));

    // Then
    assertThat(objectCaster).isNotNull();
    assertThat(result).isNotNull();
    assertThat(result).isInstanceOf(ObjectMapper.class);
    assertThat(result).isEqualTo(objectMapper);
  }


}
