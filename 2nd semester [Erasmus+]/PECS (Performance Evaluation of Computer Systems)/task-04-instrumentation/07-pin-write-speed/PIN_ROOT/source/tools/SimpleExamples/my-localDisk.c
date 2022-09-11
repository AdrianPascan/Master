#include <stdio.h>
#include <stdlib.h>

int main()
{
   FILE *fptr = fopen("my.txt", "w");

   if(fptr == NULL)
   {
      printf("Error!");   
      exit(1);
   }

   fprintf(fptr, "Hello, it's me\n");

   fclose(fptr);

   return 0;
}