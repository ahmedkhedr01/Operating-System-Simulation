
import java.util.*;
public class Controller {
public static void main(String[] args) {
	//int id=1;
	Scanner sc= new Scanner(System.in);
	Scanner ss= new Scanner(System.in);
	Scanner sss= new Scanner(System.in);
	System.out.println("Please enter the Quantum limit of instructions!");
	int limit=sc.nextInt();
	System.out.println("Please enter file name to start the Execution with. (without .txt)");
	String x= ss.nextLine();
	System.out.println("Please enter the second file name. (without .txt)");
	String y= ss.nextLine();
	System.out.println("Please enter the arrival time of the second file");
	String arrive=ss.nextLine();
	System.out.println("Please enter the third file name. (without .txt)");
	String z= sss.nextLine();
	System.out.println("Please enter the arrival time of the third file");
	String arrive1= sss.nextLine();
	Scheduler s= new Scheduler(limit);
	s.processPlan.put(arrive+2, y);
	s.processPlan.put(arrive1+3, z);
	ArrayList<String> Instructionlist = s.createProcess(x);
	s.start();
	//Process p1= new Process(id,Instructionlist);
	//id++;
	//s.Ready.add(p1);
}
}
