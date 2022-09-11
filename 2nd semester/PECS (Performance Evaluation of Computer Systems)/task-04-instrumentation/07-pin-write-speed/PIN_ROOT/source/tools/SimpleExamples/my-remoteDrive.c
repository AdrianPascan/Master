#include <stdio.h>
#include <stdlib.h>

int main()
{
   FILE *fptr = fopen("https://drive.google.com/file/d/1X8MowNd0NN2N6s2P5HTrVp9VVFx2SP_G/view?usp=sharing", "w");

   if(fptr == NULL)
   {
      printf("Error!\n");
      exit(1);
   }

   fprintf(fptr, "Hello, it's me\n");

   fclose(fptr);

   return 0;
}