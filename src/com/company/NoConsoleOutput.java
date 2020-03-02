package com.company;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
public class NoConsoleOutput {

    public static void main(String[] args) throws IOException {
        NoConsoleOutput oobj_Test_HTTP_XML=new NoConsoleOutput();
        String location="Mahiyangana";
        String apiKey="6f9e97ab828b4766938164958202902";
        int days=0;
        int obj=0;
        for(int year=2009;year<2020;year++) {
            String filename=year+"Mahi.csv";
            FileOutputStream fr = new FileOutputStream(filename);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fr));
            bw.write("Date,Temp C,WindSpeed Kmph,Wind Dir 16 Point,Weather Desc,Precip MM,Humidity,Visibility,Pressure,Cloudcover,HeatIndex C,Dew Point C,WindChill C,Wind Gust Kmph,Feels Like C, UV Index ");

            for (int month = 1; month < 13; month++) {
                String startDay = year + "-" + month + "-" + 1;
                if ((month == 1) || (month == 3) || (month == 5) || (month == 7) || (month == 8) || (month == 10) || (month == 12)) {
                    days = 31;
                    String endDay = year + "-" + month + "-" + days;
                    obj=oobj_Test_HTTP_XML.get_response(apiKey,location, startDay, endDay, days, year, month, bw);
                    if(obj!=200){
                        System.exit(1); //issues with API
                    }
                }
                if (month == 2) { //finding the leap year
                    if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
                        days = 29;
                    } else {
                        days = 28;
                    }
                    String endDay = year + "-" + month + "-" + days;
                    obj=oobj_Test_HTTP_XML.get_response(apiKey,location, startDay, endDay, days, year, month, bw);
                    if(obj!=200){
                        System.exit(1); //issues with API
                    }
                }
                if ((month == 4) || (month == 6) || (month == 9) || (month == 11)) {
                    days = 30;
                    String endDay = year + "-" + month + "-" + days;
                    obj=oobj_Test_HTTP_XML.get_response(apiKey,location, startDay, endDay, days, year, month, bw);
                    if(obj!=200){
                        System.exit(1); //issues with API
                    }
                }
            }
            bw.flush();
        }

    }

    public int get_response(String apiKey,String location, String startDay,String endDay, int days,int year,int month,BufferedWriter bw){
        String text="";
        try {
            String url = "http://api.worldweatheronline.com/premium/v1/past-weather.ashx?key="+apiKey+"&q="+location+"&tp=1&date="+startDay+"&enddate="+endDay;
            System.out.println(url);
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            int responseCode = con.getResponseCode();
            System.out.println("Response Code : " + responseCode);
            if(responseCode==200){
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                        .parse(new InputSource(new StringReader(response.toString())));
                NodeList errNodes = doc.getElementsByTagName("data");
                int count=0;
                if (errNodes.getLength() > 0) {
                    for(int j=0;j<days;j++) {
                        Element err = (Element) errNodes.item(0);
                        String time="";
                        for(int h=0;h<24;h++){
                            text=(j+1)+"/"+month+"/"+year;
                            if((h)<10){
                                time="0"+h+":00";
                            }else{
                                time=h+":00";
                            }
                            text=text+" "+time;
                            text=text+","+err.getElementsByTagName("tempC").item(count).getTextContent();
                            text=text+","+err.getElementsByTagName("windspeedKmph").item(count).getTextContent();
                            text=text+","+err.getElementsByTagName("winddir16Point").item(count).getTextContent();
                            text=text+","+err.getElementsByTagName("weatherDesc").item(count).getTextContent();
                            text=text+","+err.getElementsByTagName("precipMM").item(count).getTextContent();
                            text=text+","+err.getElementsByTagName("humidity").item(count).getTextContent();
                            text=text+","+err.getElementsByTagName("visibility").item(count).getTextContent();
                            text=text+","+err.getElementsByTagName("pressure").item(count).getTextContent();
                            text=text+","+err.getElementsByTagName("cloudcover").item(count).getTextContent();
                            text=text+","+err.getElementsByTagName("HeatIndexC").item(count).getTextContent();
                            text=text+","+err.getElementsByTagName("DewPointC").item(count).getTextContent();
                            text=text+","+err.getElementsByTagName("WindChillC").item(count).getTextContent();
                            text=text+","+err.getElementsByTagName("WindGustKmph").item(count).getTextContent();
                            text=text+","+err.getElementsByTagName("FeelsLikeC").item(count).getTextContent();
                            text=text+","+err.getElementsByTagName("uvIndex").item(count).getTextContent();
                            count++;
                            bw.newLine();
                            bw.write(text);
                        }
                    }

                }
            }
            else{
                System.out.println("**** Check Your API KEY ****");
                return responseCode;
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return 200;
    }

}