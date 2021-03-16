package com.devoteam.skillshapes.annotations;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SearchableEntity {
    String value();
}
