package telran.java2022.accounting.controller;

import java.util.Base64;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Basic;

import lombok.RequiredArgsConstructor;
import telran.java2022.accounting.dto.RolesResponseDto;
import telran.java2022.accounting.dto.UserAccountResponseDto;
import telran.java2022.accounting.dto.UserRegisterDto;
import telran.java2022.accounting.dto.UserUpdateDto;
import telran.java2022.accounting.service.UserAccountService;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class UserAccountController {

	final UserAccountService accountService;

	@PostMapping("/register")
	public UserAccountResponseDto register(@RequestBody UserRegisterDto userRegisterDto) {
		return accountService.addUser(userRegisterDto);
	}

	@PostMapping("/login")
	public UserAccountResponseDto login(@RequestHeader("Authorization") String token) {
		String[] basicAuth = token.split(" ");
		String decode = new String(Base64.getDecoder().decode(basicAuth[1]));
		String[] credentials = decode.split(":");
		return accountService.getUser(credentials[0]);
//		System.out.println(token);
//		System.out.println(basicAuth[0]);
//		System.out.println(basicAuth[1]);
//		System.out.println(decode);
//		return null;
	}

	@DeleteMapping("/user/{login}")
	public UserAccountResponseDto removeUser(@PathVariable String login) {
		return accountService.removeUser(login);
	}

	@PutMapping("/user/{login}")
	public UserAccountResponseDto updateUser(@PathVariable String login, @RequestBody UserUpdateDto userUpdateDto) {
		return accountService.editUser(login, userUpdateDto);
	}

	@PutMapping("/user/{login}/role/{role}")
	public RolesResponseDto addRole(@PathVariable String login, @PathVariable String role) {
		return accountService.changeRolesList(login, role, true);
	}

	@DeleteMapping("/user/{login}/role/{role}")
	public RolesResponseDto removeRole(@PathVariable String login, @PathVariable String role) {
		return accountService.changeRolesList(login, role, false);
	}

//	@PutMapping("/password")
//	public void changePassword() {
//		
//	}

}
