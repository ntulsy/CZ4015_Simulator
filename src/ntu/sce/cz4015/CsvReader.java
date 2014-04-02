package ntu.sce.cz4015;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class CsvReader {
	private Scanner scanner;
	public CsvReader(String filename) throws FileNotFoundException {
		scanner = new Scanner(new File(filename));
        scanner.useDelimiter(",");
	}
	
	public String[] readOneLine(){
		return scanner.nextLine().split(",");
	}
	
	public boolean hasNext(){
		return scanner.hasNext();
	}
}
