package br.edu.senai.sc.catalogo.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.senai.sc.catalogo.Service.ProdutoService;
import br.edu.senai.sc.catalogo.entities.Produto;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/produto")
public class ProdutoController {

	private final ProdutoService produtoService;

	public ProdutoController(ProdutoService produtoService) {
		this.produtoService = produtoService;
	}

	@ApiOperation(value = "Cadastrar produto")
	@PostMapping
	public ResponseEntity<String> cadastrarProduto(@RequestBody Produto produto) {
		try {
			produtoService.salvarProduto(produto);
		} catch (Exception exception) {
			return new ResponseEntity<>("Erro ao cadastrar o Produto", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Produto cadastrado com sucesso!", HttpStatus.CREATED);
	}

	@ApiOperation(value = "Buscar todos os produtos")
	@GetMapping
	public ResponseEntity<List<Produto>> buscarProdutos() {
		try {
			List<Produto> produtos = produtoService.buscarProdutos();
			return new ResponseEntity<>(produtos, HttpStatus.OK);
		} catch (Exception exception) {
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Buscar produto por código")
	@GetMapping("/{codigo}")
	public ResponseEntity<Produto> buscarProduto(@PathVariable("codigo") Long codigo) {
		try {
			Optional<Produto> produto = produtoService.buscarProdutoPorCodigo(codigo);
			if (Optional.ofNullable(produto).isPresent()) {
				return new ResponseEntity<>(produto.get(), HttpStatus.OK);
			}
		} catch (Exception exception) {
			return new ResponseEntity<>(new Produto(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(new Produto(), HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "Buscar produto por nome")
	@GetMapping("/nome")
	public ResponseEntity<List<Produto>> buscarProduto(@RequestParam("nome") String nome) {
		try {
			List<Produto> produtos = produtoService.buscarProdutoPorNome(nome);
			return new ResponseEntity<>(produtos, HttpStatus.OK);
		} catch (Exception exception) {
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Apagar produto")
	@DeleteMapping("/{codigo}")
	public ResponseEntity<String> excluirProduto(@PathVariable("codigo") Long codigo) {
		try {
			produtoService.excluirProduto(codigo);
		} catch (Exception exception) {
			return new ResponseEntity<>("Erro ao excluir o Produto" + exception, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Produto excluido com sucesso", HttpStatus.OK);
	}

	@ApiOperation(value = "Alterar produto")
	@PutMapping
	public ResponseEntity<String> alterarProduto(@RequestBody Produto produto) {
		try {
			produtoService.salvarProduto(produto);
		} catch (Exception exception) {
			return new ResponseEntity<>("Erro ao atualizar Produto!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Produto alterado com sucesso!", HttpStatus.OK);
	}

	@ApiOperation(value = "Alterar o nome do produto")
	@PatchMapping("/{codigo}")
	public ResponseEntity<String> alterarNome(@RequestParam("nome") String nome, @PathVariable("codigo") Long codigo) {
		try {
			produtoService.alterarNome(nome, codigo);
		} catch (Exception exception) {
			return new ResponseEntity<>("Erro ao alterar o Produto!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Produto alterado com sucesso!", HttpStatus.OK);
	}

	@ApiOperation(value = "Adicionar categoria")
	@PatchMapping("/addCategoria")
	public ResponseEntity<String> addCategoria(@RequestParam("produto") Long codigoProduto,
			@RequestParam("categoria") Long codigoCategoria) {
		try {
			produtoService.addCategoria(codigoProduto, codigoCategoria);
		} catch (Exception exception) {
			return new ResponseEntity<>("Erro ao adicionar a Categoria!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Categoria adicionada com sucesso!", HttpStatus.OK);
	}

	@ApiOperation(value = "Remover categoria")
	@PatchMapping("/removeCategoria/{codigo}")
	public ResponseEntity<String> removeCategoria(@PathVariable("codigo") Long codigo) {
		try {
			produtoService.removeCategoria(codigo);
		} catch (Exception exception) {
			return new ResponseEntity<>("Erro ao remover a Categoria!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Categoria removida com sucesso!", HttpStatus.OK);
	}

	@ApiOperation(value = "Adicionar imagem")
	@PatchMapping(value = "/addImagem")
	public ResponseEntity<String> addImagem(@RequestParam("produto") Long codigoProduto,
			@RequestParam("imagem") String codigoImagem) {
		try {
			produtoService.addImagem(codigoProduto, codigoImagem);
		} catch (Exception exception) {
			return new ResponseEntity<>("Erro ao adicionar a Imagem!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Imagem adicionada com sucesso!", HttpStatus.OK);
	}

	@ApiOperation(value = "Remover imagem")
	@DeleteMapping("/{codigo}/removeImagem")
	public ResponseEntity<String> removeImagem(@PathVariable("codigo") Long codigoProduto) {
		try {
			produtoService.removeImagem(codigoProduto);
		} catch (Exception exception) {
			return new ResponseEntity<>("Erro ao remover a Imagem!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Imagem removida com sucesso!", HttpStatus.OK);
	}
	// Listagem de todos os produtos que o preço é maior ou igual 1000

	@GetMapping("/preco-maior-ou-igual-a-mil")
	@ApiOperation(value = "preco-maior-ou-igual-a-mil")
	public ResponseEntity<List<Produto>> buscarProdutosMaisCarosQueMil() {
		try {
			List<Produto> produtos = produtoService.buscarProdutos();
			List<Produto> produtosCaros = new ArrayList<>();

			for (Produto produto : produtos) {
				if (produto.getPreco() != null && produto.getPreco() >= 1000) {
					produtosCaros.add(produto);
				}
			}

			return new ResponseEntity<>(produtosCaros, HttpStatus.OK);
		} catch (Exception exception) {
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
		}
	}
	// Contagem de todos os produtos que o preço é maior ou igual 1000

	@ApiOperation(value = "Contar produtos com preço maior ou igual a 1000")
	@GetMapping("/contar-produtos-preco-maior-ou-igual-a-mil")
	public ResponseEntity<Long> contarProdutosComPrecoMaiorOuIgualA1000() {
		try {
			List<Produto> produtos = produtoService.buscarProdutos();
			long quantidadeProdutosCaros = produtos.stream()
					.filter(produto -> produto.getPreco() != null && produto.getPreco() >= 1000)
					.count();

			return new ResponseEntity<>(quantidadeProdutosCaros, HttpStatus.OK);
		} catch (Exception exception) {
			return new ResponseEntity<>(0L, HttpStatus.BAD_REQUEST);
		}
	}
}
