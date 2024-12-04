# Spring_note

1. [Basi di Spring](#1-basi-di-spring)
    - [Spring Framework](#spring-framework)
2. [Struttura Base del Progetto](#2-struttura-base-del-progetto)
3. [Configurazione DB](#3-configurazione-db)
4. [Spring Repository e interfacciamento con il DB](#4-spring-repository-e-interfacciamento-con-il-db)
5. [Primo progetto REST Step by Step](#5-primo-progetto-rest-step-by-stepp)
6. [Programmazione Reattiva progetto Chat Step by Step](Programmazione Reattiva progetto Chat Step by Step)
7. [Lanciare un container con Mongo DB](#6-lanciare-un-container-con-mongo-db)
8. [Gestione task Step by Step](gestione-task-step-by-step)
   - [Creiamo un Exception Handler](creiamo-un-exception-handler)
   - [Caching](caching)

## 1. Basi di Spring
### 1.1 Spring Framework
Spring offre una serie di moduli che coprono vari aspetti dello sviluppo:

* Spring Core: Gestione dei bean e Inversion of Control (IoC).
* Spring MVC: Framework per lo sviluppo di applicazioni web.
* Spring Data: Integrazione con database.
* Spring Security: Gestione della sicurezza.
* Spring Cloud: Strumenti per microservizi, come service discovery, API gateway, e circuit breaker.
### 1.2 Spring Boot
Spring Boot semplifica il setup con funzionalità come:

* Autoconfigurazione: Configura automaticamente le componenti in base alle dipendenze nel progetto.
* Starter Dependencies: Pacchetti preconfigurati per aggiungere funzionalità come Spring Web o Spring Data.
* Embedded Servers: Tomcat o Jetty integrati per eseguire applicazioni standalone.
#### 2. Architettura a Microservizi con Spring
Un'architettura a microservizi prevede lo sviluppo di servizi indipendenti, ognuno con uno scopo specifico, comunicanti via protocolli come HTTP o message brokers (es. RabbitMQ, Kafka). Spring offre strumenti per:

* Service Discovery: Utilizzando Eureka o Consul.
* API Gateway: Con Spring Cloud Gateway.
* Comunicazione inter-servizio: Con REST, Feign Client o Event Streaming.
* Tolleranza ai guasti: Con Resilience4j o Spring Cloud Circuit Breaker.
#### 3. Annotazioni Principali
Spring utilizza annotazioni per configurare i componenti e definire il comportamento dell'applicazione. Ecco le più comuni:

##### 3.1 Gestione dei Componenti e IoC
* `@Component`: Marca una classe come bean gestito da Spring.
* `@Service`: Indica un componente di logica aziendale.
* `@Repository`: Indica un componente che interagisce con il database.
* `@Controller` / `@RestController`: Definisce un controller per gestire richieste HTTP (con `@RestController` che include `@ResponseBody` per i dati JSON).
##### 3.2 Configurazione
* `@Configuration`: Definisce una classe di configurazione.
* `@Bean`: Dichiara un bean da aggiungere al contesto Spring.
* `@PropertySource`: Carica file di configurazione esterni (es. .properties).
##### 3.3 Web e API
* `@RequestMapping`: Mappa le richieste HTTP a metodi o classi.
* `@GetMapping`, `@PostMapping`, ecc.: Versioni specializzate per richieste HTTP GET, POST, ecc.
* `@PathVariable`: Estrae parametri dalla URL.
* `@RequestParam`: Estrae parametri dalla query string.
* `@RequestBody`: Legge il corpo della richiesta HTTP (tipicamente JSON).
* `@ResponseBody`: Scrive direttamente una risposta HTTP.
##### 3.4 Dependency Injection
* `@Autowired`: Inietta dipendenze automaticamente.
* `@Qualifier`: Specifica quale bean iniettare se ci sono ambiguità.
* `@Value`: Inietta valori dalle proprietà configurate.
##### 3.5 Microservizi Specifiche
* `@EnableDiscoveryClient`: Abilita un servizio come client in un sistema di Service Discovery (Eureka, Consul).
* `@FeignClient`: Usato per comunicare con altri servizi tramite REST.
* `@CircuitBreaker`: Implementa un meccanismo di fallback per tolleranza ai guasti.
#### 4. Esempio Base
Ecco un semplice esempio per un microservizio REST:

```java
@SpringBootApplication
public class MyMicroserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyMicroserviceApplication.class, args);
    }
}

@RestController
@RequestMapping("/api")
public class MyController {

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, Microservices!";
    }
}
```
#### 5. Configurazione di Microservizi
##### 5.1 Service Discovery con Eureka
Server Eureka:
```java
@EnableEurekaServer
@SpringBootApplication
public class EurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
```
Client Eureka:
```java
@EnableDiscoveryClient
@SpringBootApplication
public class MyMicroserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyMicroserviceApplication.class, args);
    }
}
```


Organizzare un progetto Spring con una struttura chiara e modulare è fondamentale per mantenere il codice leggibile, scalabile e facile da gestire. La struttura che proponi, basata su Controller, Model, Repository e Service, è un approccio comune e segue il principio dell'architettura MVC (Model-View-Controller). È sicuramente un buon punto di partenza, e con alcune migliorie può diventare ancora più efficace.

## 2. Struttura Base del Progetto
Ecco come potrebbe apparire la struttura organizzativa di un progetto Spring:
```
com.example.myproject
│
├── controller      // Contiene i controller REST
├── model           // Contiene le entità e i DTO
├── repository      // Contiene le interfacce per accedere ai dati
├── service         // Contiene la logica di business
└── config          // Contiene le configurazioni
```
### 2.1 Package Controller
Scopo: Gestire le richieste HTTP e orchestrare le chiamate ai servizi.
Contenuto: Classi annotate con `@RestController` o `@Controller`.
Esempio:
```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }
}
```
### 2.2 Package Model
Scopo: Definire entità persistenti, DTO (Data Transfer Objects) e classi utili come enumerazioni.
Contenuto:
Entità annotate con `@Entity`.
DTO senza annotazioni (strutture dati per trasferire dati tra livelli).
Enumerazioni utili.
Esempio:
```java
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    
    // Getters and Setters
}
```
### 2.3 Package Repository
Scopo: Fornire accesso ai dati persistenti.
Contenuto: Interfacce che estendono `JpaRepository` o `CrudRepository`.
Esempio:
```java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
```
### 2.4 Package Service
Scopo: Contenere la logica di business e orchestrare l'accesso ai repository.
Contenuto: Classi annotate con @Service.
Esempio:
```java
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                             .stream()
                             .map(user -> new UserDto(user.getName(), user.getEmail()))
                             .collect(Collectors.toList());
    }
}
```
### 2.5 Package Config
Scopo: Contenere configurazioni generali per il progetto (Bean, sicurezza, ecc.).
Contenuto: Classi annotate con `@Configuration`, `@Enable`..., ecc.
Esempio:
```java
@Configuration
public class AppConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```
### 3 Vantaggi di questa Struttura
Separation of Concerns (SoC): Ogni package ha una responsabilità chiara e distinta.
Scalabilità: Puoi aggiungere nuove funzionalità o microservizi senza confondere il codice esistente.
Manutenibilità: È facile individuare il codice relativo a un determinato ambito (es. logica o accesso ai dati).
### 4. Miglioramenti e Considerazioni
### 4.1 Sottodivisione dei Package
Se il progetto cresce, puoi dividere ulteriormente i package per funzionalità:
```
com.example.myproject
│
├── user
│   ├── controller
│   ├── model
│   ├── repository
│   ├── service
│
├── order
│   ├── controller
│   ├── model
│   ├── repository
│   ├── service
```
In questo modo, ogni funzionalità (es. gestione utenti, gestione ordini) ha il proprio modulo logico.

### 4.2 Uso dei DTO
Per evitare di esporre direttamente le entità nelle API, usa i DTO (Data Transfer Objects) nel package model o in un package separato (dto):

Mappali tramite librerie come MapStruct o manualmente nei servizi.
### 4.3 Logger
Includi un logger nelle classi principali (controller, service) per tracciare le operazioni.

### 4.4 Eccezioni Personalizzate
Gestisci errori con eccezioni personalizzate e un gestore globale:

Crea un package exception per definire le tue eccezioni.
Usa un gestore annotato con `@ControllerAdvice`.
### 4.5 Testing
Aggiungi test per ogni layer:

Controller: Usa MockMvc.
Service: Test unitari con Mockito.
Repository: Test di integrazione con un database in-memory come H2.





## 3. Configurazione DB 
In Spring, la configurazione del datasource e di JPA (Java Persistence API) tramite il file application.properties è fondamentale per definire come la tua applicazione si connette a un database e come utilizza JPA per la persistenza dei dati. Vediamo come funzionano e come configurare questi elementi.

### 3.1 Spring DataSource
Un DataSource rappresenta la configurazione di connessione al database, come URL, credenziali, driver, ecc. Spring utilizza questa configurazione per creare una connessione al database.

Configurazione di Base
Nel file application.properties:

```java
# Configurazione del DataSource
spring.datasource.url=jdbc:mysql://localhost:3306/mydatabase
spring.datasource.username=root
spring.datasource.password=secret
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```
Dettagli delle Proprietà
spring.datasource.url: Specifica l'URL di connessione al database. Include:
Protocollo (jdbc:mysql per MySQL).
Host e porta (localhost:3306).
Nome del database (mydatabase).
`spring.datasource.username` e `spring.datasource.password`: Credenziali per accedere al database.
`spring.datasource.driver-class-name`: Specifica il driver JDBC utilizzato (ad esempio, per MySQL è `com.mysql.cj.jdbc.Driver`).
Connessione con Pooling
Per gestire molteplici connessioni in modo efficiente, Spring usa un connection pool, tipicamente HikariCP:

# Configurazione di HikariCP (Connection Pool predefinito in Spring Boot)
```java
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=2
spring.datasource.hikari.idle-timeout=30000
spring.datasource.hikari.max-lifetime=1800000ù
```
### 3.2 Spring JPA
Spring JPA (parte di Spring Data) semplifica l'uso di JPA per interagire con il database tramite repository, entità e operazioni CRUD.

Configurazione di Base
Spring JPA si configura tramite il file application.properties per gestire aspetti come DDL (schema generation) e comportamento delle transazioni:

# Configurazione di Spring JPA
```java
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=true
```
Dettagli delle Proprietà:
1. `spring.jpa.database-platform`: Specifica il dialect Hibernate (es. MySQL8Dialect per MySQL 8.x). Hibernate utilizza il dialect per tradurre le query in SQL specifico del database.
2. `spring.jpa.hibernate.ddl-auto`:
       * Configura come Hibernate deve gestire la creazione dello schema del database.
       * Opzioni comuni:
                `none`: Nessuna azione.
                `validate`: Confronta lo schema esistente con le entità.
                `update`: Aggiorna lo schema senza perdere dati.
                `create`: Crea uno schema ad ogni avvio (perde i dati).
                `create-drop`: Crea e rimuove lo schema quando l'app si spegne (utile per test).
`spring.jpa.show-sql`: Stampa le query SQL nel log (utile per debugging).
`spring.jpa.properties.hibernate.format_sql`: Formatta le query SQL stampate nel log per una maggiore leggibilità.
`spring.jpa.open-in-view`:
Mantiene aperta la sessione Hibernate durante il ciclo di vita della richiesta. Consigliato per semplificare l'accesso ai dati nelle viste, ma potrebbe essere inefficiente in grandi applicazioni.
### 3.2 Esempio Completo
Un esempio combinato di configurazione per MySQL:

# DataSource
```java
spring.datasource.url=jdbc:mysql://localhost:3306/mydatabase
spring.datasource.username=root
spring.datasource.password=secret
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.hikari.maximum-pool-size=10
```
# Spring JPA
```java
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=true
```
### 3.3 Configurazioni Avanzate
Database in Memory (H2)
Per i test, puoi configurare un database in-memory come H2:
```java
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create
```
Proprietà Hibernate Aggiuntive
Puoi specificare ulteriori configurazioni Hibernate:

# Cache di secondo livello
```java
spring.jpa.properties.hibernate.cache.use_second_level_cache=true
spring.jpa.properties.hibernate.cache.region.factory_class=org.hibernate.cache.jcache.JCacheRegionFactory

# Timeout di query
spring.jpa.properties.hibernate.default_batch_fetch_size=16
```
5. Testare la Configurazione
Dopo aver configurato il datasource e JPA:

Creare una Entità:
```java
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    
    // Getters and setters
}
Creare un Repository:
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
```
Verificare la Connessione: Esegui l'applicazione e controlla che Spring generi correttamente lo schema e si connetta al database.









## 4. Spring Repository e interfacciamento con il DB
In Spring, puoi creare un repository annotato con `@Repository` per eseguire operazioni sul database. Spring Data JPA offre diversi modi per fare query sul database: tramite metodi derivati, query personalizzate annotate con `@Query`, o usando Named Queries definite nelle entità. Vediamo ogni approccio.

### 4.1 Creare un Repository Base
Entità del database Definisci una classe annotata con `@Entity` per rappresentare una tabella nel database:
```java
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    // Getters, Setters e Costruttori
}
```
Repository Crea un'interfaccia che estenda JpaRepository per definire il tuo repository. Questo fornisce metodi CRUD predefiniti come `save`, `findById`, e `delete`.
```java
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
```
Ora hai un repository base che esegue operazioni standard senza dover scrivere codice aggiuntivo.

### 2. Query Personalizzate
#### 2.1 Metodi Derivati
Spring Data JPA crea automaticamente query basate sui nomi dei metodi nella tua interfaccia. Devi solo seguire un convenzione di denominazione:
```java
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Trova utenti per email
    Optional<User> findByEmail(String email);

    // Trova utenti per nome
    List<User> findByName(String name);

    // Trova utenti il cui nome contiene una stringa
    List<User> findByNameContaining(String keyword);
}
```
Convenzioni di denominazione  [JPA Query Methods](https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html):
* `findBy`: Per query basate su un campo.
* `And` / `Or`: Per combinare condizioni (`findByNameAndEmail`).
* `Containing` / `StartingWith` / `EndingWith`: Per ricerche parziali.
* `OrderBy`: Per ordinare i risultati (`findByNameOrderByEmailAsc`).
#### 2.2 @Query
Per query più complesse, usa @Query per scrivere direttamente una query JPQL o SQL nativa.
```java
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Query JPQL
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findUserByEmail(@Param("email") String email);

    // Query SQL nativa
    @Query(value = "SELECT * FROM User WHERE email = :email", nativeQuery = true)
    Optional<User> findUserByEmailNative(@Param("email") String email);
}
```
* JPQL: È basato su entità, non tabelle.
* SQL Nativa: Usa la sintassi SQL e lavora direttamente sulle tabelle.

### 3. Named Queries
Puoi definire query preconfigurate direttamente nell'entità usando l'annotazione @NamedQuery:
```java
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQuery;

@Entity
@NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
}
```
Nel repository, puoi richiamare la named query:
```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(@Param("email") String email); // Richiama "User.findByEmail"
}
```
### 4. Esempio Completo
Ecco un esempio completo che combina i vari approcci:

Entità:
```java
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    // Getters, Setters, e Costruttori
}
```
Repository:
```java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Metodo derivato
    Optional<User> findByEmail(String email);

    // Query JPQL
    @Query("SELECT u FROM User u WHERE u.name LIKE %:name%")
    List<User> searchByNameContaining(@Param("name") String name);

    // Query nativa
    @Query(value = "SELECT * FROM User WHERE email = :email", nativeQuery = true)
    Optional<User> findByEmailNative(@Param("email") String email);
}
```
Service
Creiamo un service per utilizzare il repository:
```java
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> searchUsersByName(String name) {
        return userRepository.searchByNameContaining(name);
    }
}
```
Controller
Esporremo queste operazioni tramite un controller REST:
```java
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.findUserByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @GetMapping("/search")
    public List<User> searchUsersByName(@RequestParam String name) {
        return userService.searchUsersByName(name);
    }
}
```

## 5 Primo progetto REST Step by Step 

1.  Creazione progetto con [Spring Initializr](https://start.spring.io/)

![Initializr](/img/1.png)

eliminare nel nome package il dash `-` nel nome.


3. Crea un nuovo progetto con IntelliJ e incolla gli artefatti scaricati da spring initializr  


![Initializr](/img/2.png)


3. tasto destro su `pom.xml`  click su `Generate` per generare le dipendenze




4. creare DB e tabella
```java
CREATE DATABASE <db-name>;
USE <db-name>;
CREATE TABLE <db-name>.contatti (
	id BIGINT auto_increment NOT NULL,
	nome varchar(50) NOT NULL,
	numero_telefono varchar(100) NOT NULL,
	CONSTRAINT Contatti_PK PRIMARY KEY (id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;
```


5. interfacciamo Spring al DB attrverso `application.properties`

```java   
spring.application.name=contatto-service

spring.datasource.url=jdbc:mysql://197.0.0.1:3306/<db_name>
spring.datasource.username=root
spring.datasource.password=<password>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=none
```


6. creazione Entity Contatto 
All'interno di `spring.jpa.hibernate.ddl-auto=none` crea una cartella `model`. All'interno di `model` creare un file  `Contatto.java` 

`Contatto.java`
```java
package it.eng.corso.contattoservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity // annotazione che definisce Contatto come un Entity
@Table(name = "Contatti") // con "@Table name" associamo l'entity alla tabella "Contatti" del DB alla classe "Contatto"
public class Contatto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY ) // definisce che è il DB ad occuparsi dell'autoincrement dell'id
    private Long id;  //attributo da mappare con l'id della tabella Contatti nel DB
    private String name; // per mappare name
    private String numTelefono; /* in questo caso il comportamento standard di sping è quello di trasformare il camelCase in pascal case*/
    /* In alternativa potremmo fare un associazioen esplicita tra la tabella e l'attributo con:
    @Column( name = "num_telefono, lenght = 100) // associa la colonna "num_telefono" a  "numtel".  con l'annotation column possiamo andare a dettagliare le caratteristiche della nosta colonna come "lenght" che ne indica la lunghezza massima di questo attributo nella tabella del DB
    private String numtel;
     */
}
```


[Understanding Camel Case, Pascal Case, Snake Case, Kebab Case, and Title Case](https://pranish23.hashnode.dev/string-cases-in-programming-understanding-camel-case-pascal-case-snake-case-kebab-case-and-title-case)



7. creazione della interfaccia per la repository per l'accesso al DB tramite Jpa
crea nuovo package `repository` all'interno del quale creiamo una nuova interfaccia `ContattoRepository.java`

`ContattoRepository.java`
```java
package it.eng.corso.contattoservice.repository;

import it.eng.corso.contattoservice.model.Contatto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContattoRepository extends JpaRepository<Contatto, Long>{ // specificare per quale entità dobbiamo creare la nostra repository indicare anche il tipo della chiave (id)
}
```

8. creazione dell'interfaccia per definire la signeture delle funzioni che lancieranno le query nel DB
creiamo un nuovo package `service` all'interno della quale creiamo una nuova interfaccia `ContattoService`  qui specifichiamo l'interfaccia con la signeture dei metodi che la nostra business logic dovrà implementare. per esempio se vogliamo ricevere tutti i contatti implementiamo `findAll()`.

`ContattoService.java` 
```java
package it.eng.corso.contattoservice.service;


import it.eng.corso.contattoservice.model.Contatto;

import java.util.List;

public interface ContattoService {
    List<Contatto> findAll();  // nell'implementazione lancierà una query che restituisce la lista contente tutti i contatti del DB
}
```
per il nome possiamo chiamare il file dell'interfaccia con `Inome_interfaccia` con la `I` all'inizio
oppure andare a chiamare la classe che implementa l'interfaccia come `nome_fileImpl` con Impl finale che specifica l'implementazione/utilizzo dell'interfaccia.

> [!NOTE]
> Il `component scan` all'inizio dell'esecuzione va a visionare il codice per trovare eventuali stereotype come `Service`, `Repository` e `Controller`.

9. implementazione dell'interfaccia e dei metodi che lanciano le query e recuperano le informazioni dal DB.
creiamo all'interno della folder `service` il file che implementa l'interfaccia definita nel file `ContattoService`.
questo file lo chiameremo `ContattoServiceImpl`

`ContattoServiceImpl.java`
```java
package it.eng.corso.contattoservice.service;

import it.eng.corso.contattoservice.model.Contatto;
import it.eng.corso.contattoservice.repository.ContattoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContattoServiceImpl implements ContattoService{

    @Autowired // annotatio per iniettare in maniera automatica la dipendenza di ContattoRepository
    private ContattoRepository contattoRepository;

    @Override
    public List<Contatto> findAll(){
        return contattoRepository.findAll();
    }
}
```

10. creiamo la folder `controller` dentro la quale creiamo `ContattoController`. Questo componente dovrà rispondere alla richiesta di un client che arriveranno ad un determinato path.
per fare ciò  utiliziamo lo stereotype `@RestController`. 
il `@RestController` restituirà al client un file json. questo componente richiederà le informazioni direttamente al Service


 `ContattoController.java`
```java
package it.eng.corso.contattoservice.controller;

import it.eng.corso.contattoservice.model.Contatto;
import it.eng.corso.contattoservice.service.ContattoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController // Rest controller che gestirà le richieste ricevute dai client
public class ContattoController {
    @Autowired
    private ContattoService contattoService;

    @GetMapping("/api/v1/contatti") // gestione di una richiesta GET da parte di un client
    public List<Contatto> restituisciElenco(){ // ristituiamo un lista di contatto recuperati dal DB tramite il Service
        return contattoService.findAll();
    }

}

```

lanciare il microservizio con:
`ContattoServiceAppliation` tasto destro `Run`
o 
dall'icona Play





> [!CAUTION]
> Lanciando il progetto un possibile errore può essere dovuto al mancato mapping dei nomi tra attributi della tabella del DB con gli attributi della corrispondente Entity
> gli orreri ottenibili possono essere:
> `java.sql.SQLSyntaxErrorException: Unknown column 'c1_0.name' in 'field list'`
> `java.sql.SQLSyntaxErrorException: Unknown column 'c1_0.num_telefono' in 'field list'`
>
> Questo perchè  `private String name`  e  `private String numTelefono` non corrispondono ai nomi delle tabelle
> è possibile risolvere così:
>
>    @Column( name = "nome")
>    private String name; // per mappare name
>    @Column( name = "num_telefono", length = 100)
>    private String numTelefono; 
> [Resolving “Failed to Configure a DataSource” Error](https://www.baeldung.com/spring-boot-failed-to-configure-data-source)

Quindi il nuovo codice di `Contatto` sarebbe:
```java
 package it.eng.corso.contattoservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity // annotazione che definisce Contatto come un Entity
@Table(name = "contatti") // con "@Table name" associamo l'entity alla tabella "Contatti" del DB alla classe "Contatto"
@Getter
@Setter
public class Contatto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY ) // definisce che è il DB ad occuparsi dell'autoincrement dell'id
    private Long id;  //attributo da mappare con l'id della tabella Contatti nel DB
    @Column( name = "nome")
    private String name; // per mappare name
    private String numTelefono; /* in questo caso il comportamento standard di sping è quello di trasformare il camelCase in pascal case*/
    /* In alternativa potremmo fare un associazioen esplicita tra la tabella e l'attributo con:
    @Column( name = "num_telefono, lenght = 100) // associa la colonna "num_telefono" a  "numtel".  con l'annotation column possiamo andare a dettagliare le caratteristiche della nosta colonna come "lenght" che ne indica la lunghezza massima di questo attributo nella tabella del DB
    private String numtel;
     */
```

11. richiesta GET da browser     

![Initializr](/img/3.png)

la lista restituita è vuota

12. Insert e richiesta
insert nel db
```
Insert into contatti(nome,numero_telefono) values ("Pippo", "3317658672");
```

![Initializr](/img/4.png)


## 6 Programmazione Reattiva progetto Chat Step by Step
[Create a WebFlux application with Spring Boot](https://hantsy.github.io/spring-reactive-sample/start/boot-first.html)

> [!NOTE]
> in mongoDB utliziamo dati in formato BSON (Binary Json)

> [!NOTE]
> `lombok` viene utilizato per getter setter e per l'annotation `@Data` che si porta dietro `@Getter` `@Setter` `@RequiredAgsConstrutor` > per la crezione del costruttore, `@allArgsConstructor` crea un metodo costruttore con tutti gli argomenti o `@NoArgsConstructor` per
>  fare l'opposto.
> Abbiamo anche:
> @toString
> @EqualsHashCoe

> [!NOTE]
> nella programmazione Reactor non verrà creato un server Tomkat ma un server Netty

1.
![Initializr](/img/5.png)


2. estrai file dallo zip e apri con Intellij

`application.properties`
```
spring.application.name=chat-service
spring.data.mongodb.uri: <mongodb_url>
spring.data.mongodb.database=corso
```

3.  
crea la folder `model` e dentro crea `Message`:

`Message.java`
```java
package it.eng.corso.chatservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collation = "chat") //alogo di Entity per i DB sql. specifichiamo la collection alla quale vogliamo connetterci
public class Message {

    @Id //specifichiamo l'id
    private String id;
    private Integer roomId;
    private String sender;
    private String message;
    private LocalDateTime createAt;
}
```

4.  

crea folder `repository` e dentro `MessageRepository`

`MessageRepository.java`
```java

package it.eng.corso.chatservice.service;

import it.eng.corso.chatservice.model.Message;
import it.eng.corso.chatservice.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MessageServiceImpl implements MessageService{

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public Flux<Message> findByRoomId(Integer roomId){
        return messageRepository.findByRoomId(roomId);
    }
    
    @Override
    public Mono<Message> save(Message message){
        return messageRepository.save(message);
    }
}

```

In questo caso utilizzando mongoDb in modalità reattiva, questo non restituirà una lista ma un flusso. il flusso di dati dal db non verrà mai chiuso ma resterà attivo per la ricezione dei dati


 5. 

crea la cartella service e dentro crea `MessageService`

 `MessageService.java`
```java
package it.eng.corso.chatservice.service;

import it.eng.corso.chatservice.model.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MessageService {

    Flux<Message> findByRoomId(Integer roomId);

    Mono<Message> save(Message message);
}
```


6.
`Flux` è un flusso (stream) di dati che può contenere zero o infiniti elementi, in questo modo l'elemento/classe che utilizza questo metodo resterà continuamente in attesa di elementi.
esiste un'altro flusso chiamato Mono che contiene zero o un dato, in questo modo ricevuto un elemento non rimarrà niente in attesa che dal flusso arrivino altri elementi.


crea `MessageServiceImpl` dentro service che implementerà l'interfaccia `MessageService`

`MessageServiceImpl.java`
```java
package it.eng.corso.chatservice.service;

import it.eng.corso.chatservice.model.Message;
import it.eng.corso.chatservice.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MessageServiceImpl implements MessageService{

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public Flux<Message> findByRoomId(Integer roomId){
        return messageRepository.findByRoomId(roomId);
    }

    @Override
    public Mono<Message> save(Message message){
        return messageRepository.save(message);
    }
}
```

7.
crea la folder `controller` e dentro `ChatController`

`ChatController.java`
```java
package it.eng.corso.chatservice.controller;

import it.eng.corso.chatservice.model.Message;
import it.eng.corso.chatservice.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestController
public class ChatController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/chat")
    public Mono<Message> save(@RequestBody Message message){
        message.setCreateAt((LocalDateTime.now()) );
        return messageService.save(message);
    }
}
```

8.
manda un messaggio con un richiesta POST e il json all'interno del body

![Initializr](/img/6.png)


9.
controlla la response

![Initializr](/img/7.png)

10.
implementiamo il secondo endpoint nella classe `ChatController`



11. 
si può notare che la richiesta rimane appesa perchè stiamo ricevedo  un flusso  e il client rimarrà in attesa fino a un EOF (end of file).

`ChatController.java`
```java
package it.eng.corso.chatservice.controller;

import it.eng.corso.chatservice.model.Message;
import it.eng.corso.chatservice.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestController
public class ChatController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/chat")
    public Mono<Message> save(@RequestBody Message message){
        message.setCreateAt((LocalDateTime.now()) );
        return messageService.save(message);
    }
    
    @GetMapping("/chat/room/{roomId}")
    public Flux<Message> findByRoomId(@PathVariable Integer roomId){
        return messageService.findByRoomId(roomId);
    }
}
```
12.
`http://localhost:8080/chat`

```json
{
  "roomId":11,
  "sender":"Philip",
  "message":"Hii!"
}
```

![Initializr](/img/8.png)


## 6 Lanciare un container con Mongo DB
Docker & MongoDB

* Per mantenere i dati anche se il container viene eliminato, mappa una directory del tuo host come volume:
```bash
docker run -d \
  --name mongodb-container \
  -p 27017:27017 \
  -v /Users/pserafino/data:/data/db \
  -e MONGO_INITDB_ROOT_USERNAME=admin \
  -e MONGO_INITDB_ROOT_PASSWORD=password \
  mongo
```

```bash
podman pull docker.io/mongodb/mongodb-community-server:latest
```

```bash
podman run -d --name mongodb-container -p 27017:27017 -v /Users/pserafino/data:/data/db -e MONGO_INITDB_ROOT_USERNAME=admin -e MONGO_INITDB_ROOT_PASSWORD=password docker.io/mongodb/mongodb-community-server:latest
```
```bash
podman run --detach --name todoDB -p 27017:27017 -v /Users/ptambe/data:/data/db -e MONGO_INITDB_ROOT_USERNAME=admin -e MONGO_INITDB_ROOT_PASSWORD=password docker.io/mongodb/mongodb-community-server:latest
```
## Lanciare un container con MySQL
[Deploy MySQL with podman](https://infotechys.com/deploying-mysql-using-podman/)
Pulling the MySQL Image
```bash
podman pull mysql
```

Creating a MySQL Container
```bash
podman run --name mysql-container -e MYSQL_ROOT_PASSWORD=my-secret-pwd -d -p 3306:3306 mysql:latest
```
```bash
 podman ps
```

Breaking down the command
* --name mysql-container assigns a descriptive name to the container.
* -e MYSQL_ROOT_PASSWORD=my-secret-pwd sets the root password for MySQL.
* -d runs the container in detached mode.
* -p 3306:3306 maps port 3306 from the container to port 3306 on the host, enabling external access to MySQL.

Deploying MySQL Using Podman: Accessing the MySQL Database
```bash
podman exec -it mysql-container /bin/bash
```

```bash
mysql -h localhost -p
// Enter password:
```


```bash
podman exec -it mysql-container mysql -u root -p
```

Persisting Data

```bash
podman run --name mysql-container -e MYSQL_ROOT_PASSWORD=my-secret-pwd -d -p 3306:3306 -v /path/on/host:/var/lib/mysql mysql:latest
```

## Applicazione Web Step-by-Step
[Serving JSP with Spring Boot 3](https://howtodoinjava.com/spring-boot/spring-boot-jsp-view-example/)
1.
voglimo che il risultato sia una appllicazione web quindi vogliamo un artifact finale .war
inizialmente non funzionerà perchè il punto war non sarà in un formato standard quindi dovremmo modificarlo.
verrà creata una directory con una web app 
le dipendenze iniziale sono le stesse di un app REST
un applicazione web di spring dovrebbe trovarsi in spring initializer

tuttavia intellij la cerca dentro le directory ....
[Enable Web application support](https://www.jetbrains.com/help/idea/enabling-web-application-support.html)

![Initializr](/img/10.png)

![Initializr](/img/11.png)

![Initializr](/img/12.png)

2.
resources> application.properties

```java
spring.application.name=book-web

spring.datasource.url=jdbc:mysql://195.32.10.140:3306/corso
spring.datasource.username=root
spring.datasource.password=ginopino
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=none
```

3.
Dentro `src/main/java/it.eng.corso.book_web/`
creare le folder:
`controller`
`repository`
`model`
`service`

importare lo stesso codice del cprogetto Book precedente

4. 
`model/Book.java`

```java
package it.eng.corso.book_web.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table()

public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Column(name = "autore")
    private String author;
    private Double price;

}
```


5.
`repository/BookRepository.java`

```java
package it.eng.corso.book_web.repository;

import it.eng.corso.book_web.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {

    List<Book> findByAuthorContainingIgnoreCase( String author );

    List<Book> findByAuthor( String author );

    @Query(value = "select b from Book b where b.author = ?1 and b.author = ?2 ", nativeQuery = true)
    List<Book> findMiaQuery(String author, String param2 );


    @Query("select b from Book b where b.author = :pippo and b.author = :param2 and b.author = :param3")
    List<Book> findMiaQuery2(@Param("pippo") String author,@Param("param2") String param2,@Param("param3") String param3);


    @Query(value = "select * from Book b where b.pippo = :pippo ", nativeQuery = true)
    List<Book> findMiaQuery2(@Param("pippo") String author );
}

```

6.
`service/BookService.java`


```java
package it.eng.corso.book_web.service;

import it.eng.corso.book_web.model.Book;

import java.util.List;

public interface BookService {

    Book save(Book book);

    List<Book> findAll();

    List<Book> findByAuthor(String author );

    Book findById( Long id );

    void delete(Long id);

    Book update(Long id, Book book);

    Book partialUpdate(Long id, Book book);
}
```


7. 
`service/BookServiceImpl`


```java
package it.eng.corso.book_web.service;

import it.eng.corso.book_web.model.Book;
import it.eng.corso.book_web.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;

    @Override
    public Book save(Book book) {
        return bookRepository.save( book );
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase( author );
    }

    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id).get();
    }

    @Override
    public void delete(Long id) {
        bookRepository.deleteById( id );
    }

    @Override
    public Book update(Long id, Book book) {
        Book bookToUpdate = bookRepository.findById( id ).get();

        bookToUpdate.setAuthor(book.getAuthor());
        bookToUpdate.setTitle(book.getTitle());
        bookToUpdate.setPrice(book.getPrice());

        return bookRepository.save( bookToUpdate );
    }

    @Override
    public Book partialUpdate(Long id, Book book) {
        Book bookToUpdate = bookRepository.findById( id ).get();

        if(book.getAuthor()!=null)
            bookToUpdate.setAuthor(book.getAuthor());
        if(book.getTitle()!=null)
            bookToUpdate.setTitle(book.getTitle());
        if(book.getPrice()!=null)
            bookToUpdate.setPrice(book.getPrice());

        return bookRepository.save( bookToUpdate );
    }

}
```
8. 
`controller/BookController.java`
questo giro il nostro controller non avrà la notation @RestController ma è solo @Controller
In questo non verrà più restituita la serializzazione in JSON di un oggetto o di una lista di oggetti ma restituirà una stringa che sarà il path della view da visualizzare.

in questo caso il controller ottiene la lista di libri dal Service ma anzicchè serializzarli e restituirli cone un JSON, quello che fa è  fornire la lista dei books a model che fornirà una view dei libri.

```java
    @GetMapping("/")
    public String listBook(Model model){
        model.addAttribute("books", bookService.findAll());
        bookService.findAll();
        return "home";   // home.jsp
    }
```
la stringa del return --> return "home";   deve essere corrispondente a quella di un file home.jsp cosa che creeremo di seguito
9. 
sotto resources
creare le cartelle annidate:
`META_INF/resources/WEB_INF/views/`
queste cartelle non aono accessibili dell'esterno
dentro views creo
`home.jsp`




quando il nostro controller restituirà una stringa es `home` quello che Spring andrà a fare e concatenare a questa stringa il prefisso e il suffisso che andiamo a indicare nelle propierties 

quindi andiamo in `resources/application.properties` e inseriamo
```
spring.mvc.view.prefix=/WEB-INF/views   --> troverai le views al path indicato
spring.mvc.view.suffix=.jsp
```

In questo modo quando facciamo 
 `return "home";`  
in realtà il path completo sarà
`/WEB-INF/views/home.jsp`  




```java
<dependency>
<groupId>org.apache.tomcat.embed</groupId>
<artifactId>tomcat-embed-jasper</artifactId>
<scope>provided</scope>
</dependency>
<!-- https://mvnrepository.com/artifact/org.glassfish.web/jakarta.servlet.jsp.jstl -->
<dependency>
<groupId>org.glassfish.web</groupId>
<artifactId>jakarta.servlet.jsp.jstl</artifactId>
<version>3.0.1</version>
</dependency>
<dependency>
<groupId>jakarta.servlet.jsp.jstl</groupId>
<artifactId>jakarta.servlet.jsp.jstl-api</artifactId>
<version>3.0.0</version>
</dependency>

```
> [!CAUTION]
> potremmo ricevere questo errore
>.resource.ResourceHandlerUtils  "Path with "WEB-INF" or "META-INF": [WEB-INF/views/home.jsp]"
>
> 1) una soluzione puo essere andare nel pom.xml e commentare  <!-- <scope>provided</scope>--> nella dipendenza di tomcat-embed-jasper
>
> 2) se non funziona inserire questa dipendenza
> <dependency>
>  <groupId>org.apache.tomcat</groupId>
>  <artifactId>tomcat-jasper</artifactId>
>  <version>${tomcat.version}</version>
> </dependency>
>
> 4) crea in folder main una cartella webapp/WEB-INF/views e dentro sposta sposta home.jsp
> 
> 3) altrimenti vedi anche vedi anche [questo](https://stackoverflow.com/questions/29782915/spring-boot-jsp-404)



```html
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head><title>Books</title></head>
<body>
<h1>l'elenco dei miei books</h1>
${books}
</body>
</html>
```


```html
<html>
<head>
    <title>Books</title>
</head>
<body>
    <h1>L'elenco dei miei books</h1>
    <table>
        <tr>
            <th>Autore</th>
            <th>Titolo</th>
        </tr>
        <c:forEach items="${books}" var="book">
            <tr>
                <td>${book.author}</td>
                <td>${book.title}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
```

## Gestione task Step by Step

[Spring Boot REST API CRUD Operations with MySQL](https://medium.com/@Lakshitha_Fernando/spring-boot-rest-api-crud-operations-with-mysql-6f4e81382edc)


### realizzare un servizio REST per la gestione di Task
 
gli attributi del task sono:
id (Long)
title (String)
description (String)
completed (Boolean)
 
Implementare gli endpoints per:
Creare, leggere, aggiornare e cancellare (CRUD) tasks
restituire una lista di tutti i task
restituire una lista di quelli completati 
restituire una lista di quelli non ancora completati
 
le API richieste:
	•	POST /api/v1/tasks: Crea un nuovo task.
	•	GET /api/v1/tasks: Elenco di tutti i task.
	•	GET /api/v1/tasks/{id}: Dettaglio di un task.
	•	PUT /api/v1/tasks/{id}: Aggiorna i dettagli di un task.
	•	DELETE /api/v1/tasks/{id}: Cancella un task.
	•	GET /api/v1/tasks?completed=true/false: Filtra i task in base allo stato.
 
1. 
![Initializr](/img/13.png)

```mysql
use philip_db;

create table tasks(longint id NOT NULL AUTO_INCREMENT PRIMARY KEY, VARCHAR(255) title , VARCHAR(255) description, boolean completed);

insert into tasks(title, description, completed) values ("mio task", "descrizione del task", false);
```

2. creazione cartelle:
   * model
   * service
   * repository
   * controller

3. model/Task.java

```java
package it.eng.corso.task_service.model;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@Setter
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(length = 255, nullable = false)
    private String description;

    @NotNull
    private boolean completed;
}
```

4. repository/TaskRepository

```java
package it.eng.corso.task_service.repository;

import it.eng.corso.task_service.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = "Select * from tasks t where t.completed = :completed", nativeQuery = true)
    List<Task> getTaskByCompleted(@Param("completed") boolean completed);
}
```

5. service/TaskService

```java
package it.eng.corso.task_service.service;

import it.eng.corso.task_service.model.Task;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskService {

    List<Task> getAllTasks();
    Task getTaskById(Long id);
    Task saveTask(Task task);
    Task updateTask(Task task, Long id);
    Task updateCompletedTask(Task task, boolean completed );
    void deleteTask(long id);
    List<Task> getTaskByCompleted(boolean completed);
}
```

6. service/TaskServiceImpl

```java
package it.eng.corso.task_service.service;

import it.eng.corso.task_service.model.Task;
import it.eng.corso.task_service.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CacheManager cacheManager;

    //save task in db
    @Override
    public Task saveTask(Task task){
        cacheManager.getCache("tasks").evict(SimpleKey.EMPTY); // elimina tutti gli elemneti TasK in cache che hanno una key vuota
        return taskRepository.save(task);
    }

    @Override
    @Cacheable("tasks") // inserendo qui cachable una volta che viene fatta la query per la prima volta, i dati vengono coservati nell cahce e quindi, quando la funzione viene richiamata in seguito, non verrà lanciata la cache ma i dati verranno recuperati dalla cache
    public List<Task> getAllTasks(){
        return  taskRepository.findAll();
    }

    @Override
    @Cacheable("tasks") // la cache utilizza come value il parametro formale del metodo e come value i dati restituiti dalla queery
    public Task getTaskById(Long id){
        Optional<Task> task = taskRepository.findById(id);
        if(task.isPresent()){
            return task.get();
        }else{
            throw new RuntimeException();
        }
    }

    @Override
    public Task updateTask(Task task, Long id ){
        Task existTask = taskRepository.findById(id).orElseThrow(
                () -> new RuntimeException()
        );
        existTask.setDescription(task.getDescription());
        existTask.setTitle(task.getTitle());
        existTask.setCompleted(task.isCompleted());
        taskRepository.save(existTask);
        return existTask;
    }

    @Override
    public Task updateCompletedTask(Task task, boolean completed ){
        Task existTask = taskRepository.findById(task.getId()).orElseThrow(
                () -> new RuntimeException()
                );
        existTask.setDescription(task.getDescription());
        existTask.setTitle(task.getTitle());
        existTask.setCompleted(completed);
        taskRepository.save(existTask);
        return existTask;
    }

    @Override
    @CacheEvict("tasks") // questo specifica che quando si richiama il metodo deleteTask si va ad aggiornare il dato salvato in cache per mantenerlo coerente al db
    public void deleteTask(long id) {
        cacheManager.getCache("tasks").evict(SimpleKey.EMPTY); // questo specifica che ogni qualvolta richiamo la funzione mi va ad eleminare dalla cache l'elemento con la chiave vuota
        //check
        taskRepository.findById(id).orElseThrow(()-> new RuntimeException());
        //delete
        taskRepository.deleteById(id);

    }

    @Override
    public List<Task> getTaskByCompleted( boolean completed){ //@Param("completed")
       return  taskRepository.getTaskByCompleted(completed);
    }
}
```

7. 
controller/TaskController.java

```java
package it.eng.corso.task_service.controller;

import it.eng.corso.task_service.model.Task;
import it.eng.corso.task_service.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    //Save task
    @PostMapping
    public ResponseEntity<Task> saveTask(@RequestBody Task task){
        return new ResponseEntity<Task>(taskService.saveTask(task), HttpStatus.CREATED);
    }

    //Get all
    @GetMapping
    public List<Task> getAllTasks(){
        return taskService.getAllTasks();
    }

    //Update completed
    @PutMapping("{id}")
    public ResponseEntity<Task> updateTask(@PathVariable("id") Long id, @RequestBody Task task ){
        return new ResponseEntity<Task>(taskService.updateTask(task,id), HttpStatus.OK);
    }

    //Update completed
    @PutMapping("/update-completed/{completed}")
    public ResponseEntity<Task> updateCompletedTask(@PathVariable("completed") boolean completed, @RequestBody Task task ){
        return new ResponseEntity<Task>(taskService.updateCompletedTask(task,completed), HttpStatus.OK);
    }

    //Get by Id
    @GetMapping("{id}")
    public ResponseEntity<Task> getById(@PathVariable("id") Long id, @RequestBody Task task ){
        return new ResponseEntity<Task>(taskService.getTaskById(id), HttpStatus.OK);
    }

    //Delete Task
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteTask(@PathVariable("id") long id){
        taskService.deleteTask(id);
        return new ResponseEntity<String>("Task deleted.", HttpStatus.OK);
    }

    //Get all tasks where completed is equal tu the passed value
    @GetMapping("/get-all-completed/{completed}")
    public List<Task> getTaskByCompleted(@PathVariable("completed") boolean completed){
        return taskService.getTaskByCompleted(completed);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> exception(NoSuchElementException e){
        HashMap<String, Object> body = new HashMap<>();

        body.put("timestamp", LocalDateTime.now());
        body.put("Error_Code","500");
        body.put("message", e.getMessage() + "contattare l'assistenza");

        e.printStackTrace();
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

8. ### Creiamo un Exception Handler

   creiamo una nuova directory exception e dentro andiamo a creare la classe per gestire la nostra famgilia di eccezioni e che estenderà Runtime Exception
```java
package it.eng.corso.task_service.exception;

public class NoDataFoundException  extends RuntimeException{
}

```
   
/controller/TaskCOntroller.java
```java



....

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> exception(NoSuchElementException e){
        HashMap<String, Object> body = new HashMap<>();

        body.put("timestamp", LocalDateTime.now());
        body.put("Error_Code","500");
        body.put("message", e.getMessage() + "contattare l'assistenza");

        e.printStackTrace();
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
...
```
8. 
### Exeption Handler

### Caching
grazie alla Programmazione Orientate agli Aspetti 


poer la caching Spring implementa un [Proxy](https://refactoring.guru/design-patterns/proxy) con lo stesso nome del metodo su cui abbiamo messo l'annotation. In base all'annotation che stiamo implementato, il metodo del proxy aandrà a wrappare il metodo definito da noi su cui abbiamo posto l'annotation. Il metodo wrappato verrà eseguito o prima o dopo o in mezzo al codice del proxy  wrapper.  
Perchè utilizzare un Proxy?

* Proxies are commonly used when we want to augment the functionality of a target class, without directly modifying the class itself. Typically we wrap the underlying target class with a proxy class and expose exactly the same public interface. Calls to the target class should then be indistinguishable (on the face of it) from calls to the proxy class. One way of achieving this is to subclass the target class (more on this later).

Once we have this in place, the proxy is free to decorate any calls with additional behaviours. Examples of this include adding caching, logging or security functionality. Libraries such as Hibernate use proxies for features such as lazy-loading collections on one-to-many relationships.

The main benefit of proxies is that they allow us to just focus on writing our domain code, as the proxy implementations can be handled by the library code instead. This allows us to achieve quite a lot with minimal effort and we can avoid writing a lot of boilerplate code.

In Aspect Orientated Programming (AOP) proxies are actively encouraged. AOP's goal is to separate out cross-cutting concerns from business logic code and we can see that this is a great fit with proxies. There are also other mechanisms to write AOP code, but proxies are a sensible and fairly understandable starting point. *
[why proxy?](https://ntsim.uk/posts/a-closer-look-at-spring-proxies#why-proxyhttps://ntsim.uk/posts/a-closer-look-at-spring-proxies#why-proxy)

un errore che può verificarsi con l'uso del Proxy che va a wrappare l'implementazione di 2 nostri metodi A e B.

il comportamento corretto è chiamare sempre i medotodi dichiarati da noi sempre tramite l'interfaccia del Proxy.
quindi tramite il proxy chiamo prima A e poi sempre tramite l'interfccia del proxy chaomo B,  in questo modo in ogni chiamata viene eseguito il codice del proxy che wrappa A e B
Tuttavia l'errore si ha quando dall'interfaccia del Proxy chiamo il metodo A quindi esegui il codice che wrappa A, ma all'interno dell'esecuzione del metodo A richiamo B. In questo caso il richiamo di B da A, senza passare dall'interfaccia del Proxy non esegue il codice che wrappa B, da cui l'errore.


quando utiliziamo dei @Bean in Spring questi diventano dei proxy
appena aggiungiamo una cqualunque annotation l'annotation prevede una implementazione dell'annotation attraverso l'AOP  equindi a livello implementativo tramite un Proxy che wrappa il comportamento della nostra funzione.




It appears the main motivation of this proxying is to keep developers working within the application context. By default, Spring assumes that beans are meant to be consumed with singleton scope. This means that there are only single instances of beans managed within the context, and if we try to resolve a bean from it, we will retrieve the same instance every time.

This is generally the policy many popular dependency injection (DI) containers take towards managing their objects. In the majority of cases, this is the simplest and most efficient approach and is probably most in-line with developer's expectations of what DI containers should do.
[Why is this useful?](https://ntsim.uk/posts/a-closer-look-at-spring-proxies#why-is-this-useful)

#### meccanismo di @cache 

### meccanismo @Cacheable

15. per abilitare il caching dobbiamo specificare la notation `@EnableCaching` questa la possiamo mettere in qualunque punto del codice in cui abbiamo la nessità di usare la cache tuttavia inserendola  nel taskServiceApplication (main) la abilito globalmente.

```java
package it.eng.corso.task_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching  
public class TaskServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskServiceApplication.class, args);
		System.out.println("Server is Running");

	}
}
```

16. 
con l'annotation  @Cacheable("<nome_entity>") abilito il caching su una funzione per una determinata Entity.
inserendo qui cachable una volta che viene fatta la query per la prima volta, i dati vengono coservati nell cahce e quindi, quando la funzione viene richiamata in seguito, non verrà lanciata la cache ma i dati verranno recuperati dalla cache.

 la cache salva i dati in formato chiave valore e utilizza come value il parametro formale del metodo e come value i dati restituiti dalla queery

```java
 @Override
    @Cacheable("tasks") 
    public List<Task> getAllTasks(){
        return  taskRepository.findAll();
    }

    @Override
    @Cacheable("tasks") 
    public Task getTaskById(Long id){
        Optional<Task> task = taskRepository.findById(id);
        if(task.isPresent()){
            return task.get();
        }else{
            throw new RuntimeException();
        }
    }
```

inoltre possiamo usare la notation     @CacheEvict("tasks") questa specifica che quando si richiama il metodo deleteTask si va ad aggiornare il dato salvato in cache per mantenerlo coerente al db

 cacheManager.getCache("tasks").evict(SimpleKey.EMPTY); // questo specifica che ogni qualvolta richiamo la funzione mi va ad eleminare dalla cache l'elemento con la chiave vuota
```java
    @Override
    @CacheEvict("tasks") 
    public void deleteTask(long id) {
        cacheManager.getCache("tasks").evict(SimpleKey.EMPTY); 
        //check
        taskRepository.findById(id).orElseThrow(()-> new RuntimeException());
        //delete
        taskRepository.deleteById(id);

    }

```


### clear cache
19.
Abilitiamo in `/main/TaskServiceApplication` la schedulazione dei metodi con `@EnableScheduling` che abilita l'esecuxione determinate logiche/funzioni ogni tot di tempo, ovvero mi permette di eseguire il metodoto dopo un certo delay

```java
package it.eng.corso.task_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching  // inserendo qui enable cache essendo nel taskServiceApplication (main) la abilito globalmente.
@EnableScheduling 
public class TaskServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskServiceApplication.class, args);
		System.out.println("Server is Running");

	}
}
```
20.

la notation  `@CacheEvict(allEntries = true, cacheManager = "tasks")`
mi permette di svuotare la cache per tutte le istanze di "tasks".

service/TaskServiceImpl.java
```java
    @CacheEvict(allEntries = true, cacheManager = "tasks")
    @Scheduled(initialDelay = 2000, fixedDelay = 2000)
    public void clear(){
        System.out.println("svuoto cache");
    }
```
con la notation  `@Scheduled(initialDelay = 2000, fixedDelay = 2000)`
stiamo dicendo che il metodo clear deve eseguire dopo 2000ms dall'inizio e poi ogni 2000ms

in alternativa potremmo usare [Cron](https://www.javatpoint.com/java-cron-expression)
specificando giorno e ora di ripetizione del nostro metodo.

```java
    @CacheEvict(allEntries = true, cacheManager = "tasks")
    @Scheduled(cron = "0 0 3 * * MON_FRI")
    public void clear(){
        System.out.println("svuoto cache");
    }

```
in questo modo ad esempio eseguiamo alle 3 di notte


```java

```


```java

```
    
### HTTP Request

1) GET `http://localhost:8080/api/v1/tasks`

2) GET `http://localhost:8080/api/v1/tasks/1`

3) POST `http://localhost:8080/api/v1/tasks`
```json
{
    "title" : "my bsest task",
    "description" : "my beautifull task",
    "completed" : false
}
```
4) PUT `http://localhost:8080/api/v1/tasks/3`
```json
{
    "title" : "my thirth task",
    "description" : "less is more, less is better",
    "completed" : false
}
```
### Transactional
@Transactional serve a gestire le transazioni ACID

## 

vogliamo nascondere dal DB le chiamate che permettono di recuperare le informazione della nostra applicazione che non sono strettamente necessarie da esporre. 
capendo ad esempio che gli id sono progressivi e avedo accesso alle informazioni tramite gli id potrei recuperare info di altri utenti.

`uuid` lo andiamo al valorizzare prima del savataggio nel db della nostra Entity.

```java
import java.util.UUID;

....

@Override
public Book save(Book book) {
	book.setUuid(String.valueOf(UUID.randomUUID());
 return bookRepository.save();

}
```
 utilizzando l'uuid abbiamo più entropia nella scelta dell'id

```json
{
	"title": "Paperino",
	"author": "Topolino",
	"price": 50.0
}

```

![Initializr](/img/18.png)



Utilizzo il [DTO](https://www.baeldung.com/java-dto-pattern) come oggetto per passare informazioni tra il presentation layer e il business layer in modo da mantenere queste informazioni ancora più  riservate. il busines layer trasformerà DTO in Entity per comunicare con il Data Layer, e viceversa il business layer riceverà entity dal Data Layer e le traformerà in DTO per il presentation layer

![Initializr](/img/layers-4.svg)


creiamo la caterlla dto sotto main e poi la classe dto per la nostra entity

`/dto/BookDTO.java`
```java
package it.eng.corso.bookservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@NoArgsContructor // viene creato un costruttore con nessun parametro
@AllArgsCOntructor  // viene creato un costruttore con tutti i parametri
public class BookDTO {
    private String uuid;
    private String title;
    private String author;
    private Double price;
}
```

nella nostra entity inseriamo questi 2 annotation
`@NoArgsContructor`  abilita alla creazione di un costruttore con nessun parametro
`@AllArgsCOntructor`  abilita alla creazione di un costruttore con tutti i parametri

`model/Book.java`
```java
package it.eng.corso.bookservice.model;

import ...

@Entity
@Getter
@Setter
@Table()
@NoArgsContructor // viene creato un costruttore con nessun parametro
@AllArgsCOntructor  // viene creato un costruttore con tutti i parametri
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String uuid;
    private String title;
    @Column(name = "autore")
    private String author;
    private Double price;
}
```

a questo punto il presentation layer e il business layer si devono scambiare solo BookDTO
quindi ad esempio nel controller inseriamo 

`import it.eng.corso.bookservice.dto.BookDTO;`
ed eliminiamo
`import it.eng.corso.bookservice.model.Book;`

`/controller/BookController.java`
```java
package it.eng.corso.bookservice.controller;

import it.eng.corso.bookservice.dto.BookDTO;
import it.eng.corso.bookservice.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public List<BookDTO> findAll( ){
        return bookService.findAll();
    }

    @GetMapping("/search")
    public List<BookDTO> findByAuthor(@RequestParam String author ){
        return bookService.findByAuthor( author );
    }

    @GetMapping("/{id}")
    public BookDTO findById( @PathVariable Long id ){
        return bookService.findById( id );
    }

    @PostMapping
    public BookDTO save( @RequestBody BookDTO book ){
        return bookService.save( book );
    }

    @PutMapping("/{id}")
    public BookDTO update( @PathVariable Long id, @RequestBody BookDTO book ){
        return bookService.update(id, book );
    }

    @PatchMapping("/{id}")
    public BookDTO partialUpdate( @PathVariable Long id, @RequestBody BookDTO book ){
        return bookService.partialUpdate(id, book );
    }


    @DeleteMapping("/{id}")
    public void delete( @PathVariable Long id ){
        bookService.delete( id );
    }

}

```
stessa cosa in service/BookService.java

```java

```



```java

```



```java

```


## Review Project Step-By-Step

id : book uuid
stars: 1-to-5

il controller salva una nuova recensione

![Initializr](/img/19.png)

2. creazione cartelle:
   * model
   * service
   * repository
   * controller
   * tdo

3. resources/application.proprierties
   con `ddl-auto=true` se jpa non trova la tabella la va a creare 
jpa va a creare la tabella anche se il DB esiste ma le colonne non matchano perfettamente ai nomi dell'Entity che sono quelli che si aspettadi trovare

```java
spring.application.name=review-service

spring.datasource.url=jdbc:mysql://197.0.0.1:3306/philip_db
spring.datasource.username=root
spring.datasource.password=mypsw
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=none
```

4. 
```mysql
use philip_db;

show databases;

DROP TABLE IF EXISTS  reviews;

create table reviews( id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY , created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, starts FLOAT, uuid VARCHAR(255), uuid_book VARCHAR(255) );
```
5. 
la notation `@CreationTimestamp` inizializza l'attributo sul quale è dichiarato con il timestamp della creazione dell'ogetto
```
@CreationTimestamp
private LocalDataTime createdAt;
```
creato l'oggetto

`@UpdateTimestamp` valorizza l'attibuto quando viene aggiornato l'oggetto, questo viene valorizzato di hibernate

`model/Review.java`
```java
package it.eng.corso.review_service.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
    private String uuid;
    @Column(name = "uuid_book")
    private String uuidBook;
    @Column(name = "stars")
    private double stars;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
```

6. 
`dto/ReviewDTO.java`
```java
package it.eng.corso.review_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private String uuid;
    private String uuidBook;
    private double stars;

    private LocalDateTime createdAt;
}
```

7.
`repository/ReviewRepository.java`
```java
package it.eng.corso.review_service.repository;

import it.eng.corso.review_service.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

     List<Review> findByUuidBook(String uuidBook);

     Review findByUuid(String uuid);
}
```

8.

`service/ReviewService.java`
```java
package it.eng.corso.review_service.service;

import it.eng.corso.review_service.dto.ReviewDTO;


public interface ReviewService  {

    ReviewDTO save(ReviewDTO review);

    Double average(String uuidBook);
}
```

9.

`service/ReviewServiceImpl.java`
```java
package it.eng.corso.review_service.service;

import it.eng.corso.review_service.dto.ReviewDTO;
import it.eng.corso.review_service.model.Review;
import it.eng.corso.review_service.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private WebClient webClient;

    @Override
    public ReviewDTO save(ReviewDTO review){
        review.setUuid(String.valueOf(UUID.randomUUID()));
        return modelToDto(reviewRepository.save(dtoToModel(review)));
    }

    @Override
    public Double average(String uuidBook){
        return reviewRepository.findByUuidBook(uuidBook)
                .stream()
                .map(Review::getStars)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0D); // se c'è un errore restituirò zero
    }


    private Review dtoToModel(ReviewDTO review){
        return Review.builder()
                .uuid(review.getUuid())
                .uuidBook(review.getUuidBook())
                .stars(review.getStars())
                .createdAt(review.getCreatedAt())
                .build();
    }

    private ReviewDTO modelToDto(Review review){
        return ReviewDTO.builder()
                .uuid(review.getUuid())
                .uuidBook(review.getUuidBook())
                .stars(review.getStars())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
```

10.


`controller/ReviewController.java`
```java
package it.eng.corso.review_service.controller;

import it.eng.corso.review_service.dto.ReviewDTO;
import it.eng.corso.review_service.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ReviewDTO save(@RequestBody ReviewDTO review){ // il nostro json viaggia nel body della request
        return reviewService.save(review);
    }

    @GetMapping("/avarage")
    public Double save(@RequestParam String uuidBook){ //il parametro viaggia nella query string dopo il path della richiesta con (/reviews/avarage?uuidBook=<id-book>)
        return reviewService.average(uuidBook);
    }
}
```

11.
facciamo una HTTP Request che richiama il `save()` 

```json
HTTP POST
http://localhost:8080/api/v1/reviews

{
     "uuid_book" :  "5345-42424-424234-42323",
     "stars" : 4
}


Return:
{"uuid":"1004187d-39f1-48d0-89f7-4cdd8d04c752","uuidBook":null,"stars":4.0,"createdAt":"2024-12-03T15:02:59.012467"}
```

### Crezione di un WebClient 
creiamo `config` folder 
poi creiamo `WebClientConfig.java`
```java
package it.eng.corso.review_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

public class WebClientConfig {


    @Bean // per inserire questo elemento all'interno dell'application context , ovvero dice a spring creami un instanza di una diependeza
    public WebClient getWebClient() {
        return WebClient.builder().build();
    }
}
```

e nel pom.xml inseriamo
```xml 
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-webflux</artifactId>
		</dependency>

```

andando in 
`dto/BooKDTO.java` andiamo ad inserire  `private Double stars;` per avere informazioni sulle recensione medie di un determinato libro
```java
@Data
@Builder
@NoArgsConstructor // viene creato un costruttore con nessun parametro
@AllArgsConstructor  // viene creato un costruttore con tutti i parametri
public class BookDTO {
    private String uuid;
    private String title;
    private String author;
    private Double price;

    private Double stars;
}
```

 dopo aver cercato il book dalla nostra base dati 
andiamo ad arricchire il nostro book andando a recuperare i dati da un'altro microservizio
 attraverso il webClient

andando in `service/BookServiceImpl.java` possiamo andare a inserire un `WebClient` che va a fare una richiesta GET ad un determinato endpoint, 
passiamo la uri su cui fare la richiesta
passiamo il `queryParam` che il `ReviewsController` richiede per andare a rechiamare la la "average" API
il risultato viene inserito all'interno di un canale mono
e con `block()` lo rendiamo bloccante  in modo che il thread principale resta in attesa della risposta sul mono a seguito della richiesta del WebClient

per fare questo dichiariamo un `WebCLient`
```java
    @Autowired
    private WebClient webClient;
```

```java
    @Override
    public BookDTO findByUuid(String uuid) {
        BookDTO ret = modelToDTO(    bookRepository.findByUuid(uuid).get());
        // dopo aver cercato il book dalla nostra base dati
        //andiamo ad arricchire il nostro book andando a recuperare i dati da un'altro microservizio
        // attraverso il webClient

    ret.setStars(
            webClient.get() //il web client ci sta permettendo di fare una richiesta ad una particolare endpoint
                    .uri("http://localhost:8080/api/v1/reviews/average"
                    ,uriBuilder -> uriBuilder.queryParam("uuidBook", uuid).build())
                    .retrieve()
                    .bodyToMono(Double.class)
                    .block() // stiamo bloccando il servio in maniera bloccante
            //il thread principale resta in attesa della risposta sul mono a seguito della richiesta del WebClient
    );
        return ret;
    }
```
    
```java


package it.eng.corso.bookservice.service;

import it.eng.corso.bookservice.dto.BookDTO;
import it.eng.corso.bookservice.model.Book;
import it.eng.corso.bookservice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private WebClient webClient;

    @Override
    public BookDTO save(BookDTO book) {

        book.setUuid(String.valueOf(UUID.randomUUID()));
        return modelToDTO(bookRepository.save(  dtoToModel(book)));

    }

//    @Override
//    public Optional<Book> findByUuid(String uuid){
//        return modelToDTO( bookRepository.findByUuid(uuid).orElseThrow());
//    }

    @Override
    public List<BookDTO> findAll() {

        return bookRepository.findAll()
                .stream()  // prendo uno streamo
                .map(this::modelToDTO) // passo ogni elemento dello stream alla funzione model to DTO e ottengo uno oggetto DTO
                .toList();
    }

    @Override
    public List<BookDTO> findByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase( author )
                .stream()  // prendo uno streamo
                .map(this::modelToDTO) // passo ogni elemento dello stream alla funzione model to DTO e ottengo uno oggetto DTO
                .toList();
    }

    @Override
    public BookDTO findByUuid(String uuid) {
        BookDTO ret = modelToDTO(    bookRepository.findByUuid(uuid).get());
        // dopo aver cercato il book dalla nostra base dati
        //andiamo ad arricchire il nostro book andando a recuperare i dati da un'altro microservizio
        // attraverso il webClient

    ret.setStars(
            webClient.get() //il web client ci sta permettendo di fare una richiesta ad una particolare endpoint
                    .uri("http://localhost:8080/api/v1/reviews/average"
                    ,uriBuilder -> uriBuilder.queryParam("uuidBook", uuid).build())
                    .retrieve()
                    .bodyToMono(Double.class)
                    .block() // stiamo bloccando il servio in maniera bloccante
            //il thread principale resta in attesa della risposta sul mono a seguito della richiesta del WebClient
    );
        return ret;
    }

    @Override
    public void delete(String uuid) {
        Book bookToDelete = bookRepository.findByUuid(uuid).orElseThrow();
        bookRepository.delete( bookToDelete );
    }

    @Override
    public BookDTO update(String uuid, BookDTO book) {
        BookDTO bookToUpdate = bookRepository.findByUuid( uuid ).get();
        bookToUpdate.setAuthor(book.getAuthor());
        bookToUpdate.setTitle(book.getTitle());
        bookToUpdate.setPrice(book.getPrice());

        return modelToDTO(bookRepository.save(  dtoToModel(book)));
    }

    @Override
    public BookDTO partialUpdate(String uuid, BookDTO book) {
        BookDTO bookToUpdate = bookRepository.findByUuid( uuid ).get();

        if(book.getAuthor()!=null)
            bookToUpdate.setAuthor(book.getAuthor());
        if(book.getTitle()!=null)
            bookToUpdate.setTitle(book.getTitle());
        if(book.getPrice()!=null)
            bookToUpdate.setPrice(book.getPrice());

        return modelToDTO(bookRepository.save(  dtoToModel(book)));
    }

    private Book dtoToModel ( BookDTO book){
        return Book.builder()
                .uuid(book.getUuid())
                .title(book.getTitle())
                .author(book.getAuthor())
                .price(book.getPrice())
                .build();
    }

    private BookDTO modelToDTO ( Book book){
        return BookDTO.builder()
                .uuid(book.getUuid())
                .title(book.getTitle())
                .author(book.getAuthor())
                .price(book.getPrice())
                .build();
    }
}
```

qui abbiamo la limitazione che stimao scolpendo l'endpoint ip e porta del microservizio 
questo problema può essere risolto attraverso un discoveryService dove i microservizzi si andranno a registrare e ogni microservizio verrà individuato da un nome e non da un endpoint.

inoltre per aumentare la scalabilità non setto una porta fissa per un determinato servizio, altrimenti diversi microservizi andrebbero a collidere. 


per fare ciò necessitiamo nel servizio book-service
`pom.xml`
```xml
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-webflux</artifactId>
		</dependency>
```


![Initializr](/img/20.png)

A questo punto facendo una richiesta per recuperare un book dall'id ci accorgiamo che ome oggetto di ritorno otterremo un oggetto con il campo stars valorizzato con il valore medio



### Error Handling Book-Service


### Data Validation
validazione formale del dato: verifica se il campo è effettivamente stato valorizzato nella forma corretta
ad esempio per una email verifico che ci sia la at @ ce ci sia un .it .com ecc.

nel progetto `Book-Service`
aggiungo nel `pom.xml` la dipendenza per la validation dei dati

```xml
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

in `controller/BookCOntroller.java`
aggiungo `@Valid` per la validazione formale 
```java
    @PostMapping
    public BookDTO save( @RequestBody @Valid BookDTO book ){
        return bookService.save( book );
    }
```

[Package jakarta.validation.constraints](https://jakarta.ee/specifications/bean-validation/3.0/apidocs/jakarta/validation/constraints/package-summary)

possiamo utilizzare ilmpackage import jakarta.validation.constraints.*; per importare le notation di validation che potremmo applicare all'ogetto BookDTO in modo che quando il presentation layer prova a passare un BookDTO che non rispetta la validazione formale viene restituto un errore.
e quindi possimao utilizzare le notation:

```java
    @NotNull(message = "il titolo non può essere nullo")
    @Size(min = 5, max= 50)
    @Email   
    @Null
```
    
```java
package it.eng.corso.bookservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor // viene creato un costruttore con nessun parametro
@AllArgsConstructor  // viene creato un costruttore con tutti i parametri
public class BookDTO {
    private String uuid;
    @NotNull(message = "il titolo non può essere nullo")
    @Size(min = 5, max= 50)
    private String title;
    @Email
    private String author;
    private Double price;
    @Null
    private Double stars;
}
```


possiamo fare una validatione per gruppi
creiamo la flder groups
e dentro
 * Step1
 * Step2
 * Step3

aggiungiamo i gruppi di annotation negli attributi

```java
public class BookDTO {
    @Null(groups = Step1.class)
    private String uuid;
    @NotNull(message = "il titolo non può essere nullo", groups = {Step2.class, Step1.class})
    @Size(min = 5, max= 50)
    private String title;
    @Email( groups =  Step2.class)
    private String author;
    private Double price;
    @Null
    private Double stars;
}
```

mi validi BookDTO seguendo le notation indicate in Step1.class
in questo caso valido BookDTO solo rispetto alle notatio del gruppo di appartenenza 1
```java
    @PostMapping
    public BookDTO save( @RequestBody @Validated(Step1.class) BookDTO book ){
        return bookService.save( book );
    }


```

### Aggiungiamo questa dipendenza per utilizzare la libreria di OpenAPI UI

[Swagger Petstore - OpenAPI 3.0](https://editor.swagger.io/)
lo swagger dato l' .XML di OpenAPI ci genera l'interfaccia grafica con gli endpoint. di fatto lo swagger ci serve per documentare i nostri endpoint
pom.xml
```xml
		<dependency>
			<groupId>org.springdoc</groupId>
			<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
			<version>2.5.0</version>
		</dependency>
```

quindi per usare swagger si va in `localhost:8080/v3/api-docs`
si va a recuperare il json nella nosta applicazione e si va ad incollare nella scheramta del [link](https://editor.swagger.io/)
questo ci genererà tutte le chiamate HTTP al nostro controller.

quindi openAPI ci permette a disposizione uno strumento per descrivere e documentare le chiamate al nostro controller in formato json 
recuperabile con la chiamata <endpoint>/v3/api-docs

OpenAPI ci permette anche di inserire le notation indicando il messaggio da includere all'interno della response http con un determinato codice
allo stesso modo dall' XML di OpenAPI possiamo andarci a creare il controller
```java
    @GetMapping("/{uuid}")
    @ApiResponse( value = {
            @ApiResponse(responseCode = 200, description = "tutto ok"),
            @ApiResponse(responseCode = 404, description = "L'uuid indicato  non esiste sulla base dati")
    })
    public BookDTO findById( @PathVariable String uuid ){
        return bookService.findByUuid( uuid );
    }
```

con la notation 
@CrossOrigin sulla nostra classe controller possiamo andare ad aprire il nostro endpoint verso tutto oppure creare una whitelist con gli endpoint che ci possono raggiungere
[Cors](https://www.baeldung.com/spring-cors)


```java


```

```java


```


```java


```






## Reference
[A closer look at Spring proxies](https://ntsim.uk/posts/a-closer-look-at-spring-proxies#why-is-this-useful)
