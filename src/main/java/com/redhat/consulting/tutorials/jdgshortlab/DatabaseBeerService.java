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
