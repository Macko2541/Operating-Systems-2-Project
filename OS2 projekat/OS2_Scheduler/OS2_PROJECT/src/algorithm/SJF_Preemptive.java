package algorithm;

import java.util.LinkedList;

import com.etf.os2.project.process.Pcb;
import com.etf.os2.project.scheduler.SJF_Scheduler;



public class SJF_Preemptive extends SJF_Algorithm {

	@Override
	public void put(Pcb p, LinkedList<Pcb> list) {
		list.add(p);
		for (int i=0;i<Pcb.RUNNING.length;i++) {
			if(Pcb.RUNNING[i]!=Pcb.IDLE) {  //null
			if ((p.getPcbData().getPrediction())<(Pcb.RUNNING[i].getPcbData().getPrediction()-(Pcb.getCurrentTime()-Pcb.RUNNING[i].getPcbData().getBurstStart()) ) )  //pod uslovom da getExecution time vraca i vreme dok proces radi
			{
				Pcb.RUNNING[i].preempt();
				Pcb.RUNNING[i].getPcbData().setPreempted(1);
				return;
			}
			}
		}
		
		
		// TODO Auto-generated method stub		
	}

	

}
