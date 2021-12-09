Orientações para aplicação cliente:

1. Descobrir o nomedousuario

2. verificar se o usuario já existe e também serve para obter a lista de todas IDs das imagens deste usuário:
	GET em: localhost:8080/usuario/{nomedousuario}
	se retornar JSON com nome e IDs, tudo certo, se retornar 404 not found, não existe
	
3. Caso não exista, cadastrar usuário:
	POST em: localhost:8080/usuario passando JSON {"nome": string-nome-do-usuario}
	
4. Auxiliar: caso precise, requerimento para obter todos usuários
	GET localhost:8080/usuario
	
5. Reconstruir imagem:
	POST em: localhost:8080/cgnr/reconstruir/{nomedousuario} passando JSON {"matriz_sinal_g": list-com-os-elementos-da-matriz-sinal-g}
	caso precise/queira, ele retorna por padrão o ID da imagem criada
	
6. Obter imagem de ID específico:
	GET em: localhost:8080/imagem/{id}
	
7. Auxiliar: caso precise, requerimento para obter todas imagens
	GET em: localhost:8080/imagem
	