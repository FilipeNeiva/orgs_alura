package br.com.alura.orgs.ui.activity

import android.os.Bundle
import android.util.Log
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
    private lateinit var produto: Produto
    private val binding by lazy {
        ActivityDetalheProdutoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detalhes_produto, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (::produto.isInitialized) {
            val db = AppDatabase.instance(this)
            val produtoDao = db.produtoDao()
            when (item.itemId) {
                R.id.menu_detalhes_produto_editar -> {
                    Log.i("TESTE", "Editar")
                }

                R.id.menu_detalhes_produto_remover -> {
                    produtoDao.remove(produto)
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
            binding.activityDetailValor.text = formataMoedaBrasileira(produtoCarregado)
            binding.activityDetailTitle.text = produtoCarregado.nome
            binding.activityDetailDescricao.text = produtoCarregado.descricao
            binding.activityDetailImage.tentaCarregarImagem(produtoCarregado.url)
        }
    }

    private fun formataMoedaBrasileira(produto: Produto): String? {
        val formatador: NumberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "br"))
        return formatador.format(produto.valor)
    }

}