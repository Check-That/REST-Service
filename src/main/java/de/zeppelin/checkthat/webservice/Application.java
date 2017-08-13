package de.zeppelin.checkthat.webservice;

import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import de.zeppelin.checkthat.webservice.controller.VersionControlFilter;

@Component
@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableSpringConfigured
@EnableAspectJAutoProxy
@EnableAsync
public class Application implements ApplicationContextAware {

	public static boolean deployed = true;
	public static String awsAccessKey = "AKIAIAWLXX2I44GWUSGQ";
	public static String awsSecretKey = "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";
	public static String surveyImagesBucketName = "de.mindwing.checkthat.images";
	public static String profileImagesBucketName = "de.mindwing.checkthat.profileimages";

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	public static ApplicationContext getContext() {
		return context;
	}

	@Bean
	MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize("100MB");
		factory.setMaxRequestSize("100MB");
		factory.setFileSizeThreshold("100MB");
		return factory.createMultipartConfig();
	}

	// @Bean
	// public CommonsMultipartResolver multipartResolver() {
	// CommonsMultipartResolver resolver = new CommonsMultipartResolver();
	//
	// return resolver;
	// }

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		EclipseLinkJpaVendorAdapter vendorAdapter = new EclipseLinkJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);
		vendorAdapter.setShowSql(true);

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);

		factory.setPackagesToScan("de.zeppelin.checkthat.webservice.models");
		factory.setDataSource(dataSource());
		factory.setJpaProperties(jpaProperties());
		factory.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
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
		MysqlDataSource deployDataSource = new MysqlDataSource();
		deployDataSource.setUrl(
				"jdbc:mysql://de-mindwing-checkthat-db1.cnta1mlcze2e.us-west-2.rds.amazonaws.com:3306/checkthat?useUnicode=true&characterEncoding=UTF-8");
		deployDataSource.setUser("root");
		deployDataSource.setPassword("mindwingsupersecure");

		MysqlDataSource testDataSource = new MysqlDataSource();
		testDataSource.setUrl("jdbc:mysql://localhost:3306/checkthat?useUnicode=true&characterEncoding=UTF-8");
		testDataSource.setUser("root");
		testDataSource.setPassword("rootpassword");

		return Application.deployed ? deployDataSource : testDataSource;
	}

	private Properties jpaProperties() {
		Properties properties = new Properties();
		properties.put("eclipselink.weaving", "false");
		// properties.put("eclipselink.logging.level", "ALL");
		return properties;
	}
	
	@Bean
	public Filter versionControlFilter() {
		return new VersionControlFilter();
	}
}
