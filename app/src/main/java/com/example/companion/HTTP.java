package com.example.companion;

import android.os.AsyncTask;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HTTP {
    public static class UserPost extends AsyncTask<Object,Void,Integer> {
        protected Integer doInBackground (Object... object)  {
            String json="";
            try {
                URL obj = new URL("http://192.168.1.46:8080/users/add/");
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setConnectTimeout(2000);
                //con.setRequestProperty("User-Agent", "Mozilla/5.0");

                // For POST only - START
                con.setDoOutput(true);
                OutputStream os = con.getOutputStream();
                ObjectMapper mapper=new ObjectMapper();
                json=mapper.writeValueAsString(object[0]);
                os.write(mapper.writeValueAsString(object[0]).getBytes());
                os.flush();
                os.close();
                // For POST only - END

                int responseCode = con.getResponseCode();
                //System.out.println("POST Response Code :: " + responseCode);

                return responseCode;

            }
            catch (Exception e) {

                return -1;
            }
        }
    }
    public static class UsersGet extends AsyncTask<Void,Void, ArrayList<User>> {
        protected ArrayList<User> doInBackground (Void...voids)  {
            try {
                URL obj = new URL("http://192.168.1.46:8080/users/all/");
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                int responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) { // success
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    ObjectMapper mapper=new ObjectMapper();
                    TypeFactory factory = mapper.getTypeFactory();
                    CollectionType listType = factory.constructCollectionType(ArrayList.class,User.class);
                    ArrayList<User> list = mapper.readValue(response.toString(), listType);
                    return list;
                } else {
                    return null;
                }

            }
            catch (Exception e) {
                return null;
            }
        }
    }
    public static class UserDelete extends AsyncTask<String,Void,Boolean> {
        protected Boolean doInBackground(String... strings) {
            try {
                URL obj = new URL("http://192.168.1.46:8080/users/delete/" + strings[0]);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("DELETE");
                int responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) { // success
                    return true;
                } else {
                    return false;
                }

            } catch (Exception e) {
                return false;
            }
        }
    }
}
