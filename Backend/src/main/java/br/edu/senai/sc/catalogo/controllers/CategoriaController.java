package br.edu.senai.sc.catalogo.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.edu.senai.sc.catalogo.Service.CategoriaService;
import br.edu.senai.sc.catalogo.Service.ProdutoService;
import br.edu.senai.sc.catalogo.entities.Categoria;
import br.edu.senai.sc.catalogo.entities.Produto;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/categoria")
public class CategoriaController {

	private final CategoriaService categoriaService;
	private final ProdutoService produtoService;

	public CategoriaController(CategoriaService categoriaService, ProdutoService produtoService) {
		this.categoriaService = categoriaService;
		this.produtoService = produtoService;
	}

	@ApiOperation(value = "Cadastrar categoria")
	@PostMapping
	public ResponseEntity<String> cadastrarCategoria(@RequestBody Categoria categoria) {
		try {
			categoriaService.salvarCategoria(categoria);
		} catch (Exception exception) {
			return new ResponseEntity<>("Erro ao cadastrar a Categoria", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Categoria cadastrada com sucesso!", HttpStatus.CREATED);
	}

	@ApiOperation(value = "Buscar todas as categorias")
	@GetMapping
	public ResponseEntity<List<Categoria>> buscarCategorias() {
		try {
			List<Categoria> categorias = categoriaService.buscarCategorias();
			return new ResponseEntity<>(categorias, HttpStatus.OK);
		} catch (Exception exception) {
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
		}
	}

	// Contar Categoria
	@ApiOperation(value = "Contar categorias")
	@GetMapping("/contar-categorias")
	public ResponseEntity<Integer> contarCategorias() {
		try {
			List<Categoria> categorias = categoriaService.buscarCategorias();
			Integer quantidade = categorias.size();
			return new ResponseEntity<>(quantidade, HttpStatus.OK);
		} catch (Exception exception) {
			return new ResponseEntity<>(0, HttpStatus.BAD_REQUEST);
		}
	}

	// Criar Categoria Padaria
	@ApiOperation(value = "Criar categoria Padaria e cadastrar produtos")
	@PostMapping("/criar-padaria")
	public ResponseEntity<String> criarCategoriaPadaria(@RequestBody List<Produto> produtos) {
		try {
			// Criar a categoria "Padaria"
			Categoria padaria = new Categoria();
			padaria.setNome("Padaria");
			categoriaService.salvarCategoria(padaria);

			// Associar os produtos à categoria "Padaria"
			for (Produto produto : produtos) {
				produtoService.addCategoria(produto.getCodigo(), padaria.getCodigo());
			}

			return new ResponseEntity<>("Categoria 'Padaria' e produtos cadastrados com sucesso!", HttpStatus.CREATED);
		} catch (Exception exception) {
			return new ResponseEntity<>("Erro ao criar a categoria 'Padaria' e cadastrar produtos",
					HttpStatus.BAD_REQUEST);
		}
	}

	// Lista produto da Categoria Padaria
	@ApiOperation(value = "Listar produtos da categoria Padaria")
	@GetMapping("/padaria/produtos")
	public ResponseEntity<List<Produto>> listarProdutosDaPadaria() {
		try {
			List<Produto> produtosPadaria = produtoService.listarProdutosPorCategoria("Padaria");
			return new ResponseEntity<>(produtosPadaria, HttpStatus.OK);
		} catch (Exception exception) {
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
		}
	}

	// Contar Produto Categoria Padaria
	@ApiOperation(value = "Contar produtos da categoria Padaria")
	@GetMapping("/padaria/contar-produtos")
	public ResponseEntity<Integer> contarProdutosDaPadaria() {
		try {
			List<Produto> produtosPadaria = produtoService.listarProdutosPorCategoria("Padaria");
			Integer quantidadeProdutosPadaria = produtosPadaria.size();
			return new ResponseEntity<>(quantidadeProdutosPadaria, HttpStatus.OK);
		} catch (Exception exception) {
			return new ResponseEntity<>(0, HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Buscar categoria por código")
	@GetMapping("/{codigo}")
	public ResponseEntity<Categoria> buscarCategoria(@PathVariable("codigo") Long codigo) {
		try {
			Optional<Categoria> categoria = categoriaService.buscarCategoriaPorCodigo(codigo);
			if (Optional.ofNullable(categoria).isPresent()) {
				return new ResponseEntity<>(categoria.get(), HttpStatus.OK);
			}
		} catch (Exception exception) {
			return new ResponseEntity<>(new Categoria(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(new Categoria(), HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "Buscar categoria por nome")
	@GetMapping("/nome")
	public ResponseEntity<List<Categoria>> buscarCategoria(@RequestParam("nome") String nome) {
		try {
			List<Categoria> categorias = categoriaService.buscarCategoriaPorNome(nome);
			return new ResponseEntity<>(categorias, HttpStatus.OK);
		} catch (Exception exception) {
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Apagar categoria")
	@DeleteMapping("/{codigo}")
	public ResponseEntity<String> excluirCategoria(@PathVariable("codigo") Long codigo) {
		try {
			categoriaService.excluirCategoria(codigo);
		} catch (Exception exception) {
			return new ResponseEntity<>("Erro ao excluir a Categoria", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Categoria excluída com sucesso", HttpStatus.OK);
	}

	@ApiOperation(value = "Alterar categoria")
	@PutMapping
	public ResponseEntity<String> alterarCategoria(@RequestBody Categoria categoria) {
		try {
			categoriaService.salvarCategoria(categoria);
		} catch (Exception exception) {
			return new ResponseEntity<>("Erro ao atualizar Categoria!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Categoria alterada com sucesso!", HttpStatus.OK);
	}

	@ApiOperation(value = "Alterar o nome da categoria")
	@PatchMapping("/{codigo}")
	public ResponseEntity<String> alterarNome(@RequestParam("nome") String nome, @PathVariable("codigo") Long codigo) {
		try {
			categoriaService.alterarNome(nome, codigo);
		} catch (Exception exception) {
			return new ResponseEntity<>("Erro ao alterar a Categoria!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Categoria alterada com sucesso!", HttpStatus.OK);
	}
}