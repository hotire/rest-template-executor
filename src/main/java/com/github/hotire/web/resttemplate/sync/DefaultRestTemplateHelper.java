package com.github.hotire.web.resttemplate.sync;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class DefaultRestTemplateHelper<T> implements
  RestForEntityOperations<ResponseEntity<T>, T> ,
  RestExchangeOperations<ResponseEntity<T>, T> {

  protected RestTemplate restTemplate;

  protected Class<T> responseType;

  protected String rootUrl;

  public DefaultRestTemplateHelper(RestTemplate restTemplate,  Class<T> responseType) {
    this.restTemplate =  Objects.requireNonNull(restTemplate);
    this.responseType = Objects.requireNonNull(responseType);
  }

  public DefaultRestTemplateHelper(RestTemplate restTemplate,  Class<T> responseType, String rootUrl) {
    this.restTemplate =  Objects.requireNonNull(restTemplate);
    this.responseType = Objects.requireNonNull(responseType);
    this.rootUrl = Objects.requireNonNull(rootUrl);
  }


  @Override
  public Supplier<ResponseEntity<T>> get(String url, Object... uriVariables) {
    return () -> restTemplate.getForEntity(rootUrl + url, responseType, uriVariables);
  }

  @Override
  public Supplier<ResponseEntity<T>> post(String url, Object request, Object... uriVariables) {
    return () -> restTemplate.postForEntity(rootUrl + url, request, responseType, uriVariables);
  }

  @Override
  public Supplier<ResponseEntity<T>> get(String url, HttpEntity<?> httpEntity, Object... uriVariables) {
    return () -> restTemplate.exchange(rootUrl + url, GET, httpEntity, responseType, uriVariables);
  }

  @Override
  public Supplier<ResponseEntity<T>> post(String url, HttpEntity<?> httpEntity,
    Object... uriVariables) {
    return () -> restTemplate.exchange(rootUrl + url, POST, httpEntity, responseType, uriVariables);
  }

  @Override
  public Function<T, ResponseEntity<T>> getFunction(String url, Object... uriVariables) {
    return t -> restTemplate.getForEntity(rootUrl + url, responseType, uriVariables);
  }

  @Override
  public Function<T, ResponseEntity<T>> postFunction(String url, Object request,
    Object... uriVariables) {
    return t -> restTemplate.postForEntity(rootUrl + url, request, responseType, uriVariables);
  }

  @Override
  public Function<T, ResponseEntity<T>> getFunction(String url, HttpEntity<?> httpEntity,
    Object... uriVariables) {
    return t -> restTemplate.exchange(rootUrl + url, GET, httpEntity, responseType, uriVariables);
  }

  @Override
  public Function<T, ResponseEntity<T>> postFunction(String url, HttpEntity<?> httpEntity,
    Object... uriVariables) {
    return t -> restTemplate.exchange(rootUrl + url, POST, httpEntity, responseType, uriVariables);
  }

}
