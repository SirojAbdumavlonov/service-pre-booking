package com.example.preordering.config;

import com.example.preordering.entity.Client;
import com.example.preordering.entity.UserAdmin;
import com.example.preordering.repository.ClientRepository;
import com.example.preordering.repository.UserAdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final ClientRepository clientRepository;
    private final UserAdminRepository userAdminRepository;

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return username -> {
            Optional<Client> client = clientRepository.findByEmailOrUsername(username, username);
            Optional<UserAdmin> userAdmin = userAdminRepository.findByEmailOrUsername(username, username);
            if(client.isPresent()) {
                return clientRepository.findByEmailOrUsername(username, username)
                        .orElseThrow(() -> new UsernameNotFoundException("There is no user with such email or username: " + username));
            }
            if(userAdmin.isPresent()){
                return userAdminRepository.findByEmailOrUsername(username, username)
                        .orElseThrow(() -> new UsernameNotFoundException("There is no user with such email or username: " + username));
            }
            return clientRepository.findByEmailOrUsername(username, username)
                    .orElseThrow(() -> new UsernameNotFoundException("There is no user with such email or username: " + username));
        };
    }
}