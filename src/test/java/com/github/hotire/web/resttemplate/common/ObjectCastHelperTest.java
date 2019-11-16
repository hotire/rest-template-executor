package com.github.hotire.web.resttemplate.common;

import static org.assertj.core.api.Java6Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.hotire.web.resttemplate.common.ObjectCastHelper.CastConsumer;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ObjectCastHelperTest {

  @Test
  public void testAccept() {
    // Given
    final List<Integer> integers = new ArrayList<>(Arrays.asList(1, 2, 3));
    final Object uncastedObject = integers;

    // When, Then
    ObjectCastHelper.cast(new TypeReference<List<Integer>>() {})
      .accept(uncastedObject, castedIntList ->
        Assertions.assertThat(castedIntList).isEqualTo(integers));
  }

  @Test
  public void testAccept_classType() {
    // Given
    final List<Integer> integers = new ArrayList<>(Arrays.asList(1, 2, 3));
    final Object uncastedObject = integers;

    // When, Then
    ObjectCastHelper.cast(List.class)
      .accept(uncastedObject, castedIntList ->
        Assertions.assertThat(castedIntList).isEqualTo(integers));
  }

  @Test
  public void testAccept_javaType() {
    // Given
    final List<Integer> integers = new ArrayList<>(Arrays.asList(1, 2, 3));
    final Object uncastedObject = integers;

    // When, Then
    ObjectCastHelper.cast(TypeFactory.defaultInstance().constructCollectionType(List.class, Integer.class))
      .accept(uncastedObject, castedIntList ->
        Assertions.assertThat(castedIntList).isEqualTo(integers));
  }

  @Test
  public void testCast() {
    // Given
    final CastConsumer<String> result = ObjectCastHelper.cast(o -> "");

    // Then
    assertThat(result).isNotNull();
  }

  @Test(expected = IllegalStateException.class)
  public void create_throwException()
    throws Throwable {
    // Given
    final Constructor constructor = ObjectCastHelper.class.getDeclaredConstructor();

    // When Then
    constructor.setAccessible(true);
    try {
      constructor.newInstance();
    } catch (Exception e) {
      throw e.getCause();
    }
  }
}