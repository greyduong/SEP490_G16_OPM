package service;

import jakarta.servlet.http.Part;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageService {

    private static final String API_URL = "https://api.imgbb.com/1/upload";
    private static final String API_KEY = "ad774e1bc6998d674021f7b483f711ca";

    public String upload(Part part) {
        if (part.getSize() == 0) {
            return null;
        }
        try {
            // convert file to base64
            String base64 = Base64.getEncoder().encodeToString(part.getInputStream().readAllBytes());
            return upload(base64);
        } catch (IOException ex) {
            Logger.getLogger(ImageService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public String upload(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            String base64 = Base64.getEncoder().encodeToString(fis.readAllBytes());
            return upload(base64);
        } catch(IOException ex) {
            Logger.getLogger(ImageService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String upload(String base64) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(API_URL + "?key=" + API_KEY))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(BodyPublishers.ofString("image=" + URLEncoder.encode(base64, StandardCharsets.UTF_8))).build();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            JSONObject obj = new JSONObject(response.body());
            return obj.getJSONObject("data").getString("url");
        } catch (URISyntaxException | IOException | InterruptedException | JSONException ex) {
            Logger.getLogger(ImageService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
