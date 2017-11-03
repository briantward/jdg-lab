/*
 * MIT License
 *
 * Copyright (c) 2017 briantward
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.redhat.consulting.tutorials.jdgshortlab;

import com.redhat.consulting.tutorials.jdgshortlab.model.Beer;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * @author Brian Ward <bward@redhat.com>
 */

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
