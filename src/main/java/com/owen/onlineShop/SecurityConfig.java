package com.owen.onlineShop;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@EnableWebSecurity // 要查文档 or 例子才知道需要enable 和继承如下 adapter
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // authorization: 来验证用户有没有权限来访问她请求的资源
        http
                .csrf().disable()
                .formLogin()
                .loginPage("/login"); // 这是我们定义的login URL，如果没有会用上面一行的默认表单
        http
                .authorizeRequests() // 根据具体的URL来判断了，下面的pattern是可能match到的reg ex
                .antMatchers("/cart/**").hasAuthority("ROLE_USER")
                .antMatchers("/get*/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .antMatchers("/admin*/**").hasAuthority("ROLE_ADMIN")
                .anyRequest().permitAll(); // 其他的URL请求都是任何权限可以全部permit访问
        http
                .logout()
                .logoutUrl("/logout");

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 框架无法使用Hibernate
        auth
                .inMemoryAuthentication().withUser("stefanlaioffer@gmail.com").password("123").authorities("ROLE_ADMIN");
        // 像测试用的ADMIN账号，这里可以永久保存在application的内存当中，且拥有ADMIN权限
        // 不用注册，直接可以登录

        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("SELECT emailId, password, enabled FROM users WHERE emailId=?")
                .authoritiesByUsernameQuery("SELECT emailId, authorities FROM authorities WHERE emailId=?");
        // 需要指定dataSource 也就是去query的DB，这个在applicationConfig里面定义了
        // 通过Spring来inject
        // 无法通过Hibernate访问，只能通过SQL语句由emailId来得到password, enabled and authorities
        // 根据前端发来的用户名也就是emailId 来执行SQL语句，框架会匹配输入的用户名，password来和DB进行比对
        // 找到用户所对应的权限authorities
        // 提供SQL query来得到data, 具体是否match由框架来实现！
    }

    @SuppressWarnings("deprecation")
    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
        // 暂时没有用到这部分加密，我们实际是用明文密码直接输入登录or注册的
        // one way Hash，只能Hash之后得到的信息相匹配，相同的密码Hash后结果一致，且无法reverse得到密码明文
    }
}

