package desenv.integrado.sistemas.blas.cgnr.server.models;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jblas.FloatMatrix;

public class Matriz {

	private FloatMatrix matriz;
	private String path;


	public Matriz(String path) {

		this.setPath(path);

		try {
			this.setMatriz(FloatMatrix.loadCSVFile(path));
		} catch (IOException ex) {
			Logger.getLogger(Matriz.class.getName()).log(Level.SEVERE, null, ex);
		}

	}
	
	public Matriz (List<Float> matriz_coluna) {
		this.setMatriz(new FloatMatrix(matriz_coluna));
	}


	public FloatMatrix getMatriz() {

		return matriz;

	}


	public void setMatriz(FloatMatrix matriz) {

		this.matriz = matriz;

	}


	public String getPath() {

		return path;

	}


	public void setPath(String path) {

		this.path = path;

	}


}
