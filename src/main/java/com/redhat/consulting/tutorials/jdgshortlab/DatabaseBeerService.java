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

import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.redhat.consulting.tutorials.jdgshortlab.model.Beer;
import com.redhat.consulting.tutorials.jdgshortlab.model.QBeer;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Brian Ward <bward@redhat.com>
 */

@ApplicationScoped
public class DatabaseBeerService implements BeerService {

    @PersistenceContext(unitName = "beerPU")
    private EntityManager em;

    @Transactional
    public void addBeer(Beer beer){
        em.persist(beer);
    }

    public List<Beer> getBeerByWildcardDescription(String description){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Beer> criteriaQuery = cb.createQuery(Beer.class);
        Root<Beer> beerRoot = criteriaQuery.from(Beer.class);
        CriteriaQuery query = criteriaQuery.select(beerRoot)
                .having(cb.like(beerRoot.get("description"),description));
        TypedQuery<Beer> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

    public List<Beer> getBeerByWildcardDescriptionDSL(String description){
        QueryFactory<?> queryFactory = new JPAQueryFactory(em);
        JPAQuery query = new JPAQuery(em);
        QBeer beer = QBeer.beer;

        /*List<Beer> beerList = query.from(beer)
                .where(beer.description.like(description))
                .list()
                ;*/
        throw new UnsupportedOperationException();
    }

    public List<Beer> getBeerByIbuBetween(double low, double high) {
        throw new UnsupportedOperationException();
    }

    public List<Beer> getBeerByIbuBetweenIQ(double low, double high) {
        throw new UnsupportedOperationException();
    }

    public List<Beer> getAllBeers(boolean desc) {
        throw new UnsupportedOperationException();
    }

    public List<Beer> getBeerByFuzzyMatchDescription(String description) {
        throw new UnsupportedOperationException();
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
