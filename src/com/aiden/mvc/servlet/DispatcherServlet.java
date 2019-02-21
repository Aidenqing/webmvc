package com.aiden.mvc.servlet;

import com.aiden.mvc.BaseController;
import com.aiden.mvc.anotation.Controller;
import com.aiden.mvc.anotation.RequestMapping;
import com.aiden.mvc.anotation.Scope;
import com.aiden.mvc.utils.ClassScanner;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * /* @author aiden
 * /* @creat  2019/2/21 13:54
 * /* @Description
 **/
@WebServlet(urlPatterns="*.action",initParams = {@WebInitParam(name="basePackage",value = "com.aiden.mvc")})

public class DispatcherServlet extends HttpServlet {
    //单例:
    //存放方法的映射地址，和方法类
    private Map<String,Method> methods = new HashMap<>();
    //存放controller实例 只打了controller注解的类才存进去
    private Map<String,Object> controllers = new HashMap<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        String basePackage = config.getInitParameter("basePackage");
        Map<String, Class<?>> classMap = ClassScanner.scannerClass(basePackage);
        Set<Map.Entry<String, Class<?>>> entries = classMap.entrySet();

        for (Map.Entry<String, Class<?>> entry : entries) {
            String className = entry.getKey();
            Class<?> clazz = entry.getValue();
            String path = "";
            try {
                //如果这个类标记了Controller注解
                if (clazz.isAnnotationPresent(Controller.class)) {
                    //如果这个类标记了RequestMapping注解
                    if (clazz.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping reqAnno = clazz.getAnnotation(RequestMapping.class);
                        path = reqAnno.value();
                    }
                    controllers.put(className, clazz.newInstance());
                    Method[] ms = clazz.getMethods();
                    for (Method method : ms) {
                        //如果这个方法标记了RequestMapping注解
                        if (method.isAnnotationPresent(RequestMapping.class)) {
                            String requestMappingPath = path + method.getAnnotation(RequestMapping.class).value();
                            methods.put(requestMappingPath,method);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            String uri = req.getRequestURI();
            String contextPath = req.getContextPath();
            //   /index.action 要求拿到index
            String requestMappingPath = uri.substring(contextPath.length(),uri.indexOf(".action"));
            Method method = methods.get(requestMappingPath);
            if(method == null){
                resp.sendError(404);
                return;
            }
            BaseController controller = null;
            Class<?> requestClass = method.getDeclaringClass();
            //如果这个请求的类被标记了范围
            if(requestClass.isAnnotationPresent(Scope.class) &&
                    requestClass.getAnnotation(Scope.class).value().equals("prototype")){
                //controller 多例:
                controller = (BaseController) controllers.get(method.getDeclaringClass().getName());
            }else{//controller 单例: （默认）
                controller = (BaseController) method.getDeclaringClass().newInstance();
            }
            //传递 request 和 response
            controller.init(req,resp);
            method.invoke(controller);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
