package com.coderscampus.assignment13.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coderscampus.assignment13.domain.Address;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.repository.AddressRepository;
import com.coderscampus.assignment13.repository.UserRepository;

@Service
public class AddressService {
	private UserRepository userRepository;

	@Autowired
	private AddressRepository addressRepo;
	
	@Autowired
	public AddressService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public void updateUserAddress(Long userId, Address updatedAddress) {
		User existingUser = userRepository.findById(userId).orElse(null);
		
		if (existingUser != null) {
			updateAddress(existingUser, updatedAddress);
			userRepository.save(existingUser);
		}
	}
	
	private void updateAddress(User user, Address updatedAddress) {
		Address existingAddress = user.getAddress();
		
		if (existingAddress != null) {
			existingAddress.setAddressLine1(updatedAddress.getAddressLine1());
			existingAddress.setAddressLine2(updatedAddress.getAddressLine2());
			existingAddress.setCity(updatedAddress.getCity());
			existingAddress.setRegion(updatedAddress.getRegion());
			existingAddress.setCountry(updatedAddress.getCountry());
			existingAddress.setZipCode(updatedAddress.getZipCode());
		} else {
			Address newAddress = new Address();
			
			newAddress.setAddressLine1(updatedAddress.getAddressLine1());
			newAddress.setAddressLine2(updatedAddress.getAddressLine2());
			newAddress.setCity(updatedAddress.getCity());
			newAddress.setRegion(updatedAddress.getRegion());
			newAddress.setCountry(updatedAddress.getCountry());
			newAddress.setZipCode(updatedAddress.getZipCode());
			user.setAddress(newAddress);
		}
	}
	
	public Address findAddressById(User user) {
		Long userId = user.getUserId();
		Address address = addressRepo.findAddressByUserId(userId);
		if (address == null) {
			address = new Address();
		}
		
		return address;
	}
}
