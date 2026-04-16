STORE MANAGEMENT API 

Descrizione 
- Applicazione backend sviluppata in Java con Spring Boot per la gestione di clienti e ordini in un contesto e-commerce. Il progetto espone API REST per operazioni CRUD e gestione degli ordini.
Funzionalità
- Gestione clienti, con creazione cliente, ricerca per id ed eliminazione
- Gestione ordini, con creazione ordine associato a cliente, Workflow stati ordine ( created-> pending-> shipped-> completed), validazione su disponibilità e totale
- statistiche, con anlisi ordini per mese e conteggio ordini per stato

Testing 
- Unit Test sul Service con Mockito
- Integration Test con MockMvc e database in-memory

Architettura 
-	Controller → gestione richieste HTTP
-	Service → logica di business
-	Repository → accesso dati (Spring Data JPA)
-	DTO → separazione tra API e modello dati

Tecnologie usate 
Java, Spring Boot, Spring Data JPA, Mockito e JUnit 5, H2 Database(test) 

Evoluzioni futute
- integrazione con sistemi ERP tramite WebClient
- introduzione di chiamate REST asincrone
- gestione avanzata degli errori
- miglioramento sicurezza
- logging e monitoring

Obiettivo del progetto 
Progetto sviluppato per consolidare competenze su:
	-	sviluppo backend con Spring Boot
	-	testing 
	-	gestione di logiche di business reali 
