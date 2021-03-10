package com.gft.teste.batch.configuration;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.MultiResourcePartitioner;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.gft.teste.batch.mapper.InventoryJsonLineMapper;
import com.gft.teste.batch.processor.FilterDuplicatedInSameFileItemProcessor;
import com.gft.teste.model.Inventory;
import com.gft.teste.repository.InventoryRepository;

@Configuration
public class JobConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	public EntityManagerFactory entityManagerFactory;
	
	@Autowired
	public InventoryRepository inventoryRepository;
	
	@Value("file:${com.gft.teste.input.files.location}${com.gft.teste.input.file.pattern}")
	private Resource[] inputFiles;
	
	
	@Bean
	public Partitioner partitioner() {
		MultiResourcePartitioner partitioner = new MultiResourcePartitioner();
		partitioner.setResources(inputFiles);
		return partitioner;
	}
	
	@Bean
    @StepScope
	public FlatFileItemReader<Inventory> inventoryItemReader(@Value("#{stepExecutionContext['fileName']}") String fileName) throws MalformedURLException {
    	return new FlatFileItemReaderBuilder<Inventory>().name("inventoryItemReader-" + fileName)
    													 .resource(new UrlResource(fileName))
    													 .lineMapper(new InventoryJsonLineMapper(fileName))
    													 .build();
    }
    
    @Bean
	public FilterDuplicatedInSameFileItemProcessor filterDuplicatedItemProcessor() throws IOException {
    	List<String> inputFilesName = new ArrayList<>();
    	if (inputFiles != null && inputFiles.length > 0) {
			for (int i = 0; i < inputFiles.length; i++) {
				inputFilesName.add(inputFiles[i].getURL().toExternalForm());
			}
		}
		return new FilterDuplicatedInSameFileItemProcessor(inventoryRepository, inputFilesName);
	}
    
    @Bean
	@StepScope
	public JpaItemWriter<Inventory> inventoryItemWriter() {
    	JpaItemWriter<Inventory> itemWriter = new JpaItemWriter<>();
    	itemWriter.setEntityManagerFactory(entityManagerFactory);
    	
    	return itemWriter;
	}
    
    @Bean
	public Step readAndSaveDataSlaveStep() throws IOException {
    	return stepBuilderFactory.get("readAndSaveDataSlaveStep")
				.<Inventory, Inventory>chunk(750)
				.reader(inventoryItemReader(null))
				.processor(filterDuplicatedItemProcessor())
				.writer(inventoryItemWriter())
				.build();
	}
    
    @Bean
	public ThreadPoolTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setMaxPoolSize(4);
		taskExecutor.setCorePoolSize(4);
		taskExecutor.setQueueCapacity(4);
		taskExecutor.afterPropertiesSet();
		return taskExecutor;
	}
    
    @Bean
	public Step readAndSaveDataMasterStep() throws IOException {
    	return stepBuilderFactory.get("readAndSaveDataMasterStep")
				.partitioner("readAndSaveDataSlaveStep", partitioner())
				.step(readAndSaveDataSlaveStep())
				.gridSize(4)
				.taskExecutor(taskExecutor())
				.build();
	}
	
	@Bean
    public Job loadInventoryFromFileToDatabaseJob() throws IOException {
    	return jobBuilderFactory.get("loadInventoryFromFileToDatabaseJob")
                .start(readAndSaveDataMasterStep())
                .build();
    }
	
}
