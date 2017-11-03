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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Brian Ward <bward@redhat.com>
 */

@RunWith(Arquillian.class)
public class SearchArquillianTest {

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

    @Inject
    CacheBeerService beerService;

    private boolean dataInserted = false;

    @Before
    public void makeData(){
        if (!dataInserted) {
            cacheDataBuilder.createCacheEntries(20);
            dataInserted = true;
        }
    }

    @Test
    public void findThatBitterBeerByIbu(){

        Beer bitterBeer = cache.get("10");

        List<Beer> beersult  = beerService.getBeerByIbuBetweenIQ(8.0,13.0);

        Assert.assertEquals("List returned wrong number of data.",1,beersult.size());
        Assert.assertEquals("Could not find bitter beer!", bitterBeer,beersult.get(0));
        System.out.println("Found beer: " + beersult.get(0));
    }

    @Test
    public void findThatBitterBeerByDescription(){
        Beer bitterBeer = cache.get("10");

        List<Beer> beersult  = beerService.getBeerByWildcardDescription("bitter");

        Assert.assertEquals("List returned wrong number of data.",1,beersult.size());
        Assert.assertEquals("Could not find bitter beer!", bitterBeer,beersult.get(0));
        System.out.println("Found beer: " + beersult.get(0));

    }

    @Test
    public void getAllBeers(){

        List<Beer> beersult  = beerService.getAllBeers(false);

        Assert.assertEquals("List returned wrong number of data.",21,beersult.size());

    }

}
