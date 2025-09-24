package mmb.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mmb.config.CustomUserService;

@Component
public class JwtAuthFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private CustomUserService userDetailsService;
	
	
//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//			throws ServletException, IOException {
//		
//		String authHeader=request.getHeader("Authorization");
//		
//		String token=null;
//		String username=null;
//		
//		if(authHeader!=null && authHeader.startsWith("Bearer")) {
//			token=authHeader.substring(7);
//			username = jwtService.extractUsername(token);
//		}
//		if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
//			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//			if(jwtService.validateToken(token, username)) {
//				UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
//				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//				SecurityContextHolder.getContext().setAuthentication(authToken);
//			}
//		}
//		
//		filterChain.doFilter(request, response);
//		
//	}
	
	//working code
	
//	  @Override
//	    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//	            throws ServletException, IOException {
//
//	        HttpSession session = request.getSession();
//	        String token = (String) session.getAttribute("token"); // Retrieve the token from the session
//	        String username = null;
//
//	        // Check if the token exists in the session
//	        if (token != null) {
//	            username = jwtService.extractUsername(token); // Extract the username from the token
//	        }
//
//	        // If the username is present and no authentication exists, proceed to authenticate
//	        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//	            UserDetails userDetails = userDetailsService.loadUserByUsername(username); // Load user details
//	            
//	            // Validate the token for the extracted username
//	            if (jwtService.validateToken(token, username)) {
//	                UsernamePasswordAuthenticationToken authToken = 
//	                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//	                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // Set additional details
//	                SecurityContextHolder.getContext().setAuthentication(authToken); // Set the authentication in context
//	            }
//	        }
//
//	        // Proceed with the next filter in the chain
//	        filterChain.doFilter(request, response);
//	    }
	
	@Override
	protected void doFilterInternal(HttpServletRequest request,
	                                HttpServletResponse response,
	                                FilterChain filterChain)
	        throws ServletException, IOException {

	    String token = null;
	    String username = null;

	    // 🔹 1. Try to read token from Authorization header (REST API)
	    String authHeader = request.getHeader("Authorization");
	    if (authHeader != null && authHeader.startsWith("Bearer ")) {
	        token = authHeader.substring(7); // remove "Bearer "
	    }

	    // 🔹 2. If not found, try HttpSession (Browser flow)
	    if (token == null) {
	        HttpSession session = request.getSession(false);
	        if (session != null) {
	            token = (String) session.getAttribute("token");
	        }
	    }

	    // 🔹 3. Validate token and extract username
	    if (token != null) {
	        try {
	            username = jwtService.extractUsername(token);
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	    }

	    // 🔹 4. Authenticate user if not already authenticated
	    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

	        if (jwtService.validateToken(token, username)) {
	            UsernamePasswordAuthenticationToken authToken =
	                    new UsernamePasswordAuthenticationToken(
	                            userDetails, null, userDetails.getAuthorities());

	            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	            SecurityContextHolder.getContext().setAuthentication(authToken);

	        }
	    }

	    // 🔹 5. Continue filter chain
	    filterChain.doFilter(request, response);
	}


}
