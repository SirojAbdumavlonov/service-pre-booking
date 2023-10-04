package com.example.preordering.entity;

import com.example.preordering.entity.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
@EqualsAndHashCode(callSuper = true)
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

public class UserAdmin extends DateAudit implements UserDetails {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "userAdmin_id")
        private Long userAdminId;

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
        @JsonIgnore
        private String password;

        @ElementCollection
        private List<String> phoneNumber;

        private String role;

        private String userAdminImageName;

        private String details;

//        @OneToOne(
//                fetch = FetchType.EAGER,
//                cascade = CascadeType.ALL
//        )
//        @JoinColumn(
//                name = "settings_id"
//        )
//        UserAdminSettingsOfTimetable userAdminSettingsOfTimetable;
        @JsonIgnore
        @OneToOne(
                fetch = FetchType.EAGER,
                cascade = CascadeType.ALL
        )
        @JoinColumn(
                name = "status_id"
        )
        UserAdminStatus userAdminStatus;

        @JsonIgnore
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of(new SimpleGrantedAuthority("ROLE_"+role));
        }
        @JsonIgnore
        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @JsonIgnore
        @Override
        public boolean isAccountNonLocked() {
            return true;
        }
        @Override
        @JsonIgnore
        public boolean isCredentialsNonExpired() {
            return true;
        }
        @Override
        @JsonIgnore
        public boolean isEnabled() {
            return true;
        }
    }



