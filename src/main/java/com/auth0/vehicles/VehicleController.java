package com.auth0.vehicles;

import org.springframework.web.bind.annotation.*;

@RestController
public class VehicleController {
    private final VehicleRepo repository;

    VehicleController(VehicleRepo repository) {
        this.repository = repository;
    }

    // Get all vehicles
    @GetMapping(value="/vehicles")
    Iterable<Vehicle> getAll() {
        return repository.findAll();
    }

    // Get a specific vehicle
    @GetMapping("/vehicles/{id}")
    Vehicle getOne(@PathVariable int id) {
        return repository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException(id));
    }

    // Create a vehicle
    @PostMapping("/vehicles")
    Vehicle postVehicle(@RequestBody Vehicle newVehicle) {
        return repository.save(newVehicle);
    }

    // Update a specific vehicle
    @PutMapping("/vehicles/{id}")
    Vehicle updateVehicle(@RequestBody Vehicle newVehicle, @PathVariable int id) {
        return repository.findById(id)
                .map(vehicle -> {
                    vehicle.setYear(newVehicle.getYear());
                    vehicle.setMake(newVehicle.getMake());
                    vehicle.setModel(newVehicle.getModel());
                    return repository.save(vehicle);
                })
                .orElseGet(() -> {
                    newVehicle.setId(id);
                    return repository.save(newVehicle);
                });
    }

    // Delete a specific vehicle
    @DeleteMapping("/vehicles/{id}")
    void deleteVehicle(@PathVariable int id) {
        repository.deleteById(id);
    }


}
