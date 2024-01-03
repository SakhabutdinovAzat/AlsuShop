package ru.clothingstore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.clothingstore.service.Impl.UserDetailsServiceImpl;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsServiceImpl;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(UserDetailsServiceImpl userDetailsServiceImpl, PasswordEncoder passwordEncoder) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                    .antMatchers("/auth/login", "/auth/registration", "/error", "/swagger-ui/**").permitAll()
//                    .antMatchers("/admin/**").hasRole("ROLE_ADMIN")
                    .antMatchers("/cart/**", "/order/**", "/user/**").authenticated()
//                    .anyRequest().authenticated()//hasAnyRole("ROLE_USER", "ROLE_ADMIN")
                    .anyRequest().permitAll()
                .and()
                    .formLogin().loginPage("/auth/login")
                    .loginProcessingUrl("/process_login")
                    .defaultSuccessUrl("/index", false)
                    .failureUrl("/auth/login?error")
                .and()
                    .logout().logoutUrl("/logout").logoutSuccessUrl("/auth/login")
                .and()
                    .rememberMe();
    }

    // настраивает аунтентификацию
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl)
                .passwordEncoder(passwordEncoder);
    }
}
