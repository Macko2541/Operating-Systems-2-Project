package com.etf.os2.project.scheduler;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import com.etf.os2.project.process.Pcb;
import com.etf.os2.project.process.PcbData;
import com.etf.os2.project.process.Pcb.ProcessState;

public class CUSTOM_Scheduler extends Scheduler {
	private int numOfQueues=5;
	private final int ageLimit=10000;
	private Vector<Vector<Pcb>> queues=new Vector<Vector<Pcb>>();
	private long timeSlices[]= {4,8,12,16,20};
	
	
	public CUSTOM_Scheduler() {
		for (int i=0;i<numOfQueues;i++) {
			queues.add(new Vector<Pcb>());
		}
	}
	public static Scheduler create(){
		return new CUSTOM_Scheduler();
	}
	
	@Override	
	public Pcb get(int cpuId) {
		// afinity is more important than queue priority.
		for (int i=0;i<numOfQueues;i++) {
			for (Iterator<Pcb> iter=queues.get(i).iterator();iter.hasNext();) {
				Pcb tek=iter.next();
				if(tek.getAffinity()==cpuId) {
					iter.remove();
					aging();
					return tek;
				}
			}
		}
		
		Pcb pcb=null;
		for (int i=0;i<numOfQueues;i++) {
			if (!(queues.get(i).isEmpty())) {
				pcb = queues.get(i).firstElement();
				queues.get(i).remove(0);
				aging();
				return pcb;
			}//pazi koji izvlacis ovo izvlaci prvi
		}
				
		return null;	
	}

	//NISU TI INICIAJALIZOVANI VEKTORI MOZDA TREBA!
	private void aging() {
		for (int i=1;i<numOfQueues;i++) {
			for(Iterator<Pcb> iterator = queues.get(i).iterator(); iterator.hasNext();) {
				Pcb process=iterator.next();
				process.getPcbData().setAge(process.getPcbData().getAge()+1);
				if (process.getPcbData().getAge()==ageLimit) {
					process.getPcbData().setAge(0);
					queues.get(i-1).add(process);
					iterator.remove();
				}
			}
		}
	}
	@Override
	public void put(Pcb pcb) {
		if (pcb.getPreviousState()==ProcessState.CREATED) {
			if (pcb.getPriority()<4)
				pcb.setPcbData(new PcbData(pcb.getPriority()));
			else 
				pcb.setPcbData(new PcbData(4));
			(queues.get(pcb.getPcbData().getPriority())).add(pcb);
		}
		else if(pcb.getPreviousState()==ProcessState.BLOCKED) {
			if (pcb.getPcbData().getPriority()>0) {
			pcb.getPcbData().setPriority(pcb.getPcbData().getPriority()-1);
			}
			queues.get(pcb.getPcbData().getPriority()).add(pcb);
		}
		else if(pcb.getPreviousState()==ProcessState.RUNNING) {
			if (pcb.getPcbData().getPriority()<numOfQueues-1) {
			pcb.getPcbData().setPriority(pcb.getPcbData().getPriority()+1);
			}
			queues.get(pcb.getPcbData().getPriority()).add(pcb);
		}
		pcb.setTimeslice(timeSlices[pcb.getPcbData().getPriority()]);
		
	}
	
	
	
	

}
