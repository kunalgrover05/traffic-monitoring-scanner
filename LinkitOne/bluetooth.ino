#include <LBT.h>
#include <LBTClient.h>
#include <SPI.h>
#include <LSD.h>
#include <Time.h>


LFile mbt;

void setup() {
  // put your setup code here, to run once:
Serial.begin(9600);
Serial.print("Initializing SD card...");
LSD.begin();

mbt = LSD.open("mac.txt", FILE_WRITE);
if (!mbt)
{
  Serial.println("error opening mbt.txt");
}
mbt.close();
Serial.printf("LBT start\n");
bool success = LBTClient.begin();
if(!success)
{
  Serial.printf("Cannot begin Bluetooth Client successfully\n");
  delay(0xffffffff); 
}
else
{
  Serial.printf("Bluetooth Client begin successfully\n");
   
}
}

void loop() {
  // put your main code here, to run repeatedly:
int time = millis();
int num = LBTClient.scan(3);

mbt = LSD.open("mac.txt", FILE_WRITE);
mbt.printf("Time: %d\n",time);
Serial.printf("Time: %d for devices %d\n",time,num);
for(int i=0; i<num;i++)
{ 
  
  LBTDeviceInfo info = {0};
  bool succ = LBTClient.getDeviceInfo(i, &info);
  if (succ)
  {
    mbt.printf("[%02x:%02x:%02x:%02x:%02x:%02x][%s]\n", 
            info.address.nap[1], info.address.nap[0], info.address.uap, info.address.lap[2], info.address.lap[1], info.address.lap[0],
            info.name);
    Serial.printf("[%02x:%02x:%02x:%02x:%02x:%02x][%s]\n", 
            info.address.nap[1], info.address.nap[0], info.address.uap, info.address.lap[2], info.address.lap[1], info.address.lap[0],
            info.name);
                    
  }
mbt.close();
delay(1);
}
}
