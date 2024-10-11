# Progetto: Piattaforma di Streaming con Acquisto e Noleggio di Film
Questo progetto mira a sviluppare una piattaforma di streaming, simile all'app TV Show Time, che consente agli utenti di gestire la propria esperienza di visione di film e serie TV, con l'aggiunta di funzionalità per acquistare e noleggiare contenuti.

Aggiungere ruolo così da avere una sola classe utente che poi si differenzia per metodi in base al ruolo

## Utenti
### Utente
Per ogni Utente avremo una serie di attributi:
- Id
- Nome
- Cognome
- Telefono
- Email
- Password
- Ruolo(User o Admin)
- Status Abbonamento
- Lista di film/serie già viste
- Lista di film/serie da vedere



## Film/Serie TV
I film possono essere di diversi tipi: horror, romantico, commedia, ecc (così come le serie)
Per ogni Film avremo una serie di attributi:
- Id
- Titolo
- Durata
- Genere
- Regista
- Cast
- Anno di uscita
- Se è conclusa oppure no (serie tv)
- Rating iMdb
- Prezzo acquisto
- Prezzo noleggio
- Trama/Descrizione
- Suggeriti (?)
- Rating per età (?)
- Flag che identifichi se è acquistabile o solo noleggiabile o non più disponibile

## Acquisto
Per ogni noleggio/acquisto avremo:
- Abbonamento
- Flag pagato
- Stato ordine
- Film/serie ordinato/acquistato
## Noleggio
Per un noleggio avremo:
- Data inizio noleggio
- Data fine noleggio
- Costo totale noleggio
- Flag pagato
- Film/serie noleggiato

## Funzionalità degli utenti
### Un Utente Base potrà:
- Creare un ordine a partire da un film/serie contrassegnato come ordinabile
- Vedere i propri ordini
- Vedere i propri film/serie
- Cancellare un ordine
- Creare un acquisto a partire da un film/serie contrassegnato come acquistabile
- Vedere i propri acquisti
- Creare un noleggio
- Vedere i propri noleggi
- Cancellare un noleggio
- Cancellare la propria utenza
- Modificare i dati dell’utente
- Ricercare un film secondo diversi criteri (prezzo, colore, marca, modello, ecc.)
- Ottenere i dettagli di un film specifico
### Un Utente Premium (cioè con lo status abbonamento settato true) potrà:
- Creare un ordine a partire da un film/serie contrassegnato come ordinabile
- Vedere i propri ordini
- Vedere i propri film/serie
- Cancellare un ordine
- Creare un acquisto a partire da un film/serie contrassegnato come acquistabile
- Vedere i propri acquisti
- Creare un noleggio
- Vedere i propri noleggi
- Cancellare un noleggio
- Cancellare la propria utenza
- Modificare i dati dell’utente
- Ricercare un film secondo diversi criteri (prezzo, colore, marca, modello, ecc.)
- Ottenere i dettagli di un film specifico
- Avere accesso ad una lista di titoli premium
- Accesso a Sconti su nuovi film/serie
### Un Admin potrà:
- Aggiungere un film/serie
- Modificare un film/serie
- Cancellare un film/serie
- Cambiare lo stato di un film/serie
- Creare un ordine per un utente
- Cancellare un ordine per un utente
- Modificare un ordine per un utente
- Creare un noleggio per un utente
- Cancellare un noleggio per un utente
- Modificare un noleggio per un utente
- Creare un acquisto per un utente
- Cancellare un acquisto per un utente
- Modificare un acquisto per un utente
- Verificare un film/serie quante vendite ha fatto in un determinato periodo di tempo
- Verificare un film/serie quanti soldi ha generato in un determinato periodo di tempo
- Verificare il guadagno della piattaforma in un determinato periodo
- Verificare i film/serie attualmente ordinabili/acquistabili/non disponibili/nuovi
- Cancellare un utente
- Modificare un utente
- Ottenere il film/serie più venduto/visto in un dato periodo
- Ottenere il film/serie più ricercato/ordinato
- Ottenere i dettagli di un film/serie specifico
## Autenticazione e Registrazione
Il sistema dovrà inoltre permettere il login e la registrazione degli utenti attraverso due rotte specifiche.
