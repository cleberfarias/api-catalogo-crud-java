package br.edu.senai.sc.catalogo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.edu.senai.sc.catalogo.entities.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

	List<Produto> findProdutoByNomeContaining(String nome);

	//
	@Query("SELECT p FROM Produto p JOIN p.categoria c WHERE c.nome = :categoria")
	List<Produto> listarProdutosPorCategoria(@Param("categoria") String categoria);

	@Query("SELECT p FROM Produto p WHERE p.nome LIKE %?1%")
	List<Produto> buscarProdutoPorNome(String nome);
}
