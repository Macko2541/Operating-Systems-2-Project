package com.etf.os2.project.scheduler;
import java.util.LinkedList;

import com.etf.os2.project.process.Pcb;
import com.etf.os2.project.process.Pcb.ProcessState;
import com.etf.os2.project.process.PcbData;


public class CFS_Scheduler extends Scheduler {

	private final long DEFAULT_TIME_SLICE=2;
	private LinkedList<Pcb> struktura= new LinkedList<Pcb>();
	public static CFS_Scheduler create() {
		return new CFS_Scheduler();
	}
	
	
	
	@Override
	public Pcb get(int cpuId) {
		if (struktura.isEmpty())return null;
		Pcb minPcb=struktura.getFirst();
		
		for (Pcb p:struktura) {
			if (p.getPcbData().getExTime()<minPcb.getPcbData().getExTime())
				minPcb=p;
		}
		struktura.remove(minPcb);
		long numOfProcess=(long)Pcb.getProcessCount();
		/*if (numOfProcess<=0) numOfProcess=1;
		/*minPcb.setTimeslice((Pcb.getCurrentTime()-minPcb.getPcbData().getBurstStart()-minPcb.getPcbData().getExTime())/numOfProcess);*/
		minPcb.setTimeslice((Pcb.getCurrentTime()-minPcb.getPcbData().getBurstStart())/numOfProcess);
		if (minPcb.getTimeslice()<=0) {
			minPcb.setTimeslice(DEFAULT_TIME_SLICE);
		}
		return minPcb;
	}

	
	
	@Override
	public void put(Pcb pcb) {
		if (pcb.getPreviousState()==ProcessState.CREATED) {
			pcb.setPcbData(new PcbData());
			/*pcb.getPcbData().setBurstStart(Pcb.getCurrentTime());*/
			pcb.getPcbData().setExTime(0);
			
		}
		else
		if (pcb.getPreviousState()==ProcessState.BLOCKED) {
			/*pcb.getPcbData().setBurstStart(Pcb.getCurrentTime());*/
			pcb.getPcbData().setExTime(0);
			
		}
		else 
		if(pcb.getPreviousState()==ProcessState.RUNNING){
			pcb.getPcbData().setExTime(pcb.getPcbData().getExTime()+pcb.getExecutionTime());		
		}
		pcb.getPcbData().setBurstStart(Pcb.getCurrentTime());
		struktura.add(pcb);
	}

	

}
