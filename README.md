# Requirement
* Spring Framework/SpringBoot
* Spring Web Mvc
* Jdbc
* Bean Validation
* Jpa

# Spring Data JPA
Spring Data adalah adalah sebuah ekosistem di spring framework, yang tujuanya untuk mempermudah pegelolahan data di database.  
  
Ada banyak sekali ekosistem di spring data, misalnya :
* Spring Data JPA
* Spring Data LDAP
* Spring Data Redis
* Spring Data Elasticsearch
* Spring Data R2DBC
* dan masih banyak lagi

Untuk lebih detail nya bisa kunjungi disini https://spring.io/projects/spring-data/  
  
Penggunaan teknologi di ekosistem spring data cukup general, jadi sekali mempelajari salah satu ekosistem nya misalnya Spring data JPA maka saat kita ingin menggunakan ekosistem yang lain misalnya Spring Data Elasticsearch maka akan sangat mudah.  
Karena secara garis besar konsep dan penggunaanya sama.

Spring data JPA adalah sebuah bagian dari ekosistem Spring data project untuk mempermudah pengolahan data pada database menggunakan JPA. 
 
Dengan menggunakan Spring Data JPA Kita tidak perlu melakukan konfigurasi DataSource, JPA secara manual dan tentunya mempermudah untuk berkomunikasi dengan sistem basis data, untuk detail dari spring data jpa bisa kunjungi disini https://spring.io/projects/spring-data-jpa/

# Setup Project
Kalian bisa download spring project di https://start.spring.io/ dengan menambahkan dependency sebagai berikut :
* Spring Data Jpa
* JDBC
* Bean Validation
* Spring Web
* Lombok
* Mysql Driver
* SpringBoot dev tools

Untuk databasenya, disini saya akan mengunakan docker compose :  
``` yaml
version: '1.9'
services:
  mysql:
    image: mysql:latest
    ports:
      - 3306:3306
    environment:
      - MYSQL_ROOT_PASSWORD=secret_pass
    volumes:
      - mysql_volume:/var/lib/mysql
volumes:
  mysql_volume: {}
```

Untuk menjalankanya, jalankan perintah :
``` sh
docker compose -f docker-compose.yaml up -d
```
  
Membuat Database :
``` sh
docker exec -it spring-data-jpa-mysql-1 /bin/bash
```

``` sh
mysql -u root -p
```
setelah itu masukan password kalian (sesuai dengan password pada environtment di file docker-compose.yaml)  
``` sh
CREATE DATABASE spring_data_jpa;
```

# DataSorce config
Saat menggunakan SpringBoot atau Spring framework, kita tidak perlu lagi melakukan konfigurasi DataSource secara manual, misalnya :
* membuat class connection untuk membuat koneksi
* membuat persistance.xml untuk melakukan konfigurasi jpa
* menginstansiasi EntityManagerFactory
* dan sebagainya

By default sudah dikonfigurasikan oleh spring famework menggunakan class [`DataSourceAutoConfiguration`](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/autoconfigure/jdbc/DataSourceAutoConfiguration.html), dan untuk melakukan konfigurasi database kita bisa menggunakan `application.properties` atau `application.yaml` saja dengan prefix `spring.datasource`  
``` yaml
spring:
  datasource:
    # untuk menentukan driver yang digunakan
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: secret_pass
    url: jdbc:mysql://localhost:3306/spring_data_jpa
    # untuk menentukan database pooling yang digunakan
    type: com.zaxxer.hikari.HikariDataSource
    hikari:
      minimum-idle: 8
      maximum-pool-size: 15 
```
Untuk detail konfigurasinya bisa kunjungin disini https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html#application-properties.data.spring.datasource.dbcp2


# Jpa Configuration
untuk melakukan konfigurasi JPA, misalnya menampilkan sql, format sql, dan sebagainya kita tidak perlu lagi menggunakan `persistence.xml`.  
Kita bisa menggunakan `application.properties` atau `application.yaml` untuk melakukan konfigurasi jpa nya dengan prefix `spring.jpa`

``` yaml
spring:
  datasource:
    # untuk menentukan driver yang digunakan
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: secret_pass
    url: jdbc:mysql://localhost:3306/spring_data_jpa
    # untuk menentukan database pooling yang digunakan
    type: com.zaxxer.hikari.HikariDataSource
    hikari:
      minimum-idle: 8
      maximum-pool-size: 15 
# Konfigurasi Jpa
  jpa:
    properties:
      hibernate:
        show-sql: true
        format-sql: true
```

# EntityManagerFactory
EntityManagerFactory adalah sebuah object yang digunakan untuk berinteraksi dengan database, misalnya untuk melakukan :
* CRUD
* TRANSACTION
* CRITERIA QUERY
* dan sebagainya

Saat kita menggunakan JPA secara manual maka kita akan melakukan konfigurasi EntityManagerFactory secara manual juga, namun ketika kita menggunakan Spring Data Jpa maka  tidak perlu lagi melakukan konfigurasi secara manual, karena semuanya telah otomatis dikonfigurasikan oleh spring data jpa dengan menggunakan [`JpaHibernateAutoConfiguration`](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/autoconfigure/orm/jpa/HibernateJpaAutoConfiguration.html)

``` java
@SpringBootTest(classes = SpringDataJpaApplication.class)
public class SpringDataJpaApplicationTests {

    /**
     * Jika kita membutuhkan EntityManagerFactory, disini kita bisa langsung
     * melakukan injection menggunakan @Autowired atau constructor injection dsb
     * karena EntityManagerFactory telah di buatkan dan di registrasikan sebagai
     * Spring Bean.
     * */
	private @Autowired EntityManagerFactory entityManagerFactory;

	@Test
	public void testEntityManager(){
		Assertions.assertNotNull(entityManagerFactory);
		EntityManager entityManager = this.entityManagerFactory.createEntityManager();
		Assertions.assertNotNull(entityManager);
		EntityTransaction transaction = entityManager.getTransaction();
		Assertions.assertNotNull(transaction);
		entityManager.close();
	}
}
```