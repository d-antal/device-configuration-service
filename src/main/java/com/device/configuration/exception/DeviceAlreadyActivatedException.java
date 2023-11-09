package com.device.configuration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class DeviceAlreadyActivatedException extends Exception {

	private static final long serialVersionUID = 1L;

	public DeviceAlreadyActivatedException(String message) {
		super(message);
	}
}
