package com.github.hotire.web.resttemplate.common;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


import com.github.hotire.web.resttemplate.EnableRestTemplateHelper;
import com.github.hotire.web.resttemplate.asnyc.DefaultAsyncRestTemplateHelper;
import com.github.hotire.web.resttemplate.sync.DefaultRestTemplateHelper;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.assertj.core.util.Maps;
import org.junit.Test;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

public class RestTemplateHelperRegisterTest {

 @Test
 public void registerBeanDefinitions() {
  // Given
  final Set<String> beanSet = new HashSet<>();
  final RestTemplateHelperRegister restTemplateHelperRegister = new RestTemplateHelperRegister();
  final BeanFactory beanFactory = mock(BeanFactory.class);
  restTemplateHelperRegister.setBeanFactory(beanFactory);
  restTemplateHelperRegister.setEnvironment(mock(Environment.class));

  final AnnotationMetadata annotationMetadata = mock(AnnotationMetadata.class);
  final BeanDefinitionRegistry registry = new DefaultListableBeanFactory(){
   @Override
   public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
     throws BeanDefinitionStoreException {
    beanSet.add(beanName);
   }
  };

  final Map<String, Object> metaData = Maps.newHashMap("responseType", String.class);

  // When
  when(annotationMetadata.getAnnotationAttributes(EnableRestTemplateHelper.class.getName())).thenReturn(metaData);
  when(beanFactory.getBean(RestTemplate.class)).thenReturn(mock(RestTemplate.class));
  when(beanFactory.getBean(AsyncRestTemplate.class)).thenReturn(mock(AsyncRestTemplate.class));
  restTemplateHelperRegister.registerBeanDefinitions(annotationMetadata, registry);


  // Then
  assertThat(beanSet).contains(DefaultRestTemplateHelper.class.getSimpleName());
  assertThat(beanSet).contains(DefaultAsyncRestTemplateHelper.class.getSimpleName());
 }

}
