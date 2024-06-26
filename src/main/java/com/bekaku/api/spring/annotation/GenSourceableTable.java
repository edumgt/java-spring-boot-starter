package com.bekaku.api.spring.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GenSourceableTable {

    boolean createController() default true;

    boolean createRepository() default true;

    boolean createService() default true;

    boolean createServiceImpl() default true;

    boolean createMapper() default false;

    boolean createDto() default true;

    boolean createPermission() default true;

    boolean createValidator() default false;
    boolean createFrontend() default false;
}
