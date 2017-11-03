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

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author Brian Ward <bward@redhat.com>
 */

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
