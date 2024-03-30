package com.coderscampus.assignment13.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.service.UserService;

@Controller
public class AccountController {

	@Autowired
	private UserService userService;
	
	@GetMapping("users/{userId}/accounts")
	public String getCreateAccount(ModelMap model, @PathVariable Long userId) {
		model.put("account", new Account());
		model.put("userId", userId);
		
		return "account-view";
	}
	
	@PostMapping("users/{userId}/accounts")
	public String postCreateAccount(@PathVariable Long userId) {
		userService.createAccount(userId);
		return "redirect:/users/" + userId;
	}
	
	@GetMapping("/users/{userId}/accounts/{accountId}")
	public String getSingleAccount(ModelMap model, @PathVariable Long userId, @PathVariable Long accountId) {
		model.put("user", this.userService.findById(userId));
		model.put("account", userService.findAccountById(accountId));
		
		return "account-view";
	}
	
	@PostMapping("/users/{userId}/accounts/{accountId}")
	public String updateSingleAccount(@PathVariable Long userId, @PathVariable Long accountId, @ModelAttribute Account account) {
		Account existingAccount = userService.findAccountById(accountId);
		
		if (existingAccount != null) {
			existingAccount.setAccountName(account.getAccountName());
			userService.saveAccount(existingAccount);
			return "redirect:/users/" + userId + "/accounts/" + existingAccount.getAccountId();
		}
		
		return "redirect:/error.html";
	}
}
