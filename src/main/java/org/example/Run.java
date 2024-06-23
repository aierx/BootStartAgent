package org.example;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.Instrumentation;

/**
 * @author leiwenyong
 * @since 6/23/2024
 */
public class Run {
	public static void premain(String agentArgs, Instrumentation inst) {
		inst.addTransformer((loader, className, classBeingRedefined, protectionDomain, classfileBuffer) -> {
			if (className.equals("org/springframework/beans/factory/support/AbstractAutowireCapableBeanFactory")) {
				// add code
				System.out.println("modify doCreateBean");
				try {
					ClassPool classPool = ClassPool.getDefault();
					CtClass ctClass = classPool.get("org.springframework.beans.factory.support" +
							".AbstractAutowireCapableBeanFactory");
					CtMethod ctMethod = ctClass.getDeclaredMethod("doCreateBean");
					CtClass exp = classPool.get("java.lang.Exception");
					ctMethod.addCatch("{ \n" +
							"    logger.error(\"Error creating bean, skipped. bean name is : \"+$1);\n" +
							"    return new Object();\n" +
							"}", exp);
					return ctClass.toBytecode();
				} catch (Exception e) {
					System.out.println("enhance class fail");
				}
			}
			return classfileBuffer;
		});
	}
}