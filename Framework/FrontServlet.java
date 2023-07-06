package etu002015.framework.servlet;

import java.util.*;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

// import java.util.Map;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import etu002015.framework.Mapping;
import etu002015.framework.ModelView;
import etu002015.framework.annotation.*;

import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpRequest;
// import java.text.DateFormat.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.*;
import java.lang.reflect.*;

public class FrontServlet extends HttpServlet {

    HashMap<String, Mapping> MappingUrls;

    public void init() {
        try {
            MappingUrls = getAllHashMap("etu002015.model");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // maka anle mapping
    private Mapping getMappingUrls(String key) {
        Mapping mapping = MappingUrls.get(key);
        return mapping;
    }

    // fonction pour obtenir toutes les classes d'un package donné
    public static List<Class<?>> obtenirClasses(String packageName) throws ClassNotFoundException, IOException, URISyntaxException {
        List<Class<?>> classes = new ArrayList<>();
        String path = packageName.replaceAll("[.]", "\\\\");
        Enumeration<java.net.URL> resources = Thread.currentThread().getContextClassLoader().getResources(path);
        while (resources.hasMoreElements()) {
            java.net.URL resource = resources.nextElement();
            File directory = new File(resource.getFile().replaceAll("%20", " "));
            if (directory.exists()) {
                File[] files = directory.listFiles();
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".class")) {
                        String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                        Class<?> clazz = Class.forName(className);
                        // if (clazz.getPackage().getName().equals(packageName)) {
                        classes.add(clazz);
                        // }
                    }
                }
            }
        }
        return classes;
    }

    // ici permet de completer la variable mappingurls (prend les objets d'un
    // package par méthode et les mets dans une hashmap
    public static HashMap<String, Mapping> getAllHashMap(String packageName) throws ClassNotFoundException,
            UnsupportedEncodingException, IOException, SAXException, ParserConfigurationException, URISyntaxException {
        HashMap<String, Mapping> hash = new HashMap<>();
        List<Class<?>> classes = obtenirClasses(packageName);
        for (Class cls : classes) {
            System.out.println("Class: " + cls.getName());
            Method[] methods = cls.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Annotation.class)) {
                    Annotation annotation = method.getDeclaredAnnotation(Annotation.class);
                    if (!"".equals(annotation.Url())) {
                        String url = annotation.Url();
                        String classname = cls.getSimpleName();
                        String nommethod = method.getName();
                        Mapping map = new Mapping(classname, nommethod);
                        hash.put(url, map);
                    }
                }
            }
        }
        return hash;
    }

    // maka anle classe
    private Class getClass(Mapping mapping) throws ClassNotFoundException {
        String className = "etu002015.model." + mapping.getClassName();
        return Class.forName(className);
    }

    // mdispatch anle modelview
    public void loadView(ModelView modelview, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try {
            if (modelview != null) {
                if (modelview.getData().size() != 0) {
                    for (Map.Entry<String, Object> entry : modelview.getData().entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        request.setAttribute(key, value);
                    }
                }
                RequestDispatcher dispatch = request.getRequestDispatcher(modelview.getUrl());
               // request.setAttribute(, modelview.getDate());
                 dispatch.forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace(out);
            out.println(e);
            throw new ServletException(e);
        }
    }

    // maka anle url modelview ho dispacthena
    public void getUrlModelView(String key, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try {
            out.println("135");
            Enumeration<String> parameterNames = request.getParameterNames();
            // maka anle mapping amn alalany key(cle anle hashmap)
            Mapping mapping = getMappingUrls(key);
            // String className = "etu002015.model." + mapping.getClassName();
            Class<?> classe = getClass(mapping);
            Object obj = classe.getConstructor().newInstance();
            Method[] methode = classe.getDeclaredMethods();
            Method methodToInvoke = null;

            // rehefa mapitovy ny anaranle methode
            for (Method method : methode) {
                if (method.getName().equals(mapping.getMethod())) {
                    methodToInvoke = method;
                    break;
                }
            }
            setvaluefromform(obj, request);
            // jerena ny type de retour anle Mapping(key)
            if (methodToInvoke.getReturnType() == ModelView.class) {
                Object resultat;
                Class<?>[] parameterTypes = methodToInvoke.getParameterTypes();
                // Parameter[] parameters = methodToInvoke.getParameters();
                Object[] paramValues = new Object[parameterTypes.length];
                int paramCount = methodToInvoke.getParameterCount();

                if (paramCount == 0) {
                    obj = methodToInvoke.invoke(obj, (Object[]) null);
                } else {
                    obj = methodToInvoke.invoke(obj, paramValues);
                }

                    loadView((ModelView) obj, request, response);

            }

        } catch (Exception e) {
            out.println(e);
        }
    }

    // maka anle url sur navigation
    private String getUrlArray(HttpServletRequest request) {
        return request.getRequestURI().substring(request.getContextPath().length() + 1);
    }
    // mamadika input ho majuscule ny lettre voloha
    private String capitalizeFirstLetter(String input) {
        char firstChar = Character.toUpperCase(input.charAt(0));
        return firstChar + input.substring(1);
    }
    // micaster element de type "objet"
    private <T> T castElement(String element, Class<T> objet) {
        if (element == null || objet.isAssignableFrom(String.class)) {
            return objet.cast(element);
        } else if (objet.equals(Integer.class) || objet.equals(int.class)) {
            return objet.cast(Integer.valueOf(element));
        } else if (objet.equals(Long.class) || objet.equals(long.class)) {
            return objet.cast(Long.valueOf(element));
        } else if (objet.equals(Float.class) || objet.equals(float.class)) {
            return objet.cast(Float.valueOf(element));
        } else if (objet.equals(Double.class) || objet.equals(double.class)) {
            return objet.cast(Double.valueOf(element));
        } else if (objet.equals(Boolean.class) || objet.equals(boolean.class)) {
            return objet.cast(Boolean.valueOf(element));
        } else if (objet.equals(Character.class) || objet.equals(char.class)) {
            return objet.cast(element.charAt(0));
        } else if (objet.equals(Date.class)) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                return objet.cast(dateFormat.parse(element));
            } catch (ParseException e) {
                return null;
            }
        } else if (objet.equals(LocalDate.class)) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                return objet.cast(LocalDate.parse(element, formatter));
            } catch (Exception e) {
                return null;
            }
        }

        return null;
    }

    //maka valeur anaty formulaire
    public void setvaluefromform(Object object, HttpServletRequest request) throws Exception{
        //maka ny atribut
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields){
            String var = request.getParameter(field.getName());
            //maka setter
            String setter = "set" + capitalizeFirstLetter(field.getName());
            Method mset = object.getClass().getDeclaredMethod(setter, field.getType());
            Object objc = castElement(var, field.getType());
            mset.invoke(object, objc);
        }
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        PrintWriter out = response.getWriter();
        // out.println(request.getHttpServletMapping().getMatchValue());
        // maka ny anatinle annotation @Url("key") sy mapping rehetra anaty MappingUrls
        for (Map.Entry<String, Mapping> entry : MappingUrls.entrySet()) {
            out.println(entry.getKey() + " " + entry.getValue().getClassName() + " " + entry.getValue().getMethod());
        }
        
        //setvaluefromform(out, null, null);
        //
            String url = getUrlArray(request);
            out.print(this.MappingUrls);
            Mapping mapping = getMappingUrls(url);
            
                out.println("nety aloha");
                getUrlModelView(url, request, response);
        
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
                PrintWriter out = response.getWriter();
                out.println("post");
        processRequest(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
                PrintWriter out = response.getWriter();
                out.println("post");
        processRequest(request, response);
    }




}