package com.github.hotire.web.resttemplate.sync;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.function.Function;
import java.util.function.Supplier;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class DefaultRestTemplateHelperTest {

  @Test
  public void create() {
    // Given
    final DefaultRestTemplateHelper<String> helper = new DefaultRestTemplateHelper<>(mock(
      RestTemplate.class), String.class);
    final DefaultRestTemplateHelper<String> helper2 = new DefaultRestTemplateHelper<>(mock(
      RestTemplate.class), String.class, "");

    // Then
    assertThat(helper).isNotNull();
    assertThat(helper2).isNotNull();
  }

  @Test
  public void get() {
    // Given
    final DefaultRestTemplateHelper<String> helper = new DefaultRestTemplateHelper<>(mock(
      RestTemplate.class), String.class);

    // When
    final Supplier<ResponseEntity<String>> result = helper.get("", mock(HttpEntity.class));
    final Supplier<ResponseEntity<String>> result2 = helper.get("");

    // Then
    assertThat(result).isNotNull();
    assertThat(result2).isNotNull();
  }

  @Test
  public void get_function() {
    // Given
    final DefaultRestTemplateHelper<String> helper = new DefaultRestTemplateHelper<>(mock(
      RestTemplate.class), String.class);

    // When
    final Function<String, ResponseEntity<String>> result = helper.getFunction("", mock(HttpEntity.class));
    final Function<String, ResponseEntity<String>> result2 = helper.getFunction("");

    // Then
    assertThat(result).isNotNull();
    assertThat(result2).isNotNull();
  }

  @Test
  public void post() {
    // Given
    final DefaultRestTemplateHelper<String> helper = new DefaultRestTemplateHelper<>(mock(
      RestTemplate.class), String.class);

    // When
    final Supplier<ResponseEntity<String>> result = helper.post("", mock(HttpEntity.class));
    final Supplier<ResponseEntity<String>> result2 = helper.post("", "");

    // Then
    assertThat(result).isNotNull();
    assertThat(result2).isNotNull();
  }

  @Test
  public void post_function() {
    // Given
    final DefaultRestTemplateHelper<String> helper = new DefaultRestTemplateHelper<>(mock(
      RestTemplate.class), String.class);

    // When
    final Function<String, ResponseEntity<String>> result = helper.postFunction("", mock(HttpEntity.class));
    final Function<String, ResponseEntity<String>> result2 = helper.postFunction("", "");

    // Then
    assertThat(result).isNotNull();
    assertThat(result2).isNotNull();
  }



}
