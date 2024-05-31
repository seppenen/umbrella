package com.service.userService.helpers;

import com.service.userService.entity.UserEntity;
import com.service.userService.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class UserDetailService implements ReactiveUserDetailsService {

    private final UserRepository userRepository;


    @Override
    public Mono<UserDetails> findByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return Mono.just(User.withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRoles().toString())
                .build());
    }

}
