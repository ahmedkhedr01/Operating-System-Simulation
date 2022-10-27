import java.util.*;

public class Mutex {
boolean available = true;
Queue <Process> blocked;
int ownerId;
public Mutex() {
	this.blocked= new LinkedList<Process>();
}
public  void semwait(Process p){
	if (this.available) {
		this.available=false;
		ownerId=p.id;
		
	}
	else {
		blocked.add(p);
		p.state=false;
		p.State=State.Blocked;
		//Memory.memory[p.control.lowerBound+2].data=State.Blocked;
	}
}
public  Process semsignal(Process p) {
	Process returnP = null;
	if(ownerId==p.id) {
		if(blocked.isEmpty()) {
			this.available=true;
		}
		else {
			 returnP=blocked.remove();
			p.state=true;
			returnP.state=true;
			returnP.State=State.Ready;
			//Memory.memory[returnP.control.lowerBound+2].data=State.Ready;
			// and put it in Ready list
			ownerId=returnP.id;
		}
		
	}
	return returnP;
}
}
