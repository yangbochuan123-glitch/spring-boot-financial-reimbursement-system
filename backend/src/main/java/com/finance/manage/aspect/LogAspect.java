package com.finance.manage.aspect;

import com.finance.manage.entity.SysLog;
import com.finance.manage.entity.SysUser;
import com.finance.manage.service.LogService;
import com.google.gson.Gson;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * aop 切面
 * 做日志拦截的切面
 */
@Aspect
@Component
public class LogAspect {

    @Autowired
    private LogService logService;

    private static final Gson gson = new Gson();

    //拦截login接口
    @Pointcut("execution(* com.finance.manage.controller.LoginController.login(..))")
    public void logPointCut() {}

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = point.proceed();

        try {
            //拦截之后做的内容
            saveLog(point, result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private void saveLog(ProceedingJoinPoint joinPoint, Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        SysLog log = new SysLog();

        // 获取request
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        
        // 设置操作类型和用户信息
        String methodName = method.getName();
        if ("login".equals(methodName)) {
            // 从返回结果中判断登录是否成功
            String resultStr = gson.toJson(result);
            boolean isSuccess = resultStr.contains("\"code\":200");
            log.setOperation(isSuccess ? "登录成功" : "登录失败");
            
            // 获取登录用户信息
            Object[] args = joinPoint.getArgs();
            if (args.length > 0 && args[0] instanceof SysUser) {
                SysUser user = (SysUser) args[0];
                if (isSuccess) {
                    // 如果登录成功，从返回结果中获取完整的用户信息
                    com.finance.manage.common.Result<?> resultObj = (com.finance.manage.common.Result<?>) result;
                    if (resultObj.getData() instanceof SysUser) {
                        SysUser fullUser = (SysUser) resultObj.getData();
                        log.setUserId(fullUser.getId());
                        log.setUsername(fullUser.getUsername());
                    }
                } else {
                    // 如果登录失败，记录尝试登录的用户名
                    log.setUsername(user.getUsername());
                }
                
                // 设置请求参数（密码用*号替代）
                SysUser paramUser = new SysUser();
                paramUser.setUsername(user.getUsername());
                paramUser.setPassword("******");
                log.setParams(gson.toJson(paramUser));
            }
        } else if ("logout".equals(methodName)) {
            log.setOperation("退出登录");
            // 从session获取用户信息
            SysUser user = (SysUser) request.getSession().getAttribute("user");
            if (user != null) {
                log.setUserId(user.getId());
                log.setUsername(user.getUsername());
            }
        }

        // 设置方法名
        String className = joinPoint.getTarget().getClass().getName();
        log.setMethod(className + "." + methodName + "()");

        // 设置IP地址
        log.setIp(getIpAddr(request));
        //保存登录的日志
        logService.add(log);
    }

    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
} 