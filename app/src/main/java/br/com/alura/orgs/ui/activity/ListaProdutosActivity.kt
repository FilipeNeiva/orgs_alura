package br.com.alura.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import br.com.alura.orgs.R
import br.com.alura.orgs.database.AppDatabase
import br.com.alura.orgs.databinding.ActivityListaProdutosActivityBinding
import br.com.alura.orgs.ui.recyclerview.adapter.ListaProdutosAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListaProdutosActivity : AppCompatActivity() {

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
    }

    override fun onResume() {
        super.onResume()

        val scope = MainScope()
        scope.launch {
            val produtos = withContext(Dispatchers.IO) {
                produtoDao.buscaTodos()
            }
            adapter.atualiza(produtos)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_ordena_produtos, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_ordena_produto_nome_desc -> {
                adapter.atualiza(produtoDao.buscaOrdenadaNomeDesc())
            }

            R.id.menu_ordena_produto_nome_asc -> {
                adapter.atualiza(produtoDao.buscaOrdenadaNomeAsc())
            }

            R.id.menu_ordena_produto_descricao_desc -> {
                adapter.atualiza(produtoDao.buscaOrdenadaDescricaoDesc())
            }

            R.id.menu_ordena_produto_descricao_asc -> {
                adapter.atualiza(produtoDao.buscaOrdenadaDescricaoAsc())
            }

            R.id.menu_ordena_produto_valor_desc -> {
                adapter.atualiza(produtoDao.buscaOrdenadaValorDesc())
            }

            R.id.menu_ordena_produto_valor_asc -> {
                adapter.atualiza(produtoDao.buscaOrdenadaValorAsc())
            }

            R.id.menu_ordena_produto_sem_ordenacao -> {
                adapter.atualiza(produtoDao.buscaTodos())
            }

        }

        return super.onOptionsItemSelected(item)
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