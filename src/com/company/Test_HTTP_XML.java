package com.company;

import java.io.*;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
public class Test_HTTP_XML {
    static String[] dayArr = new String[32];
    public static void main(String[] args) throws IOException {
        Test_HTTP_XML oobj_Test_HTTP_XML=new Test_HTTP_XML();
        String location="Anuradhapura";
        int days=0;
        for(int year=2009;year<2020;year++) {
            String filename=year+"Anur.csv";
            FileOutputStream fr = new FileOutputStream(filename);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fr));
            bw.write("Date,Temp C,WindSpeed Kmph,Wind Dir 16 Point,Weather Desc,Precip MM,Humidity,Visibility,Pressure,Cloudcover,HeatIndex C,Dew Point C,WindChill C,Wind Gust Kmph,Feels Like C, UV Index ");

            for (int month = 1; month < 13; month++) {
                String startDay = year + "-" + month + "-" + 1;
                if ((month == 1) || (month == 3) || (month == 5) || (month == 7) || (month == 8) || (month == 10) || (month == 12)) {
                    days = 31;
                    String endDay = year + "-" + month + "-" + days;
                    oobj_Test_HTTP_XML.get_response(location, startDay, endDay, days, year, month, bw);
                }
                if (month == 2) {
                    if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
                        days = 29;
                    } else {
                        days = 28;
                    }
                    String endDay = year + "-" + month + "-" + days;
                    oobj_Test_HTTP_XML.get_response(location, startDay, endDay, days, year, month, bw);
                }
                if ((month == 4) || (month == 6) || (month == 9) || (month == 11)) {
                    days = 30;
                    String endDay = year + "-" + month + "-" + days;
                    oobj_Test_HTTP_XML.get_response(location, startDay, endDay, days, year, month, bw);
                }
            }
            bw.flush();
        }

    }

    public void get_response(String location, String startDay,String endDay, int days,int year,int month,BufferedWriter bw){
        String text="";
        try {
            String url = "http://api.worldweatheronline.com/premium/v1/past-weather.ashx?key=957f9b482756460697e50601202402&q="+location+"&tp=1&date="+startDay+"&enddate="+endDay;
            System.out.println(url);
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            int responseCode = con.getResponseCode();
            System.out.println("Response Code : " + responseCode);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            //print in String
            // System.out.println(response.toString());
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(new InputSource(new StringReader(response.toString())));
            NodeList errNodes = doc.getElementsByTagName("data");
            int count=0;
            if (errNodes.getLength() > 0) {
                for(int j=0;j<days;j++) {
                    Element err = (Element) errNodes.item(0);
                    System.out.println("Sunrise - " + err.getElementsByTagName("date").item(j).getTextContent());
                    System.out.println("Sunrise - " + err.getElementsByTagName("sunrise").item(j).getTextContent());
                    System.out.println("Sunset - " + err.getElementsByTagName("sunset").item(j).getTextContent());
                    System.out.println("Moonrise - " + err.getElementsByTagName("moonrise").item(j).getTextContent());
                    System.out.println("Moonset - " + err.getElementsByTagName("moonset").item(j).getTextContent());
                    System.out.println("Moon Phase - " + err.getElementsByTagName("moon_phase").item(j).getTextContent());
                    System.out.println("Moon Illumination - " + err.getElementsByTagName("moon_illumination").item(j).getTextContent());
                    System.out.println("Max Temperature C - " + err.getElementsByTagName("maxtempC").item(j).getTextContent());
                    System.out.println("Min Temperature C - " + err.getElementsByTagName("mintempC").item(j).getTextContent());
                    System.out.println("Average Temperature C - " + err.getElementsByTagName("avgtempC").item(j).getTextContent());
                    System.out.println("Sun Hour - " + err.getElementsByTagName("sunHour").item(j).getTextContent());
                    System.out.println("UV Index - " + err.getElementsByTagName("uvIndex").item(j).getTextContent());
                    String time="";
                     for(int h=0;h<24;h++){
                        System.out.println("");
                        text=(j+1)+"/"+month+"/"+year;
                        if((h)<10){
                            time="0"+h+":00";
                        }else{
                            time=h+":00";
                        }
                        //System.out.println("Time - " + err.getElementsByTagName("hourly").item(count).getTextContent());
                        System.out.println("Time - " + err.getElementsByTagName("time").item(count).getTextContent());
                        text=text+" "+time;
                        System.out.println("Temp C - " + err.getElementsByTagName("tempC").item(count).getTextContent());
                        text=text+","+err.getElementsByTagName("tempC").item(count).getTextContent();
                        System.out.println("Wind Speed KMPH - " + err.getElementsByTagName("windspeedKmph").item(count).getTextContent());
                         text=text+","+err.getElementsByTagName("windspeedKmph").item(count).getTextContent();
                        //System.out.println("Wind Dir Degree - " + err.getElementsByTagName("winddirDegree").item(count).getTextContent());
                        System.out.println("Wind Dir 16 Point - " + err.getElementsByTagName("winddir16Point").item(count).getTextContent());
                         text=text+","+err.getElementsByTagName("winddir16Point").item(count).getTextContent();
                        //System.out.println("Time - " + err.getElementsByTagName("weatherCode").item(count).getTextContent());
                        //System.out.println("Time - " + err.getElementsByTagName("weatherIconUrl").item(count).getTextContent());
                        System.out.println("Weather Desc - " + err.getElementsByTagName("weatherDesc").item(count).getTextContent());
                         text=text+","+err.getElementsByTagName("weatherDesc").item(count).getTextContent();
                        System.out.println("Precip MM - " + err.getElementsByTagName("precipMM").item(count).getTextContent());
                         text=text+","+err.getElementsByTagName("precipMM").item(count).getTextContent();
                        //System.out.println("Time - " + err.getElementsByTagName("precipInches").item(count).getTextContent());
                        System.out.println("Humidity - " + err.getElementsByTagName("humidity").item(count).getTextContent());
                         text=text+","+err.getElementsByTagName("humidity").item(count).getTextContent();
                        System.out.println("Visibility - " + err.getElementsByTagName("visibility").item(count).getTextContent());
                         text=text+","+err.getElementsByTagName("visibility").item(count).getTextContent();
                        //System.out.println(" - " + err.getElementsByTagName("visibilityMiles").item(count).getTextContent());
                        System.out.println("Pressure - " + err.getElementsByTagName("pressure").item(count).getTextContent());
                         text=text+","+err.getElementsByTagName("pressure").item(count).getTextContent();
                        //System.out.println("Time - " + err.getElementsByTagName("pressureInches").item(count).getTextContent());
                        System.out.println("Cloud Cover - " + err.getElementsByTagName("cloudcover").item(count).getTextContent());
                         text=text+","+err.getElementsByTagName("cloudcover").item(count).getTextContent();
                        System.out.println("Heat Index C - " + err.getElementsByTagName("HeatIndexC").item(count).getTextContent());
                         text=text+","+err.getElementsByTagName("HeatIndexC").item(count).getTextContent();
                        //System.out.println("Time - " + err.getElementsByTagName("HeatIndexF").item(count).getTextContent());
                        System.out.println("Dew Point C - " + err.getElementsByTagName("DewPointC").item(count).getTextContent());
                         text=text+","+err.getElementsByTagName("DewPointC").item(count).getTextContent();
                        //System.out.println("Time - " + err.getElementsByTagName("DewPointF").item(count).getTextContent());
                        System.out.println("Wind Chill C - " + err.getElementsByTagName("WindChillC").item(count).getTextContent());
                         text=text+","+err.getElementsByTagName("WindChillC").item(count).getTextContent();
                        //System.out.println("Time - " + err.getElementsByTagName("WindChillF").item(count).getTextContent());
                        //System.out.println("Time - " + err.getElementsByTagName("WindGustMiles").item(count).getTextContent());
                        System.out.println("Wind Gust Kmph - " + err.getElementsByTagName("WindGustKmph").item(count).getTextContent());
                         text=text+","+err.getElementsByTagName("WindGustKmph").item(count).getTextContent();
                        System.out.println("Feels Like C - " + err.getElementsByTagName("FeelsLikeC").item(count).getTextContent());
                         text=text+","+err.getElementsByTagName("FeelsLikeC").item(count).getTextContent();
                        //System.out.println("Time - " + err.getElementsByTagName("FeelsLikeF").item(count).getTextContent());
                        System.out.println("UV Index - " + err.getElementsByTagName("uvIndex").item(count).getTextContent());
                         text=text+","+err.getElementsByTagName("uvIndex").item(count).getTextContent();
                        //System.out.println("Time - " + err.getElementsByTagName("hourly").item(count).getTextContent());
                         count++;
                         bw.newLine();
                         bw.write(text);
                    }
                    System.out.println("");
                }

            } else {
                // success
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

}