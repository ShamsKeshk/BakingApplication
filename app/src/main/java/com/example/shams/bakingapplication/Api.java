package com.example.shams.bakingapplication;

import com.example.shams.bakingapplication.model.Recipes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<ArrayList<Recipes>> getRecipes();
}
