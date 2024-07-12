package sk.f1api.f1api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sk.f1api.f1api.model.CalendarModel;
import sk.f1api.f1api.service.GrandPrixService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "GrandPrix", description = "GrandPrix management APIs")
@RestController
public class GrandPrixController
 {
	@Autowired
	GrandPrixService grandPrixService;

	@Operation(summary = "Retrieve all GrandPrixes", tags = { "get", "filter" })
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {
					@Content(schema = @Schema(implementation = CalendarModel.class), mediaType = "application/json") }),
			@ApiResponse(responseCode = "204", description = "There are no GrandPrixes", content = {
					@Content(schema = @Schema()) }),
			@ApiResponse(responseCode = "400", description = "Invalid Parameter", content = {
					@Content(schema = @Schema()) }),
			@ApiResponse(responseCode = "500", content = {
					@Content(schema = @Schema()) })
	})
	@GetMapping("/grandprix")
	public ResponseEntity<List<CalendarModel>> getAllCalendars(@RequestParam(required = false) Integer currentVersion,
			@RequestParam(required = false) Integer year) {
		try {
			List<CalendarModel> grandPrixes = grandPrixService.find(currentVersion, year);

			if (grandPrixes.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(grandPrixes, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
