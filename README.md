# Spring_note


## 1. Basi di Spring
### 1.1 Spring Framework
Spring offre una serie di moduli che coprono vari aspetti dello sviluppo:

Spring Core: Gestione dei bean e Inversion of Control (IoC).
Spring MVC: Framework per lo sviluppo di applicazioni web.
Spring Data: Integrazione con database.
Spring Security: Gestione della sicurezza.
Spring Cloud: Strumenti per microservizi, come service discovery, API gateway, e circuit breaker.
### 1.2 Spring Boot
Spring Boot semplifica il setup con funzionalità come:

Autoconfigurazione: Configura automaticamente le componenti in base alle dipendenze nel progetto.
Starter Dependencies: Pacchetti preconfigurati per aggiungere funzionalità come Spring Web o Spring Data.
Embedded Servers: Tomcat o Jetty integrati per eseguire applicazioni standalone.
#### 2. Architettura a Microservizi con Spring
Un'architettura a microservizi prevede lo sviluppo di servizi indipendenti, ognuno con uno scopo specifico, comunicanti via protocolli come HTTP o message brokers (es. RabbitMQ, Kafka). Spring offre strumenti per:

Service Discovery: Utilizzando Eureka o Consul.
API Gateway: Con Spring Cloud Gateway.
Comunicazione inter-servizio: Con REST, Feign Client o Event Streaming.
Tolleranza ai guasti: Con Resilience4j o Spring Cloud Circuit Breaker.
#### 3. Annotazioni Principali
Spring utilizza annotazioni per configurare i componenti e definire il comportamento dell'applicazione. Ecco le più comuni:

##### 3.1 Gestione dei Componenti e IoC
@Component: Marca una classe come bean gestito da Spring.
@Service: Indica un componente di logica aziendale.
@Repository: Indica un componente che interagisce con il database.
@Controller / @RestController: Definisce un controller per gestire richieste HTTP (con @RestController che include @ResponseBody per i dati JSON).
##### 3.2 Configurazione
@Configuration: Definisce una classe di configurazione.
@Bean: Dichiara un bean da aggiungere al contesto Spring.
@PropertySource: Carica file di configurazione esterni (es. .properties).
##### 3.3 Web e API
@RequestMapping: Mappa le richieste HTTP a metodi o classi.
@GetMapping, @PostMapping, ecc.: Versioni specializzate per richieste HTTP GET, POST, ecc.
@PathVariable: Estrae parametri dalla URL.
@RequestParam: Estrae parametri dalla query string.
@RequestBody: Legge il corpo della richiesta HTTP (tipicamente JSON).
@ResponseBody: Scrive direttamente una risposta HTTP.
##### 3.4 Dependency Injection
@Autowired: Inietta dipendenze automaticamente.
@Qualifier: Specifica quale bean iniettare se ci sono ambiguità.
@Value: Inietta valori dalle proprietà configurate.
##### 3.5 Microservizi Specifiche
@EnableDiscoveryClient: Abilita un servizio come client in un sistema di Service Discovery (Eureka, Consul).
@FeignClient: Usato per comunicare con altri servizi tramite REST.
@CircuitBreaker: Implementa un meccanismo di fallback per tolleranza ai guasti.
#### 4. Esempio Base
Ecco un semplice esempio per un microservizio REST:

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
5. Configurazione di Microservizi
5.1 Service Discovery con Eureka
Server Eureka:

@EnableEurekaServer
@SpringBootApplication
public class EurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
Client Eureka:

@EnableDiscoveryClient
@SpringBootApplication
public class MyMicroserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyMicroserviceApplication.class, args);
    }
}
