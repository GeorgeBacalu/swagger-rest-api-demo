package com.project.repository;

import com.project.exception.ResourceNotFoundException;
import com.project.model.Category;
import com.project.model.Pet;
import com.project.model.Status;
import com.project.model.Tag;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

@Repository
public class PetRepository {
   private Map<Long, Pet> pets = new HashMap<>();

   @PostConstruct
   private void initializePets() {
      Pet pet1 = Pet.builder().id(1L).name("dog")
              .category(new Category(1L, "dogs"))
              .photoUrls(List.of("url1", "url2"))
              .tags(List.of(new Tag(1L, "tag1"), new Tag(2L, "tag2")))
              .status(Status.AVAILABLE)
              .build();

      Pet pet2 = Pet.builder().id(2L).name("cat")
              .category(new Category(2L, "cats"))
              .photoUrls(List.of("url3", "url4", "url5"))
              .tags(List.of(new Tag(3L, "tag3"), new Tag(4L, "tag4")))
              .status(Status.PENDING)
              .build();

      Pet pet3 = Pet.builder().id(3L).name("parrot")
              .category(new Category(3L, "parrots"))
              .photoUrls(List.of("url6"))
              .tags(List.of(new Tag(5L, "tag5"), new Tag(6L, "tag6"), new Tag(7L, "tag7")))
              .status(Status.SOLD)
              .build();

      pets = Map.ofEntries(
              Map.entry(pet1.getId(), pet1),
              Map.entry(pet2.getId(), pet2),
              Map.entry(pet3.getId(), pet3));
   }

   public List<Pet> findAll() {
      return new ArrayList<>(pets.values());
   }

   public Pet findById(Long id) {
      return findAll().stream().filter(pet -> Objects.equals(pet.getId(), id)).findAny().orElseThrow(() -> new ResourceNotFoundException("Pet with id " + id + " not found!"));
   }

   public Pet save(Pet pet) {
      return pets.compute(pet.getId(), (key, value) -> pet);
   }

   public Pet updateById(Pet pet, Long id) {
      Pet petToUpdate = findById(id);
      petToUpdate.setId(pet.getId());
      petToUpdate.setName(pet.getName());
      petToUpdate.setCategory(pet.getCategory());
      petToUpdate.setPhotoUrls(pet.getPhotoUrls());
      petToUpdate.setTags(pet.getTags());
      petToUpdate.setStatus(pet.getStatus());
      return petToUpdate;
   }

   public void deleteById(Long id) {
      pets.remove(id);
   }

   public List<Pet> getPetsByStatus(String statusOptionValues) {
      String[] statusValues = statusOptionValues.split(", ");
      List<Pet> petsWithRequiredStatus = new ArrayList<>();
      for(Pet pet : pets.values()) {
         for(String status : statusValues) {
            if(status.equals(pet.getStatus().toString())) {
               petsWithRequiredStatus.add(pet);
            }
         }
      }
      return petsWithRequiredStatus;
   }

   public Long autoIncrement() {
      return pets.values().stream().map(Pet::getId).max(Long::compare).orElse(0L) + 1;
   }

}
