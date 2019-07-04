
package com.etf.os2.project.scheduler;
import com.etf.os2.project.process.Pcb;

public abstract class Scheduler {
	protected static int numberOfProcessors=1;
	
	public abstract Pcb get(int cpuId);
	
	
	public abstract void put (Pcb pcb);
	
	
	
	public static Scheduler createScheduler(String[] args)
	{
		switch(args[0].toUpperCase()) {
		
		case "SJF":
			return SJF_Scheduler.create(Double.parseDouble(args[1]),Boolean.parseBoolean(args[2]));
		
		case "MFQS":
			long timeSlicesArray[]=new long[args.length-2];
			for (int i=2;i<args.length;i++) {
				timeSlicesArray[i-2]=Long.parseLong(args[i]);
			}
			return MFQS_Scheduler.create(Integer.parseInt(args[1]),timeSlicesArray);
		
		case "CFS":
			return CFS_Scheduler.create();
		
		case "CUSTOM":
			return CUSTOM_Scheduler.create();
		default: 
			System.out.println("Nepostojeci algoritam rasporedjivanja");
			return CUSTOM_Scheduler.create();
		
		}	
	}
	
	
	
	
	
}
