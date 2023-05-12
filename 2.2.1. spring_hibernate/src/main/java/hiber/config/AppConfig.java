package hiber.config;

import hiber.model.Car;
import hiber.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


@Configuration
@PropertySource("classpath:db.properties")
@EnableTransactionManagement
@ComponentScan(value = "hiber")
public class AppConfig {

   @Autowired
   private Environment env;

   @Bean
   public DataSource getDataSource() {
      DriverManagerDataSource dataSource = new DriverManagerDataSource();
      dataSource.setDriverClassName(env.getRequiredProperty("db.driver"));
      dataSource.setUrl(env.getRequiredProperty("db.url"));
      dataSource.setUsername(env.getRequiredProperty("db.username"));
      dataSource.setPassword(env.getRequiredProperty("db.password"));
      return dataSource;
   }

   @Bean
   public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
      LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
      em.setDataSource(getDataSource());
      em.setPackagesToScan("hiber.model");
      em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
      em.setJpaProperties(getHibernateProperties());
      return em;
   }

   public Properties getHibernateProperties() {
      Properties properties = new Properties();
      try {
         InputStream inputStream = getClass().getClassLoader().getResourceAsStream("hiber.properties");
         properties.load(inputStream);

         return properties;
      } catch (IOException e) {
         throw new IllegalArgumentException("Can`t file hiber.properties in classpath!" + e);
      }
   }

   @Bean
   public PlatformTransactionManager platformTransactionManager() {
      JpaTransactionManager manager = new JpaTransactionManager();
      manager.setEntityManagerFactory(entityManagerFactory().getObject());
      return manager;
   }

//   @Bean
//   public LocalContainerEntityManagerFactoryBean getFactoryBean() {
//      LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
//      factoryBean.setDataSource(getDataSource());
//
//      Properties props=new Properties();
////      props.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
//      props.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
//      props.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
//      factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//      factoryBean.setJpaProperties(props);
//      factoryBean.setPackagesToScan("hiber.model");
//      return factoryBean;
//   }

//   @Bean
//   public PlatformTransactionManager getTransactionManager() {
//      JpaTransactionManager transactionManager = new JpaTransactionManager();
//      transactionManager.setEntityManagerFactory(getFactoryBean().getObject());
//              //.setSessionFactory(getSessionFactory().getObject());
//      return transactionManager;
//   }
}
