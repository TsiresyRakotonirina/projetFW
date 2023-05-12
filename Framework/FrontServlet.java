package etu002015.framework.servlet;

import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import etu002015.framework.Mapping;
import java.net.URL;
import java.io.*;
import java.lang.reflect.*;
import etu002015.framework.annotation.*;

public class FrontServlet extends HttpServlet {

    HashMap<String,Mapping> MappingUrls;

    public void init() throws ServletException{
        try {
            super.init();
            // String packages = String.valueOf(getInitParameter("packages"));
            String packages = "etu001935.model";
            this.MappingUrls=new HashMap<>();
            String path = packages.replaceAll("[.]","\\\\");
            URL packageUrl=Thread.currentThread().getContextClassLoader().getResource(path);
            File packDir =new File(packageUrl.toURI());
            File[] inside=packDir.listFiles(file->file.getName().endsWith(".class"));
            List<Class> lists = new ArrayList<>();
            for(File f : inside){
                    String c =packages+"."+f.getName().substring(0,f.getName().lastIndexOf("."));
                    lists.add(Class.forName(c));
            }
            for(Class c:lists){
                Method[] methods = c.getDeclaredMethods();
                for(Method m : methods){
                    if(m.isAnnotationPresent(Annotation.class)){
                        Annotation url = m.getAnnotation(Annotation.class);
                        if(!url.Url().isEmpty() && url.Url() !=null){
                            Mapping map = new Mapping(c.getName() , m.getName());
                            this.MappingUrls.put(url.Url(),map);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}