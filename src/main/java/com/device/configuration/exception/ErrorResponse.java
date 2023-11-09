package com.device.configuration.exception;

import java.util.Date;

public class ErrorResponse {

	private Date timestamp;
	private String message;

	public ErrorResponse(Date timestamp, String message) {
		super();
		this.timestamp = timestamp;
		this.message = message;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getMessage() {
		return message;
	}
}
