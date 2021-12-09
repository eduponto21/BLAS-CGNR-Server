package desenv.integrado.sistemas.blas.cgnr.server.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.jblas.FloatMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import desenv.integrado.sistemas.blas.cgnr.server.models.Imagem;
import desenv.integrado.sistemas.blas.cgnr.server.models.Matriz;
import desenv.integrado.sistemas.blas.cgnr.server.models.Usuario;
import desenv.integrado.sistemas.blas.cgnr.server.repositories.ImagemRepository;
import desenv.integrado.sistemas.blas.cgnr.server.repositories.UsuarioRepository;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;

@RestController // controller e responseBody, classe q vai receber requisições, endpoint
@RequestMapping("/cgnr")
public class CGNRController {

	@Autowired // Vai injetar uma instância que o JPA faz em UsuarioRepository em tempo de execução aqui
	private UsuarioRepository usuarioRepository;
	@Autowired // Vai injetar uma instância que o JPA faz em ImagemRepository em tempo de execução aqui
	private ImagemRepository imagemRepository;

	private final Matriz h = new Matriz("Dados/H-1.csv");


//	Implementar um servidor para reconstrução de imagens:
//
//		Receber os dados para reconstrução;
//		Carregar o modelo de reconstrução de acordo com os parâmetros recebidos;
//		Executar o algoritmo de reconstrução;
//		Executar até que o erro (ϵ) seja menor do que 1e10-4 .
//		Salvar o resultado.


	@PostMapping(path = "/reconstruir/{usuario}")
	@ResponseStatus(HttpStatus.OK)
	public Integer reconstruir_cgnr(@RequestBody List<Float> matriz_sinal_g, @PathVariable("usuario") String usuario) {

		// Chama CGNR, salva imagem e atrela o ID com o usuário respectivo
		Matriz g = new Matriz(matriz_sinal_g);

		Imagem imagem = new Imagem(usuario, "CGNR", this.parse_date_to_string(LocalDateTime.now()), "60x60");
		//Ainda falta na imagem: String tempo_fim, Integer qntd_iteracoes_executadas, byte[] img
		
		 //img é uma matriz(vetor) 3600,1
		//Aqui resolve o tempo final e iterações
		FloatMatrix img = this.executar_cgnr(h.getMatriz(), g.getMatriz(), imagem);
		
		System.out.println("\n\nqntd de iterações final salva no objeto foi de: " + imagem.getQntd_iteracoes_executadas());
		
		//chama conversor de intervalos que retorna uma matriz 60x60 que será transformada em imagem
		img = this.transforma_intervalo(img);
		
		//falta salvar a imagem em si, a imagem objeto no db e associar ID da img ao usuario
		//Transforma em imagem e salva em byte[]
        ImageProcessor ip = new FloatProcessor(img.toArray2());
        BufferedImage bi = ip.getBufferedImage();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
			ImageIO.write(bi, "jpg", baos);
		} catch (IOException e) {
			System.out.println("Falha em transformar imagem pra byte[]");
		}
//        byte[] bytes = baos.toByteArray();
        imagem.setImg(baos.toByteArray());
        
        Imagem saved_image = imagemRepository.save(imagem);
        Usuario user = usuarioRepository.getById(usuario);
        user.getId_imagens().add(saved_image.getId());
        usuarioRepository.save(user);
		
		return saved_image.getId();
		
	}


	// CGNR
	// Codificar um protótipo com o Algoritmo CGNR. Validar os resultados com os dados experimentais.
	// Medir o tempo total de execução e o consumo de recursos como memória e ocupação de CPU.
	private FloatMatrix executar_cgnr(FloatMatrix matriz_modelo_h, FloatMatrix vetor_sinal_g, Imagem img) {
		/**
		 * Entrada: H e g Saída: f g - Vetor de sinal H - Matriz de modelo f - Imagem S - Número de amostras do sinal N - Número de elementos sensores Cálculo do fator de redução (c) Cálculo do coeficiente de
		 * regularização (λ) Cálculo do erro (ϵ) Cálculo do ganho de sinal (γ) Calculo de vetores norma 2 euclidiana Matriz transposta (elevado a T)
		 **/

		// ENTRA: Matriz H-1 e G-1 (ou G-2 ou a-1)
		// No comparativo com o pseudo-código, entende-se por matrizes 0 sendo a 0 mesmo e a i
		// e matrizes b pelas i+1

		FloatMatrix f0 = FloatMatrix.zeros(3600, 1); // imagem 60x60, como definido no trabalho
		// na verdade é um vetor 3600, ai pra construir a imagem 60z60 separa uma coluna a cada 60 elementos!!
		FloatMatrix r0, z0, p0;
		System.out.println(
				"dados de h: " + matriz_modelo_h.rows + ", " + matriz_modelo_h.columns + ", " + matriz_modelo_h.length);
		System.out.println(
				"dados de G: " + vetor_sinal_g.rows + ", " + vetor_sinal_g.columns + ", " + vetor_sinal_g.length);

		FloatMatrix hf0 = matriz_modelo_h.mmul(f0);
//        System.out.println("dados de HF0: " + hf0.rows + ", " + hf0.columns + ", " + hf0.length);
		r0 = vetor_sinal_g.sub(hf0);
		System.out.println("dados de R: " + r0.rows + ", " + r0.columns + ", " + r0.length);

		FloatMatrix ht = matriz_modelo_h.transpose();
		z0 = ht.mmul(r0);
		System.out.println("dados de Z: " + z0.rows + ", " + z0.columns + ", " + z0.length);

		p0 = z0.dup();

		FloatMatrix w, fb, rb, zb, pb;
		ArrayList<Float> alpha = new ArrayList<Float>();
		ArrayList<Float> beta = new ArrayList<Float>();
		float erro = (float) 1.0;

		int i;
		for (i = 0; i < 15 && erro >= 0.0001; i++) {
			// wi
			w = matriz_modelo_h.mmul(p0);
//            System.out.println("dados de W: " + w.rows + ", " + w.columns + ", " + w.length);

			// ai = norma2 ao quadrado de zi / norma2 ao quadrado de wi
			float normaz = z0.norm2() * z0.norm2();
			float normaw = w.norm2() * w.norm2();
			alpha.add(i, normaz / normaw);
//            System.out.println("valor de alpha é: " + alpha.get(i));

			// fi+1 -> fb
			FloatMatrix aipi = p0.mmul(alpha.get(i));
			fb = f0.add(aipi);

			// ri+1 -> rb
			FloatMatrix aiwi = w.mmul(alpha.get(i));
			rb = r0.sub(aiwi);

			// zi+1 -> zb
			zb = ht.mmul(rb);

			// B1 = norma2 ao quadrado de zb / norma2 ao quadrado de z0
			float normazb = zb.norm2() * zb.norm2();
			float normaz0 = z0.norm2() * z0.norm2();
			beta.add(i, normazb / normaz0);
//            System.out.println("valor de beta é: " + beta.get(i));

			// pi+1 -> pb
			FloatMatrix bipi = p0.mmul(beta.get(i));
			pb = zb.add(bipi);

			// cálculo do erro
			erro = Math.abs(rb.norm2() - r0.norm2());
			System.out.println("Erro está em: " + erro);
			System.out.println("i é: " + i);

			// coloca os auxiliares B como 0 (o i+1 vira i)
			f0 = fb;
			r0 = rb;
			z0 = zb;
			p0 = pb;
//            System.out.println("//");
		}
		img.setQntd_iteracoes_executadas(i-1);
		img.setTempo_fim(this.parse_date_to_string(LocalDateTime.now()));

		return f0;

	}


	// Recebe um LocalDateTime e retorna String <dd/MM/yyyy HH:mm>
	private String parse_date_to_string(LocalDateTime data_hora) {

		DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		return data_hora.format(formater);

	}
	
	//transforma uma matriz 3600,1 pra uma 60,60
	private FloatMatrix transforma_intervalo(FloatMatrix imagem) {
        FloatMatrix new_image = FloatMatrix.zeros(60,60);
        float oldMax = imagem.max();
        float oldMin = imagem.min();
        float newMax = 255;
        float newMin = 0;
        
        float oldRange = (oldMax - oldMin);
        float newRange = (newMax - newMin);
        float newValue;// = (((oldValue - oldMin) * newRange) / oldRange) + newMin;
        
        int k = 0;
        for (int i = 0; i < new_image.rows; i++) {
            for (int j = 0; j < new_image.columns; j++) {
                newValue = (((imagem.get(k) - oldMin) * newRange) / oldRange) + newMin;
                new_image.put(i, j, newValue);
                k++;
            }
        }
        
        return new_image;

    }


}
