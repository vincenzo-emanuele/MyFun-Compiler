#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
char *getln(){ char *line = NULL, *tmp = NULL; int size = 0, index = 0; int ch = EOF; while (ch) { ch = getc(stdin); /* Check if we need to stop. */ if (ch == EOF || ch == '\n') ch = 0; /* Check if we need to expand. */ if (size <= index) { size += 1; tmp = realloc(line, size); if (!tmp) { free(line); line = NULL; break; } line = tmp; } /* Actually store the thing. */ line[index++] = ch; } return line; }
char *customConcat(char *str1, char *str2){ char *newStr1 = malloc(strlen(str1) + 1); char *newStr2 = malloc(strlen(str2) + 1); strcpy(newStr1, str1); strcpy(newStr2, str2); char *outStr = strcat(newStr1, newStr2); return outStr; }
char *fromRealToString(double input){ char *buffer = malloc(64*sizeof(char)); snprintf(buffer, 64, "%lf", input); return buffer; }
char *fromIntegerToString(int input){ char *buffer = malloc(64*sizeof(char)); snprintf(buffer, 64, "%d", input); return buffer; }
char *fromBooleanToString(int input){ if(input == 0){ return "false"; } else { return "true"; } }
void leggiOperazionefun(char * * opvar){
printf("%s\n", "Scegliere l\'operazione da eseguire");
*opvar = getln();


}
void leggiParametrifun(double * parametro1var, double * parametro2var){
printf("%s\n", "Inserire il primo parametro");
scanf("%lf", parametro1var);
getchar();

printf("%s\n", "Inserire il secondo parametro");
scanf("%lf", parametro2var);
getchar();


}
double calcolaRisultatofun(double parametro1var, double parametro2var, char * operazionevar){
double resultvar;
if(!strcmp(operazionevar, "+")){
double calcolaRisultatovar = calcolaRisultatofun(2.8, 2.9, "ciao");
resultvar = parametro1var + parametro2var;

}
if(!strcmp(operazionevar, "-")){
resultvar = parametro1var - parametro2var;

}
if(!strcmp(operazionevar, "*")){
resultvar = parametro1var * parametro2var;

}
if(!strcmp(operazionevar, "/")){
resultvar = parametro1var / parametro2var;

}
return resultvar;

}
int main(void){
int sceltavar = 1;
char * opvar;
double arg1var, arg2var, resultvar;
while(sceltavar != 0){
leggiOperazionefun(&opvar);
leggiParametrifun(&arg1var, &arg2var);
resultvar = calcolaRisultatofun(arg1var, arg2var, opvar);
printf("%lf ", resultvar);
printf("%s\n", "Continuare? (1/0)");
scanf("%d", &sceltavar);
getchar();


}
return 0;
}
