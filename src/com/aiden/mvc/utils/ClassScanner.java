package com.aiden.mvc.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * /* @author aiden
 * /* @creat  2019/2/21 13:37
 * /* @Description 扫描包，获取所有.class文件字节码对象
 **/
public class ClassScanner {
    public static Map<String, Class<?>> scannerClass(String basePackage) {
        Map<String, Class<?>> result = new HashMap<>();
        //把com.aiden.mvc 换成com/aiden/mvc再类加载器读取文件
        String basePath = basePackage.replaceAll("\\.", "/");
        try {
            //得到com/aiden/mvc的绝对地址 /D:xxxxx/com/aiden/mvc

            String rootPath = ClassScanner.class.getClassLoader().getResource(basePath).getPath();
            //只留com/aiden/mvc 目的为了后续拼接成一个全限定名
            if (rootPath != null) rootPath = rootPath.substring(rootPath.indexOf(basePath));

            Enumeration<URL> enumeration = ClassScanner.class.getClassLoader().getResources(basePath);
            while (enumeration.hasMoreElements()) {
                URL url = enumeration.nextElement();
                if (url.getProtocol().equals("file")) {//如果是个文件
                    File file = new File(url.getPath().substring(1));
                    scannerFile(file, rootPath, result);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }

    //扫描文件
    private static void scannerFile(File folder, String rootPath, Map<String, Class<?>> classes) {
        try {
            File[] files = folder.listFiles();
            for (int i = 0; files != null && i < files.length; i++) {
                File file = files[i];
                if (file.isDirectory()) {
                    scannerFile(file, rootPath + file.getName() + "/", classes);
                } else {
                    if (file.getName().endsWith(".class")) {
                        String className = (rootPath + file.getName()).replaceAll("/", ".");
                        className = className.substring(0, className.indexOf(".class"));//去掉扩展名得到全限定名
                        //Map容器存储全限定名和Class
                        classes.put(className, Class.forName(className));
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

    }
}
