package com.utest.service;

import org.aspectj.lang.annotation.Pointcut;

/**
 * Pointcuts that define the services layer.
 * 
 * @author Miguel Bautista
 */
public class ServiceLayer
{

	/**
	 * A pointcut that defines the entry point methods in the services layer.
	 */
	@Pointcut("execution(* com.utest.*service.api..*.*(..))")
	public void entryPointMethod()
	{
	}

}
