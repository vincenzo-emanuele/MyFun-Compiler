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
    e non come **in**;
5. Abbiamo aggiunto il supporto alla ricorsione, tuttavia abbiamo deciso di non
    renderla disponibile per il main;
6. La dichiarazione di un identificatore deve essere unica tra funzioni e variabili.
    Questa scelta è dovuta al fatto che in C non è possibile 


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

