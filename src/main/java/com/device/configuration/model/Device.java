package com.device.configuration.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Device {

	@NotNull()
	private Long id;

	@NotBlank
	private String status;

	@NotNull()
	@Min(value = -1)
	@Max(value = 10)
	private Byte temperature;

	@Min(value = 0000001)
	@Max(value = 9999999)
	private Integer pin;
}
