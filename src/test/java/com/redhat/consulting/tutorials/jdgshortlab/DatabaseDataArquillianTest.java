package com.redhat.consulting.tutorials.jdgshortlab;

import com.redhat.consulting.tutorials.jdgshortlab.model.Beer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RunWith(Arquillian.class)
public class DatabaseDataArquillianTest {

    @Deployment
    public static Archive<?> createTestArchive() {
        return ArquillianDeployment.baseDeployment()
                .addClasses(DatabaseBeerService.class, DatabaseDataBuilder.class)
                .addAsResource( "META-INF/persistence.xml")
                .addAsResource("import.sql")
                .addAsWebInfResource("test-ds.xml");
    }

    @Inject
    DatabaseBeerService databaseBeerService;

    @PersistenceContext(unitName = "beerPU")
    EntityManager entityManager;

    @Inject
    DatabaseDataBuilder databaseDataBuilder;

    @Test
    public void timeGetAllQuery(){
        databaseDataBuilder.createDatabaseEntries();

        long start = System.nanoTime();
        List<Beer> results = entityManager.createQuery("from Beer").getResultList();
        long time = System.nanoTime() - start;
        System.out.println("DatabaseDataBuilder Time to execute in nanoseconds: " + time);
        System.out.println("DatabaseDataBuilder Time to execute in milliseconds: " + time/1000000);
        System.out.println("DatabaseDataBuilder Time to execute in seconds: " + time/1000000000);

/*        for (Beer beer : results){
            System.out.println(beer);
        }*/
    }

}
