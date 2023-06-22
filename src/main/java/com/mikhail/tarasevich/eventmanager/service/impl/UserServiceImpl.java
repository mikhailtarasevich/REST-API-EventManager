package com.mikhail.tarasevich.eventmanager.service.impl;

import com.mikhail.tarasevich.eventmanager.dto.UserRequest;
import com.mikhail.tarasevich.eventmanager.dto.UserResponse;
import com.mikhail.tarasevich.eventmanager.entity.User;
import com.mikhail.tarasevich.eventmanager.repository.ContractRepository;
import com.mikhail.tarasevich.eventmanager.repository.EventRepository;
import com.mikhail.tarasevich.eventmanager.repository.RoleRepository;
import com.mikhail.tarasevich.eventmanager.repository.UserEventParticipationRepository;
import com.mikhail.tarasevich.eventmanager.repository.UserRepository;
import com.mikhail.tarasevich.eventmanager.service.UserService;
import com.mikhail.tarasevich.eventmanager.service.exception.CommonException;
import com.mikhail.tarasevich.eventmanager.service.exception.UserNotFoundException;
import com.mikhail.tarasevich.eventmanager.service.mapper.UserMapper;
import com.mikhail.tarasevich.eventmanager.service.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final EventRepository eventRepository;

    private final ContractRepository contractRepository;

    private final UserEventParticipationRepository userEventParticipationRepository;

    private final UserMapper mapper;

    private final UserValidator validator;

    private final PasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           EventRepository eventRepository,
                           ContractRepository contractRepository,
                           UserEventParticipationRepository userEventParticipationRepository,
                           UserMapper mapper,
                           UserValidator validator,
                           PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.eventRepository = eventRepository;
        this.contractRepository = contractRepository;
        this.userEventParticipationRepository = userEventParticipationRepository;
        this.mapper = mapper;
        this.validator = validator;
        this.encoder = encoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {

        return userRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findUserById(int id) {

        return mapper.toResponse(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("There is no user with id = " + id + " in DB")));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findUserByEmail(String email) {

        return mapper.toResponse(userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("There is no user with email = " + email + " in DB")));
    }

    @Override
    public UserResponse createManager(UserRequest request) {

        validator.validateEmail(request);
        validator.validatePassword(request);

        request.setId(0);
        request.setPassword(encoder.encode(request.getPassword()));

        User entity = mapper.toEntity(request);
        entity.setRole(roleRepository.findRoleByName("ROLE_MANAGER")
                .orElseThrow(() -> new CommonException("User wasn't saved in db. There is no ROLE_MANAGER in DB")));

        return mapper.toResponse(userRepository.save(entity));
    }

    @Override
    public UserResponse createParticipant(UserRequest request) {

        validator.validateEmail(request);
        validator.validatePassword(request);

        request.setId(0);
        request.setPassword(encoder.encode(request.getPassword()));

        User entity = mapper.toEntity(request);
        entity.setRole(roleRepository.findRoleByName("ROLE_PARTICIPANT")
                .orElseThrow(() -> new CommonException("User wasn't saved in db. There is no ROLE_PARTICIPANT in DB")));

        return mapper.toResponse(userRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> findAdmins() {

        return userRepository.findUsersByRoleOrderById("ROLE_ADMIN").stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> findManagers() {

        return userRepository.findUsersByRoleOrderById("ROLE_MANAGER").stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> findParticipants() {

        return userRepository.findUsersByRoleOrderById("ROLE_PARTICIPANT").stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUserById(int id) {

        userEventParticipationRepository.deleteUserEventParticipationsByUserId(id);
        userEventParticipationRepository.deleteUserEventParticipationsByEventUserId(id);
        contractRepository.deleteContractsByUserId(id);
        eventRepository.deleteEventsByUserId(id);

        userRepository.deleteById(id);
    }

}
