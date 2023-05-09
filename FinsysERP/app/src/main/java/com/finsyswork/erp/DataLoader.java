package com.finsyswork.erp;

import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class DataLoader {
    private MyApiService apiService;

    public DataLoader() {
        // Initialize your Retrofit API service
        apiService = RetrofitClient.getApiClient().create(MyApiService.class);
    }

    public Call<ArrayList<team6>> loadData(String endpoint, String acceptHeader, RequestBody requestBody) {
        return apiService.getApi6(endpoint, acceptHeader, requestBody);
    }
}

//public class DataLoader {
//    private MyApiService apiService;
//
//    public DataLoader() {
//        // Initialize your Retrofit API service
//        apiService = RetrofitClient.getApiClient().create(MyApiService.class);
//    }
//
//    public Call<ArrayList<team6>> loadData(String endpoint, String acceptHeader, RequestBody requestBody) {
//         Call<ArrayList<team6>> call = apiService.getApi6(endpoint, acceptHeader, requestBody);
//         return call;
//    }
//}