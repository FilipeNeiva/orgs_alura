package br.com.alura.orgs.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import br.com.alura.orgs.databinding.FormularioImagemProdutoBinding
import br.com.alura.orgs.extentions.tentaCarregarImagem

class FormularioImagemDialog(private var context: Context) {
    fun monstra(
        imagemPadrao: String? = null,
        quandoImagemCarragada: (imagem: String) -> Unit
    ) {

        FormularioImagemProdutoBinding.inflate(LayoutInflater.from(context)).apply {

            imagemPadrao?.let {
                dialogImagemProduto.tentaCarregarImagem(it)
                dialogProdutoUrl.setText(it)
            }

            dialogAddImageProduto.setOnClickListener {
                val url = dialogProdutoUrl.text.toString().trim()
                dialogImagemProduto.tentaCarregarImagem(url)
            }

            AlertDialog.Builder(context)
                .setView(root)
                .setPositiveButton("Confirmar") { _, _ ->
                    val url = dialogProdutoUrl.text.toString().trim()
                    quandoImagemCarragada(url)
                }
                .setNegativeButton("Cancelar") { _, _ ->

                }
                .show()
        }
    }
}