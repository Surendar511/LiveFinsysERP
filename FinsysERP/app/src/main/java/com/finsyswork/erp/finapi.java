package com.finsyswork.erp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.finsyswork.erp.fgen.Lpad;


/**
 * Created by Fin-Vipin on 04/03/2019.
 */

public class finapi {

    public static String OPERATION_NAME = "", mq = "";
    public static String urlextra = "/finapi/FinWebApisv.svc/";
    public static int timeout = 5000 * 2;//this is 2 second and the default time out is 3 second.
    public static ArrayList<team> fed = new ArrayList<>();
    public static Context context;
    public static int xcount = 0;
//    private DataLoader dataLoader;

    public static ArrayList<team6> getData(String cocd, String squery) {
        InputStream inputStream = null;
        ArrayList<team6> fed = new ArrayList<>();
        String result = "";
        boolean myval = false;

        OPERATION_NAME = "http://" + fgen.Serverip + urlextra + "Getlist";
        if(fgen.FileServerip.length() > 0)
        {
            OPERATION_NAME = "http://" + fgen.FileServerip + urlextra + "Getlist";
            fgen.FileServerip = "";
        }

        try {
            HttpParams httpParameters = new BasicHttpParams();
            // Set the timeout in milliseconds until a connection is established.
            // The default value is zero, that means the timeout is not used.
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);
            DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(OPERATION_NAME);
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("col1", cocd);
            jsonObject.accumulate("col2", "");
            jsonObject.accumulate("col3", squery);
            jsonObject.accumulate("col4", "");
            jsonObject.accumulate("col5", "");
            jsonObject.accumulate("col6", "");
            String json = jsonObject.toString();
            //StringEntity se = new StringEntity(json);
            StringEntity se = new StringEntity(json,"utf-8");
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json; charset=UTF-8");
            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject explrObject = jsonArray.getJSONObject(i);

                        fed.add(new team6(explrObject.getString("col1"), explrObject.getString("col2"), explrObject.getString("col3"), explrObject.getString("col4"), explrObject.getString("col5"), explrObject.getString("col6"), false));
                    }

                    WriteJSONResponse(jsonArray.toString(), context);

                } catch (Exception e) {
                    mq = e.toString();
                }
            } else
                result = "Did not work!";
        } catch (SocketException ex) {
            ex.printStackTrace();
            fgen.finalresult = ex.toString();
        } catch (Exception e) {
            e.printStackTrace();
            fgen.finalresult = e.toString();
        }
        return fed;
    }


    public static ArrayList<team> getData_login(String cocd, String squery) {
        InputStream inputStream = null;
        ArrayList<team> fed = new ArrayList<>();
        String result = "";
        boolean myval = false;

        OPERATION_NAME = "http://" + fgen.Serverip + urlextra + "Getlist";
        if(fgen.FileServerip.length() > 0)
        {
            OPERATION_NAME = "http://" + fgen.FileServerip + urlextra + "Getlist";
            fgen.FileServerip = "";
        }

        try {
            HttpParams httpParameters = new BasicHttpParams();
            // Set the timeout in milliseconds until a connection is established.
            // The default value is zero, that means the timeout is not used.
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);
            DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(OPERATION_NAME);
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("col1", cocd);
            jsonObject.accumulate("col2", "");
            jsonObject.accumulate("col3", squery);
            jsonObject.accumulate("col4", "");
            jsonObject.accumulate("col5", "");
            jsonObject.accumulate("col6", "");
            String json = jsonObject.toString();
            StringEntity se = new StringEntity(json,"utf-8");
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json; charset=UTF-8");
            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject explrObject = jsonArray.getJSONObject(i);

                        fed.add(new team(explrObject.getString("col1"), explrObject.getString("col2"), explrObject.getString("col3"), explrObject.getString("col4"), explrObject.getString("col5"), false));
                    }

                    WriteJSONResponse(jsonArray.toString(), context);

                } catch (Exception e) {
                    mq = e.toString();
                }
            } else
                result = "Did not work!";
        } catch (SocketException ex) {
            ex.printStackTrace();
            fgen.finalresult = ex.toString();
        } catch (Exception e) {
            e.printStackTrace();
            fgen.finalresult = e.toString();
        }
        return fed;
    }



    public static String seekiname(String cocd, String squery, String field) {
        InputStream inputStream = null;
        ArrayList<team> fed = new ArrayList<>();
        String result = "";
        boolean myval = false;
        OPERATION_NAME = "http://" + fgen.Serverip + urlextra + "seek_iname";
        if(fgen.FileServerip.length() > 0)
        {
            OPERATION_NAME = "http://" + fgen.FileServerip + urlextra + "seek_iname";
            fgen.FileServerip = "";
        }
        try {
            HttpParams httpParameters = new BasicHttpParams();
            // Set the timeout in milliseconds until a connection is established.
            // The default value is zero, that means the timeout is not used.
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);
            DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(OPERATION_NAME);
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("col1", cocd);
            jsonObject.accumulate("col2", squery);
            jsonObject.accumulate("col3", field);
            jsonObject.accumulate("col4", "");
            jsonObject.accumulate("col5", "");
            String json = jsonObject.toString();
            StringEntity se = new StringEntity(json,"utf-8");
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json; charset=UTF-8");
            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null) {
                String wsresult = convertInputStreamToString(inputStream);
                try {
                    JSONArray jsonArray = new JSONArray(wsresult);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject explrObject = jsonArray.getJSONObject(i);
                        result = explrObject.getString("col1");
                    }
                } catch (Exception e) {
                    mq = e.toString();
                }
            } else
                result = "Did not work!";
        } catch (SocketException ex) {
            ex.printStackTrace();
            fgen.finalresult = ex.toString();
        } catch (Exception e) {
            e.printStackTrace();
            fgen.finalresult = e.toString();
        }
        return result;
    }

    public static String executecmd(String cocd, String query1) {
        InputStream inputStream = null;
        ArrayList<team> fed = new ArrayList<>();
        String result = "";

        boolean myval = false;
        OPERATION_NAME = "http://" + fgen.Serverip + urlextra + "execute_transaction";

        try {
            HttpParams httpParameters = new BasicHttpParams();
            // Set the timeout in milliseconds until a connection is established.
            // The default value is zero, that means the timeout is not used.
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);
            HttpClient httpclient = new DefaultHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(OPERATION_NAME);
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("col1", cocd);
            jsonObject.accumulate("col2", query1);
            jsonObject.accumulate("col3", "-");
            jsonObject.accumulate("col4", "-");
            jsonObject.accumulate("col5", "-");
            String json = jsonObject.toString();
            StringEntity se = new StringEntity(json,"utf-8");
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json; charset=UTF-8");
            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null) {
                String wsresult = convertInputStreamToString(inputStream);
                try {
                    JSONArray jsonArray = new JSONArray(wsresult);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject explrObject = jsonArray.getJSONObject(i);
                        result = explrObject.getString("col1");
                    }

                    WriteJSONResponse(jsonArray.toString(), context);

                } catch (Exception e) {
                    result = e.toString();
                }
            } else
                result = "Did not work!";
        } catch (Exception exception) {
            fgen.finalresult = exception.toString().trim();
        }
        return result;
    }

    public static ArrayList<team> getApi(String cocd, String apiName, String col2, String col3, String col4, String col5) {
        InputStream inputStream = null;
        ArrayList<team> fed = new ArrayList<>();
        String result = "";
        boolean myval = false;
        OPERATION_NAME = "http://" + fgen.Serverip + urlextra + apiName;
        if(fgen.FileServerip.length() > 0)
        {
            OPERATION_NAME = "http://" + fgen.FileServerip + urlextra + apiName;
            fgen.FileServerip = "";
        }
        try {
            HttpParams httpParameters = new BasicHttpParams();
            // Set the timeout in milliseconds until a connection is established.
            // The default value is zero, that means the timeout is not used.
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);
            DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(OPERATION_NAME);
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("col1", cocd);
            jsonObject.accumulate("col2", col2);
            jsonObject.accumulate("col3", col3);
            jsonObject.accumulate("col4", col4);
            jsonObject.accumulate("col5", col5);
            String json = jsonObject.toString();
            StringEntity se = new StringEntity(json,"utf-8");
//            httpPost.setEntity(se);
            httpPost.setEntity(new ByteArrayEntity(json.toString().getBytes("UTF-8")));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json; charset=UTF-8");
            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject explrObject = jsonArray.getJSONObject(i);

                        fed.add(new team(explrObject.getString("col1"), explrObject.getString("col2"), explrObject.getString("col3"), explrObject.getString("col4"), explrObject.getString("col5"), false));
                    }
                    WriteJSONResponse(jsonArray.toString(), context);
                } catch (Exception e) {
                    mq = e.toString();
                }
            } else
                result = "Did not work!";
        } catch (SocketException ex) {
            ex.printStackTrace();
            fgen.finalresult = ex.toString();
        } catch (Exception e) {
            e.printStackTrace();
            fgen.finalresult = e.toString();
        }
        return fed;
    }

    public static ArrayList<team> getApi(String cocd, String apiName, String col2, String col3, String col4, String col5, String col6) {
        InputStream inputStream = null;
        ArrayList<team> fed = new ArrayList<>();
        String result = "";
        boolean myval = false;
        OPERATION_NAME = "http://" + fgen.Serverip + urlextra + apiName;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            // Set the timeout in milliseconds until a connection is established.
            // The default value is zero, that means the timeout is not used.
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);
            HttpClient httpclient = new DefaultHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(OPERATION_NAME);
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("col1", cocd);
            jsonObject.accumulate("col2", col2);
            jsonObject.accumulate("col3", col3);
            jsonObject.accumulate("col4", col4);
            jsonObject.accumulate("col5", col5);
            jsonObject.accumulate("col6", col6);
            String json = jsonObject.toString();
            StringEntity se = new StringEntity(json,"utf-8");
//            httpPost.setEntity(se);
            httpPost.setEntity(new ByteArrayEntity(json.toString().getBytes("UTF-8")));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json; charset=UTF-8");
            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject explrObject = jsonArray.getJSONObject(i);
                        fgen.ximage_name = explrObject.getString("col2");
                        if(fgen.ximage_name.equals("-")) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                            String currentDateandTime = sdf.format(new Date());

                            fgen.ximage_name = currentDateandTime;
                        }
                        fed.add(new team(explrObject.getString("col1"), explrObject.getString("col2"), explrObject.getString("col3"), explrObject.getString("col4"), explrObject.getString("col5"), false));
                    }

                    WriteJSONResponse(jsonArray.toString(), context);

                } catch (Exception e) {
                    mq = e.toString();
                }
            } else
                result = "Did not work!";
        } catch (SocketException ex) {
            ex.printStackTrace();
            fgen.finalresult = ex.toString();
        } catch (Exception e) {
            e.printStackTrace();
            fgen.finalresult = e.toString();
        }
        if(frm_jobw_prod.form_name.equals("frm_jobw_prod"))
        {   finapi.job_work_production(fed);
        }
        return fed;
    }

    public static ArrayList<team> getApiImg(String cocd, String col2, String col3, String col4, String col5, String col6) {
        InputStream inputStream = null;
        ArrayList<team> fed = new ArrayList<>();
        String result = "";
        boolean myval = false;

        if(fgen.x41.equals(""))
        {
            fgen.x41 = finapi.seekiname(fgen.mcd, "SELECT PARAMS AS COL1 FROM CONTROLS WHERE ID='X41'", "COL1");
            if(fgen.x41.trim().length() < 5)
            {
                new android.app.AlertDialog.Builder(context)
                        .setTitle("Error Found!")
                        .setMessage("Please Fill Web App URL in ERP Control Panel!!")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                return fed;
            }
        }
        OPERATION_NAME = "http://"+fgen.x41+"/om_api_fileupload.aspx";
        if(cocd.equals("SGRP"))
        {
            OPERATION_NAME = "http://sgerp.salmangroup.com/fin-base/om_api_fileupload.aspx";
        }

        try {
            HttpParams httpParameters = new BasicHttpParams();
            // Set the timeout in milliseconds until a connection is established.
            // The default value is zero, that means the timeout is not used.
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);
            HttpClient httpclient = new DefaultHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(OPERATION_NAME);
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("col1", cocd);
            jsonObject.accumulate("col2", col2);
            jsonObject.accumulate("col3", col3);
            jsonObject.accumulate("col4", col4);
            jsonObject.accumulate("col5", col5);

            String json = jsonObject.toString();
            StringEntity se = new StringEntity(json,"utf-8");
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json; charset=UTF-8");
            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject explrObject = jsonArray.getJSONObject(i);

                        fed.add(new team(explrObject.getString("col1"), explrObject.getString("col2"), explrObject.getString("col3"), explrObject.getString("col4"), explrObject.getString("col5"), false));
                    }
                    WriteJSONResponse(col2+"\n\n"+jsonArray.toString(), context);
                } catch (Exception e) {
                    mq = e.toString();
                }
            } else
                result = "Did not work!";

        } catch (SocketException ex) {
            ex.printStackTrace();
            fgen.finalresult = ex.toString();
        } catch (Exception e) {
            e.printStackTrace();
            fgen.finalresult = e.toString();
        }
        return fed;
    }

    public static ArrayList<team> getApiForPOPUP(String cocd, String apiName, String col2, String col3, String col4, String col5, String col6) {
        InputStream inputStream = null;
        ArrayList<team> fed = new ArrayList<>();
        String result = "";
        boolean myval = false;

        OPERATION_NAME = "http://" + fgen.Serverip + urlextra + "DataListA_api";
        if(col2.equals("EP860")) {
//            OPERATION_NAME = "http://mis.finsys.biz/finapi/FinWebApisv.svc/DataListA_api";
            OPERATION_NAME = "http://app3.finsys.biz/finapi/FinWebApisv.svc/DataListA_api";
        }

        try {
            HttpParams httpParameters = new BasicHttpParams();
            // Set the timeout in milliseconds until a connection is established.
            // The default value is zero, that means the timeout is not used.
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);
            HttpClient httpclient = new DefaultHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(OPERATION_NAME);
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("col1", cocd);
            jsonObject.accumulate("col2", col2);
            jsonObject.accumulate("col3", col3);
            jsonObject.accumulate("col4", col4);
            jsonObject.accumulate("col5", col5);
            jsonObject.accumulate("col6", col6);
            String json = jsonObject.toString();
            StringEntity se = new StringEntity(json,"utf-8");
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json; charset=UTF-8");
            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject explrObject = jsonArray.getJSONObject(i);
                        fgen.ximage_name = explrObject.getString("col2") ;
                        fed.add(new team(explrObject.getString("col1"), explrObject.getString("col2"), explrObject.getString("col3"), explrObject.getString("col4"), explrObject.getString("col5"), false));
                    }

                    WriteJSONResponse(col2+"\n\n"+jsonArray.toString(), context);

                } catch (Exception e) {
                    mq = e.toString();
                }
            } else
                result = "Did not work!";

        } catch (SocketException ex) {
            ex.printStackTrace();
            fgen.finalresult = ex.toString();
        } catch (Exception e) {
            e.printStackTrace();
            fgen.finalresult = e.toString();
        }
        if(frm_jobw_prod.form_name.equals("frm_jobw_prod"))
        {
            finapi.job_work_production(fed);
        }
        return fed;
    }

//    public static ArrayList<team6> getApi6(String cocd, String apiName, String col2, String col3, String col4, String col5, String col6) {
//        InputStream inputStream = null;
//        ArrayList<team6> fed = new ArrayList<>();
//        String result = "";
//        boolean myval = false;
//        OPERATION_NAME = "http://" + fgen.Serverip + urlextra + apiName;
//        try {
//            HttpParams httpParameters = new BasicHttpParams();
//            // Set the timeout in milliseconds until a connection is established.
//            // The default value is zero, that means the timeout is not used.
//            HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);
//            HttpClient httpclient = new DefaultHttpClient(httpParameters);
//            HttpPost httpPost = new HttpPost(OPERATION_NAME);
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.accumulate("col1", cocd.trim());
//            jsonObject.accumulate("col2", col2.trim());
//            jsonObject.accumulate("col3", col3.trim());
//            jsonObject.accumulate("col4", col4.trim());
//            jsonObject.accumulate("col5", col5.trim());
//            jsonObject.accumulate("col6", col6.trim());
//            String json = jsonObject.toString();
//            StringEntity se = new StringEntity(json,"utf-8");
//            httpPost.setEntity(se);
//            httpPost.setHeader("Accept", "application/json");
//            httpPost.setHeader("Content-type", "application/json; charset=UTF-8");
//            HttpResponse httpResponse = httpclient.execute(httpPost);
//            inputStream = httpResponse.getEntity().getContent();
//            if (inputStream != null) {
//                result = convertInputStreamToString(inputStream);
//                try {
//                    JSONArray jsonArray = new JSONArray(result);
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject explrObject = jsonArray.getJSONObject(i);
//                        fed.add(new team6(explrObject.getString("col1"), explrObject.getString("col2"), explrObject.getString("col3"), explrObject.getString("col4"), explrObject.getString("col5"), explrObject.getString("col6"), false));
//                    }
//
//                    WriteJSONResponse(col2+"\n\n"+jsonArray.toString(), context);
//                } catch (Exception e) {
//                    mq = e.toString();
//                }
//            } else
//                result = "Did not work!";
//        } catch (SocketException ex) {
//            ex.printStackTrace();
//            fgen.finalresult = ex.toString();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            fgen.finalresult = e.toString();
//        }
//        return fed;
//    }


    public static ArrayList<team6> getApi6_SS(String cocd, String apiName, String col2, String col3, String col4, String col5, String col6) {
        ArrayList<team6> fed = new ArrayList<>();
        String result = "";
        OPERATION_NAME = "http://" + fgen.Serverip + urlextra + apiName;

        try {
            // Create OkHttpClient instance
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(timeout, TimeUnit.MILLISECONDS);
            builder.readTimeout(timeout, TimeUnit.MILLISECONDS);
            builder.writeTimeout(timeout, TimeUnit.MILLISECONDS);
            OkHttpClient httpClient = builder.build();

            // Create JSON request body
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("col1", cocd.trim());
            jsonObject.put("col2", col2.trim());
            jsonObject.put("col3", col3.trim());
            jsonObject.put("col4", col4.trim());
            jsonObject.put("col5", col5.trim());
            jsonObject.put("col6", col6.trim());
            String json = jsonObject.toString();
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);

            // Create Retrofit instance
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://" + fgen.Serverip + urlextra)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            // Create service instance
            MyApiService service = retrofit.create(MyApiService.class);


            // Make the API call
            Call<ArrayList<team6>> call = service.getApi6(OPERATION_NAME , "application/json", requestBody);
//            String dynamicUrl = "http://" + fgen.Serverip + urlextra + apiName;//
//            Call<ArrayList<team6>> call = service.dynamicApiMethod(dynamicUrl, "application/json", requestBody);
            Response<ArrayList<team6>> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                fed = response.body();
                for (int i = 0; i < fed.size(); i++) {
                    team6 item = fed.get(i);
                    // Process each team6 item
                }
                WriteJSONResponse(col2 + "\n\n" + fed.toString(), context);

            }
            else {
                String error = response.message();
                if (error.isEmpty()) {
                    error = "Did not work!";
                }
                throw new IOException(error);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            fgen.finalresult = e.toString();
        }
        return fed;
    }

    public static ArrayList<team6> getApi6(String cocd, String apiName, String col2, String col3, String col4, String col5, String col6) {
        ArrayList<team6> fed = new ArrayList<>();
        String result = "";
        String OPERATION_NAME = "http://" + fgen.Serverip + urlextra + apiName;

        try {
            DataLoader dataLoader = new DataLoader();

            // Create JSON request body
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("col1", cocd.trim());
            jsonObject.put("col2", col2.trim());
            jsonObject.put("col3", col3.trim());
            jsonObject.put("col4", col4.trim());
            jsonObject.put("col5", col5.trim());
            jsonObject.put("col6", col6.trim());
            String json = jsonObject.toString();
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);

            // Make the API request using the DataLoader
            Call<ArrayList<team6>> call = dataLoader.loadData(OPERATION_NAME, "application/json", requestBody);
            Response<ArrayList<team6>> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                fed = response.body();
                for (int i = 0; i < fed.size(); i++) {
                    team6 item = fed.get(i);
                    // Process each team6 item
                }
                WriteJSONResponse(col2 + "\n\n" + fed.toString(), context);

            } else {
                String error = response.message();
                if (error == null || error.isEmpty()) {
                    error = "Did not work!";
                }
                throw new IOException(error);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            fgen.finalresult = e.toString();
        }
        return fed;
    }


//            call.enqueue(new Callback<ArrayList<team6>>() {
//                @Override
//                public void onResponse(Call<ArrayList<team6>> call, Response<ArrayList<team6>> response) {
//                    if (response.isSuccessful()) {
//                        ArrayList<team6> responseBody = response.body();
//                        if (responseBody != null) {
//                            fed.addAll(responseBody);
//                            for (team6 item : fed) {
//                                // Process each team6 item
//                            }
//                            WriteJSONResponse(col2 + "\n\n" + fed.toString(), context);
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ArrayList<team6>> call, Throwable t) {
//                    t.printStackTrace();
//                    String error = t.getMessage();
//                    if (error == null || error.isEmpty()) {
//                        error = "Did not work!";
//                    }
//                }
//            });
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return fed;
//    }





    //OkHttpClient APPLIED BY SURENDAR ABOVE

    public static ArrayList<team> menuApi(String cocd, String _id, String clickedMenu) {
        InputStream inputStream = null;
        ArrayList<team> fed = new ArrayList<>();
        String result = "";
        boolean myval = false;
        OPERATION_NAME = "http://" + fgen.Serverip + urlextra + clickedMenu;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            // Set the timeout in milliseconds until a connection is established.
            // The default value is zero, that means the timeout is not used.
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);
            HttpClient httpclient = new DefaultHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(OPERATION_NAME);
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("col1", cocd);
            jsonObject.accumulate("col2", _id);
            jsonObject.accumulate("col3", "-");
            jsonObject.accumulate("col4", "-");
            jsonObject.accumulate("col5", "-");
            String json = jsonObject.toString();
            StringEntity se = new StringEntity(json,"utf-8");
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json; charset=UTF-8");
            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject explrObject = jsonArray.getJSONObject(i);
                        fed.add(new team(explrObject.getString("col1"), explrObject.getString("col2"), explrObject.getString("col3"), explrObject.getString("col4"), explrObject.getString("col5"), false));
                    }

                    WriteJSONResponse(jsonArray.toString(), context);

                } catch (Exception e) {
                    mq = e.toString();
                }
            } else
                result = "Did not work!";
        } catch (SocketException ex) {
            ex.printStackTrace();
            fgen.finalresult = ex.toString();
        } catch (Exception e) {
            e.printStackTrace();
            fgen.finalresult = e.toString();
        }
        return fed;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }

    public static String SendEmail1(String firm, String mailto, String cc,
                                    String bcc, String smtp_host, String from, String send_name,
                                    String password, int port, Boolean ssl, String Subject, String body) {
        InputStream inputStream = null;
        ArrayList<team> fed = new ArrayList<>();
        String result = "";
        String tfirm = "-", tmailto = "-", tcc = "-", tbcc = "-", tsmtp_host = "-", tfrom = "-", tsend_name = "-", tpassword = "-", tsubject = "-", tbody = "-";
        if (!firm.equals("")) tfirm = firm;
        if (!mailto.equals("")) tmailto = mailto;
        if (!cc.equals("")) tcc = cc;
        if (!bcc.equals("")) tbcc = bcc;
        if (!smtp_host.equals("")) tsmtp_host = smtp_host;
        if (!from.equals("")) tfrom = from;
        if (!send_name.equals("")) tsend_name = send_name;
        if (!password.equals("")) tpassword = password;
        if (!Subject.equals("")) tsubject = Subject;
        if (!body.equals("")) tbody = body;

        String qrystr = tfirm + fgen.textseprator + tmailto + fgen.textseprator + tcc + fgen.textseprator + tbcc
                + fgen.textseprator + tsmtp_host + fgen.textseprator + tfrom + fgen.textseprator +
                tsend_name + fgen.textseprator + tpassword + fgen.textseprator + port + fgen.textseprator + ssl
                + fgen.textseprator + tsubject + fgen.textseprator
                + tbody;

        OPERATION_NAME = "http://" + fgen.Serverip + urlextra + "SendEmail";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(OPERATION_NAME);
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("col1", qrystr);
            jsonObject.accumulate("col2", "");
            jsonObject.accumulate("col3", "");
            jsonObject.accumulate("col4", "-");
            jsonObject.accumulate("col5", "-");
            String json = jsonObject.toString();
            StringEntity se = new StringEntity(json,"utf-8");
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json; charset=UTF-8");
            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null) {
                String wsresult = convertInputStreamToString(inputStream);
                try {
                    JSONArray jsonArray = new JSONArray(wsresult);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject explrObject = jsonArray.getJSONObject(i);
                        result = explrObject.getString("col1");
                    }
                } catch (Exception e) {
                    result = e.toString();
                }
            } else
                result = "Did not work!";
        } catch (Exception exception) {
            fgen.finalresult = exception.toString().trim();
        }
        return result;
    }

    public static boolean showAlertMessage(Context context, ArrayList<team> result)
    {
        String Message = "";
        String xcol2 = "";
        Gson gson = new Gson();
        String listString = gson.toJson(result, new TypeToken<ArrayList<team>>() {}.getType());

        try {
            JSONArray array = new JSONArray(listString);
            for(int i=0; i< array.length(); i++){
                JSONObject respObj = array.getJSONObject(i);
                Message = respObj.getString("col1");
                xcol2 = respObj.getString("col2");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(Message.equals(""))
        {
            new AlertDialog.Builder(context)
                    .setTitle("Error Found!!")
                    .setMessage("Connection Has Breaked Please Try Again!!")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
        else {
            if(!Message.equals("NO STOCK")) {
                new AlertDialog.Builder(context)
                        .setTitle("Message")
                        .setMessage(Message)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        }
        if(Message.contains("Data Not Saved") || Message.equals("") || Message.equals("NO STOCK")  || Message.contains("invalid username/password;")){
            if(Message.equals("NO STOCK"))
            {
                new AlertDialog.Builder(context)
                        .setTitle(Message)
                        .setMessage(xcol2)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
            return false;
        }
        return true;
    }


    public static boolean showAlertMessageForStockCheck(Context context, ArrayList<team> result)
    {
        String Message = "";
        String items = "";
        Gson gson = new Gson();
        String listString = gson.toJson(result, new TypeToken<ArrayList<team>>() {}.getType());

        try {
            JSONArray array = new JSONArray(listString);
            for(int i=0; i< array.length(); i++){
                JSONObject respObj = array.getJSONObject(i);
                Message = respObj.getString("col1");
                items = respObj.getString("col2");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(Message.equals(""))
        {
            new AlertDialog.Builder(context)
                    .setTitle("Error Found!!")
                    .setMessage("Connection Has Broken, Please Try Again!!")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
            return false;
        }
        else {
            if(Message.toUpperCase().trim().contains("NO STOCK") || Message.toUpperCase().trim().contains("STORES STOCK NOT AVAILABLE") || Message.trim().contains("invalid username/password;"))
            {
                new AlertDialog.Builder(context)
                        .setTitle(Message)
                        .setMessage(items)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                return false;
            }
            new AlertDialog.Builder(context)
                    .setTitle("Message")
                    .setMessage(Message)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }

        if(!Message.contains("Data Saved")  || Message.equals("")){
            return false;
        }
        return true;
    }


    public static boolean showAlertMessageForImage(Context context, ArrayList<team> result)
    {
        String Message = "";
        Gson gson = new Gson();
        String listString = gson.toJson(result, new TypeToken<ArrayList<team>>() {}.getType());

        try {
            JSONArray array = new JSONArray(listString);
            for(int i=0; i< array.length(); i++){
                JSONObject respObj = array.getJSONObject(i);
                Message = respObj.getString("col1");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(Message.equals(""))
        {
            return false;
        }
        if(Message.contains("Data Not Saved") || Message.equals("") || Message.contains("invalid username/password;")){
            return false;
        }
        return true;
    }

    public static boolean showAlertMessageForMultipleSaving(Context context, ArrayList<team> result)
    {
        String Message = "";
        Gson gson = new Gson();
        String listString = gson.toJson(result, new TypeToken<ArrayList<team>>() {}.getType());

        try {
            JSONArray array = new JSONArray(listString);
            for(int i=0; i< array.length(); i++){
                JSONObject respObj = array.getJSONObject(i);
                Message = respObj.getString("col1");
                fgen.xpopup_col1 = respObj.getString("col1");
                fgen.xpopup_col2 = respObj.getString("col2");
                frm_jobw_prod.xxref_no = fgen.xpopup_col1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(Message.equals(""))
        {
            new AlertDialog.Builder(context)
                    .setTitle("Error Found!!")
                    .setMessage("Connection Has Breaked Please Try Again!!")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
            return false;
        }
        if(Message.contains("Data Not Saved") || Message.equals("")  || Message.contains("invalid username/password;")){
            new AlertDialog.Builder(context)
                    .setTitle("Message")
                    .setMessage(Message)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
            return false;
        }
        return true;
    }

    public static boolean RecieveDataFromApi(Context context, ArrayList<team6> result)
    {
        String Message = "";
        Gson gson = new Gson();
        String listString = gson.toJson(result, new TypeToken<ArrayList<team>>() {}.getType());

        try {
            JSONArray array = new JSONArray(listString);
            for(int i=0; i< array.length(); i++){
                JSONObject respObj = array.getJSONObject(i);
                fgen.xpopup_col1 = respObj.getString("col1");
                fgen.xpopup_col2 = respObj.getString("col2");
                fgen.xpopup_col3 = respObj.getString("col3");
                fgen.xpopup_col4 = respObj.getString("col4");
                fgen.xpopup_col5 = respObj.getString("col5");
                fgen.xpopup_col6 = respObj.getString("col6");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(!fgen.xpopup_col1.equals("Success") && fgen.frm_request.equals("frm_inter_stage_tfr"))
        {
            if(fgen.xpopup_col6.toUpperCase().contains("PLEASE FOLLOW FIFO")){
                new AlertDialog.Builder(context)
                        .setTitle(fgen.xpopup_col1)
                        .setMessage(fgen.xpopup_col6)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (context instanceof scannerView) {
                                    ((Activity) context).finish();
                                }
                            }
                        })
                        .show();
                return  false;
            }
            else {
                new AlertDialog.Builder(context)
                        .setTitle("Error")
                        .setMessage("Sorry, Scanned QR Is Invalid!!")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (context.equals(scannerView.class))
                                    ((Activity) context).finish();
                            }
                        })
                        .show();
                return false;
            }
        }
        if(fgen.xpopup_col1.toUpperCase().equals("FAILURE") || fgen.xpopup_col1.toUpperCase().equals("")){
            if(fgen.xpopup_col6.toUpperCase().contains("PLEASE FOLLOW FIFO")){
                new AlertDialog.Builder(context)
                        .setTitle(fgen.xpopup_col1)
                        .setMessage(fgen.xpopup_col6)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (context instanceof scannerView) {
                                    ((Activity) context).finish();
                                }
                            }
                        })
                        .show();
                return false;
            }
            else {
                new AlertDialog.Builder(context)
                        .setTitle(fgen.xpopup_col1)
                        .setMessage(fgen.xpopup_col6)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (context instanceof scannerView) {
                                    ((Activity) context).finish();
                                }
                            }
                        })
                        .show();
                return false;
            }
        }
        return true;
    }


    public static boolean showAlertMessageForBinScanning(Context context, ArrayList<team> result)
    {
        String Message = "";
        Gson gson = new Gson();
        String listString = gson.toJson(result, new TypeToken<ArrayList<team>>() {}.getType());

        try {
            JSONArray array = new JSONArray(listString);
            for(int i=0; i< array.length(); i++){
                JSONObject respObj = array.getJSONObject(i);
                Message = respObj.getString("col1");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new AlertDialog.Builder(context)
                .setTitle("Message")
                .setMessage(Message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ((Activity) context).finish();
                    }
                })
                .show();
        if(Message.contains("Data Not Saved")  || Message.contains("invalid username/password;")){
            return false;
        }
        return true;
    }

    public static boolean showAlertMessageForBinScanninge(Context context, ArrayList<team> result)
    {
        String Message = "";
        Gson gson = new Gson();
        String listString = gson.toJson(result, new TypeToken<ArrayList<team>>() {}.getType());

        try {
            JSONArray array = new JSONArray(listString);
            for(int i=0; i< array.length(); i++){
                JSONObject respObj = array.getJSONObject(i);
                Message = respObj.getString("col1");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new AlertDialog.Builder(context)
                .setTitle("Message")
                .setMessage(Message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        try {
                            frm_bin_scanning.txt_bin.setText("");
                            frm_bin_scanning.txt_bin.requestFocus();
                        }catch (Exception e){}
                    }
                })
                .show();
        if(Message.contains("Data Not Saved")  || Message.contains("invalid username/password;")){
            return false;
        }
        return true;
    }


    public  static ArrayList<String> fill_record_in_listview_popup(String form_code){
        ArrayList<String> list_view = new ArrayList<>();
        ArrayList<team> result = new ArrayList<>();

        while (xcount < 2) {
            if (frm_lead_enquiry.xfrm_lead_enquiry.equals("Y") && !frm_lead_enquiry.xfg_sub_group_code.equals("")) {
                result = finapi.getApiForPOPUP(fgen.mcd, "aLeave_ins", "" + form_code, fgen.cdt1, fgen.cdt2, fgen.muname, frm_lead_enquiry.xfg_sub_group_code);
                if(result.size() > 0) {
                    frm_lead_enquiry.xfg_sub_group_code = "";
                    xcount = 2;
                }
            }
            if (!frm_lead_enquiry.xcol6_udf_data.isEmpty()) {
                result = finapi.getApiForPOPUP(fgen.mcd, "aLeave_ins", "" + form_code, fgen.cdt1, fgen.cdt2, fgen.muname, frm_lead_enquiry.xcol6_udf_data);
                if(result.size() > 0) {
                    frm_lead_enquiry.xcol6_udf_data = "";
                    xcount = 2;
                }
            } else {
                if (!frm_lead_enquiry.xfrm_lead_enquiry.equals("Y")) {
                    result = finapi.getApiForPOPUP(fgen.mcd, "aLeave_ins", "" + form_code, fgen.cdt1, fgen.cdt2, fgen.muname, "-");
                    if(result.size() > 0) {
                        xcount = 2;
                    }
                }
            }
            xcount ++;
        }
        xcount = 0;
        Gson gson = new Gson();
        String listString = gson.toJson(result, new TypeToken<ArrayList<team>>() {}.getType());

        String leads_code = "";
        String leads_name = "";

        try {
            JSONArray array = new JSONArray(listString);

            for(int i=0; i< array.length(); i++){
                JSONObject respObj = array.getJSONObject(i);
                String data = respObj.getString("col2");
                Log.d("allData  ", data);
                String[] arrOfStr = data.split("!~!~!");
                int x=0;
                for (String col2 : arrOfStr){
                    Log.d("allData  ", col2);
                    if(x==0) {
                        leads_code = col2;
                        fgen.xpopup_col1 = col2;
                    }
                    if(x == 1)
                    {
                        fgen.xpopup_col2 = col2;
                    }
                    if(x==2) {
                        leads_name = col2;
                        fgen.xpopup_col3 = col2;
                        if (!fgen.frm_request.equals("frm_job_status") && !fgen.frm_request.equals("frm_quality_inspection") && !frm_lead_enquiry.xfrm_clicked_tab.equals("txtparty"))
                        {
                            if (leads_name.equals("")) {
                                list_view.add(leads_code + leads_name);
                            } else {
                                switch (fgen.frm_request) {
                                    case "frm_paper_end_for_jobs":
                                        list_view.add(leads_code.trim() + " --- " + fgen.xpopup_col2  + " --- " + leads_name.trim());
                                        break;
                                    default:
                                        list_view.add(leads_code.trim() + " --- " + leads_name.trim());
                                        break;
                                }
                            }
                        }
                    }
                    if(x==3)
                    {
                        fgen.xpopup_col4 = col2;
                    }
                    if(x==4)
                    {
                        fgen.xpopup_col5 = col2;

                        if(fgen.frm_request.equals("frm_job_status"))
                        {
                            list_view.add(leads_code + " --- " + leads_name + " --- " + col2);
                        }
                        if(fgen.frm_request.equals("frm_quality_inspection"))
                        {
                            list_view.add(leads_name + " --- " + fgen.xpopup_col4 + " --- " + fgen.xpopup_col5);
                        }
                    }
                    if(x==5)
                    {
                        fgen.xpopup_col6 = col2;
                    }
                    if(x==6)
                    {
                        fgen.xpopup_col7 = col2;
                        if(frm_lead_enquiry.xfrm_clicked_tab.equals("txtparty")) {
                            list_view.add(leads_code + " --- " + leads_name + " --- " + fgen.xpopup_col5 + " --- " + fgen.xpopup_col6 + " --- " + fgen.xpopup_col7);
                        }
                    }
                    x +=1;
                }

                switch (fgen.frm_request) {
                    case "frm_invoice_entry" :
                        frm_invoice_entry.txtparty_name.setText(data.split("!~!~!")[8].trim() + "---"  + data.split("!~!~!")[0].trim());
                        frm_invoice_entry.txtaddress.setText(data.split("!~!~!")[5].trim());
                        frm_invoice_entry.txtinv_no.setText(data.split("!~!~!")[6].trim());
                        frm_invoice_entry.txtinv_dt.setText(data.split("!~!~!")[7].trim());
                        String xitem = data.split("!~!~!")[1].trim();
                        xitem = data.split("!~!~!")[9].trim() + "---" + xitem;
                        String xqty = data.split("!~!~!")[2].trim();
                        String xno_of_box = data.split("!~!~!")[4].trim();

                        frm_invoice_entry.handler.commaninsert_data(new comman_model(xitem, xqty, xno_of_box));
                        frm_invoice_entry.list = frm_invoice_entry.handler.comman_get_data();
                        frm_invoice_entry.adapter = new comman_adapter(context, frm_invoice_entry.list);
                        frm_invoice_entry.recyclerView.setAdapter(frm_invoice_entry.adapter);
                        frm_invoice_entry.adapter.notifyDataSetChanged();
                        break;
                    case "frm_quality_inspection" :
//                        frm_quality_inspection.txtvendor.setText(fgen.xpopup_col1);
                        xitem = fgen.xpopup_col3 + "---" + fgen.xpopup_col4;
                        String xpart_no = fgen.xpopup_col3;
                        xqty = fgen.xpopup_col5;

                        frm_quality_inspection.handler.commaninsert_data(new comman_model(xitem, xpart_no, xqty));
                        frm_quality_inspection.list = frm_quality_inspection.handler.comman_get_data();
                        frm_quality_inspection.adapter = new comman_adapter(context, frm_quality_inspection.list);
                        frm_quality_inspection.recycler2.setAdapter(frm_quality_inspection.adapter);
                        frm_quality_inspection.adapter.notifyDataSetChanged();
                        break;
                    case "frm_lead_enquiry":
                        if(frm_lead_enquiry.xparty_popup_sel.equals("Y"))
                        {
                            frm_lead_enquiry.xparty_popup_sel = "N";
                            frm_lead_enquiry.txtcountry_name.setText(fgen.xpopup_col1);
                            frm_lead_enquiry.txtstate_name.setText(fgen.xpopup_col2);
                            frm_lead_enquiry.txtcity_name.setText(fgen.xpopup_col3);
                            frm_lead_enquiry.txtcontact_no.setText(fgen.xpopup_col4);
                        }
                    default:
                        break;
                }
                Log.d("addData ", "Success");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list_view;
    }


    public  static ArrayList<String> fill_record_in_listview_popup_for_lead_enquiry(String form_code){
        ArrayList<String> list_view = new ArrayList<>();
        ArrayList<team> result = new ArrayList<>();

        if(frm_lead_enquiry.xfrm_lead_enquiry.equals("Y")) {
            result = finapi.getApiForPOPUP(fgen.mcd, "aLeave_ins", "" + form_code, fgen.cdt1, fgen.cdt2, fgen.muname, frm_lead_enquiry.xfg_sub_group_code);
            frm_lead_enquiry.xfg_sub_group_code = "";
        }
        if(!frm_lead_enquiry.xcol6_udf_data.isEmpty())
        {
            result = finapi.getApiForPOPUP(fgen.mcd, "aLeave_ins", "" + form_code, fgen.cdt1, fgen.cdt2, fgen.muname, frm_lead_enquiry.xcol6_udf_data);
            frm_lead_enquiry.xcol6_udf_data = "";
        }
        else {
            if(!frm_lead_enquiry.xfrm_lead_enquiry.equals("Y")) {
                result = finapi.getApiForPOPUP(fgen.mcd, "aLeave_ins", "" + form_code, fgen.cdt1, fgen.cdt2, fgen.muname, "-");
            }
        }

        Gson gson = new Gson();
        String listString = gson.toJson(result, new TypeToken<ArrayList<team>>() {}.getType());

        String leads_code = "";
        String leads_name = "";

        try {
            JSONArray array = new JSONArray(listString);

            for(int i=0; i< array.length(); i++){
                JSONObject respObj = array.getJSONObject(i);
                String data = respObj.getString("col2");
                Log.d("allData  ", data);
                String[] arrOfStr = data.split("!~!~!");
                int x=0;
                for (String col2 : arrOfStr){
                    Log.d("allData  ", col2);
                    if(x==0) {
                        leads_code = col2;
                        fgen.xpopup_col1 = col2;
                    }
                    if(x == 1)
                    {
                        fgen.xpopup_col2 = col2;
                    }
                    if(x==2) {
                        leads_name = col2;
                        fgen.xpopup_col3 = col2;
                        if (!fgen.frm_request.equals("frm_job_status") && !fgen.frm_request.equals("frm_quality_inspection"))
                        {
                            if (leads_name.equals("")) {
                                list_view.add(leads_code + leads_name);
                            } else {
                                list_view.add(leads_code + " --- " + leads_name);
                            }
                        }
                    }
                    if(x==3)
                    {
                        fgen.xpopup_col4 = col2;
                    }
                    if(x==4)
                    {
                        fgen.xpopup_col5 = col2;
                        if(fgen.frm_request.equals("frm_job_status"))
                        {
                            list_view.add(leads_code + " --- " + leads_name + " --- " + col2);
                        }
                        if(fgen.frm_request.equals("frm_quality_inspection"))
                        {
                            list_view.add(leads_name + " --- " + fgen.xpopup_col4 + " --- " + fgen.xpopup_col5);
                        }
                    }
                    if(x==5)
                    {
                        fgen.xpopup_col6 = col2;
                    }
                    if(x==6)
                    {
                        fgen.xpopup_col7 = col2;
                    }
                    x +=1;
                }

                switch (fgen.frm_request) {
                    case "frm_invoice_entry" :
                        frm_invoice_entry.txtparty_name.setText(data.split("!~!~!")[8].trim() + "---"  + data.split("!~!~!")[0].trim());
                        frm_invoice_entry.txtaddress.setText(data.split("!~!~!")[5].trim());
                        frm_invoice_entry.txtinv_no.setText(data.split("!~!~!")[6].trim());
                        frm_invoice_entry.txtinv_dt.setText(data.split("!~!~!")[7].trim());
                        String xitem = data.split("!~!~!")[1].trim();
                        xitem = data.split("!~!~!")[9].trim() + "---" + xitem;
                        String xqty = data.split("!~!~!")[2].trim();
                        String xno_of_box = data.split("!~!~!")[4].trim();

                        frm_invoice_entry.handler.commaninsert_data(new comman_model(xitem, xqty, xno_of_box));
                        frm_invoice_entry.list = frm_invoice_entry.handler.comman_get_data();
                        frm_invoice_entry.adapter = new comman_adapter(context, frm_invoice_entry.list);
                        frm_invoice_entry.recyclerView.setAdapter(frm_invoice_entry.adapter);
                        frm_invoice_entry.adapter.notifyDataSetChanged();
                        break;
                    case "frm_quality_inspection" :
//                        frm_quality_inspection.txtvendor.setText(fgen.xpopup_col1);
                        xitem = fgen.xpopup_col3 + "---" + fgen.xpopup_col4;
                        String xpart_no = fgen.xpopup_col3;
                        xqty = fgen.xpopup_col5;

                        frm_quality_inspection.handler.commaninsert_data(new comman_model(xitem, xpart_no, xqty));
                        frm_quality_inspection.list = frm_quality_inspection.handler.comman_get_data();
                        frm_quality_inspection.adapter = new comman_adapter(context, frm_quality_inspection.list);
                        frm_quality_inspection.recycler2.setAdapter(frm_quality_inspection.adapter);
                        frm_quality_inspection.adapter.notifyDataSetChanged();
                        break;
                    case "frm_lead_enquiry":
                        if(frm_lead_enquiry.xparty_popup_sel.equals("Y"))
                        {
                            frm_lead_enquiry.xparty_popup_sel = "N";
                            frm_lead_enquiry.txtcountry_name.setText(fgen.xpopup_col1);
                            frm_lead_enquiry.txtstate_name.setText(fgen.xpopup_col2);
                            frm_lead_enquiry.txtcity_name.setText(fgen.xpopup_col3);
//                            frm_lead_enquiry.txtcontact_no.setText(fgen.xpopup_col4);
                        }
                    default:
                        break;
                }
                Log.d("addData ", "Success");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list_view;
    }


    public static ArrayList<String> fill_record_in_listview_popup_for_truck_loading(String form_code){
        ArrayList<String> list_view = new ArrayList<>();
        ArrayList<team> result = new ArrayList<>();
        if(frm_lead_enquiry.xfrm_lead_enquiry.equals("Y")) {
            result = finapi.getApiForPOPUP(fgen.mcd, "aLeave_ins", "" + form_code, fgen.cdt1, fgen.cdt2, fgen.muname, frm_lead_enquiry.xfg_sub_group_code);
        }else {
            result = finapi.getApiForPOPUP(fgen.mcd, "aLeave_ins", "" + form_code, fgen.cdt1, fgen.cdt2, fgen.muname, "-");
        }

        Gson gson = new Gson();
        String listString = gson.toJson(result, new TypeToken<ArrayList<team>>() {}.getType());

        String leads_code = "";
        String leads_name = "";

        try {
            JSONArray array = new JSONArray(listString);

            for(int i=0; i< array.length(); i++){
                JSONObject respObj = array.getJSONObject(i);
                String data = respObj.getString("col2");
                Log.d("allData  ", data);
                String[] arrOfStr = data.split("!~!~!");
                int x=0;
                for (String col2 : arrOfStr){
                    Log.d("allData  ", col2);
                    if(x==0) {
                        leads_code = col2;
                    }
                    if(x == 1)
                    {
                        fgen.xpopup_col2 = col2;
                    }
                    if(x==2) {
                        leads_name = col2;
                        if(leads_name.equals(""))
                        {
                            list_view.add(leads_code + leads_name);
                        }
                        else {
                            list_view.add(leads_code + " --- " + leads_name + " --- " + fgen.xpopup_col2);
                        }
                    }
                    x +=1;
                }
                Log.d("addData ", "Success");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list_view;
    }


    public  static ArrayList<String> fill_record_in_listview_popup_for_item_part_no(String form_code, String xcol6){
        ArrayList<String> list_view = new ArrayList<>();
        ArrayList<team> result = new ArrayList<>();
        result = finapi.getApiForPOPUP(fgen.mcd, "aLeave_ins", "" + form_code, fgen.cdt1, fgen.cdt2, fgen.muname, xcol6);

        Gson gson = new Gson();
        String listString = gson.toJson(result, new TypeToken<ArrayList<team>>() {}.getType());

        String leads_code = "";
        String leads_name = "";

        try {
            JSONArray array = new JSONArray(listString);

            for(int i=0; i< array.length(); i++){
                JSONObject respObj = array.getJSONObject(i);
                String data = respObj.getString("col2");
                Log.d("allData  ", data);
                String[] arrOfStr = data.split("!~!~!");
                int x=0;
                for (String col2 : arrOfStr){
                    Log.d("allData  ", col2);
                    if(x==0) {
                        leads_code = col2;
                        fgen.xpopup_col1 = col2;
                    }
                    if(x == 1)
                    {
                        fgen.xpopup_col2 = col2;
                    }
                    if(x==2) {
                        leads_name = col2;   // against vehicle selection fetch loading supervisor and customer
                        if(fgen.btnid.equals("EP483")) {
                            list_view.add(leads_code + " #~# " + leads_name);
                        }
                    }
                    if(x == 3)
                    {
                        fgen.xpopup_col3 = col2;
                        if(!fgen.frm_request.equals("frm_dispatch_entry")) {
                            list_view.add(leads_code + " --- " + leads_name + " --- " + fgen.xpopup_col3 + " #~# " + fgen.xpopup_col2);
                        }
                    }
                    if(x == 4)
                    {
                        fgen.xpopup_col4 = col2;
                        if(!fgen.frm_request.equals("frm_dispatch_entry")) {
                            list_view.add(leads_code + " --- " + leads_name + " --- " + fgen.xpopup_col3 + " --- " + fgen.xpopup_col4);
                        }
                    }
                    if(x == 5)
                    {
                        fgen.xpopup_col5 = col2;
                        if(fgen.frm_request.equals("frm_dispatch_entry")) {
                            list_view.add(leads_code + " --- " + leads_name + " --- " + fgen.xpopup_col3 + " --- " + fgen.xpopup_col4 + " --- " + fgen.xpopup_col5);
                        }
                    }
                    x +=1;
                }
                Log.d("addData ", "Success");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list_view;
    }


    public  static ArrayList<String> fill_record_in_listview_popup_for_truck(String form_code){
        ArrayList<String> list_view = new ArrayList<>();
        ArrayList<team> result = new ArrayList<>();
        if(frm_lead_enquiry.xfrm_lead_enquiry.equals("Y")) {
            result = finapi.getApiForPOPUP(fgen.mcd, "aLeave_ins", "" + form_code, fgen.cdt1, fgen.cdt2, fgen.muname, frm_lead_enquiry.xfg_sub_group_code);
        }else {
            result = finapi.getApiForPOPUP(fgen.mcd, "aLeave_ins", "" + form_code, fgen.cdt1, fgen.cdt2, fgen.muname, "-");
        }

        Gson gson = new Gson();
        String listString = gson.toJson(result, new TypeToken<ArrayList<team>>() {}.getType());

        String leads_code = "";
        String leads_name = "";

        try {
            JSONArray array = new JSONArray(listString);

            for(int i=0; i< array.length(); i++){
                JSONObject respObj = array.getJSONObject(i);
                String data = respObj.getString("col2");
                Log.d("allData  ", data);
                String[] arrOfStr = data.split("!~!~!");
                int x=0;
                for (String col2 : arrOfStr){
                    Log.d("allData  ", col2);
                    if(x==0) {
                        leads_code = col2.trim();
                    }
                    if(x == 1)
                    {
                        fgen.xpopup_col2 = col2.trim();
                    }
                    if(x==2) {
                        leads_name = col2.trim();
                        if(leads_name.equals(""))
                        {
                            list_view.add(leads_code + leads_name);
                        }
                        else {
                            list_view.add(leads_code + " !~!~! " + fgen.xpopup_col2 +" !~!~! " + leads_name);
                        }
                    }
                    x +=1;
                }
                Log.d("addData ", "Success");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list_view;
    }


    public static ArrayList<String> fill_record_in_listview_popup_for_jobwprod(String form_code){
        ArrayList<String> list_view = new ArrayList<>();
        ArrayList<team> result = new ArrayList<>();
        if(frm_lead_enquiry.xfrm_lead_enquiry.equals("Y")) {
            result = finapi.getApiForPOPUP(fgen.mcd, "aLeave_ins", "" + form_code, fgen.cdt1, fgen.cdt2, fgen.muname, frm_lead_enquiry.xfg_sub_group_code);
        }else {
            result = finapi.getApiForPOPUP(fgen.mcd, "aLeave_ins", "" + form_code, fgen.cdt1, fgen.cdt2, fgen.muname, "-");
        }

        Gson gson = new Gson();
        String listString = gson.toJson(result, new TypeToken<ArrayList<team>>() {}.getType());

        String leads_code = "";
        String leads_name = "";

        try {
            JSONArray array = new JSONArray(listString);

            for(int i=0; i< array.length(); i++){
                JSONObject respObj = array.getJSONObject(i);
                String data = respObj.getString("col2");
                Log.d("allData  ", data);
                String[] arrOfStr = data.split("!~!~!");
                int x=0;
                for (String col2 : arrOfStr){
                    Log.d("allData  ", col2);
                    if(x==0) {
                        leads_code = col2;
                    }
                    if(x == 1)
                    {
                        fgen.xpopup_col2 = col2;
                    }
                    if(x==2) {
                        fgen.xpopup_col3 = col2;
                    }
                    if(x == 3)
                    {
                        leads_name = col2;
                        if(leads_name.equals(""))
                        {
                            list_view.add(leads_code + leads_name);
                        }
                        else {
                            list_view.add(leads_code + " !~!~! " + fgen.xpopup_col2 +" !~!~! " + fgen.xpopup_col3 + " !~!~! " + leads_name);
                        }
                    }
                    x +=1;
                }
                Log.d("addData ", "Success");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list_view;
    }


    static void job_work_production(ArrayList<team> result)
    {
        Gson gson = new Gson();
        String listString = gson.toJson(result, new TypeToken<ArrayList<team>>() {}.getType());

        try {
            JSONArray array = new JSONArray(listString);

            for(int i=0; i< array.length(); i++){
                JSONObject respObj = array.getJSONObject(i);
                String data = respObj.getString("col2");
                frm_jobw_prod.xweight = respObj.getString("col3");
                Log.d("allData  ", data);
                String[] arrOfStr = data.split("!~!~!");
                int x=0;
                for (String col2 : arrOfStr){
                    Log.d("allData  ", col2);
                    if(x==0) {
                        frm_jobw_prod.xjob_no = col2;
                    }
                    if(x==1) {
                        frm_jobw_prod.xjob_dt = col2;
                    }
                    if(x==2) {
                        frm_jobw_prod.xiname1 = col2;
                    }
                    if(x==3) {
                        frm_jobw_prod.xicode1 = col2;
                    }
                    if(x==4) {
                        frm_jobw_prod.xqty1 = col2;
                    }
                    x +=1;
                }
                try {
                    frm_jobw_prod.txtitem.setText(frm_jobw_prod.xicode1.trim()+" --- "+frm_jobw_prod.xiname1.trim());
                    frm_jobw_prod.txtqty.setText(frm_jobw_prod.xqty1.trim());
                    frm_jobw_prod.txtbatch_no.setText(frm_jobw_prod.xjob_no.trim());
                }catch (Exception e){}
                Log.d("addData ", "Success");
            }
            frm_jobw_prod.form_name = "";
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static ArrayList<String> issue_slip_fill_record_in_listview_popup(String form_code){
        ArrayList<String> list_view = new ArrayList<>();
        ArrayList<team> result = finapi.getApiForPOPUP(fgen.mcd, "aLeave_ins",""+form_code, fgen.cdt1, fgen.cdt2, ""+fgen.muname, fgen.xextra1_for_popup);
        fgen.xextra1_for_popup = "-";
        Gson gson = new Gson();
        String listString = gson.toJson(result, new TypeToken<ArrayList<team>>() {}.getType());

        String xreq_no = "";
        String xreq_dt = "";
        String xname = "";

        try {
            JSONArray array = new JSONArray(listString);

            for(int i=0; i< array.length(); i++){
                JSONObject respObj = array.getJSONObject(i);
                String data = respObj.getString("col2");
                String xref_no = respObj.getString("col5");
                Log.d("allData  ", data);
                String[] arrOfStr = data.split("!~!~!");
                int x=0;
                for (String col2 : arrOfStr){
                    Log.d("allData  ", col2);
                    if(x==0) {
                        xreq_no = col2;
                    }
                    if(x==1) {
                        xreq_dt = col2;
                    }
                    if(x==2) {
                        xname = col2;
                        if(!fgen.frm_request.equals("frm_quality_inspection")) {
                            if(fgen.frm_request.equals("frm_dispatch_finalise_mvin")){
                                list_view.add(xreq_no + " --- " + xreq_dt + " --- " + xname);
                            }else {
                                list_view.add(xreq_no + " --- " + xreq_dt + " --- " + xname + " --- " + xref_no);
                            }
                        }else
                        {
                            list_view.add(xreq_no + " --- " + xreq_dt + " --- " + xname + " --- " + xref_no);
                        }
                    }
                    x +=1;
                }
                Log.d("addData ", "Success");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list_view;
    }

    public static void setColorOfStatusBar(Activity myActivity)
    {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = myActivity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(myActivity.getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    public static Boolean sendImageToServer(byte[] image, Context context) {
        String fullString = "";
        try {
            fullString = Base64.encodeToString(image, Base64.DEFAULT);

        } catch (Exception e) {
        }
        String col4=fgen.mcd + "_" + fgen.ximage_name + "_" + fgen.btnid;
        ArrayList<team> result = finapi.getApiImg(fgen.mcd, "-","-",col4, fullString, "-");
        boolean msg = finapi.showAlertMessageForImage(context, result);
        return msg;
    }




    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }


    public static String currentTime()
    {   SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

    public static void alertboxH(Context context, String title, String mymessage) {
        new AlertDialog.Builder(context)
                .setMessage(Html.fromHtml(mymessage))
                .setTitle(title)
                .setNeutralButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                .show();
    }

    public static Double round_off(double d, Integer places) {
        Double result = 0.00;
        double newKB = Math.round(d * 100.0) / 100.0;
        String format = Lpad("", places, "#");
        format = "###." + format;
        DecimalFormat df = new DecimalFormat(format);
        result = Double.valueOf(df.format(d));
        return result;
    }

    public static String space(int spaceChar) {
        String spaC = "";

        for (int s = 0; s < spaceChar; s++) {
            spaC += "&nbsp;";
        }
        return spaC;
    }



    public static void WriteJSONResponse(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("jsonresponse.txt", Context.MODE_APPEND));
            outputStreamWriter.write("\n\n---------------------------------------------\n"+OPERATION_NAME+"\n\n---------------------------------------------\n"+data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "JSON Response File write failed: " + e.toString());
        }
    }


    public static String ReadJSONResponse(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("jsonresponse.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        new AlertDialog.Builder(context)
                .setTitle("JSON Response")
                .setMessage(ret)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

        return ret;
    }

    public static void deleteJsonResponseFile() {
        String path = finapi.context.getFilesDir().getAbsolutePath();
        File file = new File(path + "/jsonresponse.txt");
        try {
            file.delete();
        } catch (Exception e) {
            Log.e("error:", "" + e);
        }
    }
}