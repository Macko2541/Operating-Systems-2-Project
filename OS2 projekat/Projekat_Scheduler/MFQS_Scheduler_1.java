package com.etf.os2.project.scheduler;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import com.etf.os2.project.process.Pcb;
import com.etf.os2.project.process.Pcb.ProcessState;
import com.etf.os2.project.process.PcbData;



public class MFQS_Scheduler extends Scheduler{
	private int numOfQueues;
	private final int ageLimit=10000;
	private Vector<LinkedList<Pcb>> queues=new Vector<LinkedList<Pcb>>();
	private long timeSlices[];
	
	MFQS_Scheduler(int numOfQueues,long timeSlices[]){
		this.numOfQueues=numOfQueues;
		for (int i=0;i<numOfQueues;i++) {
			queues.add(new LinkedList<Pcb>());
		}	
		this.timeSlices=timeSlices;
	}
	
	public static MFQS_Scheduler create(int numOfQueues,long... timeSlice) {
		if (numOfQueues==timeSlice.length)
		return new MFQS_Scheduler(numOfQueues,timeSlice);
		else {
			System.out.println("Nepravilno uneseni parametri za MFQS");
			long defaultTimeSlices[]= {4,8,12};
			return new MFQS_Scheduler(3,defaultTimeSlices);
		}
		
	}
		
	@Override
	public Pcb get(int cpuId) {
		Pcb pcb=null;
		for (int i=0;i<numOfQueues;i++) {
			if (!(queues.get(i).isEmpty())) {
				pcb = queues.get(i).remove();
				break;
			}//pazi koji izvlacis ovo izvlaci prvi
		}
		if (pcb==null) return null;
		else {	//aging
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
				/*for (Pcb p:queues.get(i)) {
					p.getPcbData().setAge(p.getPcbData().getAge()+1);
					if (p.getPcbData().getAge()==ageLimit) { // if process became too old
						p.getPcbData().setAge(0);
						queues.get(i).remove(p);
						queues.get(i-1).add(p);
					}*/
				}
			}
			
			
		
		return pcb;
	}

	@Override
	public void put(Pcb pcb) {
		
		if (pcb.getPreviousState()==ProcessState.CREATED) {
			if (pcb.getPriority()<numOfQueues)
				pcb.setPcbData(new PcbData(pcb.getPriority()));
			else 
				pcb.setPcbData(new PcbData(numOfQueues-1));
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
