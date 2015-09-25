package proyectoFinal;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

import java.util.Random;
import java.util.Scanner;
import java.io.File;

/**
 * Clase que genera llaves a partir de una llave del tipo AES. O contruye una
 * llave del tipo aes apartir de punto y evaluaciones
 * 
 * @author Ismael Fernandez Martinez
 * @author Miguel Vilchis Dominguez
 * @version 1.0
 * 
 */
public class Polinomio {
	private int t_exp;
	private int max_exp;
	private BigDecimal clave;
	private BigDecimal[] polinomio;
	private BigDecimal[][] evaluaciones;

	/**
	 * Constructor que resive como parámetros la clave, y los diversos
	 * coeficientes
	 * 
	 * @param clave_original
	 *            <byte []> es la contrasenia en arreglo de bytes
	 * @param max_exp
	 *            <int> que es el máximo exponente del polinomio a crear
	 * @param t_exp
	 *            <int> que es el grado del polinomio utilizado para obtener el
	 *            coeficiente original.
	 */
	public Polinomio(byte[] clave_original, int max_exp, int t_exp) {
		this.max_exp = max_exp;
		this.t_exp = t_exp;
		BigInteger tmp = new BigInteger(clave_original);
		clave = new BigDecimal(new BigInteger(clave_original));
		llena_polinomio();
		evaluaciones = new BigDecimal[max_exp][2];
	}

	public Polinomio(String clave_original, int max_exp, int t_exp) {
		this.max_exp = max_exp;
		this.t_exp = t_exp;
		clave = new BigDecimal((new BigInteger(clave_original).toString()));
		llena_polinomio();
		evaluaciones = new BigDecimal[max_exp][2];
	}

	/**
	 * Genera cada coeficiente del polinomio, el de exponente 0 con la clave,
	 * los demás coeficientes de manera aleatoria.
	 */
	private void llena_polinomio() {
		polinomio = new BigDecimal[t_exp];
		polinomio[0] = clave;
		for (int idx = 1; idx < t_exp; idx++) {
			BigDecimal aux = new BigDecimal(new Random().nextInt() + "");
			polinomio[idx] = aux;
		}
	}

	/**
	 * Método que va a generar las 'max_exp' llaves evaluando al polinomio en la
	 * sucesión 1 a n donde n = max_exp
	 * 
	 * @param nombre
	 *            Nombre del archivo donde se guardaran las llaves
	 */
	public void genera_llaves(String nombre) {
		for (int x = 0; x < max_exp; x++) {
			BigDecimal resultado = new BigDecimal("0");
			for (int idx = 0; idx < t_exp; idx++) {
				resultado = resultado.add(polinomio[idx]
						.multiply(new BigDecimal(x + 1).pow(idx)));
			}
			evaluaciones[x][0] = new BigDecimal(x + 1);
			evaluaciones[x][1] = resultado;
		}
		generaArchivoLlaves(nombre);
	}

	/**
	 * Método que va a implementar el algoritmo de Lagrange, para al extrapolar
	 * al polinomio
	 * 
	 * @param claves
	 *            <BigDecimal[][]> que es un arreglo bidimensional, en el cual,
	 *            la primera entrada nos da el valor en el que fue evaluado el
	 *            polinomio y la 2da entrada nos da el valor obtenido a evaluar
	 *            el polinomio en ese punto.
	 */
	public static String recupera_Polinomio(BigDecimal[][] claves) {
		BigDecimal clave_original = new BigDecimal(0);
		for (int j = 0; j < claves.length; j++) {
			BigDecimal producto = new BigDecimal(1);
			BigDecimal dividendo = new BigDecimal(1);
			BigDecimal termino = new BigDecimal(1);
			for (int m = 0; m < claves.length; m++) {
				if (m != j) {
					producto = new BigDecimal(1);
					dividendo = new BigDecimal(1);
					producto = producto.multiply(claves[m][0]
							.multiply(new BigDecimal(-1)));
					dividendo = dividendo.multiply(claves[j][0]
							.subtract(claves[m][0]));
					termino = termino.multiply(producto.divide(dividendo, 100,
							BigDecimal.ROUND_HALF_UP));
				}
			}

			clave_original = clave_original.add(termino.multiply(claves[j][1]));
		}
		clave_original = clave_original.round(new MathContext(90));
		String regresa = clave_original.toString();
		if (regresa.indexOf(".") != -1)
			regresa = regresa.substring(0, regresa.indexOf("."));

		return regresa;
	}

	/**
	 * A traves de este método generamos el archivo de las n llaves, con el
	 * nombre del archivo original, cambiando la extensión
	 * 
	 * @param nombre
	 *            Nombre del archivo donde se guardaran las llaves
	 */
	private void generaArchivoLlaves(String nombre) {
		PrintWriter ayuda = null;
		try {
			ayuda = new PrintWriter(new FileOutputStream(nombre + ".frg"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < evaluaciones.length; i++) {
			ayuda.println(evaluaciones[i][0] + " , " + evaluaciones[i][1]);
		}
		ayuda.close();
	}
	public static String leeLlaves(String archivo) throws FileNotFoundException {
		
		Scanner sc = new Scanner(new File(archivo));
		String tmp = ""; 
		int no_lineas = 0;
		while(sc.hasNext()) {
			tmp += sc.nextLine();
			no_lineas++;
			tmp += "/";
		}
		BigDecimal[][] llaves = new BigDecimal[no_lineas][2];
		String [] renglones = tmp.split("/");
		for(int idx = 0; idx < no_lineas; idx++) {
			String[] aux = renglones[idx].split(",");
			llaves[idx][0] = new BigDecimal(aux[0].trim());
			llaves[idx][1] = new BigDecimal(aux[1].trim());
		}
		return recupera_Polinomio(llaves);
	}
	
	public static void main(String[] args) throws FileNotFoundException {

		Polinomio ayuda = new Polinomio(
				"2730533037212634637604676518871272610568817116641122971949417600595254297693",
				10, 6);
		ayuda.genera_llaves("prueba");
		BigDecimal[][] aux2 = new BigDecimal[6][2];
		Scanner sc = new Scanner(new File("prueba.frg"));
		sc.nextLine();
		int idx = 3;
		for (int m = 0; m < idx; m++)
			sc.nextLine();
		for (int i = 0; i < 6; i++) {
			String[] tmp = sc.nextLine().split(",");
			aux2[i][0] = new BigDecimal(tmp[0].trim());
			aux2[i][1] = new BigDecimal(tmp[1].trim());
		}
		System.out.println("la original        " + ayuda.clave);
		System.out.println("la que me da       "
				+ Polinomio.recupera_Polinomio(aux2));
	}
}