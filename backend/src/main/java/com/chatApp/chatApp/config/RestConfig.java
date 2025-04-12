package com.chatApp.chatApp.config;

import java.util.Properties;



import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
public class RestConfig {
	  @Value("${db.driver}")
	  private String DRIVER;
	  @Value("${db.password}")
	  private String PASSWORD;
	  @Value("${db.url}")
	  private String URL;
	  @Value("${db.username}")
	  private String USERNAME;
	  @Value("${hibernate.dialect}") 
	  private String DIALECT;
	  @Value("${hibernate.show-sql}")
	  private String SHOW_SQL;
	  @Value("${hibernate.hbm2ddl.auto}")
	  private String HBM2DDL_AUTO;
	  
	  
//	   @Bean
//	    public Dotenv dotenv() {
//	        return Dotenv.load();  // Loads the .env file from the project root
//	    }
	  
	  
	  @Bean
	  public DataSource dataSource()
	  {
		  DriverManagerDataSource dataSource = new DriverManagerDataSource();
		  dataSource.setDriverClassName(DRIVER);
		  dataSource.setUrl(URL);
		  dataSource.setUsername(USERNAME);
		  dataSource.setPassword(PASSWORD);
		  return dataSource;
	  }
	  @Bean
	  public LocalSessionFactoryBean sessionFactory()
	  {
		  LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		  sessionFactory.setDataSource(dataSource());
		  sessionFactory.setPackagesToScan("com.chatApp.chatApp.entity");
		  Properties hibernateProperties = new Properties();
		  hibernateProperties.put("hibernate.dialect", DIALECT);
		  hibernateProperties.put("hibernate.show_sql", SHOW_SQL);
		  System.out.println(HBM2DDL_AUTO);
		  hibernateProperties.put("hibernate.hbm2ddl.auto", HBM2DDL_AUTO);
		  sessionFactory.setHibernateProperties(hibernateProperties);
		  return sessionFactory;
	  }
	  @Bean
	  public HibernateTransactionManager transactionManager()
	  {
		  HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		  transactionManager.setSessionFactory(sessionFactory().getObject());
		  return transactionManager;
	  }
//	@Bean
//    public SessionFactory sessionFactory(EntityManagerFactory entityManagerFactory) {
//        return entityManagerFactory.unwrap(SessionFactory.class);
//    }
	  @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
}
