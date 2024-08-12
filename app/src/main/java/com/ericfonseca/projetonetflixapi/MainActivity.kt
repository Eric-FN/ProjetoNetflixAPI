package com.ericfonseca.projetonetflixapi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.ericfonseca.projetonetflixapi.api.ViaCepAPI
import com.ericfonseca.projetonetflixapi.databinding.ActivityMainBinding
import com.ericfonseca.projetonetflixapi.model.Endereco
import com.ericfonseca.projetonetflixapi.model.FilmeRecente
import com.ericfonseca.projetonetflixapi.model.FilmeResposta
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class MainActivity : AppCompatActivity() {


    private var paginaAtual = 1
    private val TAG = "info_filme"
    private val binding by lazy {
        ActivityMainBinding.inflate( layoutInflater )
    }

    private val filmeAPI by lazy {
        //RetrofitService.filmeAPI
        RetrofitService.recuperarApi( FilmeAPI::class.java )
    }

    var jobFilmeRecente: Job? = null
    var jobFilmesPopulares: Job? = null
    var gridLayoutManager: GridLayoutManager? = null
    private lateinit var filmeAdapter: FilmeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( binding.root )

        inicializarViews()


    }


    private fun inicializarViews() {
        filmeAdapter = FilmeAdapter{filme ->
            val intent = Intent(this, DetalhesActivity::class.java)
            intent.putExtra("filme", filme )
            startActivity(intent)
        }
        binding.rvPopulares.adapter = filmeAdapter

        gridLayoutManager = GridLayoutManager(
            this,
            2,
        )

        /*linearLayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )*/
        binding.rvPopulares.layoutManager = gridLayoutManager

        binding.rvPopulares.addOnScrollListener( object : OnScrollListener(){

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                val podeDescerVerticalmente = recyclerView.canScrollVertically(1)
                //1)chegar ao final da lista

                if(!podeDescerVerticalmente){ //nao pode descer mais
                    //carregar mais 20 itens itens
                    Log.i("recycler_api", "paginaAtual: $paginaAtual ")
                    recuperarFilmesPopularesProximaPagina()

                }



               /* //0...19(20)
                val ultimoItemVisivel = linearLayoutManager?.findLastVisibleItemPosition()
                val totalItens = recyclerView.adapter?.itemCount
                Log.i("recycler_test", "ultimo: $ultimoItemVisivel total: $totalItens")

               if(ultimoItemVisivel != null && totalItens != null){
                   if( totalItens-1 == ultimoItemVisivel ){//chegou no ultimo item
                       binding.fabAdicionar.hide()
                   }else{
                       binding.fabAdicionar.show()
                   }
               }*/

                /*Log.i("recycler_test", "onScrolled: dx: $dx  dy: $dy")
                if (dy > 0){ //descendo
                    binding.fabAdicionar.hide()
                }else{//subindo
                    binding.fabAdicionar.show()
                }*/
            }

        })

    }

    /*class ScrollCustomizado : OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        }
    }*/


    override fun onStart() {
        super.onStart()
        recuperarFilmeRecente()
        recuperarFilmesPopulares()

    }



    private fun recuperarFilmesPopularesProximaPagina(){
        if( paginaAtual < 1000 ){
            paginaAtual++
            recuperarFilmesPopulares(paginaAtual)
        }
    }

    private fun recuperarFilmesPopulares(pagina: Int = 1) {

        jobFilmesPopulares = CoroutineScope( Dispatchers.IO ).launch {
            var resposta: Response<FilmeResposta>? = null

            try {
                resposta = filmeAPI.recuperarFilmesPopulares(pagina)

            }catch (e: Exception){
                exibirMensagem("Erro ao fazer a requisição")
            }

            if (resposta != null){
                if(resposta.isSuccessful){

                    val filmeResposta = resposta.body()
                    val listaFilmes = filmeResposta?.filmes
                    if( listaFilmes != null && listaFilmes.isNotEmpty() ){

                        withContext(Dispatchers.Main){
                            filmeAdapter.adicionarLista( listaFilmes )
                        }

                       /* Log.i("filmes_api", "lista Filmes:")
                        listaFilmes.forEach{filme ->
                            Log.i("filmes_api","Titulo: ${filme.title}")
                        }*/
                    }



                }else{
                    exibirMensagem("Problema ao fazer a requisição CODIGO: ${resposta.code()}")
                }

            }else{
                exibirMensagem("Não foi possível fazer a requisição")
            }

        }


    }


    private fun recuperarFilmeRecente() {
        jobFilmeRecente = CoroutineScope( Dispatchers.IO ).launch {

            var resposta: Response<FilmeRecente>? = null

            try {
                resposta = filmeAPI.recuperarFilmeRecente()

            }catch (e: Exception){
                exibirMensagem("Erro ao fazer a requisição")
            }

            if (resposta != null){
                if(resposta.isSuccessful){
                    val filmeRecente = resposta.body()
                    val nomeImagem = filmeRecente?.poster_path
                    val titulo = filmeRecente?.title
                    val url = RetrofitService.BASE_URL_IMAGE + "w780" + nomeImagem

                    withContext(Dispatchers.Main){
                        /*val texto = "titulo: $titulo url: $url"
                        binding.textPopulares.text = texto*/
                        Picasso.get()
                            .load(url)
                            .error(R.drawable.capa)
                            .into(binding.imgCapa)
                    }

                }else{
                    exibirMensagem("Problema ao fazer a requisição CODIGO: ${resposta.code()}")
                }

            }else{
                exibirMensagem("Não foi possível fazer a requisição")
            }

        }


    }



    private fun exibirMensagem(mensagem: String) {
        Toast.makeText(
            applicationContext,
            mensagem,
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onStop() {
        super.onStop()
        jobFilmeRecente?.cancel()
        jobFilmesPopulares?.cancel()
    }

}