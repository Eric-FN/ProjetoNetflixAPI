package com.ericfonseca.projetonetflixapi

import com.ericfonseca.projetonetflixapi.api.AuthInterceptor
import com.ericfonseca.projetonetflixapi.api.ViaCepAPI
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitService {

        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val BASE_URL_IMAGE = "https://image.tmdb.org/t/p/"
        const val API_KEY = "bea4723ec02d4f056b9c528f417e64b4"
        const val TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJiZWE0NzIzZWMwMmQ0ZjA1NmI5YzUyOGY0MTdlNjRiNCIsIm5iZiI6MTcyMTI0OTE5Mi44NjQ3NjIsInN1YiI6IjY2OTBmMjI2Y2Y2MmZhZTY2MTgxMWIxZiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.bmdoMto9jZOdvJjMWNxYFWJZyaahECEYKqTjly0Pv3E"

        private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .writeTimeout(10, TimeUnit.SECONDS)//escrita (salvando na API)
            .readTimeout(20, TimeUnit.SECONDS)// Leitura( recuperando dados da API)
            .connectTimeout(20, TimeUnit.SECONDS) //Conexão máxima
            .addInterceptor( AuthInterceptor() )
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl( BASE_URL )
            .addConverterFactory( GsonConverterFactory.create() )
            .client( okHttpClient )
            .build()

       // val filmeAPI = retrofit.create( FilmeAPI::class.java )

        fun <T> recuperarApi(classe: Class<T>) : T{
            return retrofit.create( classe )
        }

      fun recuperarViaCep() : ViaCepAPI{
            return Retrofit.Builder()
                .baseUrl( "https://viacep.com.br/ws/" )
                .addConverterFactory( SimpleXmlConverterFactory.create() )
                .build()
                .create(ViaCepAPI::class.java)

        }

}