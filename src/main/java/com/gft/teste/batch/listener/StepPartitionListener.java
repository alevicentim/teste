package com.gft.teste.batch.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

import lombok.extern.slf4j.Slf4j;
 
@Slf4j
public class StepPartitionListener implements StepExecutionListener {
 
    @Override
    public void beforeStep(StepExecution stepExecution) {
    	// Nothing to do here
    }
 
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
		log.info("Step: [{}] {} total items processed from file {}", stepExecution.getStepName(), stepExecution.getReadCount(), stepExecution.getExecutionContext().getString("fileName"));
        return ExitStatus.COMPLETED;
    }
}
