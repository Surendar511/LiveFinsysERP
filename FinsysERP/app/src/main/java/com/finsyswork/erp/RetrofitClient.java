package com.finsyswork.erp;

import static com.finsyswork.erp.finapi.OPERATION_NAME;
import static com.finsyswork.erp.finapi.urlextra;

import androidx.appcompat.app.AlertDialog;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = OPERATION_NAME;

    public static Retrofit getApiClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://" + fgen.Serverip + urlextra)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
//public class RetrofitClient {
//
//    public static RetrofitClient instance;
//    MyApiService apiinterface;
//
//    RetrofitClient(){
//        Retrofit retrofit = new Retrofit.Builder().baseUrl(OPERATION_NAME)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        apiinterface = retrofit.create (MyApiService.class);
//    }
//
//    public static RetrofitClient getInstance(){
//        if(instance ==null){
//            instance = new RetrofitClient ();
//        }
//        return instance;
//
//    }
//    public static Retrofit getApiClient() {
//        return new Retrofit.Builder ( )
//                .baseUrl (OPERATION_NAME)  // Replace with your API base URL
//                .addConverterFactory (GsonConverterFactory.create ( ))
//                .build ( );
//    }
//}
