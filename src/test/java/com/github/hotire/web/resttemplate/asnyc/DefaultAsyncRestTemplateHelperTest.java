package com.github.hotire.web.resttemplate.asnyc;


import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.function.Supplier;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;


public class DefaultAsyncRestTemplateHelperTest {

  private AsyncRestTemplate asyncRestTemplate = mock(AsyncRestTemplate.class);

  @Test
  public void create() {
    // When
    final DefaultAsyncRestTemplateHelper<String> helper = new DefaultAsyncRestTemplateHelper<>(asyncRestTemplate, String.class);
    final DefaultAsyncRestTemplateHelper<String> helper2 = new DefaultAsyncRestTemplateHelper<>(asyncRestTemplate, String.class, "");

    // Then
    assertThat(helper).isNotNull();
    assertThat(helper2).isNotNull();
  }

  @Test
  public void get_httpEntity() {
    // Given
    final DefaultAsyncRestTemplateHelper<String> helper = new DefaultAsyncRestTemplateHelper<>(asyncRestTemplate, String.class);

    // When
    final Supplier<ListenableFuture<ResponseEntity<String>>> supplier = helper.get("", mock(
      HttpEntity.class));

    // Then
    assertThat(supplier).isNotNull();
  }

  @Test
  public void post_httpEntity() {
    // Given
    final DefaultAsyncRestTemplateHelper<String> helper = new DefaultAsyncRestTemplateHelper<>(asyncRestTemplate, String.class);

    // When
    final Supplier<ListenableFuture<ResponseEntity<String>>> supplier = helper.post("", mock(
      HttpEntity.class));

    // Then
    assertThat(supplier).isNotNull();
  }

  @Test
  public void get() {
    // Given
    final DefaultAsyncRestTemplateHelper<String> helper = new DefaultAsyncRestTemplateHelper<>(asyncRestTemplate, String.class);

    // When
    final Supplier<ListenableFuture<ResponseEntity<String>>> supplier = helper.get("");

    // Then
    assertThat(supplier).isNotNull();
  }

  @Test
  public void post() {
    // Given
    final DefaultAsyncRestTemplateHelper<String> helper = new DefaultAsyncRestTemplateHelper<>(asyncRestTemplate, String.class);

    // When
    final Supplier<ListenableFuture<ResponseEntity<String>>> supplier = helper.post("", new Object());

    // Then
    assertThat(supplier).isNotNull();
  }
}
