package sk.f1api.f1api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sk.f1api.f1api.model.CalendarModel;
import sk.f1api.f1api.model.GrandPrixModel;
import sk.f1api.f1api.service.GrandPrixService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "GrandPrix", description = "GrandPrix management APIs")
@RestController
public class GrandPrixController {
	@Autowired
	GrandPrixService grandPrixService;

	@Operation(summary = "Retrieve GrandPrix", tags = { "get", "filter" })
	@ApiResponses({
			@ApiResponse(responseCode = "200", content = {
					@Content(schema = @Schema(implementation = CalendarModel.class), mediaType = "application/json") }),
			@ApiResponse(responseCode = "204", description = "There is no GrandPrix", content = {
					@Content(schema = @Schema()) }),
			@ApiResponse(responseCode = "400", description = "Invalid Parameter", content = {
					@Content(schema = @Schema()) }),
			@ApiResponse(responseCode = "500", content = {
					@Content(schema = @Schema()) })
	})
	@GetMapping("/grandprix")
	public ResponseEntity<GrandPrixModel> getAllCalendars(@RequestParam(required = false) Integer currentVersion,
			@RequestParam(required = true) int year, @RequestParam(required = true) int round) {
		try {
			GrandPrixModel grandPrix = grandPrixService.find(currentVersion, year, round);

			if (grandPrix == null) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(grandPrix, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
