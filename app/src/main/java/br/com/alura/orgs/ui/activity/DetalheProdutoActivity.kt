package br.com.alura.orgs.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import br.com.alura.orgs.R
import br.com.alura.orgs.databinding.ActivityDetalheProdutoBinding
import br.com.alura.orgs.extentions.tentaCarregarImagem
import br.com.alura.orgs.model.Produto
import java.text.NumberFormat
import java.util.Locale

@Suppress("DEPRECATION")
class DetalheProdutoActivity : AppCompatActivity() {
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
        when (item.itemId) {
            R.id.menu_detalhes_produto_editar -> {
                Log.i("TESTE", "Editar")
            }

            R.id.menu_detalhes_produto_remover -> {
                Log.i("TESTE", "Remover")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initView() {
        val produtoParcelable = intent.getParcelableExtra<Produto>("produto")
        produtoParcelable?.let { produto ->
            binding.activityDetailValor.setText(formataMoedaBrasileira(produto))
            binding.activityDetailTitle.setText(produto.nome)
            binding.activityDetailDescricao.setText(produto.descricao)
            binding.activityDetailImage.tentaCarregarImagem(produto.url)
        }
    }

    private fun formataMoedaBrasileira(produto: Produto): String? {
        val formatador: NumberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "br"))
        return formatador.format(produto.valor)
    }

}