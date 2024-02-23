package com.gymcrm.gymcrm.config;

import com.gymcrm.gymcrm.model.Trainer;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.sql.SQLException;
import java.util.Properties;

public class HibernateSessionConfig {

    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    public static Properties hibernateProps(){
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        properties.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        properties.setProperty("hibernate.connection.url","jdbc:h2:mem:testdb");
        properties.setProperty("hibernate.connection.username", "sa");
        properties.setProperty("hibernate.connection.password", "password");
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.show_sql", "true");
        return properties;
    }
    public static SessionFactory getSessionFactory() throws SQLException {
        Configuration configuration = new Configuration().addAnnotatedClass(Trainer.class);
        StandardServiceRegistry standardServiceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(hibernateProps())
                .build();

        return configuration.buildSessionFactory(standardServiceRegistry);

    }

    public static void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}

