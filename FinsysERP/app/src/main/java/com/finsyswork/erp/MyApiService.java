package com.finsyswork.erp;
import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

//public interface MyApiService {
//
//}

//public interface MyApiService {
////    @POST("<your_endpoint>")
//    @POST
//    Call<ArrayList<team6>> getApi6(
//            @Url String url,
//            @Header("Accept") String acceptHeader,
//            @Body RequestBody body
//    );
//}
public interface MyApiService {
    @POST
    Call<ArrayList<team6>> getApi6(@Url String url, @Header("Accept") String acceptHeader, @Body RequestBody requestBody);
}