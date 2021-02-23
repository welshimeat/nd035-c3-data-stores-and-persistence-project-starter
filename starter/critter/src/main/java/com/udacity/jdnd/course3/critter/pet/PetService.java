package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class PetService {
    @Autowired
    private PetRepository petRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public PetDTO save(PetDTO petDTO){
        Pet pet = convertPetDTOToEntity(petDTO);
        pet = petRepository.save(pet);
        Customer customer = pet.getCustomer();
        if(pet.getCustomer() != null){
            pet.getCustomer().addPet(pet);
            customerRepository.save(pet.getCustomer());
        }
        return convertEntityToPetDTO(pet);
    }

    public PetDTO getPetById(Long id){
        Optional<Pet> optionalPet = petRepository.findById(id);
        if(optionalPet.isPresent()){
            return convertEntityToPetDTO(optionalPet.get());
        }
        else{
            throw new NullPointerException();
        }
    }

    public List<PetDTO> getPets(){
        List<Pet> pets = petRepository.findAll();
        List<PetDTO> petDTOs = new ArrayList<>();
        for(Pet pet : pets){
            petDTOs.add(convertEntityToPetDTO(pet));
        }
        return petDTOs;
    }

    public List<PetDTO> getPetsByOwnerId(Long ownerId){
        Optional<Customer> optionalCustomer = customerRepository.findById(ownerId);
        if(!optionalCustomer.isPresent()){
            throw new NullPointerException();
        }
        List<Pet> pets = petRepository.findAllByCustomer_Id(ownerId);
        List<PetDTO> petDTOs = new ArrayList<>();
        for(Pet pet : pets){
            petDTOs.add(convertEntityToPetDTO(pet));
        }
        return petDTOs;
    }


    private PetDTO convertEntityToPetDTO(Pet pet){
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petDTO);
        if(pet.getCustomer() != null){
            petDTO.setOwnerId(pet.getCustomer().getId());
        }
        return petDTO;
    }

    private Pet convertPetDTOToEntity(PetDTO petDTO){
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);
        if(petDTO.getOwnerId() != 0){
            pet.setCustomer(customerRepository.findById(petDTO.getOwnerId()).get());
        }
        return pet;
    }
}
