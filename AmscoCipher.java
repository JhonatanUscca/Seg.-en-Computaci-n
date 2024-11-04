import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

public abstract class AmscoCipher {

	private static final Logger LOGGER = System.getLogger(AmscoCipher.class.getName());
	
	private AmscoCipher() {}
		
	public static void main (String[] args) {		
		var message = "ESTOYSEGURO";	
		var key = new ArrayList<Integer>();
		key.add(3);
		key.add(2);
		key.add(1);
		
		System.out.println("Mensaje Original: " + message);	
		System.out.println("LLave: " + key.toString());	
		
		var encryptedMessage = encrypt(message, key);
		
		System.out.println("Mensaje Encriptado: " + encryptedMessage);
		System.out.println("Mensaje Desencriptado: " + decrypt(encryptedMessage, key));
	}
	
    public static String encrypt(String message, List<Integer> key) {     	
    	checkKey(key);
        var blocks = splitIntoAmscoBlocks(message);
        var matrix = createMatrixForEncrypting(key, blocks, key.size());           
        var encryptedText = new StringBuilder();   	
        var keysHashMap = keyPerCol(key, matrix[0].length);
        var ordenedKeysList = new ArrayList<>(keysHashMap.keySet());        
        Collections.sort(ordenedKeysList);     
        int colPerKey;
        
        /*Lee por columna y en base a la llave para encriptar el mensaje*/
        for (final var k : ordenedKeysList) {
        	colPerKey = keysHashMap.get(k);
            for (int row = 0; row < matrix.length; row++) {
                encryptedText.append(matrix[row][colPerKey]);
            }
        }
   
        return encryptedText.toString();     
    }
    
    public static String decrypt(String message, List<Integer> key) {	
    	checkKey(key);
        var blocks = splitIntoAmscoBlocks(message);
        var matrix = createMatrixForDecrypting(key, blocks, key.size());    
    	var decryptedMessage = new StringBuilder();
        
        /*createMatrixForDecrypting desencripta la matriz y lo regresa a su forma inicial
         * con el mensaje inicial, solo queda leer fila por fila*/
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[0].length; col++) {
            	decryptedMessage.append(matrix[row][col]);
            }
        }
            
        return decryptedMessage.toString();  
    }
    
    private static List<String> splitIntoAmscoBlocks(String message) {
        List<String> blocks = new ArrayList<>();
        boolean singleChar = true;
        int blockSize;
        
        int i = 0;

        while (i < message.length()) {
            blockSize = singleChar ? 1 : 2;
            
            if (i + blockSize > message.length()) {
            	blocks.add(message.substring(message.length() - 1, message.length()) + "?");
            	break;
            }
            else {
                blocks.add(message.substring(i, i + blockSize));
                i += blockSize;
                singleChar = !singleChar; 
            }
            
        }
       
        return blocks;
    }
       
    /*Construye la matriz y ubica los bloques de forma vertical*/
    private static String[][] createMatrixForEncrypting(List<Integer> key, List<String> blocks, int columns) {
        int rows = (int) Math.ceil((double) blocks.size() / columns);
        String[][] matrix = new String[rows][columns];
        int blockIndex = 0;
        var loggerSB = new StringBuilder(); //Logger      
        LOGGER.log(Level.DEBUG, String.format("Rows: %d Cols: %d", rows, columns));
        loggerSB.append("\nMatrix\n");
    	key.stream().forEach(k -> loggerSB.append(k + "  "));
    	loggerSB.append('\n'); 	
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                if (blockIndex < blocks.size()) {
                    matrix[row][col] = blocks.get(blockIndex);
                } else {
                    matrix[row][col] = "?"; 
                }    
            	loggerSB.append(matrix[row][col] + "  ");
                ++blockIndex;
            }
            loggerSB.append('\n');
        }            
        LOGGER.log(Level.INFO, loggerSB); 
        return matrix;
    }
     
    /*Construye la matriz y ubica los bloques de forma horizontal en base a la llave*/
    private static String[][] createMatrixForDecrypting(List<Integer> key, List<String> blocks, int columns) {
        int rows = (int) Math.ceil((double) blocks.size() / columns);          
        String[][] matrix = new String[rows][columns];
        int blockIndex = 0;
        var keysHashMap = keyPerCol(key, columns);
        var ordenedKeysList = new ArrayList<>(keysHashMap.keySet());        
        Collections.sort(ordenedKeysList);   
        int colPerKey;
        var loggerSB = new StringBuilder(); //Logger           
        LOGGER.log(Level.DEBUG, String.format("Rows: %d Cols: %d", rows, columns));      	
        for (final var k : ordenedKeysList) {
        	colPerKey = keysHashMap.get(k);
            for (int row = 0; row < rows; row++) {
                if (blockIndex < blocks.size()) {
                    matrix[row][colPerKey] = blocks.get(blockIndex);
                } else {
                    matrix[row][colPerKey] = "?"; 
                }             	   
                ++blockIndex;
            }            
        }

        loggerSB.append("\nMatrix\n");
    	key.stream().forEach(k -> loggerSB.append(k + "  "));
    	loggerSB.append('\n'); 
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[0].length; col++) {
            	loggerSB.append(matrix[row][col] + "  ");
            }
            loggerSB.append('\n');
        }
             
        LOGGER.log(Level.INFO, loggerSB); 
        
        return matrix;
    }
          
    private static LinkedHashMap<Integer, Integer> keyPerCol(List<Integer> key, int cols) {	
    	var keyPerColHash = new LinkedHashMap<Integer, Integer>();
    	
    	for (int col = 0; col < cols; col++) {
    		keyPerColHash.put(key.get(col), col);
    	}
    	
    	return keyPerColHash;
    }
    
    private static void checkKey(List<Integer> key) {     
    	
    	if (key.isEmpty()) {
    		throw new IllegalArgumentException();
    	}
    	 	
    	/*LinkedHashSet<Integer> garantiza que ninguna llave de columna se repita*/
    	var uniquekeysLinkedHashSet = new LinkedHashSet<Integer>();  	  	 	
    	List<Integer> tempKey = new ArrayList<>();
    			   	 	
    	key.stream().forEach(tempKey::add); 	
    	tempKey.stream().forEach(uniquekeysLinkedHashSet::add);
       	   	
    	if (uniquekeysLinkedHashSet.size() != tempKey.size()) {
    		throw new IllegalArgumentException();
    	} 		      		   	 
    }
}