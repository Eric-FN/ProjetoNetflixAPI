package com.ericfonseca.projetonetflixapi.api

import com.ericfonseca.projetonetflixapi.model.Endereco
import retrofit2.Response
import retrofit2.http.GET

interface ViaCepAPI {

    @GET("01001000/xml/")
    suspend fun recuperarEndereco() : Response<Endereco>

}