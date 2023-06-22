package com.mikhail.tarasevich.eventmanager.service.impl;

import com.mikhail.tarasevich.eventmanager.entity.Privilege;
import com.mikhail.tarasevich.eventmanager.entity.User;
import com.mikhail.tarasevich.eventmanager.repository.UserRepository;
import com.mikhail.tarasevich.eventmanager.security.UserSecurityDetails;
import com.mikhail.tarasevich.eventmanager.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException("There is no user with email = " + email + " in DB"));

        return new UserSecurityDetails(user,
                user.getRole().getPrivileges().stream()
                        .map(Privilege::getName)
                        .collect(Collectors.toList()));
    }

}
