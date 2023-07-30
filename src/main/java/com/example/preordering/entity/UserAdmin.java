package com.example.preordering.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "userAdmins", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
})

public class UserAdmin implements UserDetails {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "userAdmin_id")
        private Long userAdminId;

        @NotBlank
        @Size(max = 40)
        private String firstName;

        @NotBlank
        @Size(max = 40)
        private String lastName;

        @NotBlank
        @Size(max = 15)
        private String username;

        @NaturalId
        @NotBlank
        @Size(max = 40)
        @Email
        private String email;

        @NotBlank
        @Size(max = 100)
        private String password;

        @NaturalId
        @NotBlank
        @Size(max = 15)
        private String phoneNumber;

        @NotBlank
        private String role;

        private String userAdminImageName;

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        public String getUsername(){
            return username;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }
        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }
        @Override
        public boolean isEnabled() {
            return true;
        }
    }



