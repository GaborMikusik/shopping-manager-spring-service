package configuration;

import com.shoppingmanager.dao.shoppinglist.ShoppingListDAO;
import com.shoppingmanager.dao.shoppinglist.ShoppingListDAOImpl;
import com.shoppingmanager.dao.user.UserDAO;
import com.shoppingmanager.dao.user.UserDAOImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
//@EnableWebMvc
@ComponentScan(basePackages = "com.shoppingmanager")
public class TestDataSourceConfig {
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:testdb;INIT=RUNSCRIPT FROM 'src/main/resources/schema.sql'\\;RUNSCRIPT FROM 'src/main/resources/data.sql'");
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(this.dataSource());
    }

    @Bean
    public ShoppingListDAO shoppingListDAO() {
        return new ShoppingListDAOImpl();
    }

    @Bean
    public UserDAO userDAO() {
        return new UserDAOImpl();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
