package com.github.hotire.web.resttemplate.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.Setter;


public class ObjectCastHelper {
  @Setter(value = AccessLevel.PACKAGE)
  private static ObjectMapper objectMapper = new ObjectMapper();

  private ObjectCastHelper() {
    throw new IllegalStateException("Utility class");
  }

  public static class CastConsumer<T> {
    Function<Object, T> convertValueFunction;
    private CastConsumer(Function<Object, T> convertValueFunction) {
      this.convertValueFunction = convertValueFunction;
    }

    public Consumer<Object> accept(Consumer<T> consumer) {
      return o -> consumer.accept(
        Optional.ofNullable(o)
        .map(obj -> convertValueFunction.apply(obj))
        .orElse(null)
      );
    }

    public void accept(Object target, Consumer<T> consumer) {
      consumer.accept(convertValueFunction.apply(target));
    }
  }

  public static <T> CastConsumer<T> cast(TypeReference<T> typeReference) {
    return new CastConsumer<>(o -> objectMapper.convertValue(o, typeReference));
  }

  public static <T> CastConsumer<T> cast(Class<T> type) {
    return new CastConsumer<>(o -> objectMapper.convertValue(o, type));
  }

  public static <T> CastConsumer<T> cast(JavaType javaType) {
    return new CastConsumer<>(o -> objectMapper.convertValue(o, javaType));
  }

  public static <T> CastConsumer<T> cast(Function<Object, T> convertValueFunction) {
    return new CastConsumer<>(convertValueFunction);
  }

}
