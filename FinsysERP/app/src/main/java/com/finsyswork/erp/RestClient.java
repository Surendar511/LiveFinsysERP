package com.finsyswork.erp;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Fin-Vipin on 04/03/2019.
 */

public class RestClient {

    static Context context;
    private static int responseCode;
    private static String response;

    public static String getResponse() {
        return response;
    }

    public static void setResponse(String response) {
        RestClient.response = response;
    }

    public static int getResponseCode() {
        return responseCode;
    }

    public static void setResponseCode(int responseCode) {
        RestClient.responseCode = responseCode;
    }

    public static void Execute(String urlMethod, String col1, String col2, String col3, String col4, String col5) {
        try {
            String requestMethod = "POST";
            String OPERATION_NAME = "http://" + fgen.Serverip + "/finapi/FinWebApisv.svc/";
            StringBuilder urlString = new StringBuilder(OPERATION_NAME + urlMethod);
//            urlString.append("?col1=" + col1);
//            urlString.append("?col2=" + col2);
//            urlString.append("?col3=" + col3);
//            urlString.append("?col4=" + col4);
//            urlString.append("?col5=" + col5);

            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("col1", col1);
            jsonObject.accumulate("col2", col2);
            jsonObject.accumulate("col3", col3);
            jsonObject.accumulate("col4", col4);
            jsonObject.accumulate("col5", col5);
            String jsonData = jsonObject.toString();

            URL url = new URL(urlString.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(requestMethod);
            conn.setReadTimeout(10000 /*milliseconds*/);
            conn.setConnectTimeout(15000 /* milliseconds */);

            switch (requestMethod) {
                case "POST":
                case "PUT":
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                    conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
                    conn.connect();
                    OutputStream os = new BufferedOutputStream(conn.getOutputStream());
                    os.write(jsonData.getBytes());
                    os.flush();
                    responseCode = conn.getResponseCode();
                    break;
                case "GET":
                    responseCode = conn.getResponseCode();
                    System.out.println("GET Response Code :: " + responseCode);
                    break;
            }
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                String inputLine;
                StringBuffer tempResponse = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    tempResponse.append(inputLine);
                }
                in.close();
                response = tempResponse.toString();
                System.out.println(response.toString());
            } else {
                System.out.println("GET request not worked");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}