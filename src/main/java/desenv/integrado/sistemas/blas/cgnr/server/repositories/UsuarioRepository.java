package desenv.integrado.sistemas.blas.cgnr.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import desenv.integrado.sistemas.blas.cgnr.server.models.Usuario;

@Repository //responsável por interagir com o banco de dados, define o tipo de Dado e o tipo do ID
public interface UsuarioRepository extends JpaRepository<Usuario, String> {
	//Já implementa os métodos principais, em tempo de execução
}
