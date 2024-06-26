package br.com.alura.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.alura.orgs.R
import br.com.alura.orgs.database.AppDatabase
import br.com.alura.orgs.databinding.ActivityListaProdutosActivityBinding
import br.com.alura.orgs.extentions.vaiPara
import br.com.alura.orgs.ui.recyclerview.adapter.ListaProdutosAdapter
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class ListaProdutosActivity : UsuarioBaseActivity() {

    private val adapter = ListaProdutosAdapter(context = this)
    private val binding by lazy {
        ActivityListaProdutosActivityBinding.inflate(layoutInflater)
    }
    private val produtoDao by lazy {
        AppDatabase.instance(this).produtoDao()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraRecyclerView()
        configuraFab()

        lifecycleScope.launch {

            launch {
                usuario
                    .filterNotNull()
                    .collect {
                        Log.i("ListaProdutos", "onCreate: $it")
                        buscaProdutoUsuario()
                    }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_lista_produtos, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_ordena_produto_nome_desc -> {
                lifecycleScope.launch {
                    adapter.atualiza(produtoDao.buscaOrdenadaNomeDesc())
                }
            }

            R.id.menu_ordena_produto_nome_asc -> {
                lifecycleScope.launch {
                    adapter.atualiza(produtoDao.buscaOrdenadaNomeAsc())
                }
            }

            R.id.menu_ordena_produto_descricao_desc -> {
                lifecycleScope.launch {
                    adapter.atualiza(produtoDao.buscaOrdenadaDescricaoDesc())
                }
            }

            R.id.menu_ordena_produto_descricao_asc -> {
                lifecycleScope.launch {
                    adapter.atualiza(produtoDao.buscaOrdenadaDescricaoAsc())
                }
            }

            R.id.menu_ordena_produto_valor_desc -> {
                lifecycleScope.launch {
                    adapter.atualiza(produtoDao.buscaOrdenadaValorDesc())
                }
            }

            R.id.menu_ordena_produto_valor_asc -> {
                lifecycleScope.launch {
                    adapter.atualiza(produtoDao.buscaOrdenadaValorAsc())
                }
            }

            R.id.menu_ordena_produto_sem_ordenacao -> {
                lifecycleScope.launch {
                    buscaProdutoUsuario()
                }
            }

            R.id.menu_lsita_produto_perfil -> {
                vaiPara(PerfilUsuarioActivity::class.java)
            }

            R.id.menu_lista_produto_sair_do_app -> {
                lifecycleScope.launch {
                    deslogaUsuario()
                }
            }

        }

        return super.onOptionsItemSelected(item)
    }

    private suspend fun buscaProdutoUsuario() {
        produtoDao.buscaTodos().collect() { produtos ->
            adapter.atualiza(produtos)
        }
    }

    private fun configuraFab() {
        val fab = binding.activityListaProdutosFab
        fab.setOnClickListener {
            vaiParaFormularioProduto()
        }
    }

    private fun vaiParaFormularioProduto() {
        val intent = Intent(this, FormularioProdutoActivity::class.java)
        startActivity(intent)
    }

    private fun configuraRecyclerView() {
        val recyclerView = binding.activityListaProdutosRecyclerView
        recyclerView.adapter = adapter
    }

}