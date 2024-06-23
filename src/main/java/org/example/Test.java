package org.example;

import javassist.*;

import java.io.IOException;

/**
 * @author leiwenyong
 * @since 6/23/2024
 */
public class Test {
	public static void main(String[] args) throws NotFoundException, CannotCompileException, IOException {
		ClassPool pool = ClassPool.getDefault();
		CtClass ctClass = pool.get("org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory");
		CtClass aaaa = pool.get("java.lang.Class");
		
		System.out.println("modify doCreateBean");
		CtMethod aaa = ctClass.getDeclaredMethod("doCreateBean");
		String catchBody = "{ \n" +
				"    logger.error(\"Error creating bean, skipped. bean name is : \"+$1,$e);\n" +
				"    java.lang.Class clz = Class.forName(\"org.springframework.beans.factory.support.NullBean\");\n" +
				"    java.lang.reflect.Constructor constructor = clz.getDeclaredConstructor(null);\n" +
				"    constructor[0].setAccessible(true);\n" +
				"    return constructor[0].newInstance(); \n" +
				"}";
		CtClass exType = pool.get("java.lang.Exception");
		aaa.addCatch(catchBody, exType);
		
		ctClass.writeFile("C:\\Users\\aleiwe\\Desktop\\BootStartAgent");
	}
}
