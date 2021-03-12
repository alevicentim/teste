package com.gft.teste.batch.listener;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChunkCountListener implements ChunkListener{
	
	private int loggingInterval;
	
	public ChunkCountListener(int chunkSize) {
		this.loggingInterval = chunkSize * 4;
	}
		
	@Override
	public void beforeChunk(ChunkContext context) {
		// Nothing to do here
	}

	@Override
	public void afterChunk(ChunkContext context) {
		int count = context.getStepContext().getStepExecution().getReadCount();
		String fileName = context.getStepContext().getStepExecution().getExecutionContext().getString("fileName");
		int itemReaderReadCount = context.getStepContext().getStepExecution().getExecutionContext().getInt("inventoryItemReader-" + fileName + ".read.count");
		
		if (count > 0 && count % loggingInterval == 0 && count == itemReaderReadCount) {
			log.info("Step: [{}] {} items processed from file {}", context.getStepContext().getStepExecution().getStepName(), count, fileName);
		}
	}
	
	@Override
	public void afterChunkError(ChunkContext context) {
		// Nothing to do here		
	}
	
}
