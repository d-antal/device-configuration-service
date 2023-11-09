package com.device.configuration;

import static com.device.configuration.ConfigurationTestConstants.ID_1;
import static com.device.configuration.ConfigurationTestConstants.ID_NOT_EXIST;
import static com.device.configuration.ConfigurationTestConstants.WAREHOUSE_URI;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.device.configuration.exception.ApiResponseErrorHandler;
import com.device.configuration.exception.DeviceNotFoundException;
import com.device.configuration.model.Device;

@ContextConfiguration(classes = { Device.class })
@RestClientTest
class ConfigurationServiceErrorHandlingTest {

	@Autowired
	private RestTemplateBuilder builder;
	private MockRestServiceServer server;
	private RestTemplate restTemplate;

	@BeforeEach
	public void init() {
		restTemplate = this.builder.errorHandler(new ApiResponseErrorHandler()).build();
		this.server = MockRestServiceServer.createServer(restTemplate);
	}

	@Test
	void testUpdateDeviceWhenDeviceNotExist() {
		this.server.expect(ExpectedCount.once(), requestTo(WAREHOUSE_URI + ID_NOT_EXIST)).andExpect(method(HttpMethod.GET)).andRespond(withStatus(HttpStatus.NOT_FOUND));
		assertThrows(DeviceNotFoundException.class, () -> {
			restTemplate.getForObject(WAREHOUSE_URI + ID_NOT_EXIST, Device.class);
		}, "DeviceNotFoundException was expected");
		this.server.verify();
	}

	@Test
	void testUpdateDeviceWhenServerErrorReceived() {
		this.server.expect(ExpectedCount.once(), requestTo(WAREHOUSE_URI + ID_1)).andExpect(method(HttpMethod.GET)).andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));
		assertThrows(Exception.class, () -> {
			restTemplate.getForObject(WAREHOUSE_URI + ID_1, Device.class);
		}, "Exception was expected");
		this.server.verify();
	}
}
