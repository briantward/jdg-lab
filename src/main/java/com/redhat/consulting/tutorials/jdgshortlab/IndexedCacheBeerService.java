package com.redhat.consulting.tutorials.jdgshortlab;

import com.redhat.consulting.tutorials.jdgshortlab.model.Beer;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.infinispan.Cache;
import org.infinispan.query.CacheQuery;
import org.infinispan.query.Search;
import org.infinispan.query.SearchManager;
import org.infinispan.query.dsl.QueryFactory;
import org.infinispan.query.dsl.SortOrder;

import javax.inject.Inject;
import java.util.List;

public class IndexedCacheBeerService implements BeerService {
    @Inject
    @IndexedBeerCache
    Cache<String,Beer> cache;

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

    public List<Beer> getBeerByFuzzyMatchDescription(String description){
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

    public List<Beer> getBeerByWildcardDescription(String description){
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

    public List<Beer> getBeerByName(){
        throw new UnsupportedOperationException();
    }

    public List<Beer> getBeerByWildcardName(){
        throw new UnsupportedOperationException();
    }

    public List<Beer> getBeerByFuzzyMatchBrewery(){
        throw new UnsupportedOperationException();
    }
}
