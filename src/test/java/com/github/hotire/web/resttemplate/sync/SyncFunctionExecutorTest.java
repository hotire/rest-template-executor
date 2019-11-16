package com.github.hotire.web.resttemplate.sync;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

public class SyncFunctionExecutorTest {

  private RestTemplate restTemplate;

  @Before
  public void config() {
    restTemplate = mock(RestTemplate.class);
  }

  @Test
  public void execute() {
    // Given
    final String url = "/test";
    final String expectedValue = "hello";

    // When
    when(restTemplate.getForEntity(url, String.class))
      .thenReturn(ResponseEntity.ok().body(expectedValue));

    // Then
    RestTemplateExecutor
      .ofFunctionTask(() -> restTemplate.getForEntity(url, String.class))
      .addTask(s -> {
        Assertions.assertThat(s).isEqualTo(expectedValue);
        return ResponseEntity.ok().build();
      })
      .execute();
  }

  @Test
  public void execute_no_content() {
    // Given
    final String url = "/test";
    final String key = "key";
    final Map<String, String> resultMap = new HashMap<>();

    // When
    when(restTemplate.getForEntity(url, String.class))
      .thenReturn(ResponseEntity.noContent().build());

    RestTemplateExecutor
      .ofTask(() -> restTemplate.getForEntity(url, String.class)
        , exception -> {}, s -> resultMap.put(key, s))
      .execute();

    // Then
    assertThat(resultMap).containsKey(key);
  }

  @Test
  public void execute_not_found() {
    // Given
    final String url = "/test";
    final String key = "key";
    final Map<String, String> resultMap = new HashMap<>();

    // When
    when(restTemplate.getForEntity(url, String.class))
      .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

    // Then, When
    RestTemplateExecutor
      .ofFunctionTask(() -> restTemplate.getForEntity(url, String.class)
        , exception -> assertThat(((HttpClientErrorException) exception).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
      )
      .addTask(s -> {
        resultMap.put(key, "value");
        System.out.println();
        return ResponseEntity.ok().build();
      })
      .execute();

    // Then
    assertThat(resultMap).doesNotContainKeys(key);
  }

  @Test
  public void execute_internal_server_error() {
    // Given
    final String url = "/test";

    // When
    when(restTemplate.getForEntity(url, String.class))
      .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

    // Then
    RestTemplateExecutor
      .ofFunctionTask(() -> restTemplate.getForEntity(url, String.class)
        , exception -> assertThat(((HttpServerErrorException) exception).getStatusCode())
          .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
      )
      .execute();
  }

  @Test(expected = NullPointerException.class)
  public void doOnSuccess_throw_exception() {
    // Given
    final String url = "/test";

    // When
    when(restTemplate.getForEntity(url, String.class))
      .thenReturn(ResponseEntity.noContent().build());

    // Then
    RestTemplateExecutor
      .ofTask(() -> restTemplate.getForEntity(url, String.class),
        error -> {}, String::toString)
      .execute();
  }

  @Test
  public void notFoundInFunction() {
    // Given
    final Map<String, Object> errorMap = new HashMap<>();

    // When
    when(restTemplate.getForEntity("/test", String.class))
      .thenReturn(ResponseEntity.ok().body("test"));
    when(restTemplate.getForEntity("/test2", String.class))
      .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

    RestTemplateExecutor
      .ofFunctionTask(() -> restTemplate.getForEntity("/test", String.class))
      .addTask(s -> restTemplate.getForEntity("/test2", String.class), exception -> errorMap.put("error", exception))
      .execute();

    // Then
    assertThat(errorMap).containsKey("error");
  }

}