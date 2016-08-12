package com.hiynn.fl.jingwuyun.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * <p>Title: SystemLogAspect </p>
 * <p>Description: TODO </p>
 * Date: 16/7/26 上午10:10.
 *
 * @author zhangdepeng@hiynn.com
 * @version 1.0 </p>
 *          Significant Modify：
 *          Date               Author           Content
 *          ==========================================================
 *          16/7/26         zhangdepeng         创建文件,实现基本功能
 *          <p/>
 *          ==========================================================
 */
@Aspect
@Component
public class SystemLogAspect {


    //注入Service用于把日志保存数据库
//    @Resource
//    private LogService logService;
    //本地异常日志记录对象
    private static final Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);
    private Long startTime;
    private Long endTime;
    private String MDCLogmethod;

    /**
     * 前置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 切点
     */
    public void doBefore(JoinPoint joinPoint) {
        startTime = System.currentTimeMillis();
        MDCLogmethod = MDC.get("method");
        try {
            //*========控制台输出=========*//
            MDC.put("method", "Controller");
            StringBuffer sb = new StringBuffer();
            sb.append("\n=====前置通知开始=====\n");
            sb.append("请求方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()")+"\n");
            sb.append("方法描述:" + getControllerMethodDescription(joinPoint)+"\n");
            sb.append("=====前置通知结束====="+"\n");
            logger.debug(sb.toString());
        } catch (Exception e) {
            //记录本地异常日志
            StringBuffer sb = new StringBuffer();
            sb.append("\n==前置通知异常=="+"\n");
            sb.append("异常信息:{}" + e.getMessage()+"\n");
            logger.error(sb.toString());
        } finally {
            if (MDCLogmethod != null){
                MDC.put("method", MDCLogmethod);
            }
        }
    }

    /***
     * 后置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint
     */
    public void doAfter(JoinPoint joinPoint) {
        endTime = System.currentTimeMillis();
        try {
            //*========控制台输出=========*//
            StringBuffer sb = new StringBuffer();
            sb.append("\n=====后置通知开始=====\n");
            sb.append("请求方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()")+"\n");
            sb.append("方法描述:" + getControllerMethodDescription(joinPoint)+"\n");
            sb.append("执行时间： [ timeCost ->" + (endTime - startTime) + "ms ]"+"\n");
            sb.append("=====后置通知结束====="+"\n");
            logger.info(sb.toString());
        } catch (Exception e) {
            StringBuffer sb = new StringBuffer();
            sb.append("==后置通知异常=="+"\n");
            sb.append("异常信息:"+ e.getMessage()+"\n");
            logger.error(sb.toString());
        } finally {
            if (MDCLogmethod != null) {
                MDC.put("method", MDCLogmethod);
            }
        }
    }

    /**
     * 异常通知 用于拦截service层记录异常日志
     *
     * @param joinPoint
     * @param e
     */
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        try {
            /* ========控制台输出========= */
            StringBuffer sb = new StringBuffer();
            sb.append("\n=====异常通知开始====="+"\n");
            sb.append("异常代码:" + e.getClass().getName()+"\n");
            sb.append("异常信息:" + e.getMessage()+"\n");
            sb.append("异常方法:"
                    + (joinPoint.getTarget().getClass().getName() + "."
                    + joinPoint.getSignature().getName() + "()")+"\n");
            sb.append("方法描述:" + getControllerMethodDescription(joinPoint)+"\n");
            sb.append("=====异常通知结束====="+"\n");
            logger.error(sb.toString());
        } catch (Exception ex) {
            // 记录本地异常日志
            StringBuffer sb = new StringBuffer();
            sb.append("\n==异常通知异常=="+"\n");
            sb.append("异常信息:"+ ex.getMessage()+"\n");
            logger.error(sb.toString());
        } finally {
            if (MDCLogmethod != null) {
                MDC.put("method", MDCLogmethod);
            }
        }

    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception
     */
    public static String getControllerMethodDescription(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description = method.getAnnotation(SystemControllerLog.class).description();
                    break;
                }
            }
        }
        return description;
    }
}
