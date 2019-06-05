/*
 * WxController
 * This is the controller for the FXML document that contains the view. 
 */
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Iuliia Buniak
 */
public class WxController implements Initializable {

    @FXML
    private Button btnGetWx;

    @FXML
    private TextField txtZipcode;

    @FXML
    private Label lblCity;

    @FXML
    private Label lblTime;

    @FXML
    private Label lblWeather;

    @FXML
    private Label lblTemperature;

    @FXML
    private Label lblWindSpeed;
  
    @FXML
    private Label lblWindDirection;

    @FXML
    private Label lblPressure;

    @FXML
    private Label lblHumidity;

    @FXML
    private ImageView iconWx;

    @FXML
    private void handleButtonAction(ActionEvent e) {
        // Create object to access the Model
        WxModel weather = new WxModel();
    
        //Has the GetWeather button been pressed?
        if (e.getSource() == btnGetWx) {

            // Get zipcode
            String zipcode = txtZipcode.getText();

            // Use the model to get the weather information
            if (weather.getWx(zipcode)){
                lblCity.setText(weather.getLocation());
                lblTime.setText(weather.getTime());
                lblWeather.setText(weather.getWeather());
                lblTemperature.setText(weather.getTemp());
                lblWindSpeed.setText(weather.getWindSpeed());
                lblWindDirection.setText(weather.getWindDirection());
                lblPressure.setText(weather.getPressure());
                lblHumidity.setText(weather.getHumidity());
                iconWx.setImage(weather.getImage());
            }
            else{
                lblCity.setText("Invalid Zipcode");
                lblTime.setText("");
                lblWeather.setText("");
                lblTemperature.setText("");
                lblWindSpeed.setText("");
                lblWindDirection.setText("");
                lblPressure.setText("");
                lblHumidity.setText("");
                iconWx.setImage(new Image("badzipcode.png"));
            }
        }
    }  
    
     @Override
     public void initialize(URL url, ResourceBundle rb) {
      // TODO
     }    
  
}
