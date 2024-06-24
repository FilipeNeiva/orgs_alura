package br.com.alura.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import br.com.alura.orgs.R
import br.com.alura.orgs.database.AppDatabase
import br.com.alura.orgs.databinding.ActivityDetalheProdutoBinding
import br.com.alura.orgs.extentions.tentaCarregarImagem
import br.com.alura.orgs.model.Produto
import java.text.NumberFormat
import java.util.Locale

@Suppress("DEPRECATION")
class DetalheProdutoActivity : AppCompatActivity() {
    private var produtoId: Long? = null
    private var produto: Produto? = null
    private val binding by lazy {
        ActivityDetalheProdutoBinding.inflate(layoutInflater)
    }
    private val produtoDao by lazy {
        AppDatabase.instance(this).produtoDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    override fun onResume() {
        super.onResume()
        produtoId?.let { id ->
            produto = produtoDao.buscaPorId(id)
        }

        produto?.let {
            preencheCampos(it)
        } ?: finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detalhes_produto, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (produto != null) {
            when (item.itemId) {
                R.id.menu_detalhes_produto_editar -> {
                    Intent(this, FormularioProdutoActivity::class.java).apply {
                        putExtra("produto", produto)
                        startActivity(this)
                    }
                }

                R.id.menu_detalhes_produto_remover -> {
                    produto?.let { produtoDao.remove(it) }
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initView() {
        val produtoParcelable = intent.getParcelableExtra<Produto>("produto")
        produtoParcelable?.let { produtoCarregado ->
            produto = produtoCarregado
            produtoId = produtoCarregado.id
        }
    }

    private fun preencheCampos(produto: Produto) {
        binding.activityDetailValor.text = formataMoedaBrasileira(produto)
        binding.activityDetailTitle.text = produto.nome
        binding.activityDetailDescricao.text = produto.descricao
        binding.activityDetailImage.tentaCarregarImagem(produto.url)
    }

    private fun formataMoedaBrasileira(produto: Produto): String? {
        val formatador: NumberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "br"))
        return formatador.format(produto.valor)
    }

}