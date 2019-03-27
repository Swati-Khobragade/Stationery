package com.gl.adminportal.stationery.utils;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

	private static final Logger LOGGER = Logger.getLogger(LoggingAspect.class);

	@Pointcut("execution(* *.*(..)) "
			+ "&& target(com.gl.adminportal.stationery)")
	protected void LOGGERgingOperation() {
	}

	// @Around("execution(* *.*(..)) && target(com.gl.adminportal.stationery.controller.FeedBackController)")
	@Around("within(com.gl.adminportal.stationery.controller.*)")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		Long timeStarted = System.currentTimeMillis();
		LOGGER.info("-------------------------------------------------------------------------------------");
		LOGGER.info("The method " + joinPoint.getSignature().getName()
				+ "() begins with " + Arrays.toString(joinPoint.getArgs()));
		LOGGER.info("-------------------------------------------------------------------------------------");
		try {
			Object result = joinPoint.proceed();
			LOGGER.info("-------------------------------------------------------------------------------------");
			LOGGER.info("The method " + joinPoint.getSignature().getName()
					+ "() ends with " + result);
			LOGGER.info("-------------------------------------------------------------------------------------");
			Long timecompleted = System.currentTimeMillis();

			LOGGER.info("Time taken to complete the request "
					+ (timecompleted - timeStarted));
			return result;
		} catch (IllegalArgumentException e) {
			LOGGER.error("Illegal argument "
					+ Arrays.toString(joinPoint.getArgs()) + " in "
					+ joinPoint.getSignature().getName() + "()");
			throw e;
		}
	}

}
