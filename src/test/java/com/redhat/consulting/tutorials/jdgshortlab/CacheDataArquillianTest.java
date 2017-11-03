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
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.Collection;

/**
 * @author Brian Ward <bward@redhat.com>
 */

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
