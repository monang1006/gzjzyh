package com.strongit.oa.common.interceptor;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionInterceptor {

	/*@Pointcut("execution(* com.strongit.oa.work.*.*(..))")
	public void anyMethod(){};

	@AfterThrowing(pointcut="anyMethod()",throwing="ex")
	public void afterThrowException(Throwable ex) {
		ex.printStackTrace();
		System.err.println("抛出异常");
	}*/
}
