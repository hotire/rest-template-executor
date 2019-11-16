package com.github.hotire.web.resttemplate.common;


import com.github.hotire.web.resttemplate.EnableRestTemplateHelper;
import com.github.hotire.web.resttemplate.asnyc.DefaultAsyncRestTemplateHelper;
import com.github.hotire.web.resttemplate.sync.DefaultRestTemplateHelper;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

/**
 *
 * {@link DefaultRestTemplateHelper}
 * {@link DefaultAsyncRestTemplateHelper}
 *
 * properties rest.template.helper.rootUrl 값을 읽어드려, Bean 으로 생성해준다.
 */
public class RestTemplateHelperRegister implements ImportBeanDefinitionRegistrar,
  EnvironmentAware, BeanFactoryAware {

  private static final String ROOT_KEY = "rest.template.helper.rootUrl";

  private Environment environment;

  private BeanFactory beanFactory;

  @Override
  public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
    BeanDefinitionRegistry registry) {
    Map<String, Object> metaData = importingClassMetadata.getAnnotationAttributes(
      EnableRestTemplateHelper.class.getName());
    final AnnotationAttributes attributes = AnnotationAttributes.fromMap(metaData);

    registerBeanDefinition(DefaultRestTemplateHelper.class, attributes, registry);
    registerBeanDefinition(DefaultAsyncRestTemplateHelper.class, attributes, registry);
  }

  private void registerBeanDefinition(Class<?> beanClass, AnnotationAttributes attributes,
    BeanDefinitionRegistry registry) {

    final GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
    genericBeanDefinition.setBeanClass(beanClass);
    ConstructorArgumentValues constructorArgumentValues = genericBeanDefinition.getConstructorArgumentValues();
    constructorArgumentValues.addIndexedArgumentValue(0, getBean(beanClass));
    constructorArgumentValues.addIndexedArgumentValue(1, attributes.getClass("responseType"));
    constructorArgumentValues.addIndexedArgumentValue(2, this.getRootUrl());

    registry.registerBeanDefinition(beanClass.getSimpleName(), genericBeanDefinition);
  }

  private String getRootUrl() {
    return Optional.ofNullable(environment.getProperty(ROOT_KEY)).orElse("");
  }

  private Object getBean(Class<?> beanClass) {
    if (beanClass == DefaultRestTemplateHelper.class) {
      return beanFactory.getBean(RestTemplate.class);
    }
    return beanFactory.getBean(AsyncRestTemplate.class);
  }

  @Override
  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }

  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }
}
