package com.github.hotire.web.resttemplate.common;

import static com.github.hotire.executor.core.common.ErrorHelper.throwError;
import static com.github.hotire.web.resttemplate.common.ObjectCastHelper.cast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hotire.web.resttemplate.sync.RestTemplateExecutor;
import java.util.List;
import java.util.function.Consumer;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ObjectCasterTest {

  private RestTemplate restTemplate;

  private ObjectCaster webSuccessHelper;

  private ObjectMapper objectMapper;

  @Before
  public void config() {
    // Given
    restTemplate = mock(RestTemplate.class);
    objectMapper = new ObjectMapper();
    webSuccessHelper = new ObjectCaster(objectMapper);

    // When
    when(restTemplate.getForEntity("/test", Object.class))
      .thenReturn(ResponseEntity.ok().body(Lists.newArrayList("a", "b", "c")));
  }



  @Test
  public void bindResult_typeReference() {
    // Then
    RestTemplateExecutor
      .ofTask(() -> restTemplate.getForEntity("/test", Object.class),
        throwError(), webSuccessHelper.castAccept(new TypeReference<List<String>>() {}, strings -> strings.forEach(s -> {})))
      .execute();

  }

  @Test
  public void bindResult_javaType() {
    // Then
    RestTemplateExecutor
      .ofTask(() -> restTemplate.getForEntity("/test", Object.class),
        error -> {}, webSuccessHelper.castAccept(
          objectMapper.getTypeFactory().constructCollectionType(List.class, String.class),
          (Consumer<List<String>>) strings -> strings.forEach(s -> {})
        )
      )
      .execute();
  }

  @Test
  public void noContent() {
    // When
    when(restTemplate.getForEntity("/no_content", Object.class))
      .thenReturn(ResponseEntity.noContent().build());

    // Then
    RestTemplateExecutor
      .ofTask(() -> restTemplate.getForEntity("/no_content", Object.class), throwError(),
        cast(new TypeReference<List<Object>>(){})
          .accept(o -> {})
      ).execute();

  }

  @Test
  public void castAccept_classType() {
    final Object obj = "";
    webSuccessHelper.castAccept(String.class, s -> { }).accept(obj);
  }

  @Test
  public void castAccept_function() {
    final Object obj = "";
    webSuccessHelper.castAccept(o -> (String) o, s -> { }).accept(obj);
  }
}