# Spring Cloud Stream ETL Demo

## Setting up your environment
This demo requires [PCF Dev](https://docs.pivotal.io/pcf-dev/index.html) and [Apache Geode](https://geode.apache.org/docs/guide/12/getting_started/installation/install_standalone.html#concept_0129F6A1D0EB42C4A3D24861AF2C5425__section_D3326496B2BB47A7AB0CFC1A5E266842). 

### Starting PCF Dev
1. Start CF
    ```
    cf dev start
    ```
1. Login to CF 
    ```
    cf login -a https://api.local.pcfdev.io --skip-ssl-validation
    ``` 
1. Create MySQL and RabbitMQ services
    ```
    cf create-service p-mysql 512mb mysql-dev
    cf create-service p-rabbitmq standard rabbitmq-dev 
    ```
1. Push `jdbc-event-source-app`, `jdbc-event-processor-app`, and `geode-sink-app`
    ```
    cf push -f geode-sink-app/manifest-dev.yml
    cf push -f jdbc-event-processor-app/manifest-dev.yml
    cf push -f jdbc-event-source-app/manifest-dev.yml
    ```
    
### Starting Apache Geode
1. [Open `gfsh` terminal](#apache-geode-commands)
1. Start locator and server from `gfsh`
    ```
    start locator --name=locator
    configure pdx --auto-serializable-classes=com\.example\.springoneplatform\.scs\.demo\.model\.pdx\.* --read-serialized=true
    start server --name=server
    ```
1. Create regions
    ```
    create region --name=customer --type=PARTITION --skip-if-exists
    create region --name=customerOrder --type=PARTITION --skip-if-exists
    create region --name=item --type=PARTITION --skip-if-exists
    ```

## Running the demo
### Insert initial data
1. Run [schema.sql](./demo-steps/mysql/schema.sql) to create tables
1. Run [data.sql](./demo-steps/mysql/data.sql) to insert `CUSTOMER`, `ITEM`, `CUSTOMER_ORDERS`, and `ORDER_ITEM` rows
1. Start a `gfsh` terminal and connect to the locator
    ```
    connect
    ```
1. Query the regions to see that the data was extracted from the MySQL database, transformed to the proper types, and 
loaded into Geode
    ```
    query --query="select * from /customer"
    query --query="select * from /customerOrder"
    query --query="select * from /item"
    ```

### Trigger a data change
1. Run [trigger.sql](./demo-steps/mysql/trigger.sql) to create the triggers that will insert `DB_EVENT` rows 
when data in other tables is inserted, updated, or deleted
1. Run [delta-data.sql](./demo-steps/mysql/delta-data.sql) to do the following:
    1. `DELETE` an `ORDER_ITEM` from `order1`
    1. `DELETE` a `CUSTOMER_ORDER` from `customer1` (will `DELETE` all `ORDER_ITEM` rows for `order2` first)
1. Query the regions again to see that the data that was deleted from the MySQL database was also deleted from Geode
    ```
    query --query="select * from /customer"
    query --query="select * from /customerOrder"
    query --query="select * from /item"
    ```
---
---

## Additional Information
### Apache Geode Commands
In order to run `gfsh` commands you need to open a `gfsh` terminal. It's recommended that you create a working 
directory that you open `gfsh` from because starting locators and servers creates directories and files.
```
mkdir tmp && cd tmp
gfsh
```

Once in the `gfsh` terminal:
- `connect` connects the gfsh session to the cluster
- `list members` displays all members (locators, servers) that are part of the cluster
- `list regions` displays all regions that have been created
- `query --query="<oql here>"` executes a query ([OQL help](http://geode.apache.org/docs/guide/13/developing/querying_basics/query_basics.html))