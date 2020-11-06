package com.auth0.vehicles;

public class VehicleNotFoundException extends RuntimeException{
    VehicleNotFoundException(int id) {
        super("Vehicle not found with id: " + id);
    }
}
