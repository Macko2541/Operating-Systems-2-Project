package com.etf.os2.project;


import com.etf.os2.project.process.Process;
import com.etf.os2.project.process.RandomGenerator;
import com.etf.os2.project.scheduler.Scheduler;
import com.etf.os2.project.system.System;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
	public static double totalResponseTime=0;
	public static double totalExecutionTime=0;
	
    public static void main(String[] args) {
        if (args.length < 2) {
            java.lang.System.err.println("Invalid arguments\n\nUsage:\n" +
                    "\t<NUM_CPUS> <NUM_PROCESSES> [<scheduler_args>]");
            java.lang.System.exit(-1);
        }

        int numCpus = Integer.parseInt(args[0]);
        int numProcesses = Integer.parseInt(args[1]);

        int processType = 0, processLength = 0;
        List<Process> processes = new ArrayList<Process>();
        for (int i = 1; i <= numProcesses; i++) {
            Process process = new Process(i, i % 5,
                    Process.ProcessType.values()[processType],
                    Process.ProcessLength.values()[processLength]);   //ovde se podesava prioritet
            processes.add(process);

            processType = (processType + 1) % Process.ProcessType.values().length;
            processLength = (processLength + 1) % Process.ProcessLength.values().length;
        }

        String[] schedArgs = new String[args.length - 2];
        java.lang.System.arraycopy(args, 2, schedArgs, 0, schedArgs.length);
        Scheduler scheduler = Scheduler.createScheduler(schedArgs);
        System system = new System(scheduler, numCpus, processes);

        system.work();
        java.lang.System.out.println("Average execution: "+totalExecutionTime/numProcesses);
        java.lang.System.out.println("Average response: "+totalResponseTime/numProcesses);
    }
}
