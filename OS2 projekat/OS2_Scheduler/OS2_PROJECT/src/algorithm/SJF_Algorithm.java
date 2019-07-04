package algorithm;

import java.util.LinkedList;

import com.etf.os2.project.process.Pcb;



public abstract class SJF_Algorithm{
	public abstract void put(Pcb p, LinkedList<Pcb> list);
}
