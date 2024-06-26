package br.com.alura.orgs.ui.activity

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import br.com.alura.orgs.database.AppDatabase
import br.com.alura.orgs.database.dao.ProdutoDao
import br.com.alura.orgs.databinding.ActivityFormularioProdutoBinding
import br.com.alura.orgs.extentions.tentaCarregarImagem
import br.com.alura.orgs.model.Produto
import br.com.alura.orgs.ui.dialog.FormularioImagemDialog
import kotlinx.coroutines.launch
import java.math.BigDecimal

class FormularioProdutoActivity : UsuarioBaseActivity() {

    private val binding by lazy {
        ActivityFormularioProdutoBinding.inflate(layoutInflater)
    }
    private var url: String? = null
    private var produtoId = 0L
    private val produtoDao: ProdutoDao by lazy {
        AppDatabase.instance(this).produtoDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        title = "Cadastrar Produto"
        configuraBotaoSalvar()

        binding.activityFormularioProdutoImagem.setOnClickListener {
            FormularioImagemDialog(this).monstra(url) { imagem: String ->
                url = imagem
                binding.activityFormularioProdutoImagem.tentaCarregarImagem(url)
            }
        }

        tentaCarregarProduto()

        lifecycleScope.launch {
            produtoDao.buscaPorId(produtoId).collect() { produto ->
                produto?.let {
                    title = "Alterar Produto"
                    carregandoDados(it)
                }
            }
        }
    }

    private fun tentaCarregarProduto() {
        produtoId = intent.getLongExtra(CHAVE_PRODUTO_ID, 0L)
    }

    private fun configuraBotaoSalvar() {
        val botaoSalvar = binding.activityFormularioProdutoBotaoSalvar
        botaoSalvar.setOnClickListener {
            lifecycleScope.launch {
                usuario.value?.let { usuario ->
                    val produtoNovo = criaProduto(usuario.id)
                    produtoDao.salva(produtoNovo)
                    finish()
                }
            }
        }
    }

    private fun carregandoDados(produto: Produto) {
        produtoId = produto.id
        preencheCampos(produto)
    }

    private fun preencheCampos(produto: Produto) {
        url = produto.url
        binding.activityFormularioProdutoImagem.tentaCarregarImagem(produto.url)
        binding.activityFormularioProdutoNome.setText(produto.nome)
        binding.activityFormularioProdutoDescricao.setText(produto.descricao)
        binding.activityFormularioProdutoValor.setText(produto.valor.toPlainString())
    }

    private fun criaProduto(usuarioId: String): Produto {
        val campoNome = binding.activityFormularioProdutoNome
        val nome = campoNome.text.toString()
        val campoDescricao = binding.activityFormularioProdutoDescricao
        val descricao = campoDescricao.text.toString()
        val campoValor = binding.activityFormularioProdutoValor
        val valorEmTexto = campoValor.text.toString()
        val valor = if (valorEmTexto.isBlank()) {
            BigDecimal.ZERO
        } else {
            BigDecimal(valorEmTexto)
        }

        return Produto(
            id = produtoId,
            nome = nome,
            descricao = descricao,
            valor = valor,
            url = url,
            usuarioId = usuarioId
        )
    }

}