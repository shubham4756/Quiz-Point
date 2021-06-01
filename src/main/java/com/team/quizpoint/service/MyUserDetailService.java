package com.team.quizpoint.service;

import com.team.quizpoint.model.MyUserDetails;
import com.team.quizpoint.model.User;
import com.team.quizpoint.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

//        User u = new User("User",passwordEncoder.encode("Password"), true, Arrays.asList("ROLE_ADMIN", "ROLE_USER"));
//        userRepository.insert(u);

        Optional<User> user = userRepository.findUserByEmail(s);

        user.orElseThrow(() -> new UsernameNotFoundException("User name " + s + " not found") );

        return new MyUserDetails(user.get());
    }
}
