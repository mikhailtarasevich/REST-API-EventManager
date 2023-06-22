package com.mikhail.tarasevich.eventmanager.controller;

import com.mikhail.tarasevich.eventmanager.dto.EventRequest;
import com.mikhail.tarasevich.eventmanager.dto.EventResponse;
import com.mikhail.tarasevich.eventmanager.service.EventService;
import com.mikhail.tarasevich.eventmanager.service.exception.EventNotValidDataException;
import com.mikhail.tarasevich.eventmanager.util.BindingResultValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/api/v1/event")
@Api(tags = "Контроллер управления событиями")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('PRIVILEGE_EVENT_CREATOR')")
    List<EventResponse> showAllMangerEvents(@ApiIgnore Principal user) {

        return eventService.findAllMangerEvents(user.getName());
    }

    @PostMapping("/")
    @PreAuthorize("hasAuthority('PRIVILEGE_EVENT_CREATOR')")
    EventResponse createNewEvent(@ApiIgnore Principal user,
                                 @ApiParam(value = "Данные о новом событии", required = true) @RequestBody @Valid EventRequest request,
                                 BindingResult bindingResult) {

        BindingResultValidator.checkErrorsInBindingResult(bindingResult, EventNotValidDataException.class);

        return eventService.createNewEvent(user.getName(), request);
    }

    @DeleteMapping("/manager")
    @PreAuthorize("hasAuthority('PRIVILEGE_EVENT_CREATOR')")
    ResponseEntity<String> deleteEvent(@ApiIgnore Principal user,
                                       @ApiParam(value = "ID события, которое будет удалено. Можно удалить только событие, которое относится к аутентифицированному пользователю", example = "1", required = true) @RequestParam("id") int id) {

        eventService.deleteEventById(user.getName(), id);

        return ResponseEntity.status(HttpStatus.OK).body("Event with id = " + id + " has been deleted");
    }

    @DeleteMapping("/admin")
    @PreAuthorize("hasAuthority('PRIVILEGE_APP_ADMIN')")
    ResponseEntity<String> deleteEventByAdmin(@ApiParam(value = "ID события, которое будет удалено. Метод доступен только Администратору", example = "1", required = true) @RequestParam("id") int id) {

        eventService.deleteEventByIdForAdmin(id);

        return ResponseEntity.status(HttpStatus.OK).body("Event with id = " + id + " has been deleted by Admin");
    }

}
