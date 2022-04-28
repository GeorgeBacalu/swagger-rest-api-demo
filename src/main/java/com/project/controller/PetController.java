package com.project.controller;

import com.project.exception.InvalidDataException;
import com.project.exception.ResourceNotFoundException;
import com.project.model.Pet;
import com.project.model.Status;
import com.project.service.PetService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Api(value = "Pet Rest Controller", description = "Everything about your pets", tags = "/pet")
@RestController
@RequestMapping(value = "/pet", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PetController {
    private final PetService petService;

    @ApiOperation(value = "Get all pets", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 404, message = "No pets found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @GetMapping
    public ResponseEntity<List<Pet>> getAllPets() {
        if (petService.findAll() == null) throw new ResourceNotFoundException("No pets found");
       return ResponseEntity.ok(petService.findAll());
    }

    @ApiOperation(value = "Find pet by ID", notes = "Returns a single pet", response = Pet.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 400, message = "Invalid ID supplied"),
            @ApiResponse(code = 404, message = "No pet with given id found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @GetMapping("/{petId}")
    public ResponseEntity<Pet> getPetById(@ApiParam(value = "ID of pet to return", required = true) @PathVariable("petId") Long id) {
        if(id < 0) throw new InvalidDataException("Id " + id + " is invalid");
        if(petService.findById(id) == null) throw new ResourceNotFoundException("No pet with id " + id + " was found");
        return ResponseEntity.ok(petService.findById(id));
    }

    @Validated
    @ApiOperation(value = "Add a new pet to the store", response = Pet.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 400, message = "Bad request (invalid input)"),
            @ApiResponse(code = 405, message = "Validation exception"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Pet> savePet(
            @ApiParam(value = "Pet object that needs to be added to the store", required = true) @Valid @RequestBody Pet pet,
            BindingResult bindingResult) {
        if(bindingResult.hasErrors()) throw new InvalidDataException("Pet " + pet + " is invalid");
        return ResponseEntity.status(HttpStatus.CREATED).body(petService.save(pet));
    }

    @Validated
    @ApiOperation(value = "Update an existing pet by id", response = Pet.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 400, message = "Invalid ID supplied"),
            @ApiResponse(code = 404, message = "No pet with given id found"),
            @ApiResponse(code = 405, message = "Validation exception"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @PutMapping(value = "/{petId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Pet> updatePetById(
            @ApiParam(value = "Pet object that needs to be added to the store", required = true) @Valid @RequestBody Pet pet,
            @ApiParam(value = "ID of pet to return", required = true) @PathVariable("petId") Long id,
            BindingResult bindingResult) {
        if(bindingResult.hasErrors()) throw new InvalidDataException("Pet " + pet + " is invalid");
        return ResponseEntity.ok(petService.updateById(pet, id));
    }

    @Validated
    @ApiOperation(value = "Update an existing pet", response = Pet.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 400, message = "Invalid ID supplied"),
            @ApiResponse(code = 404, message = "No pet with given id found"),
            @ApiResponse(code = 405, message = "Validation exception"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Pet> updatePet(@ApiParam(value = "Pet object that needs to be added to the store", required = true) @Valid @RequestBody Pet pet, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) throw new InvalidDataException("The pet data provided is invalid!");
        Long id = pet.getId();
        if(id == null) throw new InvalidDataException("Id " + id + " is invalid");
        if(petService.findById(pet.getId()) == null) throw new ResourceNotFoundException("No pet was found");
        return ResponseEntity.ok(petService.save(pet));
    }

    @ApiOperation(value = "Delete a pet", response = Map.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 400, message = "Invalid ID supplied"),
            @ApiResponse(code = 404, message = "No pet with given id found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @DeleteMapping("/{petId}")
    public ResponseEntity<Map<String, Boolean>> deleteOrderById(@ApiParam(value = "ID of pet to return", required = true) @PathVariable("petId") Long id) {
        if(id < 0) throw new InvalidDataException("Id " + id + " is invalid");
        petService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("deleted", Boolean.TRUE));
    }

    @ApiOperation(value = "Find pet by status", notes = "Multiple status values can be provided with comma separated strings", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 400, message = "Invalid status supplied"),
            @ApiResponse(code = 404, message = "No pet with given status found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @GetMapping("/findByStatus")
    public ResponseEntity<List<Pet>> getPetsByStatus(
            @ApiParam(value = "Status to filter pets by", required = true, allowableValues = "available, pending, sold")
            @Valid @RequestParam String status) {
        if(status == null) throw new ResourceNotFoundException("Invalid status value");
        List<String> statusEnumList = List.of(Arrays.toString(Status.values()));
        List<String> statusSplitOptions = Arrays.stream(status.split(", ")).toList();
        statusSplitOptions.forEach(s -> {
            if(!statusEnumList.contains(s)) {
                throw new InvalidDataException("Invalid status value");
            }

        });
        return ResponseEntity.ok(petService.getPetsByStatus(status));
    }

    /*
    @ApiOperation(value = "Find pets by status", notes = "Multiple status values can be provided with comma separated strings", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 400, message = "Invalid status supplied"),
            @ApiResponse(code = 404, message = "No pet with given status found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @GetMapping("/findByStatus")
    public ResponseEntity<List<Pet>> getPetsByStatus(
            @ApiParam(value = "Status values that need to be considered for filter", required = true, allowableValues = "available, pending, sold")
            @Valid @RequestParam String[] status) {
        return ResponseEntity.ok(petService.getPetsByStatus(status));
    }
    */

    // TODO fix findPetsByStatus logic so I'm able to filter by multiple status values
    // TODO fix bug: java.lang.NumberFormatException: For input string: ""
    // TODO fix bug: java.lang.UnsupportedOperationException: null (when executing post and delete requests)
}
