package com.device.configuration.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.device.configuration.exception.DeviceAlreadyActivatedException;
import com.device.configuration.exception.DeviceNotFoundException;
import com.device.configuration.model.Device;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
@Slf4j
public class ConfigurationServiceImpl implements ConfigurationService {

	private final RestTemplate restTemplate;
	private String warehouseUri;
	private static final String STATUS_ACTIVE = "ACTIVE";

	@Autowired
	public ConfigurationServiceImpl(RestTemplate restTemplate, @Value("${warehouse.uri}") String warehouseUri) {
		this.restTemplate = restTemplate;
		this.warehouseUri = warehouseUri;
	}

	@Override
	public Device activateDevice(Long deviceId) throws DeviceNotFoundException, DeviceAlreadyActivatedException {
		log.info("Activate device by id: " + deviceId);
		Device device = restTemplate.getForObject(warehouseUri + deviceId, Device.class);
		if (device == null) {
			throw new DeviceNotFoundException("Device not found by id: " + deviceId);
		}
		if (STATUS_ACTIVE.equals(device.getStatus())) {
			throw new DeviceAlreadyActivatedException("Device is already activated, id: " + deviceId);
		}
		device.setTemperature((byte) new Random().nextInt(1, 11));
		device.setStatus(STATUS_ACTIVE);
		restTemplate.put(warehouseUri + deviceId, device);
		return device;
	}

}
