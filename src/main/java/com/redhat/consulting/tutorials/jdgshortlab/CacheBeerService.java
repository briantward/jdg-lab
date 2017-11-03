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
import org.apache.lucene.search.Query;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.infinispan.Cache;
import org.infinispan.query.Search;
import org.infinispan.query.SearchManager;
import org.infinispan.query.dsl.QueryFactory;
import org.infinispan.query.dsl.SortOrder;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Brian Ward <bward@redhat.com>
 */

public class CacheBeerService implements BeerService {
    @Inject
    @BeerCache
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
        QueryFactory queryFactory = Search.getQueryFactory(cache);
        org.infinispan.query.dsl.QueryBuilder infinispanQueryBuilder =
                queryFactory.from(Beer.class);

        if(desc){
            infinispanQueryBuilder.orderBy("id", SortOrder.DESC);
        }
        org.infinispan.query.dsl.Query infinispanQuery = infinispanQueryBuilder.build();
        return infinispanQuery.list();
    }

    public List<Beer> getBeerByFuzzyMatchDescription(String description) {
        return null;
    }

    public List<Beer> getBeerByWildcardDescription(String description){
        QueryFactory queryFactory = Search.getQueryFactory(cache);
        org.infinispan.query.dsl.Query infinispanQuery =
                queryFactory.from(Beer.class)
                        .having("description")
                        .like("%" + description + "%")
                        .build();
        return infinispanQuery.list();
    }

    public List<Beer> getBeerByName() {
        throw new UnsupportedOperationException();
    }

    public List<Beer> getBeerByWildcardName() {
        throw new UnsupportedOperationException();
    }

    public List<Beer> getBeerByFuzzyMatchBrewery() {
        throw new UnsupportedOperationException();
    }


}
