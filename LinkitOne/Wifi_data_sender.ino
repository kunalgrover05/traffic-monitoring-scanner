#include <LTask.h>
#include <LWiFi.h>
#include <LWiFiClient.h>

#define WIFI_AP "Indix-Event"
#define WIFI_PASSWORD "guest@123"
#define WIFI_AUTH LWIFI_WPA  // choose from LWIFI_OPEN, LWIFI_WPA, or LWIFI_WEP.

// Ubidots information

#define URL        "192.168.0.159"
#define PORT       8000
#define TOKEN      "xcxcxscxscmPvcvcwauSvcvcJEA2vceEIn"          // replace with your Ubidots token generated in your profile tab
#define VARIABLEID "553a952e7625420911d016f3"                // create a variable in Ubidots and put its ID here (http://app.ubidots.com/ubi/datasources/)

int value = 0;

void setup()
{
  LTask.begin();
  LWiFi.begin();
  Serial.begin(9600);

  // keep retrying until connected to AP
  Serial.println("Connecting to AP");
  while (0 == LWiFi.connect(WIFI_AP, LWiFiLoginInfo(WIFI_AUTH, WIFI_PASSWORD)))
  {
    delay(1000);
  }
  Serial.println("Connected to Wifi");
}

void loop()
{ 
  value++;
  save_value(String(value));
  // delay(500);              // Set here the desired update frequency
}


void save_value(String value){

  Serial.println("Sending value to Ubidots...");
  LWiFiClient c;
  while (!c.connect(URL, PORT))
  {
    Serial.println("Retrying to connect...");
    delay(100);
  }

//  String data = "{\"value\":"+ value + "}";
  String data = String("{\"bt\_id\":")+123456+String(",\"hardware\_id\":")+12+"}";
  String thisLength = String(data.length());
//
//  // Build HTTP POST request
  c.print("POST /upload/");
  c.println(" HTTP/1.1");
  c.println("Content-Type: application/json");
  c.println("Content-Length: " + thisLength);
  c.print("Host: ");
  c.println(URL);
  c.print("\n" + data);
  c.print(char(26));
//
//  // read server response
//
  while (c){
    Serial.print((char)c.read());
  }
//
  c.stop();

}
