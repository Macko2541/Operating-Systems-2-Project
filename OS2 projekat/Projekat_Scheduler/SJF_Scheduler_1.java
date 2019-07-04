package com.etf.os2.project.scheduler;
import java.util.Iterator;
import java.util.LinkedList;

import com.etf.os2.project.process.Pcb;
import com.etf.os2.project.process.Pcb.ProcessState;
import com.etf.os2.project.process.PcbData;

import algorithm.*;


public class SJF_Scheduler extends Scheduler {
	private SJF_Algorithm algorithm;
	private double coefficient;
	private final int ageLimit=10000;
	private LinkedList<Pcb> struktura=new LinkedList<Pcb>();
	
	public SJF_Scheduler(double coefficient,SJF_Algorithm algorithm) {
		this.coefficient=coefficient;
		this.algorithm=algorithm;
	}
	
	public static SJF_Scheduler create(double coefficient,boolean preemptive) {
		if (preemptive) return new SJF_Scheduler(coefficient,new SJF_Preemptive());
		else return new SJF_Scheduler(coefficient,new SJF_NonPreemptive());
	}
	
	
	
	@Override
	public Pcb get(int cpuId) {
	if (struktura.isEmpty()) return null;
	Pcb trazeni=struktura.getFirst();

	
		for (Pcb k:struktura) {
			if (k.getPcbData().getAge()>ageLimit && k.getPcbData().getAge()>trazeni.getPcbData().getAge())
				trazeni=k;
		}
		if (trazeni.getPcbData().getAge()<ageLimit) {
			trazeni=struktura.getFirst();
			
			for (Pcb k:struktura) {
				if (k.getPcbData().getPrediction()<trazeni.getPcbData().getPrediction()) {
					trazeni=k;
				}
			}		
		}
		else {trazeni.getPcbData().setAge(0);}
		
		struktura.remove(trazeni);
		
		for(Iterator<Pcb> iterator =struktura.iterator(); iterator.hasNext();) {
			Pcb process=iterator.next();
			process.getPcbData().setAge(process.getPcbData().getAge()+1);
		}
		
		trazeni.getPcbData().setBurstStart(Pcb.getCurrentTime());
		return trazeni;		
	}
	
	

	@Override
	public void put(Pcb pcb) {
		
		if (pcb.getPreviousState()==ProcessState.CREATED) {
			pcb.setPcbData(new PcbData());
			algorithm.put(pcb,struktura);	
		}
		else {
			if (pcb.getPcbData().getPreempted()==1 && (pcb.getPcbData().getPrediction()-pcb.getExecutionTime())>0) {
				pcb.getPcbData().setPrediction(pcb.getPcbData().getPrediction()-pcb.getExecutionTime());
				algorithm.put(pcb,struktura);
			}
			else {
			pcb.getPcbData().setPrediction(((double)(pcb.getExecutionTime()*coefficient))+((1-coefficient)*pcb.getPcbData().getPrediction()));
			algorithm.put(pcb,struktura);
			}
			pcb.getPcbData().setPreempted(0);
		}
	}
	

}
