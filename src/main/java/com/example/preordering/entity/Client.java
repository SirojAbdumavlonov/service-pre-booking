package com.example.preordering.entity;

import com.example.preordering.entity.audit.DateAudit;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "clients", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Client extends DateAudit implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long clientId;

    @Size(max = 40)
    private String firstName;

    @Size(max = 40)
    private String lastName;


    @Size(max = 15)
    private String username;

    @NaturalId
    @Size(max = 40)
    @Email
    private String email;


    @Size(max = 100)
    private String password;

    @ElementCollection
    private List<String> phoneNumber;

    private String clientImageName;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_CLIENT"));
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

