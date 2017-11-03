# JBoss Data Grid / Infinispan Tech Talk

# Slide Deck: Why Data Grid?
 - https://redhat.slides.com/bward/jdg-tt/

# A Basic App in Need of Data Grid
 - flight records
 - stock tickers
 - car mart

# Library Mode Configuration

# Query the Data Grid
 - How does it compare to searching JBDC or Hibernate / traditional EntityManager?
 - Lucene Query
 - Inifispan Query
 - MapReduce Query

# Other topics for consideration not included in this tech talk
 - Remote Server mode and Remote Querying
 - Security
 - Clustering
 - Eviction and Expiration
 - Persistence
 - Cache Integrity

# Competitors: [Top 15 In Memory Data Grid Platform](https://www.predictiveanalyticstoday.com/top-memory-data-grid-applications/)
 - Hazelcast IMDG
 - GridGain IMDG (Apache Ignite)
 - Oracle Coherence
 - Pivotal GemFire XD (Apache Geode)
 - IBM WebSphere Application Server / eXtreme Scale
 - XAP
 - Terracotta Enterprise Suite (Ehcache)


## Links

[Red Hat JBoss Data Grid 7.1 API](https://access.redhat.com/webassets/avalon/d/red-hat-jboss-data-grid/7.1/api/index.html) (based on Infinispan 8.4.0)

[Infinispan 8.2.8 API](https://docs.jboss.org/infinispan/8.2/apidocs/)

[Red Hat JBoss Data Grid](https://access.redhat.com/products/red-hat-jboss-data-grid/)

[Red Hat JBoss Data Grid Documentation](https://access.redhat.com/documentation/en/red-hat-jboss-data-grid/)

[Infinispan Community Project](http://www.infinispan.org)

[Infinispan Source Code](https://github.com/infinispan)

[infinispan-simple-tutorials](https://github.com/infinispan/infinispan-simple-tutorials)

[infinispan-quickstart](https://github.com/infinispan/infinispan-quickstart)

[Red Hat Middleware Training Labs for JBoss Data Grid](https://github.com/gpe-mw-training/jdg-labs)

[Red Hat JBoss Data Grid Quickstarts](https://github.com/jboss-developer/jboss-jdg-quickstarts)

Be sure to checkout the latest GA (tag) to get it to build correctly.  Other working branches expect older and sometimes unpublished dependency builds.  Alternatively you can download the sources zipped from access.redhat.com.

```
$ git clone git@github.com:jboss-developer/jboss-jdg-quickstarts.git
(or)
$ git clone https://github.com/jboss-developer/jboss-jdg-quickstarts.git

$ cd jboss-jdg-quickstarts
$ git checkout JDG_7.1.0.GA
```

[Red Hat JBoss Data Grid Tech Talk](https://github.com/selrahal/infinispan-lab)

[Simple JDG 7.0 Labs](https://github.com/vchintal/simple-jdg-labs)

[Red Hat Certificate of Expertise in Fast-Cache Application Development](https://www.redhat.com/en/services/certification/rhcoe-fast-cache-application-development)

[Red Hat Certificate of Expertise in Fast-Cache Application Development exam](https://www.redhat.com/en/services/training/ex453-red-hat-certificate-expertise-fast-cache-application-development-exam)

[Jags Ramnarayan on In-Memory Data Grids](https://www.infoq.com/articles/in-memory-data-grids)
