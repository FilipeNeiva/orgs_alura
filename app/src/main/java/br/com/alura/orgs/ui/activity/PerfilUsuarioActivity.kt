package br.com.alura.orgs.ui.activity

import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import br.com.alura.orgs.databinding.ActivityPerfilUsuarioBinding
import br.com.alura.orgs.model.Usuario
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class PerfilUsuarioActivity : UsuarioBaseActivity() {

    private val binding by lazy {
        ActivityPerfilUsuarioBinding.inflate(layoutInflater)
    }
    private var usuarioTextView: TextView? = null
    private var nomeTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()

        lifecycleScope.launch {
            usuario.filterNotNull().collect {
                carregaUsuario(it)
            }
        }
    }

    private fun initViews() {
        usuarioTextView = binding.perfilUsuarioNickname
        nomeTextView = binding.perfilUsuarioNome
        binding.perfilUsuarioBotaoSair.setOnClickListener {
            lifecycleScope.launch {
                deslogaUsuario()
            }
        }
    }

    private fun carregaUsuario(usuarioCarregado: Usuario) {
        usuarioTextView?.text = usuarioCarregado.nome.lowercase()
        nomeTextView?.text = usuarioCarregado.nome
    }
}