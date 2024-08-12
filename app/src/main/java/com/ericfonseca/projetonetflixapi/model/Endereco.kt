package com.ericfonseca.projetonetflixapi.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

//Raiz
@Root(name = "xmlcep", strict = false)
class Endereco {

    @field: Element(name = "bairro")//Atributo(campo)
    var bairro: String = ""

    @field: Element(name = "complemento")//Atributo(campo)
    var complemento: String = ""

    @field: Element(name = "localidade")//Atributo(campo)
    var localidade: String = ""

    @field: Element(name = "logradouro")//Atributo(campo)
    var logradouro: String = ""
}

/*
data class Endereco(

    @field: Element(name = "bairro")//Atributo(campo)
    @param: Element(name = "bairro")//Parametro do construtor
    val bairro: String,
    @field: Element(name = "complemento")//Atributo(campo)
    @param: Element(name = "complemento")//Parametro do construtor
    val complemento: String,
    @field: Element(name = "localidade")//Atributo(campo)
    @param: Element(name = "localidade")//Parametro do construtor
    val localidade: String,
    @field: Element(name = "logradouro")//Atributo(campo)
    @param: Element(name = "logradouro")//Parametro do construtor
    val logradouro: String
)*/
