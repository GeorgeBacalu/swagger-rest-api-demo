package com.project.service.impl;

import com.project.model.Pet;
import com.project.repository.PetRepository;
import com.project.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {
    private final PetRepository petRepository;

    @Override
    public List<Pet> findAll() {
        return petRepository.findAll();
    }

    @Override
    public Pet findById(Long id) {
        return petRepository.findById(id);
    }

    @Override
    public Pet save(Pet pet) {
        if(pet.getId() == null) pet.setId(petRepository.autoIncrement());
        return petRepository.save(pet);
    }

    @Override
    public Pet updateById(Pet pet, Long id) {
        return petRepository.updateById(pet, id);
    }

    @Override
    public void deleteById(Long id) {
        petRepository.deleteById(id);
    }

    @Override
    public List<Pet> getPetsByStatus(String statusValue) {
        return petRepository.getPetsByStatus(statusValue);
    }
}
