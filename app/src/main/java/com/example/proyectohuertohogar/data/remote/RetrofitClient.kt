package com.example.proyectohuertohogar.data.remote

import com.example.proyectohuertohogar.data.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Cliente Retrofit configurado como Singleton
 */
object RetrofitClient {

    // URL base de la API (Tu dirección de Railway.app)
    private const val URL_BASE = "https://api-dfs2-dm-production.up.railway.app/products"

    // Interceptor para ver peticiones y respuestas en Logcat
    private val interceptorLog = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Nivel detallado
    }

    // Cliente HTTP con timeouts y logging
    private val clienteHttp = OkHttpClient.Builder()
        .addInterceptor(interceptorLog)
        .connectTimeout(30, TimeUnit.SECONDS) // Tiempo de espera para la conexión
        .readTimeout(30, TimeUnit.SECONDS)    // Tiempo de espera para leer la respuesta
        .writeTimeout(30, TimeUnit.SECONDS)   // Tiempo de espera para enviar datos
        .build()

    // Instancia de Retrofit (se crea solo una vez)
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(URL_BASE)
            .client(clienteHttp) // Usamos el cliente HTTP configurado
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Adaptación a tu estructura: Creador del servicio ApiService
    // Es lo que usas en UserRepository: RetrofitClient.api.getProducts()
    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}