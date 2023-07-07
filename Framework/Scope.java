package etu002015.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Scope {
    public String name() default""; 
}