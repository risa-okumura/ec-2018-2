package jp.co.rakus;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import jp.co.rakus.ec2018c.domain.Order;
import jp.co.rakus.ec2018c.domain.User;

@Configuration
@EnableBatchProcessing
public class OutputBatch {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	public DataSource dateSource;
	
	private static final RowMapper<Order> ORDER_ROW_MAPPER = (rs, i) -> {
		Order order = new Order();
		order.setId					(rs.getInt		("id"));
		order.setUserId				(rs.getInt		("user_id"));
		order.setStatus				(rs.getInt		("status"));
		order.setTotalPrice			(rs.getInt		("total_price"));
		order.setOrderDate			(rs.getDate		("order_date"));
		order.setDestinationName	(rs.getString	("destination_name"));
		order.setDestinationEmail	(rs.getString	("destination_email"));
		order.setDestinationZipcode	(rs.getString	("destination_zipcode"));
		order.setDestinationAddress	(rs.getString	("destination_address"));
		order.setDestinationTel		(rs.getString	("destination_tel"));
		order.setDeliveryTime		(rs.getTimestamp("delivery_time"));
		order.setPaymentMethod		(rs.getInt		("payment_method"));
		
		User user = new User();
		user.setId(rs.getInt("u_id"));
		user.setName(rs.getString("u_name"));
		user.setEmail(rs.getString("u_email"));
		user.setPassword(rs.getString("u_password"));
		user.setZipcode(rs.getString("u_zipcode"));
		user.setEmail(rs.getString("u_address"));
		user.setTelephone(rs.getString("u_telephone"));
		order.setUser(user);
		
//		u.id AS u_id, u.name AS u_name, u.email AS u_email, u.password AS u_password, u.zipcode AS u_zipcode, u.address AS u_address, u.telephone AS u_telephone
		
		return order;
	};
	
	
	@Bean
	public JdbcCursorItemReader<Order> reader(){
		
		JdbcCursorItemReader<Order> reader = new JdbcCursorItemReader<>();
		reader.setSql("SELECT o.id, o.user_id, o.status, o.total_price, o.order_date, o.destination_name, o.destination_email,o.destination_zipcode, o.destination_address, o.destination_tel, o.delivery_time, o.payment_method, u.id AS u_id, u.name AS u_name, u.email AS u_email, u.password AS u_password, u.zipcode AS u_zipcode, u.address AS u_address, u.telephone AS u_telephone FROM orders o LEFT OUTER JOIN users u ON o.user_id = u.id ORDER BY u.name;");
		reader.setDataSource(dateSource);
		reader.setRowMapper(ORDER_ROW_MAPPER);

		
		return reader;
	}
	
	@Bean
	public FlatFileItemWriter<Order> writer(){
		
		FlatFileItemWriter<Order> writer = new FlatFileItemWriter<>();
		Date date = new Date();
		String today = new SimpleDateFormat("yyyyMMdd").format(date);
		
		writer.setResource(new FileSystemResource("C:\\env\\" + today + ".csv"));
		writer.setLineAggregator(new SampleAggregator());
		return writer;
	}
	
	
	@Bean
	public JobExecutionListener listener() {
		return new JobStartEndListener(new JdbcTemplate(dateSource));
	}
	
	@Bean Step step() {
		return stepBuilderFactory.get("step")
				.<Order,Order>chunk(10)
				.reader(reader())
				.writer(writer())
				.build();
	}
	
	@Bean 
	public Job testJob() {
		return jobBuilderFactory.get("testJob")
				.incrementer(new RunIdIncrementer())
				.listener(listener())
				.flow(step())
				.end()
				.build();
	}
	
}
