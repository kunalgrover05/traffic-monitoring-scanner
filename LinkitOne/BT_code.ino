/*
  Copyright (c) 2014 MediaTek Inc.  All right reserved.

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License..

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
   See the GNU Lesser General Public License for more details.
*/
#include <LBT.h>
#include <LBTClient.h>
#include <LDateTime.h>
#include <LGPS.h>
#include <LWiFi.h>
#include <LWiFiClient.h>

static LBTDeviceInfo info = {0};
boolean find = 0;
#define SPP_SVR "ARD_SPP" // it should be the prefer server's name,  customize it yourself.
#define ard_log Serial.printf
int read_size = 0;
#define WIFI_AP "Connectify-manu"
#define WIFI_PASSWORD "12345678"
#define WIFI_AUTH LWIFI_WPA  // choose from LWIFI_OPEN, LWIFI_WPA, or LWIFI_WEP.

// Ubidots information

#define URL        "192.168.153.102"
#define PORT       8000

datetimeInfo t;
unsigned int rtc;
int gps_on=0; //This is used to check if the GPS needs to be turned off
LWiFiClient c;

void setup()  
{
  Serial.begin(115200);
  Serial.println("Atti setup.");
  LTask.begin();
  LWiFi.begin();

  Serial.println("Connecting to AP");
  while (0 == LWiFi.connect(WIFI_AP, LWiFiLoginInfo(WIFI_AUTH, WIFI_PASSWORD)))
  {
    delay(1000);
  }
  Serial.println("Connected to Wifi");

  while (!c.connect(URL, PORT)) {
    Serial.println("Retrying to connect...");
    delay(100);
  }
//  Update clock
// LDateTime.getTime(&t);
//  LDateTime.getRtc(&rtc);
//  Serial.print("Current GMT: ");
//  Serial.print(t.mon);
//  Serial.print("/");
//  Serial.print(t.day);
//  Serial.print("/");
//  Serial.print(t.year);
//  Serial.print(" ");
//  Serial.print(t.hour);
//  Serial.print(":");
//  Serial.print(t.min);
//  Serial.print(":");
//  Serial.print(t.sec);
//  Serial.print(" Seconds since 1/1/1970 GMT: ");
//  Serial.println(rtc);
  
  //Turning on the GPS syncs up the RTC with the GPS time.
  //If the battery is pulled, the RTC goes back a couple years.
  //Turning on the GPS syncs up the RTC with the GPS time.
  //If the GPS is needed, t.year will be 2004. This will start this loop.
//  if ((gps_on != 1) && (t.year < 2010))
//  {
//    Serial.println("Using GPS to sync GMT. Please wait...");
//    LGPS.powerOn();
//    gps_on = 1;
//  }

//  //If the GPS has synced the RTC, the year will be 2015 or greater.
//  if ((gps_on == 1) && (t.year >= 2015))
//  {
//    LGPS.powerOff();
//    Serial.println("Synced! Turning off GPS. Please wait...");
//    gps_on = 0;
//  }

  //Wait one second before getting new time.
//  delay(100);
}

void send_details(String s) {
    Serial.println("Sending Data");
  while (!c.connect(URL, PORT)) {
    Serial.println("Retrying to connect...");
    delay(100);
  }

    String data = String("{\"bt_id\":")+1+String(",\"hardware_id\":")+String(s)+"}";
    String thisLength = String(data.length());
    
    //  // Build HTTP POST request
    c.print("POST /realtime/");
    c.println(" HTTP/1.1");
    c.println("Content-Type: application/json");
    c.println("Content-Length: " + thisLength);
    c.print("Host: ");
    c.println(URL);
    c.print("\n" + data);
    c.print(char(26));
    while (c){
      Serial.print((char)c.read());
    }
    c.stop();
    Serial.println("Done");
}

void loop()
{
  bool success = LBTClient.begin();
  int num = LBTClient.scan(5);
  Serial.println("Updated");
//  ard_log("scanned device number [%d]\n", num);
  for (int i = 0; i < num; i++)
  {
    if (!LBTClient.getDeviceInfo(i, &info))
    {
        continue;
    }
    String c = String(info.address.nap[1], 16) + String(info.address.nap[0], 16) + String(info.address.uap, 16) + String(info.address.lap[2], 16) + String(info.address.lap[1], 16) + String(info.address.lap[0], 16);
    send_details(c);
  }

}
