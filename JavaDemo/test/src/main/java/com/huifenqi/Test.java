package com.huifenqi;

import java.net.URL;
import java.util.*;

/**
 * Created by t3tiger on 2017/7/3.
 */
public class Test {
    public static final String FACTORIES_RESOURCE_LOCATION = "META-INF/spring.factories";
    public static void main(String[] args) throws Exception{
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> urls = (classLoader != null ? classLoader.getResources(FACTORIES_RESOURCE_LOCATION) :
                ClassLoader.getSystemResources(FACTORIES_RESOURCE_LOCATION));
        List<String> result = new ArrayList<String>();
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            System.out.println(url.getPath());
//            Properties properties = PropertiesLoaderUtils.loadProperties(new UrlResource(url));
//            String factoryClassNames = properties.getProperty(factoryClassName);
//            result.addAll(Arrays.asList(StringUtils.commaDelimitedListToStringArray(factoryClassNames)));
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<Thread, StackTraceElement[]> traces = Thread.getAllStackTraces();
                for (Thread t : traces.keySet()) {
                    System.out.println(t.getName() + "\t" + t.getContextClassLoader());

                }


                System.out.println("---------------------------");
                StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
                for (StackTraceElement stackTraceElement : stackTrace) {
                    System.out.println(stackTraceElement.getClassName());
                    System.out.println(stackTraceElement.getLineNumber());
                    System.out.println(stackTraceElement.getFileName());
                    if ("main".equals(stackTraceElement.getMethodName())) {
                        System.out.println(stackTraceElement.getClassName());
                    }
                }
            }
        }).start();

    }
}
