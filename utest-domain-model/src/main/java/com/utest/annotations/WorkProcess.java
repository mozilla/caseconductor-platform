package com.utest.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface WorkProcess
{
	String value();

	String[] flowParameterNames() default {};

	String returnName() default "";

	String wrappedWorkItemName() default "";
}
