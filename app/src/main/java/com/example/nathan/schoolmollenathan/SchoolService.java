package com.example.nathan.schoolmollenathan;

import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Nathan on 10/06/2018.
 */
public interface SchoolService {

    //String base_url = "http://" + Resources.getSystem().getString(R.string.ip) + ':' + Resources.getSystem().getString(R.string.port);

    @POST("/api/v1/users/sign_in")
    Call<JsonObject> authenticate(@Body User credential);

    @GET("/api/v1/schools")
    Call<JsonResponseSchool> getEcole(@Query("status") String status);

    @POST("/api/v1/schools")
    Call<JsonResponseSchool> saveEcole(@Body School school);

    @DELETE("/api/v1/schools/{id}")
    Call<ResponseBody> removeEcole(@Path("id") int id);

    @PATCH("/api/v1/schools/{id}")
    Call<JsonResponseSchool> updateEcole(@Path("id") int id, @Body School school);

}
