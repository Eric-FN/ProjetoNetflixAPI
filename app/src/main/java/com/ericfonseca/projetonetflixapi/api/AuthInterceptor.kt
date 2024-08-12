package com.ericfonseca.projetonetflixapi.api

import com.ericfonseca.projetonetflixapi.RetrofitService
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        //chain = cadeia, corrente
        //ex: 123456


        //1) Acessar a requisição
        val construtorRequisicao = chain.request().newBuilder()

        //2) Alterar URL ou rota da requisição
        // https://api.themoviedb.org/3/ + movie/latest + api_key
       /* val urlAtual = chain.request().url()
        val novaUrl = urlAtual.newBuilder()
        novaUrl.addQueryParameter(
            "api_key",
            RetrofitService.API_KEY
        )

        //3) Configurar nova url na requisição
        construtorRequisicao.url( novaUrl.build() )*/

        //Utilizando Bearer Token
        val requisicao = construtorRequisicao.addHeader(
            "Authorization", "Bearer ${RetrofitService.TOKEN}"
        ).build()

        return chain.proceed( requisicao ) //response
    }
}