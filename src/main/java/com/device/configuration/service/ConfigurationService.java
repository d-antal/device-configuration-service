package com.device.configuration.service;

import org.springframework.web.client.RestClientException;

import com.device.configuration.exception.DeviceAlreadyActivatedException;
import com.device.configuration.exception.DeviceNotFoundException;
import com.device.configuration.model.Device;

public interface ConfigurationService {

	Device activateDevice(Long deviceId) throws DeviceNotFoundException, DeviceAlreadyActivatedException, RestClientException;
}
