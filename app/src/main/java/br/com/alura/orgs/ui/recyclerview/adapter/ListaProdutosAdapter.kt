package br.com.alura.orgs.ui.recyclerview.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import br.com.alura.orgs.R
import br.com.alura.orgs.database.AppDatabase
import br.com.alura.orgs.databinding.ProdutoItemBinding
import br.com.alura.orgs.extentions.tentaCarregarImagem
import br.com.alura.orgs.model.Produto
import br.com.alura.orgs.ui.activity.CHAVE_PRODUTO_ID
import br.com.alura.orgs.ui.activity.DetalheProdutoActivity
import br.com.alura.orgs.ui.activity.FormularioProdutoActivity
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.Locale

class ListaProdutosAdapter(
    private val context: Context, produtos: List<Produto> = emptyList()
) : RecyclerView.Adapter<ListaProdutosAdapter.ViewHolder>() {

    private val produtos = produtos.toMutableList()

    class ViewHolder(private val binding: ProdutoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun vincula(produto: Produto, context: ListaProdutosAdapter) {
            val nome = binding.produtoItemNome
            nome.text = produto.nome
            val descricao = binding.produtoItemDescricao
            descricao.text = produto.descricao
            val valor = binding.produtoItemValor
            val valorEmMoeda = formataMoedaBrasileira(produto)
            valor.text = valorEmMoeda
            val holder = binding.root

            val visibilidade = if (produto.url != null) View.VISIBLE
            else View.GONE

            binding.imageView.visibility = visibilidade

            binding.imageView.tentaCarregarImagem(produto.url)

            holder.setOnClickListener {

                val intent = Intent(it.context, DetalheProdutoActivity::class.java).putExtra(
                    CHAVE_PRODUTO_ID, produto.id
                )
                it.context.startActivity(intent)
            }

            holder.setOnLongClickListener { it ->
                val popupMenu = PopupMenu(it.context, it)
                popupMenu.inflate(R.menu.menu_detalhes_produto)

                popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(item: MenuItem?): Boolean {
                        val db = AppDatabase.instance(it.context)
                        val produtoDao = db.produtoDao()

                        when (item?.itemId) {
                            R.id.menu_detalhes_produto_editar -> {
                                Intent(it.context, FormularioProdutoActivity::class.java).apply {
                                    putExtra(CHAVE_PRODUTO_ID, produto.id)
                                    it.context.startActivity(this)
                                }
                            }

                            R.id.menu_detalhes_produto_remover -> {
                                MainScope().launch {
                                    withContext(IO) {
                                        produtoDao.remove(produto)
                                    }
                                    context.produtos.remove(produto)
                                    context.notifyDataSetChanged()

                                }
                            }
                        }
                        return false
                    }
                })
                popupMenu.show()
                true
            }
        }

        private fun formataMoedaBrasileira(produto: Produto): String? {
            val formatador: NumberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "br"))
            return formatador.format(produto.valor)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = ProdutoItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val produto = produtos[position]
        holder.vincula(produto, this)
    }

    override fun getItemCount(): Int = produtos.size

    fun atualiza(produtos: List<Produto>) {
        this.produtos.clear()
        this.produtos.addAll(produtos)
        notifyDataSetChanged()
    }

}
