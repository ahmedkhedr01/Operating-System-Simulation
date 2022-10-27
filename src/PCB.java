
public class PCB {
int id;
int pc;
State State;
int lowerBound;
int upperBound;
public PCB(int id,int pc,State State, int lb,int ub) {
	this.id=id;
	this.pc=pc;
	this.State=State;
	this.lowerBound=lb;
	this.upperBound=ub;
	
}
}
