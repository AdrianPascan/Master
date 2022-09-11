#include <stdio.h>
#include <stdlib.h>

long long create_ramdisk(void) {
  FILE * fp;
  int ch;
  long long space = 0;
  system("mkdir -p /mnt/ramdisk");
  system("/sbin/mke2fs -q /dev/ram");
  system("mount /dev/ram /mnt/ramdisk");
  system("df /mnt/ramdisk tmp.txt");
  fp = fopen("tmp.txt", "r");
  if (!fp) {
    printf("Can't open file\n");
    return 0;
  }
  while ((ch = fgetc(fp)) != EOF && ch != '\n');
  while ((ch = fgetc(fp)) != EOF && ch != ' ');
  while ((ch = fgetc(fp)) != EOF && ch == ' ');
  while ((ch = fgetc(fp)) != EOF && ch != ' ');
  while ((ch = fgetc(fp)) != EOF && ch == ' ');
  while ((ch = fgetc(fp)) != EOF && ch != ' ');
  while ((ch = fgetc(fp)) != EOF && ch == ' ');
  ungetc(ch, fp);
  if (feof(fp)) {
    printf("Unexpected read\n");
    return 0;
  }
  fscanf(fp, "%lld", & space);
  fclose(fp);
  remove("tmp.txt");
  return space;
}

void remove_ramdisk(void) {
  system("umount /mnt/ramdisk");
}

int main(void) {
  long long space = create_ramdisk();
  printf("Ramdisk has %lld bytes free\n", space);
  remove_ramdisk();
  return 0;
}