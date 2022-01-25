#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
char *getln(){ char *line = NULL, *tmp = NULL; int size = 0, index = 0; int ch = EOF; while (ch) { ch = getc(stdin); /* Check if we need to stop. */ if (ch == EOF || ch == '\n') ch = 0; /* Check if we need to expand. */ if (size <= index) { size += 1; tmp = realloc(line, size); if (!tmp) { free(line); line = NULL; break; } line = tmp; } /* Actually store the thing. */ line[index++] = ch; } return line; }
char *customConcat(char *str1, char *str2){ char *newStr1 = malloc(strlen(str1) + 1); char *newStr2 = malloc(strlen(str2) + 1); strcpy(newStr1, str1); strcpy(newStr2, str2); char *outStr = strcat(newStr1, newStr2); return outStr; }
char *fromRealToString(double input){ char *buffer = malloc(64*sizeof(char)); snprintf(buffer, 64, "%lf", input); return buffer; }
char *fromIntegerToString(int input){ char *buffer = malloc(64*sizeof(char)); snprintf(buffer, 64, "%d", input); return buffer; }
char *fromBooleanToString(int input){ if(input == 0){ return "false"; } else { return "true"; } }
double sommafun(double n1var, double n2var){
return n1var + n2var;

}
double prodottofun(double n1var, int n2var){
int ivar = 1;
double tempvar = n1var;
while(ivar < n2var){
n1var = n1var + tempvar;
ivar = ivar + 1;

}
return n1var;

}
int divInterafun(int n1var, int n2var){
if(n1var < 0 || n2var < 0){
return -1;

}
return n1var/n2var;

}
double potenzafun(double n1var, double n2var){
return pow(n1var, n2var);

}
int fibonaccifun(int nvar){
if(nvar == 0){
return 0;

}
if(nvar == 1){
return 1;

}
return fibonaccifun(nvar - 1) + fibonaccifun(nvar - 2);

}
int main(void){
int opvar;
printf("%s\n", "Scegli un'operazione:");
printf("%s\n", "1 Somma");
printf("%s\n", "2 Moltiplicazione");
printf("%s\n", "3 Divisione intera tra numeri positivi");
printf("%s\n", "4 Elevamento a potenza");
printf("%s\n", "5 Successione di Fibonacci");
scanf("%d", &opvar);
getchar();

if(opvar == 1){
double n1var, n2var, resultvar;
printf("%s\n", "Inserisci il primo numero reale");
scanf("%lf", &n1var);
getchar();

printf("%s\n", "Inserisci il secondo numero reale");
scanf("%lf", &n2var);
getchar();

resultvar = sommafun(n1var, n2var);
printf("%lf\n", resultvar);

}
if(opvar == 2){
double resultvar, n1var;
int n2var;
printf("%s\n", "Inserisci il primo numero (reale)");
scanf("%lf", &n1var);
getchar();

printf("%s\n", "Inserisci il secondo numero (intero)");
scanf("%d", &n2var);
getchar();

resultvar = prodottofun(n1var, n2var);
printf("%lf\n", resultvar);

}
if(opvar == 3){
int n1var, n2var, resultvar;
printf("%s\n", "Inserisci il primo numero (intero)");
scanf("%d", &n1var);
getchar();

printf("%s\n", "Inserisci il secondo numero (intero)");
scanf("%d", &n2var);
getchar();

resultvar = divInterafun(n1var, n2var);
printf("%d\n", resultvar);

}
if(opvar == 4){
double n1var, n2var, resultvar;
printf("%s\n", "Inserisci il primo numero (reale)");
scanf("%lf", &n1var);
getchar();

printf("%s\n", "Inserisci il secondo numero (reale)");
scanf("%lf", &n2var);
getchar();

resultvar = potenzafun(n1var, n2var);
printf("%lf\n", resultvar);

}
if(opvar == 5){
int nvar;
int resultvar;
printf("%s\n", "Inserisci un numero (intero)");
scanf("%d", &nvar);
getchar();

resultvar = fibonaccifun(nvar);
printf("%d\n", resultvar);

}
return 0;
}
