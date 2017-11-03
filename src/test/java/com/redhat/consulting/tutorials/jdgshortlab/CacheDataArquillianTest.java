package com.redhat.consulting.tutorials.jdgshortlab;


import com.redhat.consulting.tutorials.jdgshortlab.model.Beer;
import org.infinispan.Cache;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.Collection;

@RunWith(Arquillian.class)
public class CacheDataArquillianTest {

    @Deployment
    public static Archive<?> createTestArchive() {
        return ArquillianDeployment.baseDeployment()
                .addClasses(
                        CacheDataBuilder.class,
                        CacheResources.class,
                        BeerCache.class,
                        CacheBeerService.class);
    }

    @Inject
    @BeerCache
    Cache<String,Beer> cache;

    @Inject
    CacheDataBuilder cacheDataBuilder;

    @Test
    public void timeGetAllQuery(){

        cacheDataBuilder.createCacheEntries(40000);

        long start = System.nanoTime();
        Collection<Beer> results = cache.values();
        long time = System.nanoTime() - start;
        System.out.println("Cache Time to execute in nanoseconds: " + time);
        System.out.println("Cache Time to execute in milliseconds: " + time/1000000);
        System.out.println("Cache Time to execute in seconds: " + time/1000000000);

/*        for (Beer beer : results){
            System.out.println(beer);
        }*/
    }
}
