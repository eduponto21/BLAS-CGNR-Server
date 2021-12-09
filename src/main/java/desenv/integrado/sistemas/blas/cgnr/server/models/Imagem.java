package desenv.integrado.sistemas.blas.cgnr.server.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity // JPA
public class Imagem {

//	Cada imagem deverá conter no mínimo os seguintes dados:
//
//		Identificação do usuário;
//		Identificação do algoritmo utilizado
//		Data e hora do início da reconstrução;
//		Data e hora do término da reconstrução;
//		Tamanho em pixels;
//		O número de iterações executadas.

	@Id // primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private String usuario;
	@Column
	private String algoritmo;
	@Column
	private String tempo_inicio;
	@Column
	private String tempo_fim;
	@Column
	private String tamanho_pixels;
	@Column
	private Integer qntd_iteracoes_executadas;
	@Column
	private byte[] img;


	public Imagem(String usuario, String algoritmo, String tempo_inicio, String tamanho_pixels) {

		super();
		this.usuario = usuario;
		this.algoritmo = algoritmo;
		this.tempo_inicio = tempo_inicio;
		this.tamanho_pixels = tamanho_pixels;

	}


	public Integer getId() {

		return id;

	}


	public void setId(Integer id) {

		this.id = id;

	}


	public String getUsuario() {

		return usuario;

	}


	public void setUsuario(String usuario) {

		this.usuario = usuario;

	}


	public String getAlgoritmo() {

		return algoritmo;

	}


	public void setAlgoritmo(String algoritmo) {

		this.algoritmo = algoritmo;

	}


	public String getTempo_inicio() {

		return tempo_inicio;

	}


	public void setTempo_inicio(String tempo_inicio) {

		this.tempo_inicio = tempo_inicio;

	}


	public String getTempo_fim() {

		return tempo_fim;

	}


	public void setTempo_fim(String tempo_fim) {

		this.tempo_fim = tempo_fim;

	}


	public String getTamanho_pixels() {

		return tamanho_pixels;

	}


	public void setTamanho_pixels(String tamanho_pixels) {

		this.tamanho_pixels = tamanho_pixels;

	}


	public Integer getQntd_iteracoes_executadas() {

		return qntd_iteracoes_executadas;

	}


	public void setQntd_iteracoes_executadas(Integer qntd_iteracoes_executadas) {

		this.qntd_iteracoes_executadas = qntd_iteracoes_executadas;

	}


	public byte[] getImg() {

		return img;

	}


	public void setImg(byte[] img) {

		this.img = img;

	}


}
