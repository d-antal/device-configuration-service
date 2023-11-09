package com.device.configuration;

import static com.device.configuration.ConfigurationTestConstants.ACTIVATE_DEVICE_URI;
import static com.device.configuration.ConfigurationTestConstants.DEVICE_ACTIVE;
import static com.device.configuration.ConfigurationTestConstants.ID_1;
import static com.device.configuration.ConfigurationTestConstants.ID_2;
import static com.device.configuration.ConfigurationTestConstants.ID_NOT_EXIST;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestClientException;

import com.device.configuration.controller.DeviceConfigurationController;
import com.device.configuration.exception.DeviceAlreadyActivatedException;
import com.device.configuration.exception.DeviceNotFoundException;
import com.device.configuration.service.ConfigurationService;
import com.fasterxml.jackson.databind.ObjectMapper;
@WebMvcTest(DeviceConfigurationController.class)
class ConfigurationControllerIntegrationTest {

	

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ConfigurationService configurationService;
	private static final ObjectMapper OM = new ObjectMapper();

	@Test
	void testUpdateDevice() throws Exception {
		when(configurationService.activateDevice(ID_1)).thenReturn(DEVICE_ACTIVE);
		mockMvc.perform(put(ACTIVATE_DEVICE_URI + ID_1).content(OM.writeValueAsString(DEVICE_ACTIVE)).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())		
				.andExpect(jsonPath("$.id").value(DEVICE_ACTIVE.getId()))
				.andExpect(jsonPath("$.status").value(DEVICE_ACTIVE.getStatus()))
				.andExpect(jsonPath("$.temperature").value(Integer.valueOf(DEVICE_ACTIVE.getTemperature())))
				.andExpect(jsonPath("$.pin").value(DEVICE_ACTIVE.getPin()));	
		verify(configurationService, times(1)).activateDevice(ID_1);
	}

	@Test
	void testUpdateDeviceWhenDeviceNotFound() throws Exception {
		when(configurationService.activateDevice(ID_NOT_EXIST)).thenThrow(DeviceNotFoundException.class);
		mockMvc.perform(put(ACTIVATE_DEVICE_URI + ID_NOT_EXIST).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
		verify(configurationService, times(1)).activateDevice(ID_NOT_EXIST);
	}

	@Test
	void testUpdateDeviceWhenDeviceAlreadyActivated() throws Exception {
		when(configurationService.activateDevice(ID_2)).thenThrow(DeviceAlreadyActivatedException.class);
		mockMvc.perform(put(ACTIVATE_DEVICE_URI + ID_2).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict());
		verify(configurationService, times(1)).activateDevice(ID_2);
	}
	
	@Test
	void testUpdateDeviceWhenRemoteServiceServerError() throws Exception {
		when(configurationService.activateDevice(ID_2)).thenThrow(RestClientException.class);
		mockMvc.perform(put(ACTIVATE_DEVICE_URI + ID_2).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError());
		verify(configurationService, times(1)).activateDevice(ID_2);
	}

}