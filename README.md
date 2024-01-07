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
    show-sql: true
    properties:
      '[format-sql]': true
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
# Repository
Saat menggunakan Spring Data JPA, kita akan jarang sekali menggunakan secara langsung `EntityManagerFactory` bahkan mungkin tidak akan pernah menggunakanya secara langsung.  
Lantas bagaimana cara kita berinteraksi dengan database ??  
Spring Data JPA menganut konsep DDD(Domain Driven Design) pada konsep DDD ini ketika kita ingin berinteraksi dengan database maka kita akan menggunakan layer Repository.  
  
Sebenarnya didalam layer Repository memuat EntityManager. Jadi layer repository ini hadir untuk mempermudah kita ketika berinteraksi dengan database, sehingga kita tidak perlu lagi memikirkan kekompleksitasan `EntityManagerFactory`  
  
Kode sebelum menggunakan Repository 
``` java
public void save(Payment payment) {
    EntityManager entityManager = this.entityManagerFactory.createEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    transaction.begin();
    entityManager.persist(payment);
    transaction.commit();
}
```

kode setelah menggunakan repository :
``` java
public void save(Payment payment);
```

Untuk membuat Repository cukuplah mudah, cukup membuat interface yang mengextend salahsatu interface berikut :
* [JPaRepository\<E, ID>](https://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/repository/JpaRepository.html)
* [CrudRepository\<E, ID>](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html)
* [ListCrudRepository\<E, ID>](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/ListCrudRepository.html)
* [PagingAndSortingRepository\<E, ID>](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/PagingAndSortingRepository.htmlk)

Setelah itu kita bisa menambahkan annotation [@Repository](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/stereotype/Repository.html) pada interface nya.  
  
Sebelum kita membuat repository, pastikan kita telah memiliki Entity nya terlebih dahulu.  
Misalnya disini kita akan membuat `UserRepository` maka pastikan kita telah memiliki User entity.  
``` sql
CREATE TABLE users(
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    PRIMARY KEY(id)
)ENGINE=InnoDb;
```

``` java
@Builder @Entity @Table(name = "users")
@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class User implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;
}
```

``` java
@Repository
public interface UserRepository extends JpaRepository<User, Long> { }
```
>**NOTE:** *Type Generic pada `JpaRepository` dan sebagainya adalah : E yang artinya **Entity** dan ID yang artinya **Id primarykey entity nya***

[`JpaRepository<E, ID>`](https://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/repository/JpaRepository.html) adalah interface turunan dari [`ListCrudRepository<E, ID>`](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/ListCrudRepository.html), [`PagingAndSortingRepository<E, ID>`](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/PagingAndSortingRepository.htmlk), Iterface tersebut memiliki banyak sekali method misalnya untuk operasi CRUD dan sebagainya.  
  
Ketika kita menggunakan layer Repository kita tidak perlu menggunakan `EtityManager` secara langsung untuk melakukan operasi CRUD, melainkan cukup menggunakan method yang disediakan oleh interface-interface Repository. Misalnya ketika kita ingin melakukan insert data, maka kita bisa menggunakan method `save(entity)`.  
  
> **NOTE:** *method `save(entity)` digunakan untuk insert dan update*
``` java
@SpringBootTest(classes = SpringDataJpaApplication.class)
public class UserRepositoryTest {
    
    /**
     * Disini kita bisa secara langsung menginject
     * UserRepository secara langsung, karena telah di registrasikan
     * menjadi spring bean,
     * 
     * jadi semua interface yang meng extend interface repository maka secara
     * otomatis dijadikan spring bean
     * */
    private @Autowired UserRepository userRepository;

    @Test
    public void testInsert(){
        User user = User.builder()
                    .username("Abdillah")
                    .password("secret_pass")
                    .build();
        // melakukan insert data
        this.userRepository.save(user);
    }
}
```

# Declarative Transaction
Ketika menggunakan JPA, saat kita ingin melakukan transaction maka kita akan melakukan seperti berikut ini :
``` java
EntityManager entityManager = this.entityManagerFactory.createEntityManager();
Transaction transaction = entityManager.getTransaction();
transaction.begin();
// operasi CRUD

transaction.commit();
```
Cara diatas adalah cara melakukan transaction secara manual, tentunya agak ribet karena kita harus menghandle secara manual unutk commit dan rollback nya.  
  
Saat kita menggunakan Spring Data JPA, jika kita ingin melakukan transaction kita tidak perlu lagi melakukan cara seperti itu. Spring Data JPA menyediakan annotation [`@Transactional`](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/transaction/annotation/Transactional.html) untuk melakukan transaction.  
  
Cara kerja Annotation `@Transactional` adalah menggunakan Spring AOP, jadi ketika method yang di annotation dengan `@Transactional` maka ketika method tersebut diakses dari object luar/Object lain maka transaction tersebut akan dijalankan(melakukan commit atau rollback secara otomatis)  
![cross_aop](/src/main/resources/images/cross_aop.jpg)


``` java
@Service @AllArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;

    @Transactional
    public void create() {
        User user = User.builder()
                    .username("Abdillah")
                    .password("secret")
                    .build();

        User user1 = User.builder()
                    .username("Alli")
                    .password("secret")
                    .build();
        User user2 = User.builder()
                    .username("Nabila")
                    .password("secret")
                    .build();
        List<User> userList = new ArrayList<>(List.of(user, user1, user2));
        userList.forEach(u -> {
            this.userRepository.save(u);
        });
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "error");
    }
}
```

``` java
@SpringBootTest(classes = SpringDataJpaApplication.class)
public class UserServiceTest {
    
    private @Autowired UserService userService;

    @Test
    public void testIsert(){
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            // akan melakukan rollback secara otomatis
            this.userService.create();
        });
    }
}
```
Perlu diketahui bahwa Spring AOP hanya bekerja ketika ada object luar yang mentriger obejct yang memiliki annotation yang dimanage AOP.  
  
jika Obejct yang dimanage oleh Spiring AOP diakses dengan method nya sendiri maka Spring AOP tidak akan bekerja. Hal tersebut berlaku juga ketika kita menggunakan annotation `@Transactional` karena annotation tersebut dimanage oleh Spirng AOP  
![non_cross_aop](./src/main/resources/images/non_cross_aop.jpg)
``` java
@Service @AllArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;

    @Transactional
    public void create() {
        User user = User.builder()
                    .username("Abdillah")
                    .password("secret")
                    .build();

        User user1 = User.builder()
                    .username("Alli")
                    .password("secret")
                    .build();
        User user2 = User.builder()
                    .username("Nabila")
                    .password("secret")
                    .build();
        List<User> userList = new ArrayList<>(List.of(user, user1, user2));
        userList.forEach(u -> {
            this.userRepository.save(u);
        });

        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "error");
    }

    // Ketika method ini memanggila method create(); maka transaction tidak akan dijalankan
    // karena annotation @Transaction hanya bekerja ketika method create() diakses oleh object lain(diakses oleh luar Object)
    public void call() {
        create();
    }
}
```

``` java
@SpringBootTest(classes = SpringDataJpaApplication.class)
public class UserServiceTest {
    
    private @Autowired UserService userService;

    @Test
    public void testInsertFail(){
        Assertions.assertThrows(ResponseStatusException.class, () -> {
          /**
           * ketika method call(); dipanggil
           * transaction tidak akan dijalankan
           * */
            this.userService.call();
        });
    }
}
```
  
Saat menggunakan annotation `@Transactional` kita bisa menambahkan beberapa pengaturan, misalnya :
| Pengaturan  | Default Value | Deskripsi
|-------------|---------------|---------------------
| readOnly    | false         | Jika transaction tidak mengubah data samasekali
| timeout     | -1            | Membatasi lama transaction
| propagation | REQUIRED      | Membuat transaction jika belum ada dan tidak membuat transaction jika sudah ada

dan masih banyak lagi, untuk lebih detailny abisa kunjungi disini https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/transaction/annotation/Transactional.html

``` java
@Service @AllArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<User> getUserts() {
        /**
        * ini tidak melakukan perubahan data sama sekali, maka kita busa gunakan
        * aturan readOnly = true
        *
        * Propagation.REQUIRED(default value)
        **/
        return this.userRepository.findAll();
    }
}
```

# Transaction Propagation
Sebelumnya kita telah menyinggung tentang pengaturan propagattion yang ada pada annotation `@Transactional`.  
Transaction propagation digunakan ketika transaction yang sedang berjalan mengakses transaction lain, misalnya ketika method transaction pada `UserService` memanggil method transaction pada `PaymentService`.  
Ada banyak sekali pengaturan yang bisa kita gunakan pada transaction propagation, diantaranya yaitu
| Propagation   | Deskripsi 
|---------------|-----------
| REQUIRED      | Membuat transaction jika transaction belum dibuat
| MANDATORY     | Transaction harus diakses oleh transaction lain
| NEVER         | Tidak melakukan transaction

Dan masih banyak lagi, untuk lebih detail nya bisa kunjungi disini https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/transaction/annotation/Propagation.html


``` java
@Entity @Table(name = "payments")
@Setter @Getter @AllArgsConstructor @NoArgsConstructor @Builder
public class Payment implements Serializable {
    
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String reciver;

    private Date date;

    private Double amount;
}
```

``` java
@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> { }
```

``` java
@Builder
@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class PaymentRequest implements Serializable {
    
    private String id;

    private String reciver;

    private Double amount;
}
```

``` java
@Service @AllArgsConstructor
public class PaymentService {
    
    private final PaymentRepository paymentRepository;

    private final ObjectMapper objectMapper;
    
    @Transactional(propagation = Propagation.MANDATORY)
    public void tranfer(PaymentRequest request) throws JsonMappingException, JsonProcessingException {
        Payment payment = this.objectMapper.readValue(this.objectMapper.writeValueAsString(request), Payment.class);
        payment.setDate(new Date());
        this.paymentRepository.save(payment);
    }
}
```

``` java
@Service @AllArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;

    private final PaymentService paymentService;

    @Transactional
    public void userTranfer(PaymentRequest request) throws JsonMappingException, JsonProcessingException {
        this.paymentService.tranfer(request);
    }
}
```

``` java
@SpringBootTest(classes = SpringDataJpaApplication.class)
public class UserServiceTest {
    
    private @Autowired UserService userService;

    private @Autowired PaymentService paymentService;

    @Test
    public void testPropagationSuccess() throws JsonMappingException, JsonProcessingException{
        PaymentRequest paymentRequest = PaymentRequest.builder()
                    .amount(10.0000d)
                    .reciver("Alli")
                    .build();
        Assertions.assertDoesNotThrow(() -> {
            this.userService.userTranfer(paymentRequest);
        });
    }

    @Test
    public void testFailPropagation() throws JsonMappingException, JsonProcessingException {
        Assertions.assertThrows(IllegalTransactionStateException.class, () -> {
            PaymentRequest paymentRequest = PaymentRequest.builder()
                            .reciver("Nero")
                            .amount(30.000d)
                            .build();
            this.paymentService.tranfer(paymentRequest);
        });
    }
}
```
