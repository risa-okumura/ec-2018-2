//package jp.co.rakus;
//
//
//import javax.sql.DataSource;
//
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.JobExecutionListener;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
//import org.springframework.batch.item.database.JdbcBatchItemWriter;
//import org.springframework.batch.item.file.FlatFileItemReader;
//import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
//import org.springframework.batch.item.file.mapping.DefaultLineMapper;
//import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import jp.co.rakus.ec2018c.domain.Item;
//
//@Configuration
//@EnableBatchProcessing
//public class Batch {
//	
//	@Autowired
//	public JobBuilderFactory jobBuilderFactory;
//	
//	@Autowired
//	public StepBuilderFactory stepBuilderFactory;
//	
//	@Autowired
//	public DataSource dateSource;
//	
//	@Bean
//	public FlatFileItemReader<Item> reader(){
//		
//		FlatFileItemReader<Item> reader = new FlatFileItemReader<Item>();
//		reader.setResource(new ClassPathResource("items.csv"));
//		reader.setLineMapper(new DefaultLineMapper<Item>() {{
//			setLineTokenizer(new DelimitedLineTokenizer() {{
//				setNames(new String[] {"id","name","description","price_m","price_l","image_path","deleted"});
//			}});
//			setFieldSetMapper(new BeanWrapperFieldSetMapper<Item>() {{
//				setTargetType(Item.class);
//			}});
//		}});
//		return reader;
//	}
//	
//	@Bean
//	public JdbcBatchItemWriter<Item> writer(){
//		JdbcBatchItemWriter<Item> writer = new JdbcBatchItemWriter<Item>();
//		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Item>());
//		writer.setSql("INSERT INTO items (id,name,description,price_m,price_l,image_path,deleted) VALUES (:id,:name,:description,:priceM,:priceL,:imagePath,:deleted);");
//		writer.setDataSource(dateSource);
//		return writer;
//	}
//	
//	
//	@Bean
//	public JobExecutionListener listener() {
//		return new JobStartEndListener(new JdbcTemplate(dateSource));
//	}
//	
//	@Bean Step step() {
//		return stepBuilderFactory.get("step")
//				.<Item,Item>chunk(10)
//				.reader(reader())
//				.writer(writer())
//				.build();
//	}
//	
//	@Bean 
//	public Job testJob() {
//		return jobBuilderFactory.get("testJob")
//				.incrementer(new RunIdIncrementer())
//				.listener(listener())
//				.flow(step())
//				.end()
//				.build();
//	}
//
//}
