package com.github.hotire.web.resttemplate;


import com.github.hotire.web.resttemplate.common.RestTemplateHelperConfig;
import com.github.hotire.web.resttemplate.common.RestTemplateHelperRegister;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

/**
 * Auto Configuration 자동 설정
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({RestTemplateHelperConfig.class, RestTemplateHelperRegister.class})
public @interface EnableRestTemplateHelper {
  Class responseType() default Object.class;
}
