package com.coderscampus.assignment13.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.Address;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.repository.AccountRepository;
import com.coderscampus.assignment13.repository.AddressRepository;
import com.coderscampus.assignment13.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private AddressRepository addressRepo;
	@Autowired
	private AccountRepository accountRepo;

	public Set<User> findAll() {
		List<User> userList = new ArrayList<>(userRepo.findAllUsersWithAccountsAndAddresses());
		Set<User> distinctUsers = new HashSet<>(userList);
		return distinctUsers;
	}

	public User findById(Long userId) {
		Optional<User> userOpt = userRepo.findById(userId);
		return userOpt.orElse(new User());
	}

	public User saveUser(User user) {

		User existingUser = findById(user.getUserId());

		if (existingUser != null) {

			if (user.getUsername() != null) {
				existingUser.setUsername(user.getUsername());
			}
			if (user.getName() != null) {
				existingUser.setName(user.getName());
			}
			if (!user.getPassword().isEmpty()) {
				existingUser.setPassword(user.getPassword());
			}
			return userRepo.save(existingUser);
		} else {
			return userRepo.save(user);
		}
	}

//	public User saveUser(User user) {
//		Address address = new Address();
//		if (user.getUserId() == null) {
//			System.out.println("User ID was Null");
//
//		} else if (address.getUserId() == null) {
//			user.setAddress(address);
//			address.setAddressLine1(user.getAddress().getAddressLine1());
//			address.setAddressLine2(user.getAddress().getAddressLine2());
//			address.setCity(user.getAddress().getCity());
//			address.setRegion(user.getAddress().getRegion());
//			address.setCountry(user.getAddress().getCountry());
//			address.setZipCode(user.getAddress().getZipCode());
//			address.setUserId(user.getUserId());
//			address.setUser(user);
//			user.setAddress(address);
//		}
//		return userRepo.save(user);
//	}

	public User saveUser(User user, Address address) {

		user.setAddress(address);
		address.setUser(user);

		addressRepo.save(address);

		return userRepo.save(user);
	}

	public void delete(Long userId) {
		userRepo.deleteById(userId);
	}

	public Account createAccount(Long userId) {
		User user = findById(userId);

		if (user != null) {
			Account newAccount = new Account();
			newAccount.getUsers().add(user);
			newAccount.setAccountName("Account #" + user.getAccounts().size());
			user.getAccounts().add(newAccount);
			userRepo.save(user);
			accountRepo.save(newAccount);

			return newAccount;
		}

		return null;
	}

	public Account findAccountById(Long accountId) {
		Optional<Account> accountOpt = accountRepo.findById(accountId);
		return accountOpt.orElse(null);
	}

	public Account saveAccount(Account account) {
		return accountRepo.save(account);

	}

}
