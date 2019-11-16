package com.github.hotire.web.resttemplate.sync;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

public class SyncSupplierExecutorTest {

  @Mock
  private RestTemplate restTemplate;

  @Before
  public void config() {
    restTemplate = mock(RestTemplate.class);
  }

  @Test
  public void execute_supplier() {
    // When
    when(restTemplate.getForEntity("/test", String.class))
      .thenReturn(ResponseEntity.ok().body("hello"));
    when(restTemplate.getForEntity("/key", String.class))
      .thenReturn(ResponseEntity.ok().body("value"));

    // Then
    RestTemplateExecutor
      .ofTask(() -> restTemplate.getForEntity("/test", String.class)
        , exception -> {}
        , s -> Assert.assertEquals("hello", s))
      .execute();

    // When
    Map<String, String> map = new HashMap<>();
    RestTemplateExecutor
      .ofTask(() -> ResponseEntity.ok().body("value")
        , exception -> {}
        , s -> map.put("key", s))
      .execute();

    // Then
    assertThat(map.get("key")).isEqualTo("value");
  }

  @Test
  public void execute_supplier_not_found() {
    // When
    when(restTemplate.getForEntity("/test", String.class))
      .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

    // Then
    RestTemplateExecutor
      .ofTask(() -> restTemplate.getForEntity("/test", String.class)
        , exception -> {
          assertThat(exception).isInstanceOf(HttpClientErrorException.class);
          assertThat(((HttpClientErrorException)exception).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        })
      .execute();
  }

  @Test
  public void execute_supplier_internal_server_error() {
    // When
    when(restTemplate.getForEntity("/test", String.class))
      .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

    // Then
    RestTemplateExecutor
      .ofTask(() -> restTemplate.getForEntity("/test", String.class)
        , exception -> {
          assertThat(exception).isInstanceOf(HttpServerErrorException.class);
          assertThat(((HttpServerErrorException)exception).getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        })
      .execute();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void executeByAsync() {
    RestTemplateExecutor
      .ofTask(() -> restTemplate.getForEntity("/test", String.class))
      .executeByAsync();
  }

}