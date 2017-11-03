package com.redhat.consulting.tutorials.jdgshortlab;

import com.redhat.consulting.tutorials.jdgshortlab.model.Beer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class DatabaseDataBuilder {

    @Inject
    DatabaseBeerService databaseBeerService;

    public void createDatabaseEntries(){

        for (int i=20000; i< 40000; i++) {
            //String sql = "insert into beer (id,name,abv,ibu,description,style) values (" + i + ",'dunkelganger " + i + "',5.6,6,'a dark beer','dunkel');";

            Beer newbeer = new Beer();
            newbeer.setName("White Moon " + i);
            newbeer.setId(i);
            newbeer.setAbv(3.4);
            newbeer.setIbu(6);
            newbeer.setDescription("A wheat beer.");
            newbeer.setStyle("Belgian wheat.");
            databaseBeerService.addBeer(newbeer);
        }
    }

}
