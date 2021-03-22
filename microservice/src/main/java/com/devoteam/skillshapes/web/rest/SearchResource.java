package com.devoteam.skillshapes.web.rest;

import com.devoteam.skillshapes.annotations.SearchableEntity;
import com.devoteam.skillshapes.domain.UserProfile;
import io.quarkus.hibernate.orm.panache.Panache;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.StartupEvent;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.sort.dsl.FieldSortOptionsStep;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

import org.hibernate.search.engine.search.sort.dsl.SearchSortFactory;
import org.hibernate.search.engine.search.query.dsl.SearchQueryOptionsStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SearchResource {

    @Inject
    SearchSession searchSession;
    private List<String> fields;
    private List<String> sortFields;

    private final Logger log = LoggerFactory.getLogger(UserProfileResource.class);

    @Transactional
    void onStart(@Observes StartupEvent ev) throws Exception{
        Class entity = getEntityClass();

        if(entity != null){
            initializeLists(entity);
            Method count = entity.getMethod("count");
            if (count != null) {
                Object c = count.invoke(entity);
                if((Long)c > 0){
                    log.info("Elastic Search Mass Indexer started  "+entity);
                    searchSession.massIndexer().startAndWait();
                }
            }
        }
    }

    /**
     * Initialize fields and sortFields lists by iterating through the entity annotations
     * @param entity
     * @return boolean
     */
    private boolean initializeLists(Class entity) {
        try{
            if (entity != null) {
                fields = new ArrayList<>(1);
                sortFields = new ArrayList<>(1);

                for (Field field : entity.getDeclaredFields()) {
                    if (field.getAnnotationsByType(FullTextField.class).length > 0) {
                        fields.add(field.getName());
                    }
                    if (field.getAnnotationsByType(KeywordField.class).length > 0) {
                        Annotation annotation = field.getAnnotation(KeywordField.class);
                        String sortName = ((KeywordField) annotation).name();
                        sortFields.add(sortName);
                    }
                }
                return true;
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Search Endpoint for any Resource
     *
     * @param pattern
     * @param size
     * @return List<?>
     * @throws Exception
     */
    @GET
    @Path("/search")
    @Transactional
    public List<?> search(@QueryParam String pattern,
                               @QueryParam Optional<Integer> size) throws Exception{
        Class entity = getEntityClass();
        SearchQueryOptionsStep step =  searchSession.search(getEntityClass())
        .where(f ->
                pattern == null || pattern.trim().isEmpty() ?
                    f.matchAll() :
                    f.simpleQueryString()
                        .fields(fields.toArray(new String[0])).matching(pattern)
            );

        if(sortFields.size() > 0) step = step.sort(f -> { return sortInnerFields((SearchSortFactory)f, sortFields); });

        return step.fetchHits(size.orElse(20));
    }

    /**
     * Prepare sorting for entity fields
     * @param sf
     * @param sortFields
     * @return FieldSortOptionsStep
     */
    private FieldSortOptionsStep sortInnerFields(SearchSortFactory sf, List<String> sortFields){
        try{
            if(sortFields.size() >= 1){
                FieldSortOptionsStep step;
                Collections.reverse(sortFields);
                for(int i = 0; i <= sortFields.size()-2;i++) {
                    String fieldName = sortFields.get(i);
                    sf = sf.field(fieldName).then();
                }
                return sf.field(sortFields.get(sortFields.size()-1));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves the Entity to be searched for from the SearchableEntity Annotation
     * @return Class
     */
    private Class<?> getEntityClass() {
       try{
        Class<?> resource = this.getClass();
           for(Annotation annotation : resource.getAnnotations()){
            if(annotation instanceof SearchableEntity){
                SearchableEntity entity = (SearchableEntity) annotation;
                return  Class.forName(entity.value());
            }
        }
       }
       catch (Exception e){
           e.printStackTrace();
       }
       return null;
    }
}
