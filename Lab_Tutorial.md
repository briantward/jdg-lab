# JBoss Data Grid / Infinispan Tech Talk
## Lab Tutorial

### Set up dependencies
1. Unzip EAP server
    ```
    unzip jboss-eap-7.0.0.zip
    ```

2. Unzip JDG library and move overtop EAP server modules.
    ```
    unzip jboss-datagrid-7.1.0-eap-modules-library.zip
    mv jboss-datagrid-7.1.0-eap-modules-library/modules/* jboss-eap-7.0/modules/
    ```

3. Update you server directory in the arquillian.xml file
    ```
    <property name="jbossHome">/PATH/TO/jboss-eap-7.0</property>
    ```

4. Build or package (skips tests because you need a container)
    ```
    mvn clean package
    ```

5. Run all tests:
    ```
    mvn clean test -Parq-tests
    ```

### Overview
1. Create the cache configuration and container objects.
    - GlobalConfigurationBuilder & GlobalConfiguration - establish basic JMX statistics
    - ConfigurationBuilder & Configuration - creates the configuration from specifications you provide.
    - EmbeddedCacheManager - interface to create a CacheContainer given a configuration.

    A typical setup may be like so:

    ```
    GlobalConfiguration globalConfig = new GlobalConfigurationBuilder()
        .globalJmxStatistics()
        .enable()
        .cacheManagerName("myCacheManager")
        .build();

    Configuration config = new ConfigurationBuilder()
        .jmxStatistics().enable()
        .persistence().addSingleFileStore.purgeOnStartup(true)
        .build();

    EmbeddedCacheManager cacheManager = new DefaultCacheManager(globalConfig, config);
    Cache<String,Integer> myDefaultCache = cacheManager.getCache(); // default cache
    Cache<String,Integer> myCache = cacheManager.getCache("aCache"); // named cache
    ```

2. The cache object is an implementation of a ConcurrentMap and so behaves like one:

    ```
    Cache<String,String> cache = cacheManager.getCache();

    String oldValue = cache.put("key","newvalue"); // returns null if not present

    cache.putIfAbsent("key","value"); // ONLY stores value if not present

    String value = cache.get("key");

    int size = cache.size();

    boolean empty = cache.isEmpty();

    boolean containsSomething = cache.containsKey("something");

    String value = cache.remove("key"); // if null, nothing present to remove

    String oldValue = cache.replace("key","newValue");

    boolean replacedExactMatch = cache.replace("key","oldValue","newValue");
    ```

3. Cache Persistence

    Cache Stores
        - file
        - remote cache
        - JDBC
        - JPA
        - LevelDB

4. Finding entries in the cache
     - [Lucene Query](https://access.redhat.com/documentation/en-us/red_hat_jboss_data_grid/7.1/html-single/developer_guide/#querying).  Indexing is required and the cache must be an embedded type.
     - [Infinispan Query](https://access.redhat.com/documentation/en-us/red_hat_jboss_data_grid/7.1/html-single/developer_guide/#the_infinispan_query_dsl). May be used in either embedded or remote type caches, BUT
     they are implemented differently (using different class imports).
     - [Ickle Query](https://access.redhat.com/documentation/en-us/red_hat_jboss_data_grid/7.1/html-single/developer_guide/#building_ickle_query)
     - [Remote Querying](https://access.redhat.com/documentation/en-us/red_hat_jboss_data_grid/7.1/html-single/developer_guide/#remote_querying)
     - [Query Comparison](https://access.redhat.com/documentation/en-us/red_hat_jboss_data_grid/7.1/html-single/developer_guide/#querying_comparison)
     - [Streams](https://access.redhat.com/documentation/en-us/red_hat_jboss_data_grid/7.1/html-single/developer_guide/#streams)

     Indexing causes querying to perform better at the cost of slower
     cache operations such as adding, removing, and updating entries.
     Objects must be annotated with @Indexed at the class level and @Field
     at the field level.

    Start a Lucene search:
    ```
    Search.getSearchManager(getCache());
    ```

    Start an Infinispan search:
    ```
    Search.getQueryFactory(getCache());
    ```

    Lucene sorting:
    ```
    Sort sort = new Sort(new SortField("id", SortField.Type.LONG));
    cq.sort(sort); //cq was a CacheQuery wrapper of a Lucene query
    List<Object> results = cq.list();  //Note that it returns Object
    ```

    Lucene API query to get all items
    ```
    public List<Beer> getAllBeers(boolean desc) {
        SearchManager searchManager = Search.getSearchManager(cache);
        QueryBuilder qb = searchManager.buildQueryBuilderForClass(Beer.class).get();
        Query query = qb.all().createQuery();
        CacheQuery cq = searchManager.getQuery(query); //cq is a CacheQuery wrapper of a Lucene query
        if(desc){
            Sort sort = new Sort(new SortField("id", SortField.Type.LONG));
            cq.sort(sort);
        }
        List<Beer> result = (List<Beer>)(List)cq.list();
        return result;
    }
    ```

    Infinispan API query to get all items
    ```
    public List<Beer> getAllBeers(boolean desc) {
        QueryFactory queryFactory = Search.getQueryFactory(cache);
        org.infinispan.query.dsl.QueryBuilder infinispanQueryBuilder =
                queryFactory.from(Beer.class);

        if(desc){
            infinispanQueryBuilder.orderBy("id", SortOrder.DESC);
        }
        org.infinispan.query.dsl.Query infinispanQuery = infinispanQueryBuilder.build();
        return infinispanQuery.list();
    }
    ```

    Lucene API query to get all items between some date and now.
    ```
    public List<Beer> getBeerByIbuBetween(double low, double high){
        SearchManager searchManager = Search.getSearchManager(cache);
        QueryBuilder qb = searchManager.buildQueryBuilderForClass(Beer.class).get();
        Query query = qb.bool()
                .must( qb.range().onField("ibu").above(low).createQuery())
                .must( qb.range().onField("ibu").below(high).createQuery())
                .createQuery();
        List<Beer> result = (List<Beer>)(List)searchManager.getQuery(query, Beer.class).list();
        return result;
    }
    ```

    Infinispan API query to get all items between some date and now.
    ```
    public List<Beer> getBeerByIbuBetweenIQ(double low, double high){
        QueryFactory queryFactory = Search.getQueryFactory(cache);
        org.infinispan.query.dsl.Query infinispanQuery =
                queryFactory.from(Beer.class)
                        .orderBy("ibu", SortOrder.ASC)
                        .having("ibu")
                        .between(low,high)
                        .build();
        return infinispanQuery.list();
    }
    ```

    Lucene Fuzzy search
    ```
    public List<Beer> getBeerByFuzzyMatch(String description){
        SearchManager searchManager = Search.getSearchManager(cache);
        QueryBuilder qb = searchManager.buildQueryBuilderForClass(Beer.class).get();
        Query query = qb.keyword()
                .fuzzy()
                .withPrefixLength(1)
                .onField("description")
                .matching(description)
                .createQuery();
        List<Beer> items = (List<Beer>)(List)searchManager.getQuery(query, Beer.class).list();
        return items;
    }
    ```
    Lucene Wildcard search
    ```
    public List<Beer> getBeerByWildcard(String description){
        SearchManager searchManager = Search.getSearchManager(cache);
        QueryBuilder qb = searchManager.buildQueryBuilderForClass(Beer.class).get();
        Query query = qb.keyword()
                .wildcard()
                .onField("description")
                .matching(description + "*")
                .createQuery();
        List<Beer> items = (List<Beer>)(List)searchManager.getQuery(query, Beer.class).list();
        return items;
    }
    ```
