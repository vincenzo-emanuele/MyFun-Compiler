#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
char *getln(){ char *line = NULL, *tmp = NULL; int size = 0, index = 0; int ch = EOF; while (ch) { ch = getc(stdin); /* Check if we need to stop. */ if (ch == EOF || ch == '\n') ch = 0; /* Check if we need to expand. */ if (size <= index) { size += 1; tmp = realloc(line, size); if (!tmp) { free(line); line = NULL; break; } line = tmp; } /* Actually store the thing. */ line[index++] = ch; } return line; }
char *customConcat(char *str1, char *str2){ char *newStr1 = malloc(strlen(str1) + 1); char *newStr2 = malloc(strlen(str2) + 1); strcpy(newStr1, str1); strcpy(newStr2, str2); char *outStr = strcat(newStr1, newStr2); return outStr; }
int mod_stringvar = 20;
int mod_string2var;
char *fromRealToString(double input){ char *buffer = malloc(64*sizeof(char)); snprintf(buffer, 64, "%lf", input); return buffer; }
char *fromIntegerToString(int input){ char *buffer = malloc(64*sizeof(char)); snprintf(buffer, 64, "%d", input); return buffer; }
char *fromBooleanToString(int input){ if(input == 0){ return "false"; } else { return "true"; } }
int mod_string2fun(int bvar, char * * str2var){
printf("%s\n", customConcat("In mod_string2: ", fromIntegerToString(mod_stringvar)));
printf("%s\n", *str2var);

}
int mod_stringfun(char * * avar){
*avar = "Ciao";
mod_string2fun(2, avar);

}
int main(void){
int mod_stringvar = -10;
int ivar = 1;
char * inputvar;
mod_stringfun(&inputvar);
if(1){
int mod_stringvar = 5;
printf("%s\n", customConcat("Nell'if: ", fromIntegerToString(mod_stringvar)));
while(ivar < 5){
int jjvar = 5;
if(ivar > 0){
printf("%d\n", ivar + 1);
printf("%d\n", jjvar);

}
ivar = ivar + 1;

}

} else {
int mod_stringvar = 7;
printf("%s\n", customConcat("Nell'else: ", fromIntegerToString(mod_stringvar)));

}
while(ivar < 5){
int mod_stringvar = 77;
printf("%s\n", customConcat("Nel while: ", fromIntegerToString(mod_stringvar)));
ivar = ivar + 1;

}
printf("%s\n", inputvar);
printf("%s\n", customConcat("Alla fine del main: ", fromIntegerToString(mod_stringvar)));
printf("%s\n", "Str, int, int");
inputvar = getln();
scanf("%d", &mod_stringvar);
getchar();
scanf("%d", &ivar);
getchar();

printf("%s\n", customConcat(customConcat(customConcat(customConcat(inputvar, " "), fromIntegerToString(mod_stringvar)), " "), fromIntegerToString(ivar)));
return 0;
}
