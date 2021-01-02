package com.hanaset.hana.common.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.*
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jmx.export.MBeanExporter
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate
import javax.sql.DataSource


@Configuration
@ComponentScan(basePackages = [
    "com.hanaset.hana.common.repository"
])
@EnableJpaRepositories(
        basePackages = ["com.hanaset.hana.common.repository"],
        entityManagerFactoryRef = "hanaEntityManagerFactory",
        transactionManagerRef = "hanaTransactionManager"
)
@PropertySource("classpath:properties/database/hana-database-\${spring.profiles.active}.properties")
class HanaJpaDatabaseConfig(private val mbeanExporter: MBeanExporter) {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "hana.jpa")
    fun hanaJpaProperties(): JpaProperties {
        return JpaProperties()
    }

    @Bean
    @Primary
    fun hanaHibernateSettings(): HibernateSettings {
        return HibernateSettings()
    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "hana")
    fun hanaHikariConfig(): HikariConfig {
        return HikariConfig()
    }

    @Bean
    @Primary
    fun hanaDataSource(): DataSource {
        val dataSource = HikariDataSource(hanaHikariConfig())
        mbeanExporter.addExcludedBean("hanaDataSource")
        return dataSource
    }

    @Bean
    @Primary
    fun hanaEntityManagerFactory(builder: EntityManagerFactoryBuilder): LocalContainerEntityManagerFactoryBean {
        return builder
                .dataSource(hanaDataSource())
                .packages("com.hanaset.hana.common.entity")
                .persistenceUnit("hanaPersistenceUnit")
                .properties(getVendorProperties(hanaDataSource()))
                .build()
    }

    private fun getVendorProperties(dataSource: DataSource): Map<String, String> {
        var properties = hanaJpaProperties().properties
//        properties.put("hibernate.dialec", "org.hibernate.dialect.MySQL5InnoDBDialect")
        return properties
    }

    @Bean(name = ["hanaJdbcTemplate"])
    fun hanaJdbcTemplate(@Qualifier("hanaDataSource") dataSource: DataSource): JdbcTemplate {
        return JdbcTemplate(dataSource)
    }

    @Bean
    @Primary
    fun hanaTransactionManager(builder: EntityManagerFactoryBuilder): PlatformTransactionManager {
        return JpaTransactionManager(hanaEntityManagerFactory(builder).getObject()!!)
    }

    @Bean
    @Primary
    fun hanaTransactionTemplate(builder: EntityManagerFactoryBuilder): TransactionTemplate {
        return TransactionTemplate(hanaTransactionManager(builder))
    }
}