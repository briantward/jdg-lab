package com.redhat.consulting.tutorials.jdgshortlab;

import com.redhat.consulting.tutorials.jdgshortlab.model.Beer;
import org.infinispan.Cache;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class IndexedCacheDataBuilder {

    @Inject
    @IndexedBeerCache
    Cache<String,Beer> indexedBeerCache;

    public void createCacheEntries(int volume){
        for (int i=20000; i< 20000+volume; i++) {
            Beer newbeer = new Beer();
            newbeer.setName("White Moon " + i);
            newbeer.setId(i);
            newbeer.setAbv(3.4);
            newbeer.setIbu(6);
            newbeer.setDescription("A wheat beer.");
            newbeer.setStyle("Belgian Wheat");
            indexedBeerCache.put(String.valueOf(newbeer.getId()),newbeer);
        }

        Beer newbeer = new Beer();
        newbeer.setName("Sharp IPA " + 10);
        newbeer.setId(10);
        newbeer.setAbv(7.7);
        newbeer.setIbu(11);
        newbeer.setDescription("A knockout bitter beer.");
        newbeer.setStyle("IPA");
        indexedBeerCache.put(String.valueOf(newbeer.getId()),newbeer);

    }

}
