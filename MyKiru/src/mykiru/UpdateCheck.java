/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mykiru;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.JEditorPane;
import javax.swing.JMenu;


/**
 *
 * @author Tad
 */
public class UpdateCheck {
    public Functions fs = new Functions();
    public boolean Check(String update_url, JEditorPane text, JMenu jLabel10){
        fs.w(fs.find_message("connecting_to_server", fs.lang(), "{Update_Url}", update_url), text);
            String data = "";
            String inputLine;
            try {
            URL server = new URL(update_url + "/UOAU/?data=updates");
            URLConnection connection = server.openConnection();
            try (BufferedReader in = new BufferedReader(new
                InputStreamReader(connection.getInputStream()))) {
                while ((inputLine = in.readLine()) != null) {
                 data = data + inputLine;
                }
                if (read_text(data, jLabel10, text)) {return true;}
            }
        } catch (Exception e) {
        fs.w(fs.find_message("error_connecting_to_server", fs.lang(), "{Error_Message}", e.toString()), text);
            return false;}
         
        
        return false;
    }
    
   
    
    
    private boolean read_text(String paramStr, JMenu jLabel10, JEditorPane text){
        try{
            String version = "";
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(paramStr);
            if (element.isJsonObject()) {
                JsonObject jsonOb = element.getAsJsonObject();
                version = jsonOb.get("kiru_version").getAsString();
            }
            
            if(Double.parseDouble(version) > Double.parseDouble(jLabel10.getText().split(" ")[1])){
                fs.w(fs.find_message("update_your_kiru", fs.lang(), "{Server_Kiru_Version}", version)
                .replace("{Client_Kiru_Version}",jLabel10.getText().split(" ")[1])
                , text);
                return false;
            }        
        } catch(Exception E){
            fs.w("There was an error retrieving information." + E.getMessage(), text);
            return false;
        }
        return true;
        }
}
