package com.redhat.consulting.tutorials.jdgshortlab;

import com.redhat.consulting.tutorials.jdgshortlab.model.Beer;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class CacheResources {
    private DefaultCacheManager cacheManager;

    private DefaultCacheManager getCacheManager(){
        if(cacheManager ==null){

            Configuration configuration = new ConfigurationBuilder()
                    .jmxStatistics().enable()
                    .build();

            cacheManager = new DefaultCacheManager(configuration);
        }
        return cacheManager;
    }

    public Cache<String,String> getMyCacheProvider(){
        return getCacheManager().getCache();
    }

    @PreDestroy
    private void cleanup() {
        if (cacheManager != null)
            cacheManager.stop();
    }

    @Produces
    @BeerCache
    public Cache<String,Beer> beerCacheProducer(){
        return getCacheManager().getCache();
    }

}
