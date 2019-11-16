package com.github.hotire.web.resttemplate.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hotire.executor.core.common.Caster;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class ObjectCaster {

  private final ObjectMapper objectMapper;

  @SuppressWarnings("unchecked")
  public <R> Consumer<Object> castAccept(JavaType javaType, Consumer<R> consumer) {
    return Caster.castAccept(obj -> objectMapper.convertValue(obj, javaType), consumer);
  }

  public <R> Consumer<Object> castAccept(TypeReference<R> typeReference, Consumer<R> consumer) {
    return Caster.castAccept(obj -> objectMapper.convertValue(obj, typeReference), consumer);
  }

  public <R> Consumer<Object> castAccept(Class<R> type, Consumer<R> consumer) {
    return Caster.castAccept(obj -> objectMapper.convertValue(obj, type), consumer);
  }

  public <R> Consumer<Object> castAccept(Function<Object, R> convertValueFunction, Consumer<R> consumer) {
    return Caster.castAccept(convertValueFunction, consumer);
  }

}
