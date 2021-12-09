package desenv.integrado.sistemas.blas.cgnr.server.models;

import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity // JPA
public class Usuario {
	
	@Id
	private String nome;
	@Column
	private ArrayList<Integer> id_imagens;
	
	
	public String getNome() {
	
		return nome;
	
	}
	public void setNome(String nome) {
	
		this.nome = nome;
	
	}
	public ArrayList<Integer> getId_imagens() {
	
		return id_imagens;
	
	}
	public void setId_imagens(ArrayList<Integer> id_imagens) {
	
		this.id_imagens = id_imagens;
	
	}
	
	
	

}
