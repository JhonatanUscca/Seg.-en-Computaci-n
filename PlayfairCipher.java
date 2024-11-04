import java.lang.System.Logger.Level;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.StringTokenizer;

public class PlayfairCipher {
	
	private static final System.Logger LOGGER = System.getLogger(PlayfairCipher.class.getName());
		
	public static final String FORMAT_STRING_REGEX = "^[a-zA-Z][a-zA-Z\\s]*$";
	
	public static final char LETTER_I = 73;
	
	public static final char LETTER_J = 74;
	
	public static final int MATRIX_SIZE = 5;
			
	private record PlayfairString(String processedMessage, 
								List<Integer> whiteSpaceIndeces, 
								List<Character[]> diSplitString) {
		
		public static PlayfairString valueOfWithLetterI(String message) {		
			String processedMessage = getProcessMessage(message);
			List<Integer> whiteSpaceIndeces = getWhiteSpaceIndeces(message); 
			
			processedMessage = processedMessage.replace(PlayfairCipher.LETTER_J, PlayfairCipher.LETTER_I);
			
			List<Character[]> splitedProcessedMessage = getSplitedProcessedMessage(processedMessage); 
					
			return new PlayfairString(processedMessage, whiteSpaceIndeces, splitedProcessedMessage);	
		}	
		
		public static PlayfairString valueOfWithLetterJ(String message) {		
			String processedMessage = getProcessMessage(message);
			List<Integer> whiteSpaceIndeces = getWhiteSpaceIndeces(message); 
			
			processedMessage = processedMessage.replace(PlayfairCipher.LETTER_I, PlayfairCipher.LETTER_J);
			
			List<Character[]> splitedProcessedMessage = getSplitedProcessedMessage(processedMessage); 
					
			return new PlayfairString(processedMessage, whiteSpaceIndeces, splitedProcessedMessage);	
		}
				
		public static String[] tokenize(String message) {
			
			var messageTokenizer = new StringTokenizer(message.strip().toUpperCase());			
						
			var tokens = new ArrayList<String>();			
			while(messageTokenizer.hasMoreTokens()) {		
				tokens.add(messageTokenizer.nextToken());
			}	
			
			return tokens.toArray(new String[0]);
		}
		
		public static String mix(String message) {
			var tokens = tokenize(message);
			var strBuildre = new StringBuilder();
			
			for (final var TOKEN : tokens) {
				strBuildre.append(TOKEN);
			}
			
			return strBuildre.toString();
		}
		
		private static String getProcessMessage(String message) {
			var messageBuilder = new StringBuilder();
			var tokens = tokenize(message);
			
			for (final var TOKEN : tokens) {
				messageBuilder.append(TOKEN);
			}
			if (messageBuilder.toString().length() % 2 != 0) {
				messageBuilder.append('X');
			}		
			
			return messageBuilder.toString();
		}
		
		private static List<Integer> getWhiteSpaceIndeces(String message) {
			var tokens = tokenize(message);
			List<Integer> whiteSpaceIndeces = new ArrayList<>(); 
			int whiteSpaceIndex = 0;	
			
			if (tokens.length > 1) {			
				for (int i = 0; i < tokens.length - 1; i++) {
					whiteSpaceIndex += tokens[i].length();
					whiteSpaceIndeces.add(whiteSpaceIndex);
				}
			} 
			
			return whiteSpaceIndeces;
		}
		
		private static List<Character[]> getSplitedProcessedMessage(String processedMessage) {			
			List<Character[]> splitedProcessedMessage = new ArrayList<>(); 
			var processedMessageArray = processedMessage.toCharArray();
			int processedMessageSize = processedMessageArray.length;
				
			for (int i = 0, j = 1; j < processedMessageSize; i += 2, j += 2) {
				Character[] letters = { processedMessageArray[i], processedMessageArray[j] }; 
				splitedProcessedMessage.add(letters);
			}		
			
			return splitedProcessedMessage;
		}
	}
	
	private PlayfairCipher() {}
	
	public static void main (String[] args) {		
		var message = "ATAQUE INMINENTE";	
		var key = "SEGURIDAD";
			
		System.out.println("Mensaje Original: " + message);		
		System.out.println("LLave: " + key);
		
		var encryptedMessage = encryptWithLetterI(message, key);
		
		System.out.println("Mensaje Encriptado: " + encryptedMessage);
		System.out.println("Mensaje Desencriptado: " + decryptWithLetterI(encryptedMessage, key));
	}
	
	public static String encryptWithLetterI(String message, String key) {		
		if (!message.matches(FORMAT_STRING_REGEX) || !key.matches(FORMAT_STRING_REGEX)) {
			throw new IllegalArgumentException();
		}			
		return encrypt(PlayfairString.valueOfWithLetterI(message), createMatrixWithI(key));
	}		
	
	public static String encryptWithLetterJ(String message, String key) {
		if (!message.matches(FORMAT_STRING_REGEX) || !key.matches(FORMAT_STRING_REGEX)) {
			throw new IllegalArgumentException();
		}				
		return encrypt(PlayfairString.valueOfWithLetterJ(message), createMatrixWithJ(key));
	}
		
	public static String decryptWithLetterI(String message, String key) {		
		if (!message.matches(FORMAT_STRING_REGEX) || !key.matches(FORMAT_STRING_REGEX)) {
			throw new IllegalArgumentException();
		}				
		return decrypt(PlayfairString.valueOfWithLetterI(message), createMatrixWithI(key));
	}		
	
	public static String decryptWithLetterJ(String message, String key) {
		if (!message.matches(FORMAT_STRING_REGEX) || !key.matches(FORMAT_STRING_REGEX)) {
			throw new IllegalArgumentException();
		}					
		return decrypt(PlayfairString.valueOfWithLetterJ(message), createMatrixWithJ(key));
	}
	
 	public static char[][] createMatrixWithJ(String key) { 		
		if (!key.matches(FORMAT_STRING_REGEX)) {
			throw new IllegalArgumentException();
		}
 		
		var matrix = new char[MATRIX_SIZE][MATRIX_SIZE];
		var processedKey = PlayfairString.mix(key.toUpperCase().replace(LETTER_I, LETTER_J));		
 		var linkHashSetLetters = getListOfAlphabetUsingKey(PlayfairString.mix(processedKey)); 
 		var loggerSB = new StringBuilder();
 		linkHashSetLetters.remove(Character.valueOf(LETTER_I));	
 		
 		var iteratorLinkHashSetLetters = linkHashSetLetters.iterator();
 		
 		loggerSB.append("\nMatrix\n");
		for (int i = 0; i < MATRIX_SIZE; i++) {
			for (int j = 0; j < MATRIX_SIZE; j++) {
				matrix[i][j] = iteratorLinkHashSetLetters.next();
		 		loggerSB.append(matrix[i][j]);
			}
	 		loggerSB.append('\n');
		}
		
		LOGGER.log(Level.INFO, loggerSB.toString());
		return matrix;	
 	}
 	
 	public static char[][] createMatrixWithI(String key) {
 		
		if (!key.matches(FORMAT_STRING_REGEX)) {
			throw new IllegalArgumentException();
		}
 		
		var matrix = new char[MATRIX_SIZE][MATRIX_SIZE];
		var processedKey = PlayfairString.mix(key.toUpperCase().replace(LETTER_J, LETTER_I));		
 		var linkHashSetLetters = getListOfAlphabetUsingKey(PlayfairString.mix(processedKey)); 
 		var loggerSB = new StringBuilder();
 		linkHashSetLetters.remove(Character.valueOf(LETTER_J));	
 		
 		var iteratorLinkHashSetLetters = linkHashSetLetters.iterator();
 		
 		loggerSB.append("\nMatrix\n");
		for (int i = 0; i < MATRIX_SIZE; i++) {
			for (int j = 0; j < MATRIX_SIZE; j++) {
				matrix[i][j] = iteratorLinkHashSetLetters.next();
		 		loggerSB.append(matrix[i][j] + " ");
			}
	 		loggerSB.append('\n');
		}

		LOGGER.log(Level.INFO, loggerSB.toString());
		return matrix;	
 	}
	
	private static String putWhiteSpaces(String message, PlayfairString playfairMessage) {		
		int lastIndex = 0;
		var finalMessage = new StringBuilder(); 
			
		for (final var INDEX : playfairMessage.whiteSpaceIndeces()) {
			finalMessage.append(message.substring(lastIndex, INDEX));	
			finalMessage.append(' ');
			lastIndex = INDEX;
		}		
			
		finalMessage.append(message.substring(lastIndex, message.length()));
				
		return finalMessage.toString();
	}
		
	private static LinkedHashSet<Character> getListOfAlphabetUsingKey(String key) {	
							
		var linkHashSetLetters = new LinkedHashSet<Character>();  
		var tempKey = key.toCharArray();
	
		for (final var c : tempKey) {
			linkHashSetLetters.add(c);			
		}
			
		LOGGER.log(Level.DEBUG, linkHashSetLetters.toString());
					
		for (char ascii = 65; ascii < 91; ascii++) {	
			linkHashSetLetters.add(Character.valueOf(ascii));
		}
		
		LOGGER.log(Level.DEBUG, linkHashSetLetters.toString());
		
		return linkHashSetLetters;
	}
	
	private static String encrypt(PlayfairString playfairStringMessage, char[][] matrix) {		
			
		/*Objeto HashMap<Character, Integer[]> que almacena las coordenadas de cada caracter en base a la matriz*/
		var matrixCoordinates = createMatrixCoordinates(matrix);
			
		/*Objeto que almacenara el mensaje encriptado*/
		var encryptedMessage = new StringBuilder();
		
		/*Bucle for que itera sobre cada par de caracteres del mensaje procesado por la clase PlayfairString*/
		for (final var CHARS : playfairStringMessage.diSplitString()) {		
			
			/*Array Integer[] que contiene el indice de la fila y columna de un caracter
			 * dentro de la matriz de claves*/
			var firstCharCoordinates = matrixCoordinates.get(CHARS[0]);
			var secondCharCoordinates = matrixCoordinates.get(CHARS[1]);
				
			/*Comprueba si dos caracteres se encuentran en la misma fila*/
			if (areSameRow(matrix, CHARS[0], CHARS[1])) {								
				LOGGER.log(Level.DEBUG, String.format("Char %s and Char %s are in the same ROW", CHARS[0], CHARS[1]));
				
				/*Para el primer caracter del par, comprueba si se ubica en el final de la fila matriz*/
				if (firstCharCoordinates[1] < MATRIX_SIZE - 1) {
					encryptedMessage.append(matrix[firstCharCoordinates[0]][firstCharCoordinates[1] + 1]);
				} else {
					encryptedMessage.append(matrix[firstCharCoordinates[0]][0]);
				}
				
				/*Para el segundo caracter del par, comprueba si se ubica en el final de la fila de la matriz*/
				if (secondCharCoordinates[1] < MATRIX_SIZE - 1) {
					encryptedMessage.append(matrix[secondCharCoordinates[0]][secondCharCoordinates[1] + 1]);
				} else {
					encryptedMessage.append(matrix[secondCharCoordinates[0]][0]);
				}
					
			/*Comprueba si dos caracteres se encuentran en la misma colunma*/
			} else if(areSameCol(matrix, CHARS[0], CHARS[1])) {				
				LOGGER.log(Level.DEBUG, String.format("Char %s and Char %s are in the same COL", CHARS[0], CHARS[1]));
				
				/*Para el primer caracter del par, comprueba si se ubica en el final de la columna de la matriz*/
				if (firstCharCoordinates[0] < MATRIX_SIZE - 1) {
					encryptedMessage.append(matrix[firstCharCoordinates[0] + 1][firstCharCoordinates[1]]);
				} else {
					encryptedMessage.append(matrix[0][firstCharCoordinates[1]]);
				}
					
				/*Para el segundo caracter del par, comprueba si se ubica en el final de la columna de la matriz*/
				if (secondCharCoordinates[0] < MATRIX_SIZE - 1) {
					encryptedMessage.append(matrix[secondCharCoordinates[0] + 1][secondCharCoordinates[1]]);
				} else {
					encryptedMessage.append(matrix[0][secondCharCoordinates[1]]);
				}
				
			/*Si el par de caracteres no se encuentran en la misma fila ni columna, entonces se ubican en forma rectangular*/		
			} else {
				LOGGER.log(Level.DEBUG, String.format("Char %s and Char %s are neither in the same row nor col", CHARS[0], CHARS[1]));
				encryptedMessage.append(matrix[firstCharCoordinates[0]][secondCharCoordinates[1]]);
				encryptedMessage.append(matrix[secondCharCoordinates[0]][firstCharCoordinates[1]]);			
			}		
		}
				
		/*La funcion putWhiteSpaces coloca los espacios en blanco del mensaje original dentro del mensaje encriptado*/
		return putWhiteSpaces(encryptedMessage.toString(), playfairStringMessage);
	}
	
	private static String decrypt(PlayfairString playfairStringMessage, char[][] matrix) {		
		
		/*Objeto HashMap<Character, Integer[]> que almacena las coordenadas de cada caracter en base a la matriz*/
		var matrixCoordinates = createMatrixCoordinates(matrix);
		
		/*Objeto que almacenara el mensaje encriptado*/
		var decryptedMessage = new StringBuilder();
		
		/*Bucle for que itera sobre cada par de caracteres del mensaje procesado por la clase PlayfairString*/
		for (final var CHARS : playfairStringMessage.diSplitString()) {
			
			/*Array Integer[] que contiene el indice de la fila y columna de un caracter
			 * dentro de la matriz de claves*/
			var firstCharCoordinates = matrixCoordinates.get(CHARS[0]);
			var secondCharCoordinates = matrixCoordinates.get(CHARS[1]);
				
			/*Comprueba si dos caracteres se encuentran en la misma fila*/
			if (areSameRow(matrix, CHARS[0], CHARS[1])) {		
				LOGGER.log(Level.DEBUG, String.format("Char %s and Char %s are in the same ROW", CHARS[0], CHARS[1]));
				
				/*Para el primer caracter del par, comprueba si se ubica en el inicio de la fila matriz*/
				if (firstCharCoordinates[1] > 0) {
					decryptedMessage.append(matrix[firstCharCoordinates[0]][firstCharCoordinates[1] - 1]);
				} else {
					decryptedMessage.append(matrix[firstCharCoordinates[0]][MATRIX_SIZE - 1]);
				}
					
				/*Para el segundo caracter del par, comprueba si se ubica en el inicio de la fila de la matriz*/
				if (secondCharCoordinates[1] > 0) {
					decryptedMessage.append(matrix[secondCharCoordinates[0]][secondCharCoordinates[1] - 1]);
				} else {
					decryptedMessage.append(matrix[secondCharCoordinates[0]][MATRIX_SIZE - 1]);
				}
						
			/*Comprueba si dos caracteres se encuentran en la misma colunma*/
			} else if(areSameCol(matrix, CHARS[0], CHARS[1])) {			
				LOGGER.log(Level.DEBUG, String.format("Char %s and Char %s are in the same COL", CHARS[0], CHARS[1]));
				
				/*Para el primer caracter del par, comprueba si se ubica en el inicio de la columna de la matriz*/
				if (firstCharCoordinates[0] > 0) {
					decryptedMessage.append(matrix[firstCharCoordinates[0] - 1][firstCharCoordinates[1]]);
				} else {
					decryptedMessage.append(matrix[MATRIX_SIZE - 1][firstCharCoordinates[1]]);
				}
				
				/*Para el segundo caracter del par, comprueba si se ubica en el inicio de la columna de la matriz*/	
				if (secondCharCoordinates[0] > 0) {
					decryptedMessage.append(matrix[secondCharCoordinates[0] - 1][secondCharCoordinates[1]]);
				} else {
					decryptedMessage.append(matrix[MATRIX_SIZE - 1][secondCharCoordinates[1]]);
				}
			/*Si el par de caracteres no se encuentran en la misma fila ni columna, entonces se ubican en forma rectangular*/		
			} else {		
				LOGGER.log(Level.DEBUG, String.format("Char %s and Char %s are neither in the same row nor col", CHARS[0], CHARS[1]));
				decryptedMessage.append(matrix[firstCharCoordinates[0]][secondCharCoordinates[1]]);
				decryptedMessage.append(matrix[secondCharCoordinates[0]][firstCharCoordinates[1]]);			
			}		
		}
				
		/*La funcion putWhiteSpaces coloca los espacios en blanco del mensaje original dentro del mensaje desencriptado*/
		return putWhiteSpaces(decryptedMessage.toString(), playfairStringMessage);
	}

	private static HashMap<Character, Integer[]> createMatrixCoordinates(char[][] matrix) {
		var tempMatrixCoordinates = new HashMap<Character, Integer[]>();
		
		for (int i = 0; i < MATRIX_SIZE; i++) {
			for (int j = 0; j < MATRIX_SIZE; j++) {			
				Integer[] coordinates = { i, j };			
				tempMatrixCoordinates.put(Character.valueOf(matrix[i][j]), coordinates);			
			}
		}
		
		LOGGER.log(Level.DEBUG, tempMatrixCoordinates);
		
		return tempMatrixCoordinates;
	}
	
	private static boolean areSameCol(char[][] matrix, char firstChar, char secondChar) {
		
		boolean isFirstChar = false;
		boolean isSecondChar = false;
		
		for (int col = 0; col < MATRIX_SIZE; col++) {
			for (int row = 0; row < MATRIX_SIZE; row++) {
				if (matrix[row][col] == firstChar) {
					isFirstChar = true;
				}
				if (matrix[row][col] == secondChar) {
					isSecondChar = true;
				}
			}
			
			if (isFirstChar && isSecondChar) {
				return true;
			}		
		}
		
		return false;
	}
	
	private static boolean areSameRow(char[][] matrix, char firstChar, char secondChar) {
		
		boolean isFirstChar = false;
		boolean isSecondChar = false;
		
		for (int row = 0; row < MATRIX_SIZE; row++) {
			for (int col = 0; col < MATRIX_SIZE; col++) {
				if (matrix[row][col] == firstChar) {
					isFirstChar = true;
				}
				if (matrix[row][col] == secondChar) {
					isSecondChar = true;
				}
			}
			
			if (isFirstChar && isSecondChar) {
				return true;
			}		
		}
		
		return false;
	}
}
