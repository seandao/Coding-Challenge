package com.auth0.vehicles;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import static org.mockito.Mockito.doReturn;
import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = VehicleController.class)
class VehicleControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VehicleRepo repository;

    @Test
    void testGetAllVehicles() throws Exception {
        Vehicle vehicle1 = new Vehicle(1972, "Honda", "Civic");
        Vehicle vehicle2 = new Vehicle(2012, "Toyota", "Camry");
        doReturn(Lists.newArrayList(vehicle1, vehicle2)).when(repository).findAll();

        mvc.perform(get("/vehicles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate response
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].year", is(1972)))
                .andExpect(jsonPath("$[0].make", is("Honda")))
                .andExpect(jsonPath("$[0].model", is("Civic")))
                .andExpect(jsonPath("$[1].year", is(2012)))
                .andExpect(jsonPath("$[1].make", is("Toyota")))
                .andExpect(jsonPath("$[1].model", is("Camry")));
    }

    @Test
    public void testGetVehicleById() throws Exception {
        Vehicle vehicle = new Vehicle(1972, "Honda", "Civic");
        doReturn(Optional.of(vehicle)).when(repository).findById(1);

        mvc.perform(get("/vehicles/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate response
                .andExpect(jsonPath("$.year", is(1972)))
                .andExpect(jsonPath("$.make", is("Honda")))
                .andExpect(jsonPath("$.model", is("Civic")));
    }

    @Test
    public void testPostVehicle() throws Exception {
        Vehicle vehicle = new Vehicle(1972, "Honda", "Civic");
        Vehicle vehicleToReturn = new Vehicle(1972, "Honda", "Civic");
        doReturn(vehicleToReturn).when(repository).save(any());

        mvc.perform(post("/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicle)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate response
                .andExpect(jsonPath("$.year", is(1972)))
                .andExpect(jsonPath("$.make", is("Honda")))
                .andExpect(jsonPath("$.model", is("Civic")));
    }

    @Test
    @DisplayName("PUT /vehicles/1")
    void testUpdateWidgetById() throws Exception {
        Vehicle putVehicle = new Vehicle(1972, "Honda", "Civic");
        Vehicle returnFindByVehicle = new Vehicle(2012, "Toyota", "Camry");
        Vehicle saveVehicle = new Vehicle(2019, "Tesla", "M3");
        doReturn(Optional.of(returnFindByVehicle)).when(repository).findById(1);
        doReturn(saveVehicle).when(repository).save(any());

        mvc.perform(put("/vehicles/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.IF_MATCH, 2)
                .content(objectMapper.writeValueAsString(putVehicle)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate Response
                .andExpect(jsonPath("$.year", is(2019)))
                .andExpect(jsonPath("$.make", is("Tesla")))
                .andExpect(jsonPath("$.model", is("M3")));
    }

    @Test
    public void testDeleteVehicle() throws Exception {
        mvc.perform(delete("/vehicles/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}