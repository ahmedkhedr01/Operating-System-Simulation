import java.io.BufferedReader;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
public class SystemCalls {
	
	Hashtable<String,Object> disk;
	
	public SystemCalls() {
		
		this.disk= new Hashtable<String,Object>();
	}
	  
	  public  void print (Object x) {
          
		  if (x.getClass() == Integer.class) {
	            System.out.println((int)x);
	        } else if (x.getClass() == String.class) {
	            System.out.println((String)x);
	        
          }
		  }
	

public  void assign(String x, Object y,Process p) {
  //this.disk.put(x, y);
  for (int i = p.control.lowerBound; i < p.control.lowerBound+19; i++) {
	if(Memory.memory[i]==null) {
		Memory.memory[i]=new Pair(x,y);
		break;
	}
}
     }
public static void printFromTo(String x, String y) {
	int first = Integer.parseInt(x);
	int last = Integer.parseInt(y);
	if(first<last) {
		while(first<=last) {
			if(first==last) {
				System.out.print(first);
				break;
			}
			System.out.print(first+", ");
			first++;
		}
	}
	else {
		while(last<=first) {
			if(first==last) {
				System.out.print(first);
				break;
			}
			System.out.print(last+", ");
			last++;
	}
}
	System.out.println();
}
public  String readFile(String fileName ) throws IOException {
    String filePath = fileName;

    String txt= ".txt";
    BufferedReader reader = new BufferedReader(new FileReader(filePath+txt));
    StringBuilder stringBuilder = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null) {
        stringBuilder.append(line);
        stringBuilder.append("\n");
    }

    // delete the last new line separator
    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
    reader.close();

    return stringBuilder.toString();
}
public  void writeFile(String fileName, Object data) {
    String file=fileName;
    String txt=".txt";
    Object dataWrite=data;
      try {
          File myObj = new File(file+txt);
          if (myObj.createNewFile()) {
            System.out.println("File created: " + myObj.getName());
          } else {
        	  if(!fileName.equals("Disk"))
            System.out.println("File already exists.");
          }
          FileWriter myWriter = new FileWriter(file+txt);
          myWriter.write((String)dataWrite);
          myWriter.close();
          if(!fileName.equals("Disk"))
          System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
          System.out.println("An error occurred.");
          e.printStackTrace();
        }
}

}


