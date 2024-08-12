package com.ericfonseca.projetonetflixapi

import com.ericfonseca.projetonetflixapi.model.FilmeRecente
import com.ericfonseca.projetonetflixapi.model.FilmeResposta
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FilmeAPI {

    //@GET("movie/latest?api_key=${RetrofitService.API_KEY}")
    //Agora usando Interceptor -> não precisa do api_key aqui
    @GET("movie/latest")
    suspend fun recuperarFilmeRecente() : Response<FilmeRecente>

    //@GET("movie/popular?api_key=${RetrofitService.API_KEY}")
    @GET("movie/popular")
    suspend fun recuperarFilmesPopulares(
        @Query("page") pagina: Int
    ) : Response<FilmeResposta>

}