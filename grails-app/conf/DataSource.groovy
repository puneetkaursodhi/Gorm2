dataSource {
    pooled = true
    driverClassName = "com.mysql.jdbc.Driver"
    dialect = org.hibernate.dialect.MySQL5InnoDBDialect
    username = "root"
    password = ""
    properties {
        maxActive = 40
        initialSize = 20
        minIdle = 5
        numTestsPerEvictionRun = 3
        testWhileIdle = true
        validationQuery = "SELECT 1"
        minEvictableIdleTimeMillis = (1000 * 60 * 5)
        timeBetweenEvictionRunsMillis = (1000 * 60 * 5)
    }
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
}
// environment specific settings
environments {

    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:mem:testDb;MVCC=TRUE"
        }
    }

    development {
        dataSource {
            username = "root"
            password = "igdefault"
            dbCreate = "create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''
            url = "jdbc:mysql://localhost:3306/gorm2?autoReconnect=true&useUnicode=yes&characterEncoding=UTF-8"
            logSql = true
        }
    }
    production {
        dataSource {
            dbCreate = "update"
            url = "jdbc:mysql://localhost:3306/gorm2"
            pooled = true
            properties {
                maxActive = -1
                minEvictableIdleTimeMillis = 1800000
                timeBetweenEvictionRunsMillis = 1800000
                numTestsPerEvictionRun = 3
                testOnBorrow = true
                testWhileIdle = true
                testOnReturn = true
                validationQuery = "SELECT 1"
            }
        }
    }
}
