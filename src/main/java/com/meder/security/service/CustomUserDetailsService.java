package com.meder.security.service;

import com.meder.security.error.AccountBlockedException;
import com.meder.security.model.User;
import com.meder.security.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LoginAttemptService loginAttemptService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final String ip = getClientIP();
        if (loginAttemptService.isBlocked(ip)) {
            throw new AccountBlockedException();
        }
        try {
            final Optional<User> user = userRepository.findByEmail(username);
            if (!user.isPresent()) {
                throw new UsernameNotFoundException("No user found with username: " + username);
            }

            return user.get();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }

    }
    private String getClientIP() {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null) {
            return xfHeader.split(",")[0];
        }
        return request.getRemoteAddr();
    }
}
