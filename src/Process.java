import java.util.ArrayList;

public class Process {
int id;
String name;
ArrayList<String> instructionList;
ArrayList<String> copy;
int PC;
boolean state;
PCB control;
State State;
public Process(int id,ArrayList<String> instructionList,String name) {
	this.id=id;
	this.instructionList=instructionList;
	//handle states and PC in all methods
	this.state=true;
	this.PC=0;
	this.name=name;
	this.control=null;
	this.copy= instructionList;
}

}
