package com.hiynn.fl.jingwuyun.util;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
/**
 * 
 * <p>Title: HiynnAdvisor </p>
 * <p>Description: TODO </p>
 * Date: 2016年5月31日 上午11:45:46
 * @author loulvlin@hiynn.com
 * @version 1.0 </p> 
 * Significant Modify：
 * Date               Author           Content
 * ==========================================================
 * 2016年5月31日         loulvlin         创建文件,实现基本功能
 * 
 * ==========================================================
 */
public class HiynnAdvisor implements MethodInterceptor {

    private Boolean ServiceLogFlag=false;
    private Boolean daoLogFlag=false;
	private static final Logger CAT = LoggerFactory
			.getLogger(HiynnAdvisor.class);
	
	@SuppressWarnings("rawtypes")
	//@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		String flag = getMethodString(invocation);
		long start = System.currentTimeMillis();
        String value=MDC.get("method");
        try{
            if (ServiceLogFlag) {
                MDC.put("method","Service");
            } else if (daoLogFlag) {
            	MDC.put("method","Dao");
			}else {
                MDC.put("method","OtherMethod");
            }

			Object result = invocation.proceed();
			long cost =  System.currentTimeMillis() - start;
			StringBuilder sb = new StringBuilder();
            sb.append("Executed " + flag + " [ timeCost -> " + cost + " ms , result(size) -> ");
            if (result instanceof List) {
                sb.append(((List) result).size());
            } else if (result instanceof Map) {
            	sb.append(((Map) result).size());
            } else {
                sb.append(result);
            }
            sb.append(" ]");
            CAT.info(sb.toString());
			return result;
	    }catch(Exception e){
	    	StringBuilder sb = new StringBuilder();
            sb.append("Executed " + flag + " [ timeCost -> " + (System.currentTimeMillis() - start) + " ms ] ");
            sb.append(e.getClass().getName() + ",");
            sb.append(e.toString());
            CAT.error(sb.toString());
	    	throw e;
	    }finally {
            MDC.put("method",value);
        }

	}
	
	/**
     * GetMethodString: 获取MethodInvocation的输出文本
     *
     * @param invocation
     * @return 输出文本
     */
    private static String getMethodString(MethodInvocation invocation) {
        Method method = invocation.getMethod();
        StringBuffer sb = new StringBuffer();
        sb.append(method.getDeclaringClass().getSimpleName());
        sb.append("@[");
        sb.append(method.getReturnType().getSimpleName()).append(" ");
        sb.append(method.getName());
        sb.append("(");
        @SuppressWarnings("rawtypes")
        Class[] params = method.getParameterTypes();
        for (int j = 0; j < params.length; j++) {
            // TODO 参数是泛型是，无法区分两个方法，后续补充
            sb.append(params[j].getSimpleName());
            if (j < (params.length - 1)) {
                sb.append(",");
            }
        }
        sb.append(")]");
        return sb.toString();
    }

    public Boolean getdaoLogFlag() {
        return daoLogFlag;
    }

    public void setdaoLogFlag(Boolean daoLogFlag) {
        this.daoLogFlag = daoLogFlag;
    }

    public Boolean getServiceLogFlag() {

        return ServiceLogFlag;
    }

    public void setServiceLogFlag(Boolean serviceLogFlag) {
        ServiceLogFlag = serviceLogFlag;
    }
}
