package com.example.jokeapp
import retrofit2.Call
import retrofit2.http.GET
interface JokeApiService {
    @GET("joke/Any?type=single")
    fun getJoke(): Call<JokeResponse>
}