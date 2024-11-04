import java.lang.System.Logger;
import java.lang.System.Logger.Level;

public abstract class RouteCipher {

	private static final Logger LOGGER = System.getLogger(RouteCipher.class.getName());
	
	public enum ROUTE { UP_TO_DOWN, DOWN_TO_UP }
	public enum ORIENTATION { LEFT_TO_RIGHT, RIGHT_TO_LEFT }
	
	private RouteCipher() {}
	
	public static void main (String[] args) {	
		var message = "ATAQUETEMPRANO";	
		System.out.println("Mensaje Original: " + message);	
		System.out.println(String.format("Ruta: %s Orientacion: %s", 
				ROUTE.DOWN_TO_UP.toString(),
				ORIENTATION.RIGHT_TO_LEFT.toString()));	
		var encryptedMessage = encrypt(
				message, 
				ROUTE.DOWN_TO_UP, 
				ORIENTATION.RIGHT_TO_LEFT, 
				4, 4);					
		System.out.println("Mensaje Encriptado: " + encryptedMessage); 	
		var desencryptedMessage = encrypt(
				encryptedMessage, 
				ROUTE.DOWN_TO_UP, 
				ORIENTATION.RIGHT_TO_LEFT, 
				4, 4);		
		System.out.println("Mensaje Desencriptado: " + desencryptedMessage);
	}
	
	public static String encrypt(String message, 
								ROUTE key, 
								ORIENTATION ori ,
								int rows,
								int cols) {	
			
		var matrix = createMatrix(message.toUpperCase().replace(" ", ""), rows, cols);
	
		if (key == ROUTE.UP_TO_DOWN && ori == ORIENTATION.LEFT_TO_RIGHT) {
			return upToDownFromLeft(matrix);
		} else if (key == ROUTE.UP_TO_DOWN && ori == ORIENTATION.RIGHT_TO_LEFT) {
			return upToDownFromRight(matrix);
		} else if (key == ROUTE.DOWN_TO_UP && ori == ORIENTATION.LEFT_TO_RIGHT) {
			return downToUpFromLeft(matrix);
		} else if (key == ROUTE.DOWN_TO_UP && ori == ORIENTATION.RIGHT_TO_LEFT) {
			return downToUpFromRight(matrix);
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public static char[][] createMatrix(String message, int rows, int cols) {	
		
		int matrixSize = rows * cols;
		
		if (message.length() > matrixSize) {
			throw new IllegalArgumentException();
		}
	
		var matrix = new char[rows][cols];
		var messageCharArray = message.toCharArray();
		var fixedMessage = new StringBuilder();
		var loggerSB = new StringBuilder();
		var lastIndex = 0;
		
		for (int i = 0; i < messageCharArray.length; i++) {			
			fixedMessage.append(messageCharArray[i]);
			++lastIndex;
		}
		
		for (int i = lastIndex; i < matrixSize; i++) {
			fixedMessage.append('?');
		}
			
		var fixedMessageCharArray = fixedMessage.toString().toCharArray();
		
        loggerSB.append("\nMatrix\n");
		for (int row = 0, i = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++, i++) {
				matrix[row][col] = fixedMessageCharArray[i];
            	loggerSB.append(matrix[row][col] + "  ");
			}
            loggerSB.append('\n');
		}
		
        LOGGER.log(Level.INFO, loggerSB);
		return matrix;
	}
	
	private static String upToDownFromLeft(char[][] matrix) {	
		
		var message = new StringBuilder();

		for (int col = 0; col < matrix[0].length; col++) {					
			for (int row = 0; row < matrix.length; row++) {
				message.append(matrix[row][col]);
			}	
		}
		
		return message.toString();
	}
	
	private static String upToDownFromRight(char[][] matrix) {	
		
		var message = new StringBuilder();

		for (int col = matrix[0].length - 1; col > -1; col--) {					
			for (int row = 0; row < matrix.length; row++) {
				message.append(matrix[row][col]);
			}	
		}
		
		return message.toString();
	}
	
	private static String downToUpFromLeft(char[][] matrix) {	
		
		var message = new StringBuilder();

		for (int col = 0; col < matrix[0].length; col++) {
			for (int row = matrix.length - 1; row > -1 ; row--) {					
				message.append(matrix[row][col]);
			}
		}
		
		return message.toString();
	}	
	
	private static String downToUpFromRight(char[][] matrix) {	
		
		var message = new StringBuilder();

		for (int col = matrix[0].length - 1; col > -1; col--) {
			for (int row = matrix.length - 1; row > -1; row--) {					
				message.append(matrix[row][col]);
			}
		}
		
		return message.toString();
	}	
}