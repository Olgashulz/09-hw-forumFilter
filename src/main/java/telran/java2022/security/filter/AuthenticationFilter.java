package telran.java2022.security.filter;

import java.io.IOException;
import java.util.Base64;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import telran.java2022.accounting.dao.UserAccountRepository;
import telran.java2022.accounting.dto.exceptions.UserNotFoundException;
import telran.java2022.accounting.model.UserAccount;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements Filter {
	
	final UserAccountRepository userAccountRepository;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		if (checkEndPoint(request.getMethod(), request.getServletPath())) {
			String token = request.getHeader("Authorization");
			if(token == null) {
				response.sendError(401, "Token not exist");
				return;
			}
			String[] credentials = getCredentialsFromToken(token);
			UserAccount user = userAccountRepository.findById(credentials[0]).orElseThrow(() -> new UserNotFoundException(credentials[0]));
		    if(!user.getLogin().equals(credentials[0]) || !user.getPassword().equals(credentials[1])) {
		    	response.sendError(401, "Incorrect login or password");
				return;
		    }		
		}
//		System.out.println(request.getHeader("Authorization"));
//		System.out.println(request.getMethod());
//		System.out.println(request.getServletPath());
		chain.doFilter(request, response);
	}

	private String[] getCredentialsFromToken(String token) {
		String[] basicAuth = token.split(" ");
		String decode = new String(Base64.getDecoder().decode(basicAuth[1]));
		String[] credentials = decode.split(":");
		return credentials;
	}

	private boolean checkEndPoint(String method, String servletPath) {
		return !("POST".equalsIgnoreCase(method) && servletPath.equals("/account/register"));
	}

}
