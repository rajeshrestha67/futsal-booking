package com.futsalrecord.futsalApp.bll;

import android.content.Context;
import android.content.SharedPreferences;

import com.futsalrecord.futsalApp.api.FutsalAPI;
import com.futsalrecord.futsalApp.api.UsersAPI;
import com.futsalrecord.futsalApp.serverResponse.RegisterResponse;
import com.futsalrecord.futsalApp.url.Url;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class LoginBLL {
    boolean isSuccess = false;

    public boolean checkUser(String username, String password) {
        UsersAPI usersAPI = Url.getInstance().create(UsersAPI.class);
        Call<RegisterResponse> usersCall = usersAPI.checkUser(username, password);
        try {
            Response<RegisterResponse> loginResponse = usersCall.execute();
            if (loginResponse.isSuccessful() && loginResponse.body().getStatus()
                    .equals("Login successful")) {
                Url.tokenUser += loginResponse.body().getToken();
                isSuccess = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    public boolean checkFutsal(String futsalName, String futsalPassword) {
        FutsalAPI futsalAPI = Url.getInstance().create(FutsalAPI.class);
        Call<RegisterResponse> futsalCall = futsalAPI.checkUser(futsalName, futsalPassword);
        try {
            Response<RegisterResponse> loginResponse = futsalCall.execute();
            if (loginResponse.isSuccessful() && loginResponse.body().getStatus()
                    .equals("Login successful")) {
                Url.token += loginResponse.body().getToken();
                isSuccess = true;
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return isSuccess;
    }
}
