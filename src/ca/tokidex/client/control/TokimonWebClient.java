package ca.tokidex.client.control;

import ca.tokidex.client.model.Tokimon;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Web client class responsible for making the actual HTTP connections
 */
public class TokimonWebClient {

    private final Gson gson = new Gson();

    public List<Tokimon> getAllTokimonsCall() {
        try {
            URL url = new URL("http://localhost:8080/api/tokimon/all");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            List<Tokimon> tokis = gson.fromJson(br, new TypeToken<List<Tokimon>>(){}.getType());
            System.out.println(String.format("%s %s\nResponse: %s", connection.getRequestMethod(), url.toString(), tokis));
            connection.disconnect();

            return tokis;
        } catch (IOException e) {
            System.out.println("Could not complete call to server!");
            e.printStackTrace();
            return null;
        }
    }

    public Tokimon getTokimonByIdCall(Long id) {
        try {
            URL url = new URL("http://localhost:8080/api/tokimon/" + id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            Tokimon toki;
            if (connection.getResponseCode() == 200) {
                toki = gson.fromJson(br, Tokimon.class);
            } else {
                System.out.println(connection.getResponseMessage());
                return null;
            }
            System.out.println(String.format("%s %s\nResponse: %s", connection.getRequestMethod(), url.toString(), toki));
            connection.disconnect();
            return toki;
        } catch (IOException e) {
            System.out.println("Could not complete call to server!");
            e.printStackTrace();
            return null;
        }
    }

    public int addNewTokimonCall(Tokimon tokimon) {
        try {
            URL url = new URL("http://localhost:8080/api/tokimon/add");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
            String json = gson.toJson(tokimon);
            System.out.println(String.format("%s %s\n %s", connection.getRequestMethod(), url.toString(), json));

            wr.write(json);
            wr.flush();
            wr.close();
            connection.connect();
            System.out.println(connection.getResponseCode());
            connection.disconnect();
            return connection.getResponseCode();
        } catch (IOException e) {
            System.out.println("Could not complete call to server!");
            e.printStackTrace();
        }
        return 0;
    }

    public int changeTokimonByIdCall(Tokimon tokimon) {
        try {
            URL url = new URL("http://localhost:8080/api/tokimon/change/" + tokimon.getTokimonId());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
            String json = gson.toJson(tokimon);
            System.out.println(String.format("%s %s\n %s", connection.getRequestMethod(), url.toString(), json));

            wr.write(json);
            wr.flush();
            wr.close();
            connection.connect();
            connection.disconnect();
            return connection.getResponseCode();
        } catch (IOException e) {
            System.out.println("Could not complete call to server!");
            e.printStackTrace();
            return 0;
        }
    }

    public int deleteTokimonByIdCall(Long id) {
        try {
            URL url = new URL("http://localhost:8080/api/tokimon/" + id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.connect();
            System.out.println(String.format("%s %s", connection.getRequestMethod(), url.toString()));
            return connection.getResponseCode();
        } catch (IOException e) {
            System.out.println("Could not complete call to server!");
            e.printStackTrace();
        }
        return 0;
    }
}
