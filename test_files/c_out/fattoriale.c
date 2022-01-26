#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
char *getln(){ char *line = NULL, *tmp = NULL; int size = 0, index = 0; int ch = EOF; while (ch) { ch = getc(stdin); /* Check if we need to stop. */ if (ch == EOF || ch == '\n') ch = 0; /* Check if we need to expand. */ if (size <= index) { size += 1; tmp = realloc(line, size); if (!tmp) { free(line); line = NULL; break; } line = tmp; } /* Actually store the thing. */ line[index++] = ch; } return line; }
char *customConcat(char *str1, char *str2){ char *newStr1 = malloc(strlen(str1) + 1); char *newStr2 = malloc(strlen(str2) + 1); strcpy(newStr1, str1); strcpy(newStr2, str2); char *outStr = strcat(newStr1, newStr2); return outStr; }
int fattorialevar;
char *fromRealToString(double input){ char *buffer = malloc(64*sizeof(char)); snprintf(buffer, 64, "%lf", input); return buffer; }
char *fromIntegerToString(int input){ char *buffer = malloc(64*sizeof(char)); snprintf(buffer, 64, "%d", input); return buffer; }
char *fromBooleanToString(int input){ if(input == 0){ return "false"; } else { return "true"; } }
int fattorialefun(int nvar){
int fattorialevar = 1;
if(nvar > 0){
int ivar = 1;
while(ivar <= nvar){
fattorialevar = fattorialevar * ivar;
ivar = ivar + 1;

}

}
return fattorialevar;

}
int main(void){
int nvar = 10;
int jvar = 5;
printf("%s ", customConcat(customConcat(fromIntegerToString(nvar), fromIntegerToString(jvar)), "Dimmi"));
printf("%d\t", fattorialefun(8));
return 0;
}
