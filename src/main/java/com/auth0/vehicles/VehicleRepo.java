package com.auth0.vehicles;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface VehicleRepo extends PagingAndSortingRepository<Vehicle, Integer> {
}
