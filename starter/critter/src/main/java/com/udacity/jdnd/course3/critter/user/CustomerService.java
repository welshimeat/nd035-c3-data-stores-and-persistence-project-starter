package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import com.udacity.jdnd.course3.critter.schedule.Schedule;
import com.udacity.jdnd.course3.critter.schedule.ScheduleDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PetRepository petRepository;

    public CustomerDTO save(CustomerDTO customerDTO){
        Customer customer = convertCustomerDTOToEntity(customerDTO);
        customer = customerRepository.save(customer);
        return convertEntityToCustomerDTO(customer);
    }

    public List<CustomerDTO> getCustomers(){
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOs = new ArrayList<>();
        for(Customer customer : customers){
            customerDTOs.add(convertEntityToCustomerDTO(customer));
        }
        return customerDTOs;
    }

    public CustomerDTO getCustomerByPet(Long petId){
        Optional<Pet> optionalPet = petRepository.findById(petId);
        if(optionalPet.isPresent()){
            Optional<Customer> optionalCustomer = customerRepository.findById(optionalPet.get().getCustomer().getId());
            if(optionalCustomer.isPresent()){
                return convertEntityToCustomerDTO(optionalCustomer.get());
            }
            else throw new NullPointerException();
        }
        else throw new NullPointerException();
    }

    private CustomerDTO convertEntityToCustomerDTO(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        if(customer.getPets() != null){
            List<Long> petIds = new ArrayList<>();
            customer.getPets().forEach(pet -> petIds.add(pet.getId()));
            customerDTO.setPetIds(petIds);
        }
        return customerDTO;
    }

    private Customer convertCustomerDTOToEntity(CustomerDTO customerDTO){
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        if(customerDTO.getPetIds() != null){
            List<Pet> pets = new ArrayList<>();
            customerDTO.getPetIds().forEach(petId -> pets.add(petRepository.findById(petId).get()));
            customer.setPets(pets);
        }
        return customer;
    }
}
