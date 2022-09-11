#include <stdio.h>
#include <stdlib.h>

int main()
{
   FILE *fptr = fopen("/dev/null", "w");

   if(fptr == NULL)
   {
      printf("Error!\n");   
      exit(1);
   }

   fprintf(fptr, "Hello, it's me\n");

   fclose(fptr);

   return 0;
}