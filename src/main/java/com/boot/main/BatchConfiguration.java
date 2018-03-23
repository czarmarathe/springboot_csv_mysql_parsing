package com.boot.main;

import java.time.Instant;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.boot.main.formatter.CsvHeaderWriter;
import com.boot.main.model.*;

@Configuration

@EnableBatchProcessing

public class BatchConfiguration {
	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	public DataSource dataSource;
	
	@Bean
	public DataSource dataSource() {
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		
		// Configure database settings here
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost/testpeople");
		dataSource.setUsername("root");
		dataSource.setPassword("root");
		
		return dataSource;
	
	}
	
	@Bean
	public FlatFileItemReader<People> reader() {
		
		FlatFileItemReader<People> reader = new FlatFileItemReader<People>();
		
		// Name of source csv file . Relative path is src/main/resources
		reader.setResource(new ClassPathResource("testData.csv"));
		
		//Setting token names for the values extracted from csv
		reader.setLineMapper(new DefaultLineMapper<People>() {{
			setLineTokenizer(new DelimitedLineTokenizer() {{
				setNames(new String[] {"name","image","b1","location"});
			}});
			setFieldSetMapper(new BeanWrapperFieldSetMapper<People>() {{
				setTargetType(People.class);
			}});
		}});
		
		return reader;
		
	}
	
	
	@Bean
	public PeopleItemProcessor processor() {
		return new PeopleItemProcessor();
	}
	
	@Bean
	public JdbcBatchItemWriter<People> writer() {
		JdbcBatchItemWriter<People> writer = new JdbcBatchItemWriter<People>();
		
		// Update MySQL database using insert statements and object fields (starting with :)
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<People>());
		writer.setSql("Insert Into People (name,image,b1,location) "
				+ "VALUES (:name,:blob_image,:b1_out,:location)");
		writer.setDataSource(dataSource);
		
		return writer;
	}
	
	
	// First step reads data from CSV and inserts it into MySQL database
	@Bean
	public Step step() {
		
		return stepBuilderFactory.get("step").<People,People> chunk(3).reader(reader())
				.processor(processor())
				.writer(writer())
				.build();
	}
	
	@Bean
	public BadDataProcessor processor2() {
		return new BadDataProcessor();
	}
	
	@Bean
	public FlatFileItemWriter<People> writer2() {
		FlatFileItemWriter<People> writer = new FlatFileItemWriter<People>();
		
		// Assign Header Information for the Flat File
		String header = "Name,Image,Bool,Location";
		CsvHeaderWriter headerWriter = new CsvHeaderWriter(header);
		writer.setHeaderCallback(headerWriter);
		
		// Retrieve Timestamp in epoch notation and assign file name
		Instant instant = Instant.now();
		long ts = instant.toEpochMilli();
		String csv_res = "bad-data-"+ String.valueOf(ts)+".csv";
		
		//Relative Path to Project
		String path = "src/main/resources/csv/";
		writer.setResource(new FileSystemResource(path + csv_res));
		
		DelimitedLineAggregator<People> delimitedLineAggregator = new DelimitedLineAggregator<People>();
		delimitedLineAggregator.setDelimiter(",");
		
		// Extracts values from People class object fields
		BeanWrapperFieldExtractor<People> fieldExtractor = new BeanWrapperFieldExtractor<People>();
		fieldExtractor.setNames(new String[] {"name","image","b1","location"});
		delimitedLineAggregator.setFieldExtractor(fieldExtractor);
		writer.setLineAggregator(delimitedLineAggregator);
		
		return writer;
	}
	
	
	// Second step reads data from CSV and filters out incorrect information to another CSV file
	@Bean
	public Step step2() {
		
		return stepBuilderFactory.get("step2").<People,People> chunk(3).reader(reader())
				.processor(processor2())
				.writer(writer2())
				.build();
	}
	
		
	@Bean
	public Job importUserJob() { 
		
		FlowBuilder<Flow> flowBuilder = new FlowBuilder<Flow>("flow");
		Flow flow = flowBuilder
					.start(step())
					.next(step2())
					.end();
		
		return jobBuilderFactory.get("importUserJob")
				.incrementer(new RunIdIncrementer())
				.start(flow)
				.end()
				.build();
	}
	
}
