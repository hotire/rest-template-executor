package com.github.hotire.web.resttemplate.asnyc;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

import java.util.function.Supplier;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;

public class DefaultAsyncRestTemplateHelper<T> implements
  AsyncRestForEntityOperations<ListenableFuture<ResponseEntity<T>>>,
  AsyncRestExchangeOperations<ListenableFuture<ResponseEntity<T>>> {

  protected AsyncRestTemplate asyncRestTemplate;

  protected Class<T> responseType;

  protected String rootUrl;

  public DefaultAsyncRestTemplateHelper(AsyncRestTemplate asyncRestTemplate, Class<T> responseType) {
    this.asyncRestTemplate = asyncRestTemplate;
    this.responseType = responseType;
  }

  public DefaultAsyncRestTemplateHelper(AsyncRestTemplate asyncRestTemplate, Class<T> responseType, String rootUrl) {
    this.asyncRestTemplate = asyncRestTemplate;
    this.responseType = responseType;
    this.rootUrl = rootUrl;
  }

  @Override
  public Supplier<ListenableFuture<ResponseEntity<T>>> get(String url, HttpEntity<?> httpEntity,
    Object... uriVariables) {
    return () -> asyncRestTemplate.exchange(rootUrl + url, GET, httpEntity, responseType, uriVariables);
  }

  @Override
  public Supplier<ListenableFuture<ResponseEntity<T>>> post(String url, HttpEntity<?> httpEntity,
    Object... uriVariables) {
    return () -> asyncRestTemplate.exchange(rootUrl + url, POST, httpEntity, responseType, uriVariables);
  }


  @Override
  public Supplier<ListenableFuture<ResponseEntity<T>>> get(String url, Object... uriVariables) {
    return () -> asyncRestTemplate.getForEntity(rootUrl + url, responseType, uriVariables);
  }

  @Override
  public Supplier<ListenableFuture<ResponseEntity<T>>> post(String url, Object request,
    Object... uriVariables) {
    return () -> asyncRestTemplate.postForEntity(rootUrl + url, (HttpEntity<?>) request, responseType, uriVariables);
  }

}
