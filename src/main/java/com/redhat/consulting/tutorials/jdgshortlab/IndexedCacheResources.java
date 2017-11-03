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

/**
 * @author Brian Ward <bward@redhat.com>
 */

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