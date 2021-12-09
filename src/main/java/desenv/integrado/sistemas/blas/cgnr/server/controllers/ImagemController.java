package desenv.integrado.sistemas.blas.cgnr.server.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import desenv.integrado.sistemas.blas.cgnr.server.models.Imagem;
import desenv.integrado.sistemas.blas.cgnr.server.repositories.ImagemRepository;

@RestController // controller e responseBody, classe q vai receber requisições, endpoint
@RequestMapping("/imagem")
public class ImagemController {

	@Autowired // Vai injetar uma instância que o JPA faz em ImagemRepository em tempo de execução aqui
	private ImagemRepository imagemRepository;

	// se for um get em /imagem, cai aqui:get de todas imagens
	@GetMapping
	public List<Imagem> listar_imagens() {

		return imagemRepository.findAll();

	}


	// da GET de uma imagem específica: /imagem/id-da-imagem
	@GetMapping(path = "/{id}")
	public ResponseEntity<Imagem> consultar(@PathVariable("id") Integer id) {

		return imagemRepository.findById(id).map(record -> ResponseEntity.ok().body(record))
				.orElse(ResponseEntity.notFound().build());

	}

}
