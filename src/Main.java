import proyectoFinal.*;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;

/**
 * Clase que encripta archivos a partir de una contraseña dada y genera llaves
 * para desencriptarlos
 * 
 * @author Ismael Fernandez Martinez
 * @author Miguel Vilchis Dominhuez
 * @version 1.0
 * 
 */
public class Main {

	public static void main(String[] args) {
		if (args.length != 5 & args.length != 3) {
			System.err.println("USO: ");
			System.err.println();
			System.err
					.println("MODO CIFRADO: java Main c archivoEva numE pnts archivo");
			System.err.println("c: opcion cifrar.");
			System.err
					.println("archivoEva: El nombre del archivo en el que serán guardadas las n evaluaciones del polinomio.");
			System.err
					.println("numE: El número total de evaluaciones requeridas (n > 2).");
			System.err
					.println("pnts: El número mínimo de puntos necesarios para descifrar (1 < t ≤ n).");
			System.err
					.println("archivo: El nombre del archivo con el documento claro.");
			System.err.println();
			System.err
					.println("MODO DESCIFRADO: java Main d archivo.frg archivoCifrado");
			System.err.println("d: opcion descifrar");
			System.err
					.println("archivo.frg: El nombre del archivo con, al menos, t de las n evaluaciones del polinomio.");
			System.err
					.println("archivoCifrado: El nombre del archivo cifrado.");
			System.err.println();
			System.exit(0);
			System.exit(0);
		}
		FileEncryption aux = null;
		Polinomio pol;
		switch (args[0].charAt(0)) {
		case 'c':
			int evaluacionesRequeridas = 0;
			int puntosNecesarios = 0;
			try {
				evaluacionesRequeridas = Integer.parseInt(args[2]);
				puntosNecesarios = Integer.parseInt(args[3]);
			} catch (Exception e) {
				System.err
						.println("USO: java Main c archivoEva numE pnts archivo");
				System.err.println("c: opcion cifrar.");
				System.err
						.println("archivoEva: El nombre del archivo en el que serán guardadas las n evaluaciones del polinomio.");
				System.err
						.println("numE: El número total de evaluaciones requeridas (n > 2).");
				System.err
						.println("pnts: El número mínimo de puntos necesarios para descifrar (1 < t ≤ n).");
				System.err
						.println("archivo: El nombre del archivo con el documento claro.");
				System.exit(0);
			}
			Console console = System.console();
			if (console == null) {
				System.err.println("ERROR: Incapaz de obtener consola");
				System.exit(0);
			}
			String password = new String(console
					.readPassword("Introduce una contraseña: "));

			try {
				aux = new FileEncryption();
				aux.makeKey(password);
			} catch (GeneralSecurityException e1) {
				System.err.println("ERROR: Error al generar la contraseña");
				System.exit(0);
			}

			pol = new Polinomio(aux.getAesKey(), evaluacionesRequeridas,
					puntosNecesarios);
			try {
				aux.encrypt(new File(args[4]), new File(args[4].split("\\.")[0]
						+ ".aes"));
			} catch (InvalidKeyException e) {
			} catch (IOException e) {
				System.err.println("ERROR: El archivo " + args[4]
						+ " no se encuentra en el directorio");

				System.exit(0);
			}
			pol.genera_llaves(args[1]);
			break;
		case 'd':
			String llave = null;
			try {
				llave = Polinomio.leeLlaves(args[1]);
			} catch (FileNotFoundException e) {
				System.err.println("ERROR: El archivo " + args[1]
						+ " no se encuentra en el directorio");
			}
			try {
				aux = new FileEncryption();
			} catch (GeneralSecurityException e1) {
				System.exit(0);
			}
			aux.setAesKey(llave);
			try {
				aux.decrypt(new File(args[2]),
						new File(args[2].split("\\.")[0]));
			} catch (InvalidKeyException e) {
				System.err.println("ERROR: Error desencriptando, la llave no coincide");
				System.exit(0);
			} catch (IOException e) {
				System.err.println("ERROR: El archivo " + args[2]
						+ " no se encuentra en el directorio");
				System.exit(0);
			}
			break;
		default:
			System.err.println("USO: ");
			System.err.println();
			System.err
					.println("MODO CIFRADO: java Main c archivoEva numE pnts archivo");
			System.err.println("c: opcion cifrar.");
			System.err
					.println("archivoEva: El nombre del archivo en el que serán guardadas las n evaluaciones del polinomio.");
			System.err
					.println("numE: El número total de evaluaciones requeridas (n > 2).");
			System.err
					.println("pnts: El número mínimo de puntos necesarios para descifrar (1 < t ≤ n).");
			System.err
					.println("archivo: El nombre del archivo con el documento claro.");
			System.err.println();
			System.err
					.println("MODO DESCIFRADO: java Main d archivo.frg archivoCifrado");
			System.err.println("d: opcion descifrar");
			System.err
					.println("archivo.frg: El nombre del archivo con, al menos, t de las n evaluaciones del polinomio.");
			System.err
					.println("archivoCifrado: El nombre del archivo cifrado.");
			System.err.println();
			System.exit(0);
			System.exit(0);
			break;
		}
	}
}
