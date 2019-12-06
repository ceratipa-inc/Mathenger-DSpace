package com.example.mathengerapi.services;

import com.example.mathengerapi.models.User;
import com.example.mathengerapi.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private static final int MIN_PASSWORD_LENGTH = 8;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(username);
        return user.orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        var user = userRepository.findById(id);
        return user.orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public User generatePasswordResetCode(String email) {
        var user = userRepository.findByEmailIgnoreCaseAndActive(email, true)
                .orElseThrow(() -> new IllegalArgumentException("User with such email not found!"));
        user.setPasswordRecoveryCode(UUID.randomUUID().toString());
        return userRepository.save(user);
    }

    public User findByPasswordRecoveryCode(String recoveryCode) {
        return userRepository
                .findByPasswordRecoveryCodeAndActive(recoveryCode, true)
                .orElseThrow(() -> new IllegalArgumentException("Recovery code is not valid"));
    }

    public void recoverPassword(User user, String password) {
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("Password has to be at least "
                    + MIN_PASSWORD_LENGTH + " characters long!");
        }
        user.setPassword(passwordEncoder.encode(password));
        user.setPasswordRecoveryCode(null);
        userRepository.save(user);
    }
}
