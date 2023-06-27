package com.mikhail.tarasevich.eventmanager.controller;

import com.mikhail.tarasevich.eventmanager.dto.UserResponse;
import com.mikhail.tarasevich.eventmanager.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@Api(tags = "Контроллер атунтифицированного и авторизированного пользователя")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    @ApiOperation(value = "Показать персональные данные аутентифицированного пользователя")
    UserResponse showUser(@ApiIgnore Principal user) {

        return userService.findUserByEmail(user.getName());
    }

    @GetMapping("/admins")
    @PreAuthorize("hasAuthority('PRIVILEGE_APP_ADMIN')")
    @ApiOperation(value = "Показать всех администраторов приложеиния", notes = "Доступно только для администратора приложения")
    List<UserResponse> showAllAdmins() {

        return userService.findAdmins();
    }

    @GetMapping("/managers")
    @PreAuthorize("hasAuthority('PRIVILEGE_APP_ADMIN')")
    @ApiOperation(value = "Показать всех менджеров мероприятий", notes = "Доступно только для администратора приложения")
    List<UserResponse> showAllManagers() {

        return userService.findManagers();
    }

    @GetMapping("/participants")
    @PreAuthorize("hasAuthority('PRIVILEGE_APP_ADMIN')")
    @ApiOperation(value = "Показать всех потенциальных участников мероприятий", notes = "Доступно только для администратора приложения")
    List<UserResponse> showAllParticipants() {

        return userService.findParticipants();
    }

    @DeleteMapping("/")
    @PreAuthorize("hasAuthority('PRIVILEGE_APP_ADMIN')")
    @ApiOperation(value = "Удалить пользователя из базы данных", notes = "Доступно только для администратора приложения")
    ResponseEntity<String> deleteUser(@ApiParam(value = "ID пользователя, которому будет удален", example = "8", required = true) @RequestParam("id")int id) {

        userService.deleteUserById(id);

        return ResponseEntity.status(HttpStatus.OK).body("User with id = " + id + " has been deleted");
    }

}
