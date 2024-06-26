package br.com.alura.orgs.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.alura.orgs.model.Produto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProdutoDao {

    @Query("SELECT * FROM Produto")
    fun buscaTodos(): Flow<List<Produto>>

    @Query("SELECT * FROM Produto WHERE usuarioId = :usuarioId")
    fun buscaTodosDoUsuario(usuarioId: String): Flow<List<Produto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salva(vararg produto: Produto)

    @Delete
    suspend fun remove(produto: Produto)

    @Query("SELECT * FROM Produto WHERE id = :id")
    fun buscaPorId(id: Long): Flow<Produto?>

    @Query("SELECT * FROM Produto ORDER BY nome DESC")
    suspend fun buscaOrdenadaNomeDesc(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY nome ASC")
    suspend fun buscaOrdenadaNomeAsc(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY descricao DESC")
    suspend fun buscaOrdenadaDescricaoDesc(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY descricao ASC")
    suspend fun buscaOrdenadaDescricaoAsc(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY valor DESC")
    suspend fun buscaOrdenadaValorDesc(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY valor ASC")
    suspend fun buscaOrdenadaValorAsc(): List<Produto>
}