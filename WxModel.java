/*
 * Name:     Iuliia Buniak
 *
 * Course:   CS-13, Spring 2019
 *
 * Date:     05/01/19
 *
 * Filename: WxModel.java
 *
 * Purpose:  To handle all data access and manipulation in MVC model 
 *           for weather application which will get the weather by zipcode
 */
import com.google.gson.JsonElement;
import com.google.gson.JsonParser; 
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import javafx.scene.image.Image;
import java.net.URLEncoder;
import java.net.URL;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


import com.google.gson.*;

public class WxModel{

    private JsonElement jse;
    final private String appid = "151299208a36b2f534ef585fda4fd673";

    public boolean getWx(String zip){
	    try {
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
        
		catch (java.io.UnsupportedEncodingException uee){
		    uee.printStackTrace();	
		}
		catch (java.net.MalformedURLException mue){
		    mue.printStackTrace();
		}
		catch (java.io.IOException ioe){
            System.out.println("No cities match your search query");
		}
        catch (java.lang.NullPointerException npe){
            System.out.println("No cities match your search query");
        }
        
        // Check to see if the zip code was valid.
        return isValid();
        
    }// end getWx

    // Method to check if the zip code is valid
    
    public boolean isValid(){
        try{
            String name = jse.getAsJsonObject().get("name").getAsString();
            return true;
        }
        catch (java.lang.NullPointerException npe){
            return false;
        }
    }// end isValid   
    
         
    // Getting all necessary information from json
    // Name of a town/city   
	public String getLocation() {
        return jse.getAsJsonObject().get("name").getAsString();
    }
    
    // Time of observaion
    public String getTime(){
        String time = jse.getAsJsonObject().get("dt").getAsString();
        //converting from Unix time to regular time
        long unixTime = Long.parseLong(time);
        Date date = new Date (unixTime*1000L);
        SimpleDateFormat dateFormat = new SimpleDateFormat("M-dd-yyyy hh:mm:ss");
        // Converting date into string 
        String strDate = dateFormat.format(date);
        return strDate;
    }    
      
    // Weather conditions
    public String getWeather(){ 
        JsonArray obs = jse.getAsJsonObject().get("weather").getAsJsonArray();
        String conditions = obs.get(0).getAsJsonObject().get("main").getAsString();
        return conditions;
    }
      
    // Temperature in F
    public String getTemp(){
        String tempStr = jse.getAsJsonObject().get("main").getAsJsonObject().get("temp").getAsString();
        double temp = Double.parseDouble(tempStr);
        String tempFmt = String.format("%.1f", temp);
        return tempFmt;
    } 
    // Speed of wind
    public String getWindSpeed(){
        String windSpeedStr = jse.getAsJsonObject().get("wind").getAsJsonObject().get("speed").getAsString();
        double windSpeed = Double.parseDouble(windSpeedStr);
        String windSpeedFmt = String.format("%.1f", windSpeed);
        return windSpeedFmt;
    }
    
    // Wind direction
    public String getWindDirection(){
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
        return dir;     
    }
    
    // Pressure
    // formula to convert hPa to inHG: pinHg = 0.02952998751 × phPa
    public String getPressure(){
        String pressureStr = jse.getAsJsonObject().get("main").getAsJsonObject().get("pressure").getAsString();
        double phPa = Double.parseDouble(pressureStr);
        double pinHg = 0.02952998751 * phPa; 
        String pinHgFmt = String.format("%.2f", pinHg);
        return pinHgFmt;
    }
    
    public String getHumidity(){
        String humidityStr = jse.getAsJsonObject().get("main").getAsJsonObject().get("humidity").getAsString();
        int humidity = Integer.parseInt(humidityStr);
        String humidityFmt = String.format("%d", humidity);
        return humidityFmt;
    }
    
    public Image getImage(){
        JsonArray obs = jse.getAsJsonObject().get("weather").getAsJsonArray();
        String icon = obs.get(0).getAsJsonObject().get("icon").getAsString();
        String iconURL = "http://openweathermap.org/img/w/"
					            + icon
					            + ".png";
        return new Image(iconURL);
    }
	
}//end class