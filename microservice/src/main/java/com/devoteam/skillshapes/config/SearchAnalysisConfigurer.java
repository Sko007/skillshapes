package com.devoteam.skillshapes.config;

import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurationContext;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurer;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

@Dependent
@Named("SkillShapesAnalysisConfigurer")
public class SearchAnalysisConfigurer implements ElasticsearchAnalysisConfigurer {

    @Override
    public void configure(ElasticsearchAnalysisConfigurationContext context){
        context.analyzer("name").custom()
            .tokenizer("standard")
            .tokenFilters("asciifolding","lowercase");

        context.analyzer("english").custom()
            .tokenizer("standard")
            .tokenFilters("asciifolding", "lowercase", "porter_stem");

        context.normalizer("sort").custom()
            .tokenFilters("asciifolding","lowercase");
    }
}
