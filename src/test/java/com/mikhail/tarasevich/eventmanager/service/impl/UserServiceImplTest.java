package com.mikhail.tarasevich.eventmanager.service.impl;

import com.mikhail.tarasevich.eventmanager.dto.UserRequest;
import com.mikhail.tarasevich.eventmanager.dto.UserResponse;
import com.mikhail.tarasevich.eventmanager.entity.Role;
import com.mikhail.tarasevich.eventmanager.entity.User;
import com.mikhail.tarasevich.eventmanager.repository.ContractRepository;
import com.mikhail.tarasevich.eventmanager.repository.EventRepository;
import com.mikhail.tarasevich.eventmanager.repository.RoleRepository;
import com.mikhail.tarasevich.eventmanager.repository.UserEventParticipationRepository;
import com.mikhail.tarasevich.eventmanager.repository.UserRepository;
import com.mikhail.tarasevich.eventmanager.service.exception.CommonException;
import com.mikhail.tarasevich.eventmanager.service.exception.UserNotFoundException;
import com.mikhail.tarasevich.eventmanager.service.mapper.UserMapper;
import com.mikhail.tarasevich.eventmanager.service.validator.UserValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private UserEventParticipationRepository userEventParticipationRepository;

    @Mock
    private UserMapper mapper;

    @Mock
    private UserValidator validator;

    @Mock
    private PasswordEncoder encoder;

    @Test
    void findAll_validInput_returnsListOfUsers() {

        User user1 = new User();
        user1.setId(1);
        user1.setEmail("user1@example.com");

        User user2 = new User();
        user2.setId(2);
        user2.setEmail("user2@example.com");

        List<User> users = List.of(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        UserResponse userResponse1 = new UserResponse();
        userResponse1.setId(1);
        userResponse1.setEmail("user1@example.com");

        UserResponse userResponse2 = new UserResponse();
        userResponse2.setId(2);
        userResponse2.setEmail("user2@example.com");

        when(mapper.toResponse(user1)).thenReturn(userResponse1);
        when(mapper.toResponse(user2)).thenReturn(userResponse2);

        List<UserResponse> expected = List.of(userResponse1, userResponse2);
        List<UserResponse> actual = userService.findAll();

        assertEquals(expected, actual);
    }

    @Test
    void findUserById_existingUser_returnsUserResponse() {

        int userId = 1;

        User user = new User();
        user.setId(userId);
        user.setEmail("user1@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserResponse expected = new UserResponse();
        expected.setId(userId);
        expected.setEmail("user1@example.com");

        when(mapper.toResponse(user)).thenReturn(expected);

        UserResponse actual = userService.findUserById(userId);

        assertEquals(expected, actual);
    }

    @Test
    void findUserById_nonExistingUser_throwsUserNotFoundException() {

        int userId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findUserById(userId));
    }

    @Test
    void findUserByEmail_existingUser_returnsUserResponse() {

        String userEmail = "user1@example.com";

        User user = new User();
        user.setId(1);
        user.setEmail(userEmail);

        when(userRepository.findUserByEmail(userEmail)).thenReturn(Optional.of(user));

        UserResponse expected = new UserResponse();
        expected.setId(1);
        expected.setEmail(userEmail);

        when(mapper.toResponse(user)).thenReturn(expected);

        UserResponse actual = userService.findUserByEmail(userEmail);

        assertEquals(expected, actual);
    }

    @Test
    void findUserByEmail_nonExistingUser_throwsUserNotFoundException() {

        String userEmail = "nonexistent@example.com";

        when(userRepository.findUserByEmail(userEmail)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findUserByEmail(userEmail));
    }

    @Test
    void createManager_validInput_returnsUserResponse() {

        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("manager@example.com");
        userRequest.setPassword("password");

        Role role = Role.builder().withName("ROLE_MANAGER").build();

        User userEntity = new User();
        userEntity.setId(1);
        userEntity.setEmail("manager@example.com");
        userEntity.setRole(role);

        doNothing().when(validator).validateEmail(userRequest);
        doNothing().when(validator).validatePassword(userRequest);
        when(roleRepository.findRoleByName("ROLE_MANAGER")).thenReturn(Optional.of(role));
        when(encoder.encode("password")).thenReturn("encodedPassword");
        when(mapper.toEntity(userRequest)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        UserResponse expected = new UserResponse();
        expected.setId(1);
        expected.setEmail("manager@example.com");

        when(mapper.toResponse(userEntity)).thenReturn(expected);

        UserResponse actual = userService.createManager(userRequest);

        assertEquals(expected, actual);
    }

    @Test
    void createManager_roleNotFound_throwsCommonException() {

        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("manager@example.com");
        userRequest.setPassword("password");

        when(roleRepository.findRoleByName("ROLE_MANAGER")).thenReturn(Optional.empty());

        assertThrows(CommonException.class, () -> userService.createManager(userRequest));
    }

    @Test
    void createParticipant_validInput_returnsUserResponse() {

        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("participant@example.com");
        userRequest.setPassword("password");

        Role role = Role.builder().withName("ROLE_PARTICIPANT").build();

        User userEntity = new User();
        userEntity.setId(1);
        userEntity.setEmail("participant@example.com");
        userEntity.setRole(role);

        doNothing().when(validator).validateEmail(userRequest);
        doNothing().when(validator).validatePassword(userRequest);
        when(roleRepository.findRoleByName("ROLE_PARTICIPANT")).thenReturn(Optional.of(role));
        when(encoder.encode("password")).thenReturn("encodedPassword");
        when(mapper.toEntity(userRequest)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        UserResponse expected = new UserResponse();
        expected.setId(1);
        expected.setEmail("participant@example.com");

        when(mapper.toResponse(userEntity)).thenReturn(expected);

        UserResponse actual = userService.createParticipant(userRequest);

        assertEquals(expected, actual);
    }

    @Test
    void createParticipant_roleNotFound_throwsCommonException() {

        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("participant@example.com");
        userRequest.setPassword("password");

        when(roleRepository.findRoleByName("ROLE_PARTICIPANT")).thenReturn(Optional.empty());

        assertThrows(CommonException.class, () -> userService.createParticipant(userRequest));
    }

    @Test
    void findAdmins_multipleAdminsExist_returnsAdminResponses() {

        User admin1 = User.builder().withEmail("admin1@example.com").withRole(Role.builder().withName("ROLE_ADMIN").build()).build();
        User admin2 = User.builder().withEmail("admin2@example.com").withRole(Role.builder().withName("ROLE_ADMIN").build()).build();

        when(userRepository.findUsersByRoleOrderById("ROLE_ADMIN")).thenReturn(List.of(admin1, admin2));

        UserResponse adminResponse1 = UserResponse.builder().withEmail("admin1@example.com").build();
        UserResponse adminResponse2 = UserResponse.builder().withEmail("admin2@example.com").build();
        when(mapper.toResponse(admin1)).thenReturn(adminResponse1);
        when(mapper.toResponse(admin2)).thenReturn(adminResponse2);

        List<UserResponse> result = userService.findAdmins();

        assertEquals(2, result.size());
        assertEquals("admin1@example.com", result.get(0).getEmail());
        assertEquals("admin2@example.com", result.get(1).getEmail());
    }

    @Test
    void findAdmins_noAdminsExist_returnsEmptyList() {

        when(userRepository.findUsersByRoleOrderById("ROLE_ADMIN")).thenReturn(Collections.emptyList());

        List<UserResponse> result = userService.findAdmins();

        assertTrue(result.isEmpty());
    }

    @Test
    void findManagers_multipleManagersExist_returnsManagerResponses() {

        User manager1 = User.builder().withEmail("manager1@example.com").withRole(Role.builder().withName("ROLE_MANAGER").build()).build();
        User manager2 = User.builder().withEmail("manager2@example.com").withRole(Role.builder().withName("ROLE_MANAGER").build()).build();

        when(userRepository.findUsersByRoleOrderById("ROLE_MANAGER")).thenReturn(List.of(manager1, manager2));

        UserResponse managerResponse1 = UserResponse.builder().withEmail("manager1@example.com").build();
        UserResponse managerResponse2 = UserResponse.builder().withEmail("manager2@example.com").build();
        when(mapper.toResponse(manager1)).thenReturn(managerResponse1);
        when(mapper.toResponse(manager2)).thenReturn(managerResponse2);

        List<UserResponse> result = userService.findManagers();

        assertEquals(2, result.size());
        assertEquals("manager1@example.com", result.get(0).getEmail());
        assertEquals("manager2@example.com", result.get(1).getEmail());
    }

    @Test
    void findManagers_noManagersExist_returnsEmptyList() {

        when(userRepository.findUsersByRoleOrderById("ROLE_MANAGER")).thenReturn(Collections.emptyList());

        List<UserResponse> result = userService.findManagers();

        assertTrue(result.isEmpty());
    }

    @Test
    void findParticipants_multipleParticipantsExist_returnsParticipantResponses() {

        User participant1 = User.builder()
                .withEmail("participant1@example.com")
                .withRole(Role.builder().withName("ROLE_PARTICIPANT").build())
                .build();
        User participant2 = User.builder()
                .withEmail("participant2@example.com")
                .withRole(Role.builder().withName("ROLE_PARTICIPANT").build())
                .build();

        when(userRepository.findUsersByRoleOrderById("ROLE_PARTICIPANT"))
                .thenReturn(List.of(participant1, participant2));

        UserResponse participantResponse1 = UserResponse.builder()
                .withEmail("participant1@example.com")
                .build();
        UserResponse participantResponse2 = UserResponse.builder()
                .withEmail("participant2@example.com")
                .build();

        when(mapper.toResponse(participant1)).thenReturn(participantResponse1);
        when(mapper.toResponse(participant2)).thenReturn(participantResponse2);

        List<UserResponse> result = userService.findParticipants();

        assertEquals(2, result.size());
        assertEquals("participant1@example.com", result.get(0).getEmail());
        assertEquals("participant2@example.com", result.get(1).getEmail());
    }

    @Test
    void findParticipants_noParticipantsExist_returnsEmptyList() {

        when(userRepository.findUsersByRoleOrderById("ROLE_PARTICIPANT")).thenReturn(Collections.emptyList());

        List<UserResponse> result = userService.findParticipants();

        assertTrue(result.isEmpty());
    }

    @Test
    void deleteUserById_existingUser_deletesUserAndRelatedEntities() {

        int userId = 1;

        doNothing().when(userEventParticipationRepository).deleteUserEventParticipationsByUserId(userId);
        doNothing().when(userEventParticipationRepository).deleteUserEventParticipationsByEventUserId(userId);
        doNothing().when(contractRepository).deleteContractsByUserId(userId);
        doNothing().when(eventRepository).deleteEventsByUserId(userId);
        doNothing().when(userRepository).deleteById(userId);

        userService.deleteUserById(userId);

        verify(userEventParticipationRepository).deleteUserEventParticipationsByUserId(userId);
        verify(userEventParticipationRepository).deleteUserEventParticipationsByEventUserId(userId);
        verify(contractRepository).deleteContractsByUserId(userId);
        verify(eventRepository).deleteEventsByUserId(userId);
        verify(userRepository).deleteById(userId);
    }

}
