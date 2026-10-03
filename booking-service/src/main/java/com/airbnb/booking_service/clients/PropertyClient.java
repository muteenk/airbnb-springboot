package com.airbnb.booking_service.clients;

import com.airbnb.booking_service.dtos.responses.UnitResponse;
import com.airbnb.booking_service.payloads.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "property-service")
public interface PropertyClient {
    @GetMapping("/v1/units/{unitId}")
    ApiResponse<UnitResponse> getUnit(@PathVariable("unitId") Long unitId);
}
