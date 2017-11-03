package com.redhat.consulting.tutorials.jdgshortlab;


import org.infinispan.Cache;
import org.junit.Test;

import java.util.Map;

public class CacheTest {

    @Test
    public void doSomeStuff(){
        CacheResources cacheResources =  new CacheResources();
        Cache<String,String> cache = cacheResources.getMyCacheProvider();

        cache.put("A","vim");
        cache.put("B","go");

        for (Map.Entry<String,String> entry :  cache.entrySet()){
            System.out.println("Key: " + entry.getKey() + " , Value: " + entry.getValue());
        }
    }
}
