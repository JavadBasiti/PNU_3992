package com.barshid.schematech.model.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.JdbcUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@EnableWebSecurity
public class UserSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
//    @Bean
//    @Override
//    public UserDetailsService userDetailsService () {
//        UserDetails user = User.builder ().username("user"). password (passwordEncoder (). encode ("secret")).
//                roles ("USER"). build ();
//
//        UserDetails userAdmin = User.builder (). username ("admin"). password (passwordEncoder (). encode ("secret")).
//                roles ("ADMIN"). build ();
//
//        return new InMemoryUserDetailsManager(user, userAdmin);
//    }
//    @Bean
//    public JdbcUserDetailsManager userDetailsManager() {
//        JdbcUserDetailsManager manager = new JdbcUserDetailsManager();
//        manager.setDataSource(dataSource);
//        manager.pass
//        manager.setUsersByUsernameQuery(
//                "select username,password,enabled from users where username=?");
////        manager.setAuthoritiesByUsernameQuery(
////                "select username, role from user_roles where username=?");
////        manager.setRolePrefix("ROLE_");
//        manager.setGroupAuthoritiesByUsernameQuery("select g.id, g.group_name, ga.authority from user_group g,user u, group_members gm, group_authorities ga " +
//                "where CONCAT(u.country_code,u.phone_number) = ? " +
//                "and u.id = gm.user_id and g.id = ga.group_id and g.id = gm.group_id ");
//        return manager;
//    }
//
//    @Autowired
//    public void configAuthentication(AuthenticationManagerBuilder builder)
//            throws Exception {
//
//        builder.userDetailsService(userDetailsManager());
//    }




    @Override
    @Bean//(BeanIds.USER_DETAILS_SERVICE)
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }

    private static JdbcUserDetailsManagerConfigurer<AuthenticationManagerBuilder> cfg;
    @Autowired
    public PasswordEncoder passwordEncoder;
//    private PasswordEncoder passwordEncoder() {
//        return passwordEncoder;
//    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // BCryptPasswordEncoder() is used for users.password column
        cfg = auth.jdbcAuthentication()
                .passwordEncoder(passwordEncoder).dataSource(dataSource)
                .usersByUsernameQuery("select CONCAT(country_code,phone_number) as username,password,enabled " +
                        "from user where CONCAT(country_code,phone_number) = ?")
                .groupAuthoritiesByUsername("select g.id, g.group_name, ga.authority from user_group g,user u, group_members gm, group_authorities ga where CONCAT(u.country_code,u.phone_number) = ? and u.id = gm.user_id and g.id = ga.group_id and g.id = gm.group_id ")
                ;

        cfg.getUserDetailsService().setEnableGroups(true);
        cfg.getUserDetailsService().setEnableAuthorities(false);
    }
    public static void updateUsernameQuery(String query){
        cfg.usersByUsernameQuery(query);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().antMatchers("/resetpass").permitAll()
                .and().authorizeRequests().anyRequest().authenticated()
            .and()
                .formLogin().loginPage("/customlogin")
                .permitAll();
        ;
    }



}

