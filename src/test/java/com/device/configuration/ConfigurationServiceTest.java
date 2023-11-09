package com.device.configuration;

import static com.device.configuration.ConfigurationTestConstants.DEVICE_ACTIVE;
import static com.device.configuration.ConfigurationTestConstants.DEVICE_DEFAULT;
import static com.device.configuration.ConfigurationTestConstants.DEVICE_NOT_FOUND_ERROR_MESSAGE;
import static com.device.configuration.ConfigurationTestConstants.ID_1;
import static com.device.configuration.ConfigurationTestConstants.ID_2;
import static com.device.configuration.ConfigurationTestConstants.ID_NOT_EXIST;
import static com.device.configuration.ConfigurationTestConstants.STATUS_ACTIVE;
import static com.device.configuration.ConfigurationTestConstants.WAREHOUSE_URI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.temporal.ValueRange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.device.configuration.exception.DeviceAlreadyActivatedException;
import com.device.configuration.exception.DeviceNotFoundException;
import com.device.configuration.model.Device;
import com.device.configuration.service.ConfigurationService;
import com.device.configuration.service.ConfigurationServiceImpl;

class ConfigurationServiceTest {

	ConfigurationService configurationService;

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private RestTemplateBuilder builder;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		configurationService = new ConfigurationServiceImpl(restTemplate, WAREHOUSE_URI);
	}

	@Test
	void testActivateDevice() throws DeviceNotFoundException, DeviceAlreadyActivatedException {
		when(restTemplate.getForObject(WAREHOUSE_URI + ID_1, Device.class)).thenReturn(DEVICE_DEFAULT);
		doNothing().when(restTemplate).put(WAREHOUSE_URI + ID_1, Device.class);
		Device activatedDevice = configurationService.activateDevice(ID_1);
		assertEquals(STATUS_ACTIVE, activatedDevice.getStatus());
		assertTrue(ValueRange.of(1, 10).isValidIntValue(activatedDevice.getTemperature()));
		assertEquals(DEVICE_DEFAULT.getPin(), activatedDevice.getPin());
		verify(restTemplate, times(1)).getForObject(WAREHOUSE_URI + ID_1, Device.class);
		verify(restTemplate, times(1)).put(WAREHOUSE_URI + ID_1, activatedDevice);
	}

	@Test
	void testActivateDeviceWhenDeviceNotFound() throws Exception {
		when(restTemplate.getForObject(WAREHOUSE_URI + ID_NOT_EXIST, Device.class)).thenThrow(new DeviceNotFoundException("Device not found by id: " + ID_NOT_EXIST));
		assertThrows(DeviceNotFoundException.class, () -> {
			configurationService.activateDevice(ID_NOT_EXIST);
		}, "DeviceNotFoundException was expected");
		verify(restTemplate, times(1)).getForObject(WAREHOUSE_URI + ID_NOT_EXIST, Device.class);
		verify(restTemplate, never()).put(any(String.class), any(Device.class));
	}

	@Test
	void testActivateDeviceWhenNullReceived() throws Exception {
		when(restTemplate.getForObject(WAREHOUSE_URI + ID_NOT_EXIST, Device.class)).thenReturn(null);
		assertThrows(DeviceNotFoundException.class, () -> {
			configurationService.activateDevice(ID_NOT_EXIST);
		}, "DeviceNotFoundException was expected");
		verify(restTemplate, times(1)).getForObject(WAREHOUSE_URI + ID_NOT_EXIST, Device.class);
		verify(restTemplate, never()).put(any(String.class), any(Device.class));
	}

	@Test
	void testActivateDeviceWhenDeviveAlreadyActivated() throws Exception {
		when(restTemplate.getForObject(WAREHOUSE_URI + ID_2, Device.class)).thenReturn(DEVICE_ACTIVE);
		assertThrows(DeviceAlreadyActivatedException.class, () -> {
			configurationService.activateDevice(ID_2);
		}, "DeviceAlreadyActivatedException was expected");
		verify(restTemplate, times(1)).getForObject(WAREHOUSE_URI + ID_2, Device.class);
		verify(restTemplate, never()).put(any(String.class), any(Device.class));
	}

	@Test
	void testActivateDeviceWhenServerErrorReceived() throws Exception {
		when(restTemplate.getForObject(WAREHOUSE_URI + ID_2, Device.class)).thenThrow(new RestClientException(DEVICE_NOT_FOUND_ERROR_MESSAGE));
		assertThrows(RestClientException.class, () -> {
			configurationService.activateDevice(ID_2);
		}, "Exception was expected");
		verify(restTemplate, times(1)).getForObject(WAREHOUSE_URI + ID_2, Device.class);
		verify(restTemplate, never()).put(any(String.class), any(Device.class));
	}
}
