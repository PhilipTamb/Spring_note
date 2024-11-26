# Spring_note

1. [Basi di Spring](#1-basi-di-spring)
    - [Spring Framework](#spring-framework)
2. [Struttura Base del Progetto](#2-struttura-base-del-progetto)
3. [Configurazione DB](#3-configurazione-db)
4. [Spring Repository e interfacciamento con il DB](#4-spring-repository-e-interfacciamento-con-il-db)
5. [Primo progetto Step-by-Step](#5-primo-progetto-step-by-step)

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

## 5 Primo progetto Step-by-Step

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
CREATE TABLE philip_db.Contatti (
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
dentro
`spring.jpa.hibernate.ddl-auto=none`
crea una cartella `model`
crea un file  `Contatto` dentro `model`

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
crea nuovo package `repository`
crea nuova interfaccia `ContattoRepository`

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
creiamo un nuovo package service
creiamo una nuova interfaccia `ContattoService`  qui specifichiamo l'interfaccia con la signeture dei metodi che la nostra business logic dovrà implementare. per esempio se vogliamo ricevere tutti i contatti implementiamo findAll() 
```java
package it.eng.corso.contattoservice.service;


import it.eng.corso.contattoservice.model.Contatto;

import java.util.List;

public interface ContattoService {
    List<Contatto> findAll();  // nell'implementazione lancierà una query che restituisce la lista contente tutti i contatti del DB
}
```
per il nome possiamo chiamare il file dell'interfaccia con Inome_interfaccia con la I all'inizio
oppure andare a chiamare la classe che implementa l'interfaccia come nome_fileImpl con Impl finale che specifica l'implementazione/utilizzo dell'interfaccia.


component scan all'inizio va a visionare il codice per trovare eventuali stereotype come Service, Repository e Controller

9. implementazione dell'interfaccia e dei metodi che lanciano le query e recuperano le informazioni dal DB.
creiamo all'interno della folder `service` il file che implementa l'interfaccia definita nel file `ContattoService`.
questo file lo chiameremo `ContattoServiceImpl`

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

10. creiamo la folder controller dentro la quale creiamo ContattoController
questo componente dovrà rispondere alla richiesta di un client che avverrà ad un determinato path.
per fare ciò  utiliziamo lo stereitype @RestController. 
il @RestController restituirà al client un file json. questo componente richiederà le informazioni direttamente al Service

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

`ContattoServiceAppliation` tasto destro `Run`
