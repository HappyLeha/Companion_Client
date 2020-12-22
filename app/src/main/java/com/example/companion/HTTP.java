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
import java.util.List;

public class HTTP {
    public static class UserPost extends AsyncTask<Object,Void,Integer> {
        protected Integer doInBackground (Object... object)  {
            try {
                URL obj = new URL("http://192.168.1.46:8080/users/add/");
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setConnectTimeout(2000);
                con.setDoOutput(true);
                OutputStream os = con.getOutputStream();
                ObjectMapper mapper=new ObjectMapper();
                os.write(mapper.writeValueAsString(object[0]).getBytes());
                os.flush();
                os.close();
                int responseCode = con.getResponseCode();
                return responseCode;
            }
            catch (Exception e) {
                return -1;
            }
        }
    }
    public static class UserCountPost extends AsyncTask<String,Void,Object> {
        protected Object doInBackground (String... strings)  {
            try {
                URL obj = new URL("http://192.168.1.46:8080/users/count/"+strings[0]);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setConnectTimeout(2000);
                int responseCode = con.getResponseCode();
                switch (responseCode) {
                    case HttpURLConnection.HTTP_OK:
                        BufferedReader in = new BufferedReader(new InputStreamReader(
                                con.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();
                        ObjectMapper mapper=new ObjectMapper();
                        Count count = mapper.readValue(response.toString(), Count.class);
                        return count;
                    case HttpURLConnection.HTTP_NOT_FOUND: {
                        return new Count(null,null);
                    }
                    default: return null;
                }
            }
            catch (Exception e) {
                return null;
            }
        }
    }
    public static class UsersGet extends AsyncTask<Void,Void,List<User>> {
        protected List<User> doInBackground (Void...voids)  {
             try {
                URL obj = new URL("http://192.168.1.46:8080/users/all/");
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                con.setConnectTimeout(2000);
                int responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
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
                    CollectionType listType = factory.constructCollectionType(List.class,User.class);
                    List<User> list = mapper.readValue(response.toString(), listType);
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
    public static class UserGet extends AsyncTask<String,Void, Object> {

        protected Object doInBackground (String...strings)  {
            try {
                URL obj = new URL("http://192.168.1.46:8080/users/"+strings[0]);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                con.setConnectTimeout(2000);
                int responseCode = con.getResponseCode();
                switch (responseCode) {
                    case HttpURLConnection.HTTP_OK:
                        BufferedReader in = new BufferedReader(new InputStreamReader(
                                con.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();
                        ObjectMapper mapper=new ObjectMapper();
                        User user = mapper.readValue(response.toString(), User.class);
                        return user;
                    case HttpURLConnection.HTTP_NOT_FOUND: {
                        return null;
                    }
                    default: return -1;
                }
            }
            catch (Exception e) {
                return -1;
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
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    return true;
                } else {
                    return false;
                }

            } catch (Exception e) {
                return false;
            }
        }
    }
    public static class UserPut extends AsyncTask<Object,Void,Integer> {
        protected Integer doInBackground (Object... object)  {
            try {
                URL obj = new URL("http://192.168.1.46:8080/users/edit/"+object[0].toString());
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("PUT");
                con.setRequestProperty("Content-Type", "application/json");
                con.setConnectTimeout(2000);
                con.setDoOutput(true);
                OutputStream os = con.getOutputStream();
                ObjectMapper mapper=new ObjectMapper();
                os.write(mapper.writeValueAsString(object[1]).getBytes());
                os.flush();
                os.close();
                int responseCode = con.getResponseCode();
                return responseCode;
            }
            catch (Exception e) {
                return -1;
            }
        }
    }
    public static class TripsPost extends AsyncTask<Object,Void,List<Trip>> {
        protected List<Trip> doInBackground (Object...object)  {
            try {
                URL obj = new URL("http://192.168.1.46:8080/trips/all/");
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setConnectTimeout(2000);
                OutputStream os = con.getOutputStream();
                ObjectMapper mapper=new ObjectMapper();
                os.write(mapper.writeValueAsString(object[0]).getBytes());
                os.flush();
                os.close();
                int responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    mapper=new ObjectMapper();
                    TypeFactory factory = mapper.getTypeFactory();
                    CollectionType listType = factory.constructCollectionType(List.class,Trip.class);
                    List<Trip> list = mapper.readValue(response.toString(), listType);
                    if (list==null) list=new ArrayList<Trip>();
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
    public static class TripsComplietedGet extends AsyncTask<String,Void,List<Trip>> {

        protected List<Trip> doInBackground (String...strings)  {
            try {
                URL obj = new URL("http://192.168.1.46:8080/trips/complieted/"+strings[0]);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                con.setConnectTimeout(2000);
                int responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
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
                    CollectionType listType = factory.constructCollectionType(List.class,Trip.class);
                    List<Trip> list = mapper.readValue(response.toString(), listType);
                    if (list==null) list=new ArrayList<Trip>();
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
    public static class TripsFutureGet extends AsyncTask<String,Void,List<Trip>> {

        protected List<Trip> doInBackground (String...strings)  {
            try {
                URL obj = new URL("http://192.168.1.46:8080/trips/future/"+strings[0]);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                con.setConnectTimeout(2000);
                int responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
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
                    CollectionType listType = factory.constructCollectionType(List.class,Trip.class);
                    List<Trip> list = mapper.readValue(response.toString(), listType);
                    if (list==null) list=new ArrayList<Trip>();
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
    public static class TripPost extends AsyncTask<Object,Void,Integer> {
        protected Integer doInBackground (Object... object)  {
            try {
                URL obj = new URL("http://192.168.1.46:8080/trips/add/");
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setConnectTimeout(2000);
                con.setDoOutput(true);
                OutputStream os = con.getOutputStream();
                ObjectMapper mapper=new ObjectMapper();
                os.write(mapper.writeValueAsString(object[0]).getBytes());
                os.flush();
                os.close();
                int responseCode = con.getResponseCode();
                return responseCode;
            }
            catch (Exception e) {
                return -1;
            }
        }
    }

    public static class TripGet extends AsyncTask<Integer,Void, Object> {

        protected Object doInBackground (Integer...integers)  {
            try {
                URL obj = new URL("http://192.168.1.46:8080/trips/"+integers[0]);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                con.setConnectTimeout(2000);
                int responseCode = con.getResponseCode();
                switch (responseCode) {
                    case HttpURLConnection.HTTP_OK:
                        BufferedReader in = new BufferedReader(new InputStreamReader(
                                con.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();
                        ObjectMapper mapper=new ObjectMapper();
                        Trip trip = mapper.readValue(response.toString(), Trip.class);
                        return trip;
                    case HttpURLConnection.HTTP_NOT_FOUND: {
                        return null;
                    }
                    default: return -1;
                }
            }
            catch (Exception e) {
                return -1;
            }
        }
    }
    public static class TripDelete extends AsyncTask<Integer,Void,Boolean> {
        protected Boolean doInBackground(Integer... integers) {
            try {
                URL obj = new URL("http://192.168.1.46:8080/trips/delete/" + integers[0]);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("DELETE");
                int responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    return true;
                } else {
                    return false;
                }

            } catch (Exception e) {
                return false;
            }
        }
    }
    public static class TripPut extends AsyncTask<Object,Void,Integer> {
        protected Integer doInBackground (Object... object)  {
            try {
                URL obj = new URL("http://192.168.1.46:8080/trips/edit/"+object[0].toString());
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("PUT");
                con.setRequestProperty("Content-Type", "application/json");
                con.setConnectTimeout(2000);
                con.setDoOutput(true);
                OutputStream os = con.getOutputStream();
                ObjectMapper mapper=new ObjectMapper();
                os.write(mapper.writeValueAsString(object[1]).getBytes());
                os.flush();
                os.close();
                int responseCode = con.getResponseCode();
                return responseCode;
            }
            catch (Exception e) {
                return -1;
            }
        }
    }
    public static class ConnectionPost extends AsyncTask<Object,Void,Integer> {
        protected Integer doInBackground (Object... object)  {
            try {
                URL obj = new URL("http://192.168.1.46:8080/connections/add/");
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setConnectTimeout(2000);
                con.setDoOutput(true);
                OutputStream os = con.getOutputStream();
                ObjectMapper mapper=new ObjectMapper();
                os.write(mapper.writeValueAsString(object[0]).getBytes());
                os.flush();
                os.close();
                int responseCode = con.getResponseCode();
                return responseCode;
            }
            catch (Exception e) {
                return -1;
            }
        }
    }
    public static class ConnectionDelete extends AsyncTask<Object,Void,Integer> {
        protected Integer doInBackground (Object... object)  {
            try {
                URL obj = new URL("http://192.168.1.46:8080/connections/delete/");
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("DELETE");
                con.setRequestProperty("Content-Type", "application/json");
                con.setConnectTimeout(2000);
                con.setDoOutput(true);
                OutputStream os = con.getOutputStream();
                ObjectMapper mapper=new ObjectMapper();
                os.write(mapper.writeValueAsString(object[0]).getBytes());
                os.flush();
                os.close();
                int responseCode = con.getResponseCode();
                return responseCode;
            }
            catch (Exception e) {
                return -1;
            }
        }
    }
    public static class ConnectionHead extends AsyncTask<Object,Void,Integer> {
        protected Integer doInBackground (Object... object)  {
            try {
                URL obj = new URL("http://192.168.1.46:8080/connections/head/");
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setConnectTimeout(2000);
                con.setDoOutput(true);
                OutputStream os = con.getOutputStream();
                ObjectMapper mapper=new ObjectMapper();
                os.write(mapper.writeValueAsString(object[0]).getBytes());
                os.flush();
                os.close();
                int responseCode = con.getResponseCode();
                return responseCode;
            }
            catch (Exception e) {
                return -1;
            }
        }
    }
    public static class RatingPost extends AsyncTask<Object,Void,Integer> {
        protected Integer doInBackground (Object... object)  {
            try {
                URL obj = new URL("http://192.168.1.46:8080/ratings/add/");
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setConnectTimeout(2000);
                con.setDoOutput(true);
                OutputStream os = con.getOutputStream();
                ObjectMapper mapper=new ObjectMapper();
                os.write(mapper.writeValueAsString(object[0]).getBytes());
                os.flush();
                os.close();
                int responseCode = con.getResponseCode();
                return responseCode;
            }
            catch (Exception e) {
                return -1;
            }
        }
    }
    public static class RatingsGet extends AsyncTask<String,Void, Double> {
        protected Double doInBackground (String...strings)  {
            try {
                URL obj = new URL("http://192.168.1.46:8080/ratings/all/"+strings[0]);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                con.setConnectTimeout(2000);
                int responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    ObjectMapper mapper=new ObjectMapper();
                    Double aDouble = mapper.readValue(response.toString(), Double.class);
                    return aDouble;
                } else {
                    return null;
                }
            }
            catch (Exception e) {
                return null;
            }
        }
    }
}
