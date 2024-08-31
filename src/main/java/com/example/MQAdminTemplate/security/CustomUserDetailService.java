package com.example.MQAdminTemplate.security;

import com.example.MQAdminTemplate.domain.Admin;
import com.example.MQAdminTemplate.domain.User;
import com.example.MQAdminTemplate.repository.AdminRepository;
import com.example.MQAdminTemplate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    @Autowired
    public CustomUserDetailService(UserRepository userRepository, AdminRepository adminRepository) {
        System.out.println("CustomUserDetailService initialized");
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmailUser(username);

        if (user != null) {
            return buildUserDetails(user, "USER");
        } else {
            Admin admin = adminRepository.findAdminByEmailAdmin(username);
            if (admin != null) {
                // User found in the Admin table
                System.out.println("User found in Admin table");
                return buildUserDetails(admin, "ADMIN");
            } else {
                System.out.println("User not found");
                throw new UsernameNotFoundException("User not found.");
            }
        }
    }

    private UserDetails buildUserDetails(Object user, String role) {
        if (user instanceof User) {
            return org.springframework.security.core.userdetails.User
                    .withUsername(((User) user).getEmailUser())
                    .password(((User) user).getPasswordUser())
                    .roles(role)
                    .build();
        } else if (user instanceof Admin) {
            return org.springframework.security.core.userdetails.User
                    .withUsername(((Admin) user).getEmailAdmin())
                    .password(((Admin) user).getPasswordAdmin())
                    .roles(role)
                    .build();
        }
        throw new UsernameNotFoundException("User not found.");
    }
}
