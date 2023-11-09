package com.device.configuration.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.device.configuration.exception.DeviceAlreadyActivatedException;
import com.device.configuration.exception.DeviceNotFoundException;
import com.device.configuration.model.Device;
import com.device.configuration.service.ConfigurationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/configuration")
public class DeviceConfigurationController {

	private final ConfigurationService configurationService;

	@Operation(summary = "Activate a device")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Device is activated", content = { @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = Device.class)) }),
							@ApiResponse(responseCode = "400", description = "Invalid id", content = @Content), 
							@ApiResponse(responseCode = "404", description = "Device not found", content = @Content),
							@ApiResponse(responseCode = "409", description = "Device is already activated", content = @Content),
							@ApiResponse(responseCode = "500", description = "The Configuration service failed to process the request", content = @Content) })
	@PutMapping("/activate/{id}")
	public Device updateDevice(@PathVariable(value = "id") Long deviceId) throws DeviceNotFoundException, DeviceAlreadyActivatedException {
		return configurationService.activateDevice(deviceId);
	}
}
