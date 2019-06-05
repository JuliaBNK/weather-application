/*
 * Name:     Iuliia Buniak
 *
 * Course:   CS-13, Spring 2019
 *
 * Date:     04/24/19
 *
 * Filename: Wxhw.java
 *
 * Purpose:  To display the weather at the curtain location using Json
 */


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.net.URL;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.google.gson.*;

public class Wxhw
{
	public String getWx(String zip)
	{
		JsonElement jse = null;
        String wxReport = null;
        String appid = "151299208a36b2f534ef585fda4fd673";

		try
		{
			// Construct WxStation API URL
			URL wxURL = new URL("http://api.openweathermap.org/data/2.5/weather?zip="
					            + zip
					            + "&units=imperial&appid=" + appid);
            // Open the URL
			InputStream is = wxURL.openStream(); // throws an IOException
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			// Read the result into a JSON Element
			jse = new JsonParser().parse(br);
      
			// Close the connection
			is.close();
			br.close();
		}
		catch (java.io.UnsupportedEncodingException uee)
		{
			uee.printStackTrace();
		}
		catch (java.net.MalformedURLException mue)
		{
			mue.printStackTrace();
		}
		catch (java.io.IOException ioe)
		{
			System.out.println("No cities match your search query");
		}
       
		if (jse != null)
		{
            // Build a weather report
      
            // Name of a town/city
            String name = jse.getAsJsonObject().get("name").getAsString();
            wxReport = "Location:        " + name + "\n";
      
            // Time of last update
            String time = jse.getAsJsonObject().get("dt").getAsString();
            //converting from Unix time to regular time
            long unixTime = Long.parseLong(time);
            Date date = new Date (unixTime*1000L);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            String strDate = dateFormat.format(date);

            wxReport = wxReport + "Time:            " + "Last Updated on " + strDate + "\n";
      
      
            // Weather conditions 
            JsonArray obs = jse.getAsJsonObject().get("weather").getAsJsonArray();
            String conditions = obs.get(0).getAsJsonObject().get("main").getAsString();
            wxReport = wxReport + "Weather:         " + conditions + "\n";
      
            // The temperature in F
            String temp = jse.getAsJsonObject().get("main").getAsJsonObject().get("temp").getAsString();
            wxReport = wxReport + "Temperature F:   " + temp + "\n";
      
            // Getting degree and speed of wind
            //String windDeg = jse.getAsJsonObject().get("wind").getAsJsonObject().get("deg").getAsString();
            //String windSpeed = jse.getAsJsonObject().get("wind").getAsJsonObject().get("speed").getAsString();
            //wxReport = wxReport +  "Wind:            " + "From " + windDeg + " degrees at " + windSpeed + " MPH\n";
      
      
            // Pressure
            // formula to convert hPa to inHG: pinHg = 0.02952998751 × phPa
            String pressureStr = jse.getAsJsonObject().get("main").getAsJsonObject().get("pressure").getAsString();
            double phPa = Double.parseDouble(pressureStr);
            double pinHg = 0.02952998751 * phPa; 
            wxReport = wxReport + "Pressure inHG:   " + pinHg + "\n";
            
            
            String dir = null; 
        String windDegStr = jse.getAsJsonObject().get("wind").getAsJsonObject().get("deg").getAsString();
        double windDeg = Double.parseDouble(windDegStr);
        if ( windDeg >= 337.5 || windDeg < 22.5 ){
            dir = "N";
        }    
        else if ( windDeg >= 22.5 && windDeg < 67.5 ){
            dir = "NE";
        }
        else if ( windDeg >= 67.5 && windDeg < 112.5 ){
            dir = "E";
        }
        else if ( windDeg >= 112.5 && windDeg < 157.5){
            dir = "SE";
        }
        else if ( windDeg >= 157.5 && windDeg < 202.5 ){
            dir = "S";
        }
        else if ( windDeg >= 202.5 && windDeg < 247.5){
            dir = "SW";
        }
        else if ( windDeg >= 247.5 && windDeg < 292.5 ){
            dir = "W";
        }
        else if ( windDeg >= 292.5 && windDeg < 337.5){
            dir = "NW";    
        } 
        wxReport = wxReport + "Wind Deg:   " + dir + "\n";
        
        
        String cod = jse.getAsJsonObject().get("cod").getAsString();
        wxReport = wxReport + "Cod:   " + cod + "\n";

	    }
        return wxReport;
	}

	public static void main(String[] args)
	{   
        // Take the zip from the command line as a string 
        if (args.length == 0)
        {
            System.out.println("You need to enter a valid zip code");
            System.exit(1);
        }
        //String zip = args[0];
		Wxhw b = new Wxhw();
		String wx = b.getWx(args[0]);
        if ( wx != null )
		    System.out.println(wx);
	} 
} // end class