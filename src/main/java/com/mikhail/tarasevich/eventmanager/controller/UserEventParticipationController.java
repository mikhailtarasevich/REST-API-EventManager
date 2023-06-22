package com.mikhail.tarasevich.eventmanager.controller;

import com.mikhail.tarasevich.eventmanager.dto.UserEventParticipationRequest;
import com.mikhail.tarasevich.eventmanager.dto.UserEventParticipationResponse;
import com.mikhail.tarasevich.eventmanager.service.UserEventParticipationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/event/participation")
@Api(tags = "Контроллер управления запросами на участие в мероприятии")
public class UserEventParticipationController {

    private final UserEventParticipationService uepService;

    @Autowired
    public UserEventParticipationController(UserEventParticipationService uepService) {
        this.uepService = uepService;
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAuthority('PRIVILEGE_EVENT_CREATOR')")
    List<UserEventParticipationResponse> showAllPendingUserEventParticipationsForManager(@ApiIgnore Principal user,
                                                                               @ApiParam(value = "ID события", example = "1", required = true) @RequestParam("eventId") int eventId) {

        return uepService.findPendingUserEventParticipationsByEventId(user.getName(), eventId);
    }

    @GetMapping("/rejected")
    @PreAuthorize("hasAuthority('PRIVILEGE_EVENT_CREATOR')")
    List<UserEventParticipationResponse> showAllRejectedUserEventParticipationsForManager(@ApiIgnore Principal user,
                                                                                @ApiParam(value = "ID события", example = "1", required = true) @RequestParam("eventId") int eventId) {

        return uepService.findRejectedUserEventParticipationsByEventId(user.getName(), eventId);
    }

    @GetMapping("/accepted")
    @PreAuthorize("hasAuthority('PRIVILEGE_EVENT_CREATOR')")
    List<UserEventParticipationResponse> showAllAcceptedUserEventParticipationsForManager(@ApiIgnore Principal user,
                                                                                @ApiParam(value = "ID события", example = "1", required = true) @RequestParam("eventId") int eventId) {

        return uepService.findAcceptedUserEventParticipationsByEventId(user.getName(), eventId);
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('PRIVILEGE_PARTICIPANT')")
    List<UserEventParticipationResponse> showAllUserEventParticipationsForParticipant(@ApiIgnore Principal user) {

        return uepService.findAllUserEventParticipationsByUserEmail(user.getName());
    }

    @PostMapping("/")
    @PreAuthorize("hasAuthority('PRIVILEGE_PARTICIPANT')")
    UserEventParticipationResponse createNewUserEventParticipation(@ApiIgnore Principal user,
                                                                   @ApiParam(value = "Данные о заявке на участии в мероприятии", required = true) @RequestBody @Valid UserEventParticipationRequest request) {

        return uepService.createUserEventParticipation(user.getName(), request);
    }

    @PatchMapping("/accept/{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE_EVENT_CREATOR')")
    ResponseEntity<String> acceptParticipationRequest(@ApiIgnore Principal user,
                                                      @ApiParam(value = "ID заявки на участие в базе данных", example = "1", required = true) @PathVariable("id") int id) {

        uepService.setUserEventParticipationsStatusAccepted(user.getName(), id);

        return ResponseEntity.status(HttpStatus.OK).body("Participation Request with id = " + id + " has been accepted");
    }

    @PatchMapping("/reject/{id}")
    @PreAuthorize("hasAuthority('PRIVILEGE_EVENT_CREATOR')")
    ResponseEntity<String> rejectParticipationRequest(@ApiIgnore Principal user,
                                                      @ApiParam(value = "ID заявки на участие в базе данных", example = "1", required = true) @PathVariable("id") int id) {

        uepService.setUserEventParticipationsStatusRejected(user.getName(), id);

        return ResponseEntity.status(HttpStatus.OK).body("Participation Request with id = " + id + " has been rejected");
    }

    @DeleteMapping("/participant")
    @PreAuthorize("hasAuthority('PRIVILEGE_PARTICIPANT')")
    ResponseEntity<String> deleteUserEventParticipation(@ApiIgnore Principal user,
                                       @ApiParam(value = "ID заявки на участие, которая будет удалена. Можно удалить только заявку, которое относится к аутентифицированному пользователю", example = "1", required = true) @RequestParam("id") int id) {

        uepService.deleteUserEventParticipationsById(user.getName(), id);

        return ResponseEntity.status(HttpStatus.OK).body("Participation request with id = " + id + " has been deleted");
    }

    @DeleteMapping("/admin")
    @PreAuthorize("hasAuthority('PRIVILEGE_APP_ADMIN')")
    ResponseEntity<String> deleteUserEventParticipationAdmin(@ApiParam(value = "ID заявки на участие, которая будет удалена. Метод доступен только Администратору", example = "1", required = true) @RequestParam("id") int id) {

        uepService.deleteUserEventParticipationsByIdForAdmin(id);

        return ResponseEntity.status(HttpStatus.OK).body("Participation request with id = " + id + " has been deleted by Admin");
    }

}
