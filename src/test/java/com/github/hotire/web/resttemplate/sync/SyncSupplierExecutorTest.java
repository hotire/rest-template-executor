package com.github.hotire.web.resttemplate.sync;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

public class SyncSupplierExecutorTest {

  private RestTemplate restTemplate;

  @Before
  public void config() {
    restTemplate = mock(RestTemplate.class);
  }

  @Test
  public void execute_supplier() {
    // Given
    final String url = "/test";
    final String expectedBody = "hello";

    // When
    when(restTemplate.getForEntity(url, String.class))
      .thenReturn(ResponseEntity.ok().body(expectedBody));

    // Then
    RestTemplateExecutor
      .ofTask(() -> restTemplate.getForEntity(url, String.class)
        , exception -> {}
        , s -> Assertions.assertThat(s).isEqualTo(expectedBody))
      .execute();
  }

  @Test
  public void execute_supplier_not_found() {
    // Given
    final String url = "/test";

    // When
    when(restTemplate.getForEntity(url, String.class))
      .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

    // Then
    RestTemplateExecutor
      .ofTask(() -> restTemplate.getForEntity(url, String.class)
        , exception -> {
          assertThat(exception).isInstanceOf(HttpClientErrorException.class);
          assertThat(((HttpClientErrorException)exception).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        })
      .execute();
  }

  @Test
  public void execute_supplier_internal_server_error() {
    // Given
    final String url = "/test";

    // When
    when(restTemplate.getForEntity(url, String.class))
      .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

    // Then
    RestTemplateExecutor
      .ofTask(() -> restTemplate.getForEntity(url, String.class)
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