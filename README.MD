# Martone-Criscuolo_es5_scg

Per l'analisi semantica abbiamo adottato le seguenti convenzioni:
1. Per ```+, -, *, /``` viene assunto il tipo di dimensione maggiore
   (come mostrato anche nella tabella che ci è stata fornita);
2. L'operatore ```&``` ammette concatenazioni solo tra stringhe;
3. Gli operatori di confronto di disuguaglianza (```<, <=, ...```) sono
    ammessi solo tra *interi* e *real*, mentre gli operatori di uguaglianza
   (```=, !=```) sono ammessi anche tra *stringhe*. Tutti restituiscono
   un valore di tipo booleano;
4. Se un parametro è di output, ovvero il suo tipo è ```out <type>```, allora
    quando viene passato come input ad un'altra funzione viene passato come **out**
    e non come **in**, mentre se viene utilizzato in un espressione (o come rval o lval),
    viene dereferenziato in automatico;
5. Abbiamo aggiunto il supporto alla ricorsione, tuttavia abbiamo deciso di non
    renderla disponibile per il main;
6. Non permettiamo l'inzializzazione di una variabile con sè stessa o con una variabile definita
    dopo la suddetta;
7. L'**IF** crea un nuovo scoping nel quale è possibile anche oscurare identificatori già dichiarati
   (stesso discorso per il **WHILE**);
8. Anche se non vengono dichiarati nuovi identificatori in un determinato livello di scoping, la tabella
    viene creata lo stesso e lasciata vuota;
9. Non è possibile utilizzare una funzione (o una variabile) prima che questa sia dichiarata;
10. È previsto il meccanismo di oscuramento delle variabili;

Per la generazione del codice, abbiamo fatto le seguenti scelte:
1. vista la difficoltà nel gestire i puntatori, stringhe e le conversioni
   verso le stringhe, come intestazione di ogni programma MyFun abbiamo deciso di aggiungere delle funzioni
   di servizio che ci aiutano nei vari compiti;
2. Per la dichiarazione degli identificatori, dal momento che il C non contempla un'istruzione
   del tipo `id = id(...)` e visto che permettiamo la dichiarazione di funzioni e variabili
   con lo stesso identificatore, nella generazione del codice viene effettuata l'aggiunta di un
   suffisso *var* oppure *fun* se si tratta di una variabile o una funzione, rispettivamente;
3. Quando viene fatta una **READ** su una lista di variabili (per stringhe leggere punto 4), viene generata una `scanf(...)` diversa
    per ogni variabile e, se è presente anche l' `Expr` finale (stringa da stampare), viene generata prima
   la rispettiva `printf(...)` e poi il resto;
4. Per la **READ** di stringhe, visto che non esistono come tipo in C, abbiamo realizzato una helper function `getln()`
   che permette la creazione di un `char *` di dimensione dinamica contenente la sequenza di caratteri data in input da
   tastiera;

Ulteriori type rules utilizzati durante lo sviluppo sono:
- **DIVINT, Elevamento a potenza**

| op     | operando1 | operando2 | risultato |
|--------|-----------|-----------|-----------|
| DIVINT | INTEGER   | INTEGER   | INTEGER   |
| POW    | INTEGER   | INTEGER   | INTEGER   |
| POW    | REAL      | REAL      | REAL      |
| POW    | INTEGER   | REAL      | REAL      |
| POW    | REAL      | INTEGER   | REAL      |

- **IF-THEN-ELSE**

<img src="https://latex.codecogs.com/svg.image?\frac{\Gamma\vdash&space;e:boolean\&space;\land&space;\Gamma\vdash&space;stmt1:notype\&space;\land&space;\Gamma\vdash&space;stmt2:notype}{\Gamma\vdash&space;if\&space;e\&space;then\&space;stmt1\&space;else\&space;stmt2\&space;end\&space;if&space;:notype}" title="\frac{\Gamma\vdash e:boolean\ \land \Gamma\vdash stmt1:notype\ \land \Gamma\vdash stmt2:notype}{\Gamma\vdash if\ e\ then\ stmt1\ else\ stmt2\ end\ if :notype}" />

- **Operatore di uguaglianza**

<img src="https://latex.codecogs.com/svg.image?\frac{\Gamma\vdash&space;e_1:\tau&space;\land&space;\Gamma\vdash&space;e_2:\tau}{\Gamma\vdash&space;e_1&space;==&space;e_2:boolean" title="\frac{\Gamma\vdash e_1:\tau \land \Gamma\vdash e_2:\tau}{\Gamma\vdash e_1 == e_2:boolean" />

- **Assegnamento con parametri di tipo out**

<img src="https://latex.codecogs.com/svg.image?\\\frac{\Gamma\vdash&space;e_1:out\&space;\tau&space;\land&space;\Gamma\vdash&space;e_2:\tau}{\Gamma\vdash&space;\&space;^*e_1&space;=&space;e_2:notype}\\\\\\\frac{\Gamma\vdash&space;e_1:\tau&space;\land&space;\Gamma\vdash&space;e_2:out\&space;\tau}{\Gamma\vdash&space;e_1&space;=\&space;^*&space;e_2:notype}&space;" title="\\\frac{\Gamma\vdash e_1:out\ \tau \land \Gamma\vdash e_2:\tau}{\Gamma\vdash \ ^*e_1 = e_2:notype}\\\\\\\frac{\Gamma\vdash e_1:\tau \land \Gamma\vdash e_2:out\ \tau}{\Gamma\vdash e_1 =\ ^* e_2:notype} " />

- **Read statement**

<img src="https://latex.codecogs.com/svg.image?\frac{\Gamma\vdash&space;id_1:\tau_1\&space;,&space;...\&space;,&space;id_i:\tau_i&space;\&space;\land&space;\Gamma\vdash&space;expr:string}{\Gamma\vdash&space;\%\&space;id_1\&space;,&space;...,\&space;&space;id_n,\&space;expr&space;:&space;notype}" title="\frac{\Gamma\vdash id_1:\tau_1\ , ...\ , id_i:\tau_i \ \land \Gamma\vdash expr:string}{\Gamma\vdash \%\ id_1\ , ...,\ id_n,\ expr : notype}" />

