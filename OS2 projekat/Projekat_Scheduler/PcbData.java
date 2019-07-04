package com.etf.os2.project.process;

public class PcbData {
	private final double FIRST_PREDICTION=10;
	private int priority;
	private int age=0;
	private double prediction=FIRST_PREDICTION;
	private long exTime=0;
	private long burstStart=0;	
	private int processorId=0;
	private int preempted=0;
	
	public int getPreempted() {
		return preempted;
	}

	public void setPreempted(int preempted) {
		this.preempted = preempted;
	}

	public int getProcessorId() {
		return processorId;
	}

	public void setProcessorId(int processorId) {
		this.processorId = processorId;
	}

	public PcbData(int priority) {
		this.priority=priority;
	}
	
	public PcbData() {
		
		
	}
	
	public long getExTime() {
		return exTime;
	}

	public void setExTime(long exTime) {
		this.exTime = exTime;
	}

	public void setBurstStart(long burstS) {
		burstStart=burstS;
	}

	
	public long getBurstStart(){
		return burstStart;
	}
	
	public void setAge(int age) {
		this.age=age;
	}
	
	public int getAge() {
		return age;
	}
	
	public void setPriority(int priority) {
		this.priority=priority;
	}
	
	public int getPriority() {
		return priority;
	}

	public double getPrediction() {
		return prediction;
	}

	public void setPrediction(double prediction) {
		this.prediction = prediction;
	}
}
