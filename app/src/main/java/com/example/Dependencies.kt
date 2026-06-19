package com.example

import android.content.Context
import androidx.room.Room
import com.example.data.api.NwsApi
import com.example.data.api.OpenMeteoApi
import com.example.data.local.AppDatabase
import com.example.data.repository.WeatherRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class AppContainer(context: Context) {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("User-Agent", "OnyxWeatherApp/1.0 (nehemiahporter992@gmail.com)")
                .build()
            chain.proceed(request)
        }
        .build()

    private val openMeteoRetrofit = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private val nwsRetrofit = Retrofit.Builder()
        .baseUrl("https://api.weather.gov/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val openMeteoApi: OpenMeteoApi = openMeteoRetrofit.create(OpenMeteoApi::class.java)
    val nwsApi: NwsApi = nwsRetrofit.create(NwsApi::class.java)

    private val database: AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "weather_db"
    ).build()

    val locationDao = database.locationDao()
    val repository = WeatherRepository(nwsApi, openMeteoApi)
}
