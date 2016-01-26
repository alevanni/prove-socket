# prove-socket
Esercizi sull'uso dei sockets
Maggio 2015
La chat non funziona, il multiechoserver e il client si. Nemmeno il client per la chat funziona. Sono stati scopiazzati da una vecchia versione del multiechoserver, che non funzionava. Ora il multiechoserver funziona, quindi riprova.



-----------------------------------------------------



20/08/15
La chat funziona, ma solo da terminale (graficamente e' un incubo).
Vanno implementate la disconnessione e l'interfaccia grafica.
I programmi da usare sono provachat.java (per il server) e provachatclient.java (per il client).
Se monti il server sul raspi ricordati il port forwarding per l'esterno.

26/01/16
La chat funziona, la disconnessione e' brutta ma funziona, l'interfaccia manca.
Ai due utenti sono collegati due pin del raspberry (GPIO#18 e GPIO#23), attaccati a due led
(rosso e giallo). I led si accendono per 0.5 secondi quando l'utente corrispondente si collega 
o manda un messaggio.
Ricordati di aggiungere anche la descrizione del circuito. La resistenza da usare e' quella da 330 ohm.