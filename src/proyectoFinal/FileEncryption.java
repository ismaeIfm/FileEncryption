package proyectoFinal;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.*;

import java.util.Scanner;

import javax.crypto.*;
import javax.crypto.spec.*;

/**
 * Clase que encripta y desencripta archivos con el algoritmo AES
 * 
 * @author Ismael Fernandez Martinez
 * @author Miguel Vilchis Dominhuez
 * @version 1.0
 * 
 */
public class FileEncryption {

	Cipher aesCipher;
	byte[] aesKey;
	SecretKeySpec aeskeySpec;

	/**
	 * Establece una llave a patir de un numero
	 * 
	 * @param key
	 *            Numero
	 */
	public void setAesKey(String key) {
		aesKey = new BigInteger(key).toByteArray();
		aeskeySpec = new SecretKeySpec(aesKey, "AES");
	}

	/**
	 * Regresa la llave con la que se esta trabajando en el moento
	 * 
	 * @return La llave
	 */
	public byte[] getAesKey() {
		return aesKey;
	}

	/**
	 * Contructor por omision
	 * 
	 * @throws GeneralSecurityException
	 */
	public FileEncryption() throws GeneralSecurityException {
		aesCipher = Cipher.getInstance("AES");
	}

	/**
	 * Construlle una llave simetrica apartir de una cadena
	 * 
	 * @param pass
	 *            cadena apartir de la cual se desea construir la llave
	 * @throws NoSuchAlgorithmException
	 */
	public void makeKey(String pass) throws NoSuchAlgorithmException {

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		}

		try {
			md.update(pass.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}
		aesKey = md.digest();
		aeskeySpec = new SecretKeySpec(aesKey, "AES");
	}

	/**
	 * Encripta un archivo y luego copia el archivo encriptado a otro archivo
	 * 
	 * @param in
	 *            Archivo que se desea desencriptar
	 * @param out
	 *            Archivo en el que se desea colocar el contenido encriptado del
	 *            archivo
	 * @throws IOException
	 *             Lanzada si no se encuentra el archivo que se desea encriptar
	 * @throws InvalidKeyException
	 *             Lanzada si la llave es invalida
	 */
	public void encrypt(File in, File out) throws IOException,
			InvalidKeyException {
		aesCipher.init(Cipher.ENCRYPT_MODE, aeskeySpec);

		FileInputStream is = new FileInputStream(in);
		CipherOutputStream os = new CipherOutputStream(
				new FileOutputStream(out), aesCipher);

		copy(is, os);

		os.close();
	}

	/**
	 * Decripta un archivo y luego copia el contenido del archivo desencriptado
	 * a otro archivo
	 * 
	 * @param in
	 *            Archivo que se desea desencriptar
	 * @param out
	 *            Archivo en el que se desea guardar el contenido del archivo
	 *            desencriptado
	 * @throws IOException
	 *             Lanzada cuando no se encuentra el archivo que se desea
	 *             desencriptar
	 * @throws InvalidKeyException
	 *             Lanzada cuando la llave es invalida
	 */
	public void decrypt(File in, File out) throws IOException,
			InvalidKeyException {
		aesCipher.init(Cipher.DECRYPT_MODE, aeskeySpec);

		CipherInputStream is = new CipherInputStream(new FileInputStream(in),
				aesCipher);
		FileOutputStream os = new FileOutputStream(out);

		copy(is, os);

		is.close();
		os.close();
	}

	/**
	 * Copia el flujo de informacion
	 * 
	 * @param is
	 *            Flujo de entrada
	 * @param os
	 *            Flujo de salida
	 * @throws IOException
	 */
	private void copy(InputStream is, OutputStream os) throws IOException {
		int i;
		byte[] b = new byte[1024];
		while ((i = is.read(b)) != -1) {
			os.write(b, 0, i);
		}
	}

	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);
		FileEncryption aux;
		try {
			aux = new FileEncryption();
			System.out.println("Introduce una contraseña: ");
			aux.makeKey(keyboard.nextLine());
			try {
				aux.encrypt(new File("prueba.txt"), new File("prueba.aes"));
				aux.setAesKey(new BigInteger(aux.getAesKey()).toString());
				aux.decrypt(new File("prueba.aes"), new File("prueba-des.txt"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		System.out.println("popo");

	}
}