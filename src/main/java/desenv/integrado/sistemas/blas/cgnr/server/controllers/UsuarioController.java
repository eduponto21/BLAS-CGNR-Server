package desenv.integrado.sistemas.blas.cgnr.server.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import desenv.integrado.sistemas.blas.cgnr.server.models.Usuario;
import desenv.integrado.sistemas.blas.cgnr.server.repositories.UsuarioRepository;

@RestController // controller e responseBody, classe q vai receber requisições, endpoint
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired // Vai injetar uma instância que o JPA faz em UsuarioRepository em tempo de execução aqui
	private UsuarioRepository usuarioRepository;


	// se for um get em /usuario, cai aqui: get de todos usuarios
	@GetMapping
	public List<Usuario> listar_usuarios() {

		return usuarioRepository.findAll();

	}


	// da GET de um usuário específico: /usuario/nomedousuario
	@GetMapping(path = "/{usuario}")
	public ResponseEntity<Usuario> consultar(@PathVariable("usuario") String usuario) {

		return usuarioRepository.findById(usuario).map(record -> ResponseEntity.ok().body(record))
				.orElse(ResponseEntity.notFound().build());

	}


	// se for um post em /usuario, cai aqui
	// {
	// "nome": "Usuario Exemplo"
	// }
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED) // 201
	public Usuario cadastrar_usuario(@RequestBody Usuario usuario) {

		System.out.println("Cadastrando novo usuário: " + usuario.getNome());
		usuario.setId_imagens(new ArrayList<Integer>());

		return usuarioRepository.save(usuario);

	}

}
