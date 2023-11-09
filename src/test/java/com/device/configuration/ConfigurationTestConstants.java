package com.device.configuration;

import com.device.configuration.model.Device;

public class ConfigurationTestConstants {

	static final String WAREHOUSE_URI = "http://localhost:8080/warehouse/devices/";
	static final String ACTIVATE_DEVICE_URI = "/configuration/activate/";
	static final byte TEMPERATURE_DEFAULT = -1;
	static final byte TEMPERATURE_ACTIVE = 5;
	static final String STATUS_DEFAULT = "READY";
	static final String STATUS_ACTIVE = "ACTIVE";
	static final Integer PIN_1 = 1111111;
	static final Integer PIN_2 = 2222222;
	static final long ID_1 = 1l;
	static final long ID_2 = 2l;
	static final long ID_NOT_EXIST = 123l;
	static final Device DEVICE_DEFAULT = new Device(ID_1, STATUS_DEFAULT, TEMPERATURE_DEFAULT, PIN_1);
	static final Device DEVICE_ACTIVE = new Device(ID_2, STATUS_ACTIVE, TEMPERATURE_ACTIVE, PIN_2);
	static final String DEVICE_NOT_FOUND_ERROR_MESSAGE = "Device not found by id: ";

}
