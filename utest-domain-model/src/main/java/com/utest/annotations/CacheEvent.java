package com.utest.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CacheEvent
{
	public final static String	USER_MATCHING		= "1";
	public final static String	TEST_CYCLE_MATCHING	= "2";
	public final static String	USER_CONTEXT		= "3";

	public final static String	CACHE_READ			= "1";
	public final static String	CACHE_REFRESH		= "2";

	String operation();

	String type();

}
