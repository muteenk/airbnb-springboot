package com.airbnb.booking_service.services;

import com.airbnb.booking_service.clients.PropertyClient;
import com.airbnb.booking_service.dtos.responses.UnitResponse;
import com.airbnb.booking_service.exceptions.ApiException;
import com.airbnb.booking_service.exceptions.ResourceDoesNotExist;
import com.airbnb.booking_service.payloads.ApiResponse;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PropertyRemoteService {

    private final PropertyClient propertyClient;

    @Retry(name="propertyService")
    @CircuitBreaker(name = "propertyService", fallbackMethod = "verifyUnitFallback")
    public void verifyUnitExists(Long unitId) {
        try {
            ApiResponse<UnitResponse> response = propertyClient.getUnit(unitId);
            if (response == null || response.getData() == null) {
                throw new ResourceDoesNotExist("Unit doesn't exist");
            }
        } catch (FeignException.NotFound ex) {
            throw new ResourceDoesNotExist("Unit doesn't exist");
        }
    }

    @SuppressWarnings("unused")
    private void verifyUnitFallback(Long unitId, Throwable ex) {
        if (ex instanceof ResourceDoesNotExist resourceDoesNotExist) {
            throw resourceDoesNotExist;
        }
        throw new ApiException(
                "Property Service is unavailable.",
                HttpStatus.SERVICE_UNAVAILABLE
        );
    }
}
