package de.zeppelin.checkthat.webservice;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		EclipseLinkJpaVendorAdapter vendorAdapter = new EclipseLinkJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);
		vendorAdapter.setShowSql(true);

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("de.zeppelin.checkthat.webservice.Models");
		factory.setDataSource(dataSource());
		factory.setJpaProperties(jpaProperties());
		// factory.setLoadTimeWeaver(new InstrumentationLoadTimeWeave);
		return factory;
	}

	@Bean
	public JpaTransactionManager transactionManager() {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory().getObject());
		return txManager;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	@Bean
	public DataSource dataSource() {
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setUrl("jdbc:mysql://localhost:3306/checkthat");
		dataSource.setUser("root");
		dataSource.setPassword("rootpasswort");
		return dataSource;
	}

	private Properties jpaProperties() {
		Properties properties = new Properties();
		properties.put("eclipselink.weaving", "false");
		return properties;
	}

	// @Bean
	// public DataSource dataSource() {
	// DriverManagerDataSource dataSource = new DriverManagerDataSource();
	//
	// dataSource.setDriverClassName("com.mysql.jdbc.Driver");
	// dataSource.setUrl("jdbc:mysql://localhost:3306/checkthat");
	// dataSource.setUsername("root");
	// dataSource.setPassword("rootpasswort");
	//
	// return dataSource;
	// }
	//
	// @Autowired
	// @Bean(name = "sessionFactory")
	// public SessionFactory getSessionFactory(DataSource dataSource) {
	//
	// LocalSessionFactoryBuilder sessionBuilder = new
	// LocalSessionFactoryBuilder(dataSource);
	//
	// sessionBuilder.addAnnotatedClasses(User.class);
	// sessionBuilder.addProperties(getHibernateProperties());
	//
	// return sessionBuilder.buildSessionFactory();
	// }
	//
	// private Properties getHibernateProperties() {
	// Properties properties = new Properties();
	// properties.put("hibernate.show_sql", "true");
	// properties.put("hibernate.dialect",
	// "org.hibernate.dialect.MySQLDialect");
	// properties.put("hibernate.format_sql", "true");
	// return properties;
	// }
}
