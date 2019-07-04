package algorithm;

import java.util.LinkedList;

import com.etf.os2.project.process.Pcb;




public class SJF_NonPreemptive extends SJF_Algorithm {

	@Override
	public void put(Pcb p,LinkedList<Pcb> list) {
		list.add(p);
	}

}
