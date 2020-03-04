package com.stacksimplify.restservices.config;

import org.springframework.context.annotation.Configuration;

import io.micrometer.appoptics.AppOpticsConfig;
import io.micrometer.appoptics.AppOpticsMeterRegistry;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.lang.Nullable;

@Configuration
public class MonitoringConfig {

	AppOpticsConfig appopticsConfig = new AppOpticsConfig() {
		@Override
		public String apiToken() {
			return "p6q1x4PLnu4VgqEyxNX5NEi-dICb1moDF6wRdLxN31P8P8iQlJBhc-vxHAYovE92hgb0DPc";
		}

		@Override
		@Nullable
		public String get(String k) {
			return null;
		}
	};
	MeterRegistry registry = new AppOpticsMeterRegistry(appopticsConfig, Clock.SYSTEM);

}
