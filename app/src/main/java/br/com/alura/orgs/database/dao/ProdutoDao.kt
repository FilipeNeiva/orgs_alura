package br.com.alura.orgs.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.alura.orgs.model.Produto

@Dao
interface ProdutoDao {

    @Query("SELECT * FROM Produto")
    fun buscaTodos(): List<Produto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun salva(vararg produto: Produto)

    @Delete
    fun remove(produto: Produto)

    @Query("SELECT * FROM Produto WHERE id = :id")
    fun buscaPorId(id: Long): Produto?

    @Query("SELECT * FROM Produto ORDER BY nome DESC")
    fun buscaOrdenadaNomeDesc(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY nome ASC")
    fun buscaOrdenadaNomeAsc(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY descricao DESC")
    fun buscaOrdenadaDescricaoDesc(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY descricao ASC")
    fun buscaOrdenadaDescricaoAsc(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY valor DESC")
    fun buscaOrdenadaValorDesc(): List<Produto>

    @Query("SELECT * FROM Produto ORDER BY valor ASC")
    fun buscaOrdenadaValorAsc(): List<Produto>
}