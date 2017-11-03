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
