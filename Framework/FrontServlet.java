package etu002015.framework.servlet;

import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import etu002015.framework.Mapping;
import etu002015.framework.ModelView;
import etu002015.framework.annotation.*;

import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.RequestDispatcher;

import java.net.URL;
import java.net.http.HttpRequest;
import java.io.*;
import java.lang.reflect.*;

public class FrontServlet extends HttpServlet {

    HashMap<String,Mapping> MappingUrls;

    
    public void init(){
        try {
            super.init();
            String packages = "etu002015.model";
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
                            //mameno hashmap amn alalany url sy mapping
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //maka anle mapping
    private Mapping getMappingUrls(String key) {
        Mapping mapping = MappingUrls.get(key);
        return mapping;
    }

    //maka anle classe
    private Class getClass(Mapping mapping) throws ClassNotFoundException{
        String className = "etu002015.model." + mapping.getClassName();
        return Class.forName(className);
    }

    //mdispatch anle modelview
    public void loadView(ModelView modelview, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        PrintWriter out = response.getWriter();
        try {
            if(modelview != null){
                RequestDispatcher dispatch = request.getRequestDispatcher(modelview.getUrl());
                dispatch.forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace(out);
            out.println(e);
            throw new ServletException(e);
        }
    }

    // maka anle url modelview de dispacthena
    public void getUrlModelView(String key, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        PrintWriter out = response.getWriter();
        try {
            Enumeration<String> parameterNames = request.getParameterNames();
            Mapping mapping = getMappingUrls(key);
            // String className = "etu002015.model." + mapping.getClassName();
            Class<?> classe = getClass(mapping);
            Object obj = classe.getConstructor().newInstance();
            Method[] methode = classe.getDeclaredMethods();
            Method methodToInvoke = null;
            
            //rehefa mapitovy ny anaranle methode 
            for (Method method : methode){
                if(method.getName().equals(mapping.getMethod())){
                    methodToInvoke = method;
                    break;
                }
            }
            
            //jerena ny type de retour anle Mapping(key)
            if (methodToInvoke.getReturnType() == ModelView.class){
                Object resultat;
                Class<?>[] parameterTypes = methodToInvoke.getParameterTypes();
                //Parameter[] parameters = methodToInvoke.getParameters();
                Object[] paramValues = new Object[parameterTypes.length];
                int paramCount = methodToInvoke.getParameterCount();

                if(paramCount == 0){
                    resultat = methodToInvoke.invoke(obj, (Object[]) null);
                } else {
                    resultat = methodToInvoke.invoke(obj, paramValues);
                }

                if(resultat instanceof ModelView){
                    loadView((ModelView) resultat,request,response);
                }

            }

        } catch (Exception e) {
            e.printStackTrace(out);
            throw new ServletException(e);
        }
    }

    //maka anle url sur navigation
    private String getUrlArray(HttpServletRequest request){
        return request.getRequestURI().substring(request.getContextPath().length() + 1); 
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        PrintWriter out = response.getWriter();
        out.println(request.getHttpServletMapping().getMatchValue());
        //maka ny anatinle annotation @Url("key") sy mapping rehetra anaty MappingUrls 
        for (Map.Entry<String, Mapping> entry : MappingUrls.entrySet()) {
            out.println(entry.getKey() + " " + entry.getValue().getClassName() + " " + entry.getValue().getMethod());
        }
        //
        try {
            String url = getUrlArray(request);
            Mapping mapping = getMappingUrls(url);
            if (mapping != null) {
                getUrlModelView(url, request, response);
            }

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        processRequest(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        processRequest(request, response);
    }

}