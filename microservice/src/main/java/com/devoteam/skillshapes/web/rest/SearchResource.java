package com.devoteam.skillshapes.web.rest;

import com.devoteam.skillshapes.annotations.SearchableEntity;
import com.devoteam.skillshapes.domain.UserProfile;
import io.quarkus.hibernate.orm.panache.Panache;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.StartupEvent;
import org.hibernate.search.mapper.orm.session.SearchSession;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

public class SearchResource {

    @Inject
    SearchSession searchSession;

    @Transactional
    void onStart(@Observes StartupEvent ev) throws Exception{

        Class entity = getEntityClass();
        System.out.println(entity);
        if(entity != null){
            Method count = entity.getMethod("count");
            if (count != null) {
                System.out.println("count exists");
                Object c = count.invoke(entity);
                System.out.println(c);
                if((Long)c > 0){
                    System.out.println("Start Mass Indexer");
                    searchSession.massIndexer().startAndWait();
                }
            }
        }
    }

    @GET
    @Path("/search")
    @Transactional
    public List<Object> search(@QueryParam String pattern,
                               @QueryParam Optional<Integer> size) throws Exception{
        Class entity = getEntityClass();
        List<String> fields = new ArrayList<>(1);
        for(Field field : entity.getFields()){
            if(field.getAnnotationsByType(FullTextField.class).length > 0) fields.add(field.getName());
        }

        /*return searchSession.search(entity)
            .where(f ->
                pattern == null || pattern.trim().isEmpty() ?
                    f.matchAll() :
                    f.simpleQueryString()
                        .fields("firstName", "lastName", "books.title").matching(pattern)
            )
            .sort(f -> f.field("lastName_sort").then().field("firstName_sort"))
            .fetchHits(size.orElse(20)); */
        return null;
    }

    private Class<?> getEntityClass() {
       try{
        Class<?> resource = this.getClass();
           for(Annotation annotation : resource.getAnnotations()){
            if(annotation instanceof SearchableEntity){
                SearchableEntity entity = (SearchableEntity) annotation;
                return   Class.forName(entity.value());
            }
        }

       }
       catch (Exception e){
           e.printStackTrace();
       }
       return null;
    }
}
