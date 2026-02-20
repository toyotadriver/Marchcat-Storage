package mcstorage.util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class Logging {
	private static Logger logger;
	
	public Logging() {
		this.logger = Logger.getLogger(Logging.class.getName());
//		ConsoleHandler consoleHandler = new ConsoleHandler();
//		logger.setLevel(Level.ALL);
//		logger.addHandler(consoleHandler);
	}
	
	public static void log(String msg) {
		logger.info(msg);
	}
	
	@Before(value = "mcsService()")
	public void logService(JoinPoint joinPoint) {
		logger.info(logClassCallInfo(joinPoint));
	}
	
	@AfterThrowing(pointcut = "mcPicturesExceptions()", throwing = "e")
	public void logPicturesExceptions(JoinPoint joinPoint, Throwable e) {
		logger.info(logExceptions(joinPoint, e));
	}
	
	private String logClassCallInfo(JoinPoint joinPoint) {
		
		Signature signature = joinPoint.getSignature();
		Class clazz = signature.getDeclaringType();
		
		String out = "Called class: " + clazz.getName() +
				" | method: " + signature.getName() +
				" | arguments: " + joinPoint.getArgs().toString();
		return out;
	}
	
	/**
	 * Log the exception and the cause (if != null).
	 * @param joinPoint
	 * @param e
	 * @return
	 */
	private String logExceptions(JoinPoint joinPoint, Throwable e) {
		StringBuilder message = new StringBuilder();
		message.append(logClassCallInfo(joinPoint));
		message.append("Thrown an exception: \n");
		message.append(e.getMessage() + "\n");
		if(e.getCause() != null)
			message.append("Caused by: " + e.getCause().getMessage());
		return message.toString();
	}
	
	
	
	@Pointcut("execution(public * mcstorage.service.*.*(..))")
	private void mcsService() {};
	
	@Pointcut("execution( * mcstorage.pictures.*.*(..))")
	private void mcsServiceExceptions() {}

}
