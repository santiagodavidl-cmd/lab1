package com.udea.proyecto;

import com.udea.proyecto.config.AsyncSyncConfiguration;
import com.udea.proyecto.config.DatabaseTestcontainer;
import com.udea.proyecto.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = {
        AerolineaVirtualApp.class,
        JacksonConfiguration.class,
        AsyncSyncConfiguration.class,
        com.udea.proyecto.config.JacksonHibernateConfiguration.class,
    }
)
@ImportTestcontainers(DatabaseTestcontainer.class)
public @interface IntegrationTest {}
