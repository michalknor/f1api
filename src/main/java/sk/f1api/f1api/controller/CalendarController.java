package sk.f1api.f1api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sk.f1api.f1api.model.CalendarModel;
import sk.f1api.f1api.service.CalendarService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Calendar", description = "Calendar management APIs")
@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class CalendarController {
  @Autowired
  CalendarService calendarService;

  @Operation(summary = "Retrieve all Calendars", tags = { "calendars", "get", "filter" })
  @ApiResponses({
      @ApiResponse(responseCode = "200", content = {
          @Content(schema = @Schema(implementation = CalendarModel.class), mediaType = "application/json") }),
      @ApiResponse(responseCode = "204", description = "There are no Calendars", content = {
          @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "500", content = {
          @Content(schema = @Schema()) })
  })
  @GetMapping("/calendars")
  public ResponseEntity<List<CalendarModel>> getAllCalendars(@RequestParam(required = false) String year) {
    try {
      List<CalendarModel> calendars = new ArrayList<CalendarModel>();
      calendarService.loadAll();

      if (year == null) {
        calendarService.getCalendars().forEach(calendars::add);
      }

      if (calendars.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(calendars, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
