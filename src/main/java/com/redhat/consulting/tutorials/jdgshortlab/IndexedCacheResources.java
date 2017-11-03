package com.redhat.consulting.tutorials.jdgshortlab;

import com.redhat.consulting.tutorials.jdgshortlab.model.Beer;
import org.hibernate.search.cfg.SearchMapping;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.cache.Index;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.lang.annotation.ElementType;
import java.util.Properties;

@ApplicationScoped
class IndexedCacheResources {

    private DefaultCacheManager indexedCacheManager;

    private DefaultCacheManager getIndexedCacheManager(){
        if(indexedCacheManager ==null){

            GlobalConfiguration globalConfiguration = new GlobalConfigurationBuilder()
                    .globalJmxStatistics().enable()
                    .jmxDomain("indexed")
                    .build();

            SearchMapping mapping = new SearchMapping();
            mapping.entity(Beer.class).indexed().providedId()
                    .property("ibu", ElementType.FIELD).field()
                    .property("description", ElementType.FIELD).field();

            Properties properties = new Properties();
            properties.put(org.hibernate.search.cfg.Environment.MODEL_MAPPING, mapping);

            Configuration configuration = new ConfigurationBuilder()
                    .indexing()
                        .index(Index.LOCAL)
                        .withProperties(properties)
                    .build();

            indexedCacheManager = new DefaultCacheManager(globalConfiguration,configuration);
        }
        return indexedCacheManager;
    }

    @PreDestroy
    private void cleanup() {
        if (indexedCacheManager != null)
            indexedCacheManager.stop();
    }

    @Produces
    @IndexedBeerCache
    public Cache<String,Beer> indexedBeerCacheProducer(){
        return getIndexedCacheManager().getCache("indexedBeer");
    }


}