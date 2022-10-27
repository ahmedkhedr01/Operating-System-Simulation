import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
public class Scheduler extends SystemCalls {
int limit;
String Temp;
String Temp1;
int time;
Hashtable<String,String> processPlan;
ArrayList<Process> availableProcesses;
Queue <Process> Ready;
Queue<Process>blockedGen;
Mutex userInput;
Mutex file;
Mutex userOutput;


public Scheduler(int limit) {
	this.limit=limit;
	this.time=0;
	this.processPlan= new Hashtable<String,String>(); 
	this.blockedGen= new LinkedList<Process>();
	this.availableProcesses= new ArrayList<Process>();
	this.Ready= new LinkedList<Process>();
	this.userInput= new Mutex();
	this.file= new Mutex();
	this.userOutput= new Mutex();
	this.Temp="";
	this.Temp1="";
	
}
public void start() {
	while(!this.Ready.isEmpty()) {
		//
		System.out.println();
		System.out.println("Ready Queue: ");
		if(this.Ready.isEmpty())
			System.out.print("Queue is Empty");
		for(Process s:this.Ready) {
			System.out.print(s.name+", ");
		}
		System.out.println();
		System.out.println("Blocked Queue: ");
		if(this.blockedGen.isEmpty())
			System.out.print("Queue is Empty");
		for(Process s:this.blockedGen) {
			System.out.print(s.name+", ");
		}
		System.out.println();
		
		Process p = (Process) this.Ready.remove();
		p.State=State.Running;
		//Memory.memory[p.control.lowerBound+2].data=p.State;
		if((Integer)Memory.memory[0].data!=p.id && (Integer)Memory.memory[20].data!=p.id) {
		if(Memory.memory[2].data.equals(State.Finished)) {
			System.out.println("Process of id: "+Memory.memory[0].data+" swapped into the disk.");
			System.out.println("Process of id: "+p.id+" swapped out of the disk.");
			swap(0,p);
			Memory.timer0=0;
		}else if(Memory.memory[22].data.equals(State.Finished)) {
			System.out.println("Process of id: "+Memory.memory[20].data+" swapped into the disk.");
			System.out.println("Process of id: "+p.id+" swapped out of the disk.");
			swap(20,p);
			Memory.timer20=0;
		}else {
			int x=Memory.timer0;
			int y=Memory.timer20;
			if(x>y) {
				System.out.println("Process of id: "+Memory.memory[0].data+" swapped into the disk.");
				System.out.println("Process of id: "+p.id+" swapped out of the disk.");
				swap(0,p);
				Memory.timer0=0;
				
			}
			else {
				System.out.println("Process of id: "+Memory.memory[20].data+" swapped into the disk.");
				System.out.println("Process of id: "+p.id+" swapped out of the disk.");
				swap(20,p);
				Memory.timer20=0;
			}
		}
		
		
	}
		Memory.memory[p.control.lowerBound+2].data=p.State;
		//print here
		//System.out.println("at time: "+time);
		
		System.out.println();
		System.out.println(p.name+" is currently executing");
		//print currently executing
		for (int i = 0; i < limit; i++) {
			System.out.println("at time: "+time);
			String current=p.instructionList.get(0);
			System.out.println(current+" is currently executing, Instruction number: "+(p.PC+1));
			this.readInstruction(p);	
		p.instructionList.remove(0);	
			p.PC++;
			Memory.memory[p.control.lowerBound+1].data=p.PC;
			//handle assign
			if(Memory.memory[0]!=null)
			Memory.timer0++;
			if(Memory.memory[20]!=null)
			Memory.timer20++;
			if(processPlan.containsKey(time+"2")) {
				createProcess(processPlan.get(time+"2"));
				System.out.println(processPlan.get(time+"2")+" arrived.");
				System.out.println();
			}
			if(processPlan.containsKey(time+"3")) {
				createProcess(processPlan.get(time+"3"));
				System.out.println(processPlan.get(time+"3")+" arrived.");
				System.out.println();
			}
			System.out.println();
			System.out.println("Memory");
			System.out.println("index: content");
			printMem();
			time++;
			
			if(!p.state) {
				blockedGen.add(p);
				p.State=State.Blocked;
				Memory.memory[p.control.lowerBound+2].data=State.Blocked;
				//print here
				System.out.println(p.name+" is blocked");
				break;
				}
			if(p.instructionList.isEmpty()) {
				//print here
				System.out.println(p.name+" finished executing");
				Memory.memory[p.control.lowerBound+2].data=State.Finished;
				break;
				
			}
		}
		
		if(!(p.instructionList.isEmpty()) && p.state==true) {
		this.Ready.add(p);
		p.State=State.Ready;
		Memory.memory[p.control.lowerBound+2].data=State.Ready;
		}
		//updateState();
		
		
	}
}
public void swap(int i,Process p) {
	int c=1;
	String txt="";
	try {
		 txt= readFile("Disk");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	String[]temp= txt.split("\n");
	
	String start="";
	for (int j = 0; j < Memory.memory[i].name.length()-3; j++) {
		start+=Memory.memory[i].name.charAt(j);
	}
	writeFile("Disk",start);
int m =i;
for(;i<m+20;i++) {
	if(Memory.memory[i]!=null) {
	try {
		writeFile("Disk",readFile("Disk")+"\n"+Memory.memory[i].name+","+Memory.memory[i].data);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	Memory.memory[i]=null;
	}
}
int j;
if(Memory.memory[0]!=null)
 j=20;
else 
	j=0;
p.control.lowerBound=j;
p.control.upperBound=j+19;
Memory.memory[j]=new Pair(p.name+" id",p.control.id);
j++;
c++;
Memory.memory[j]=new Pair(p.name+" PC",p.control.pc);
j++;
c++;
Memory.memory[j]=new Pair(p.name+" State",p.control.State);
j++;
c++;
Memory.memory[j]=new Pair(p.name+" lower Boundaries",p.control.lowerBound);
j++;
c++;
Memory.memory[j]= new Pair(p.name+" upper Boundaries",p.control.upperBound);
j++;
c++;
 m=j;
int xx=0;
int ins=1;
for (; j < p.copy.size()+m; j++) {
	Memory.memory[j]= new Pair(p.name+" "+ins,p.copy.get(xx));
	xx++;
	c++;
	ins++;
}
for (; c < temp.length; c++) {
	String[]split=temp[c].split(",");
	Memory.memory[j]= new Pair(split[0],split[1]);
	j++;
}
}
public  ArrayList<String> createProcess(String x) {
	ArrayList <String> instructionList = new ArrayList<String>();
    String txt=".txt";
    try {
        File myObj = new File(x+txt);
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
          String data = myReader.nextLine();
          String[] split= data.split(" ");
          if(split[0].equals("assign")) {
        	  if(split[2].equals("input")) {
        		  instructionList.add(split[2]);
        		  instructionList.add(split[0]+" "+split[1]);
        	  }
        	  else {
        		  instructionList.add(split[2]+" "+split[3]);
        		  instructionList.add(split[0]+" "+split[1]);
        	  }
          }
          else {
          instructionList.add(data);
          }
        }
        myReader.close();
      } catch (FileNotFoundException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
      }
   
    int latestId;
    if(!availableProcesses.isEmpty())
     latestId=availableProcesses.get(availableProcesses.size()-1).id;
    else {
    	latestId=0;
    	}
    
    Process p=new Process(latestId+1,instructionList,x);
   PCB control=null;
    if((Memory.memory[0]==null)||(Memory.memory[20]==null)) {
    	int i;
    	if(Memory.memory[0]!=null)
    	 i=20;
    	else 
    		i=0;
    	
    	  control= new PCB(p.id,p.PC,State.Ready,i,i+19);
    	    p.control=control;
    	    
    	Memory.memory[i]=new Pair(x+" id",control.id);
    	i++;
    	Memory.memory[i]=new Pair(x+" PC",control.pc);
    	i++;
    	Memory.memory[i]=new Pair(x+" State",control.State);
    	i++;
    	Memory.memory[i]=new Pair(x+" lower Boundaries",control.lowerBound);
    	i++;
    	Memory.memory[i]= new Pair(x+" upper Boundaries",control.upperBound);
    	i++;
    	int m=i;
    	int xx=0;
    	int ins=1;
    	for (; i < instructionList.size()+m; i++) {
			Memory.memory[i]= new Pair(x+" "+ins,instructionList.get(xx));
			xx++;
			ins++;
		}
    	
    	this.Ready.add(p);
    }else {
    	int j,l;
    	if(Memory.memory[2].data.equals(State.Running)) {
    		j=20;
    	    l=40;
    	    }
    	else {
    		j=0;
    		l=20;
    	}
    	PCB control1= new PCB(p.id,p.PC,State.Ready,j,j+19);
    	p.control=control1;
    	String start="";
    	for (int k = 0; k < Memory.memory[j].name.length()-3; k++) {
    		start+=Memory.memory[j].name.charAt(k);
    	}
    	writeFile("Disk",start);
    	
    	for  (; j < l; j++) {
			if(Memory.memory[j]!=null) {
    		try {
				writeFile("Disk",readFile("Disk")+"\n"+Memory.memory[j].name+","+Memory.memory[j].data);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Memory.memory[j]=null;
			}
		}
    	int k=l-20;
    	Memory.memory[k]=new Pair(x+" id",control1.id);
    	k++;
    	Memory.memory[k]=new Pair(x+" PC",control1.pc);
    	k++;
    	Memory.memory[k]=new Pair(x+" State",control1.State);
    	k++;
    	Memory.memory[k]=new Pair(x+" lower Boundaries",control1.lowerBound);
    	k++;
    	Memory.memory[k]= new Pair(x+" upper Boundaries",control1.upperBound);
    	k++;
    	int m=k;
    	int g=0;
    	int inss=1;
    	for (; k < instructionList.size()+m; k++) {
			Memory.memory[k]= new Pair(x+" "+inss,instructionList.get(g));
			g++;
			inss++;
		}
    	this.Ready.add(p);
    }
      this.availableProcesses.add(p);
      
      
      
  return instructionList;
}
public void printMem() {
	for (int i = 0; i < Memory.memory.length; i++) {
		if(Memory.memory[i]!=null)
		System.out.println(i+": "+Memory.memory[i].name+", "+Memory.memory[i].data);
	}
}
public  void readInstruction(Process p) {
	Scanner sc= new Scanner(System.in);
	String[] split=p.instructionList.get(0).split(" ");
	switch(split[0]) {
	case("readFile"):
		String x="";
	for (int i = p.control.lowerBound; i < p.control.lowerBound+19; i++) {
		if(Memory.memory[i].name.equals(split[1]+p.id)){
			x=(String)Memory.memory[i].data;
			break;
		}
	}
		try {
			Temp1=this.readFile(x);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	break;
	case("input"):
	System.out.println("Please enter a value to assign a variable with!");
	Temp= sc.nextLine();
	break;
	case("assign"):
	this.assign(split[1]+p.id, Temp,p);
	break;
	case("print"):
		print(Temp1);
	    break;
	case("writeFile"):
		String y="";
		for (int i = p.control.lowerBound; i < p.control.lowerBound+19; i++) {
			if(Memory.memory[i].name.equals(split[1]+p.id)){
				y=(String)Memory.memory[i].data;
				break;
			}
		}
		String z="";
		for (int i = p.control.lowerBound; i < p.control.lowerBound+19; i++) {
			if(Memory.memory[i].name.equals(split[2]+p.id)){
				z=(String)Memory.memory[i].data;
				break;
			}
		}
	String fileName= y;
	String data= z;
	    this.writeFile(fileName, data);  
	    break;
	case("printFromTo"):
		String xy="";
		for (int i = p.control.lowerBound; i < p.control.lowerBound+19; i++) {
			if(Memory.memory[i]!=null && Memory.memory[i].name.equals(split[1]+p.id)){
				xy=(String)Memory.memory[i].data;
				break;
			}
		}
		String xz="";
		for (int i = p.control.lowerBound; i < p.control.lowerBound+19; i++) {
			if(Memory.memory[i]!=null && Memory.memory[i].name.equals(split[2]+p.id)){
				xz=(String)Memory.memory[i].data;
				break;
			}
		}
		printFromTo(xy,xz);
	break;
	case ("semWait"):
		if(split[1].equals("userInput")) {
			this.userInput.semwait(p);
			
		}
		else if(split[1].equals("userOutput")) {
			this.userOutput.semwait(p);
		}
		else {
			this.file.semwait(p);
		}
	break;
	case("semSignal"):
		if(split[1].equals("userInput")) {
       Process q= this.userInput.semsignal(p);
        if(q!=null) {
        	this.Ready.add(q);
        	this.blockedGen.remove(q);
        }
		}
		else if(split[1].equals("userOutput")) {
			Process q=this.userOutput.semsignal(p);
			if(q!=null) {
	        	this.Ready.add(q);
	        	this.blockedGen.remove(q);
	        	//Memory.memory[q.control.lowerBound+2].data=State.Ready;
	        }
		}
		else {
			Process q=this.file.semsignal(p);
			if(q!=null) {
	        	this.Ready.add(q);
	        	this.blockedGen.remove(q);
	        	//Memory.memory[q.control.lowerBound+2].data=State.Ready;
	        }
		}
	
	}
}
public void updateState() {
	int id1=(Integer)Memory.memory[0].data;
	int id2=0;
	if(Memory.memory[20]!=null) {
		id2=(Integer)Memory.memory[20].data;
	}
	Process p1= null;
	Process p2=null;
	for (int i = 0; i < availableProcesses.size(); i++) {
		if(availableProcesses.get(i).id==id1) {
			p1=availableProcesses.get(i);
		}else if(availableProcesses.get(i).id==id2){
			p2=availableProcesses.get(i);
		}
	}
	if((Integer)Memory.memory[0].data==p1.id) {
		Memory.memory[2].data=p1.State;
	}
	if(p2!=null) {
		if((Integer)Memory.memory[0].data==p2.id) {
			Memory.memory[2].data=p2.State;
		}
	}
	if(Memory.memory[20]!=null) {
		if((Integer)Memory.memory[20].data==p1.id) {
			Memory.memory[22].data=p1.State;
		}
		if((Integer)Memory.memory[20].data==p2.id) {
			Memory.memory[22].data=p2.State;
		}
	}
}

}
