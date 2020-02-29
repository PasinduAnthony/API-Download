package com.company;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
public class Main {

    public static void main(String[] args)  {
        try{
            String location="kandy";
            String startDay="2009-01-1";
            String endDay="2009-01-31";
            URL u = new URL("http://api.worldweatheronline.com/premium/v1/past-weather.ashx?key=957f9b482756460697e50601202402&q="+location+"&tp=1&date="+startDay+"&enddate="+endDay);
            HttpURLConnection hr = (HttpURLConnection) u.openConnection();
            //System.out.println(hr.getResponseCode());
            if(hr.getResponseCode()==200){
                InputStream im = hr.getInputStream();
                StringBuffer sb = new StringBuffer();
                FileOutputStream fr = new FileOutputStream("test.csv");
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fr));
                BufferedReader br = new BufferedReader(new InputStreamReader(im));
                String line = br .readLine();
                bw.write("Hello/yellow/mellow");
                bw.newLine();
                bw.write("10,20,30");
                while(line!=null){
                    System.out.println(line);
                    bw.write(line);
                    bw.newLine();
                    bw.flush();
                    line=br.readLine();
                }
                br.close();

                Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(br.toString())));
                NodeList errNodes = doc.getElementsByTagName("weather");
                if (errNodes.getLength() > 0) {
                    Element err = (Element)errNodes.item(0);
                    //System.out.println("date - "+err.getElementsByTagName("data").item(0).getTextContent());
                    System.out.println("sunrise -"+err.getElementsByTagName("request").item(0).getTextContent());
                    System.out.println("sunset -"+err.getElementsByTagName("sunset").item(0).getTextContent());
                    System.out.println("moonrise -"+err.getElementsByTagName("moonrise").item(0).getTextContent());
                    System.out.println("raw -"+err.getElementsByTagName("moonset").item(0).getTextContent());
                    System.out.println("offset -"+err.getElementsByTagName("moon_phase").item(0).getTextContent());
                }else{}

            }
        }catch(Exception e){
            System.out.println(e);
        }
	// write your code here
    }
}
