package com.barshid.schematech.controller;


import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@EnableResourceServer
@RestController
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter{
//	private SecurityFilter securityFilter;
//	@Autowired
//	public void setSecurityFilter(SecurityFilter securityFilter) {
//		this.securityFilter = securityFilter;
//	}
//	private CustomSecFilter customSecFilter;
//	@Autowired
//	public void setCustomSecFilter(CustomSecFilter customSecFilter) {
//		this.customSecFilter = customSecFilter;
//	}

	@RequestMapping("/")
	public String index() {
		return "Welcome to schematek";
	}
	@RequestMapping("/publica")
	public String publico() {
		return "Pagina Publica";
	}
	@RequestMapping("/private")
	public String privada() {
		return "Pagina Private";
	}
	@RequestMapping("/admin")
	public String admin() {
		return "Pagina Administrador";
	}
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/oauth/token", "/oauth/authorize**", "/publica","/webhook").permitAll();
//			 .anyRequest().authenticated();

//		http.addFilterBefore( this.customSecFilter, UsernamePasswordAuthenticationFilter.class);// new WebAsyncManagerIntegrationFilter())

//		String encodedClientData =
//				Base64Utils.encodeToString("bael-client-id:bael-secret".getBytes());

//		http.oauth2Login().authorizationEndpoint(). baseUri()
		http.authorizeRequests().antMatchers("/user").permitAll();//.anonymous(); //.permitAll()
//		http.authorizeRequests().antMatchers("/user/phoneLogin").permitAll().and().headers("Authorization", "Basic " + encodedClientData);//.anonymous(); //.permitAll()
		http.authorizeRequests().antMatchers("/user/phoneLogin").permitAll();
		http.authorizeRequests().antMatchers("/user/forgetPassword").permitAll();
//		http.authorizeRequests().antMatchers("/static/script.js").permitAll();
//		http.authorizeRequests().antMatchers("/user/setNewPassword").permitAll();

		//Deny access to "/private"
		http.requestMatchers().antMatchers("/private")
				.and().authorizeRequests()
				.antMatchers("/private").access("hasRole('USER')")

				.and().requestMatchers().antMatchers(
						"/user/**",
								"/questionnaire/**",
								"/therapist/**",
								  "/acstoken/**")
				.and().authorizeRequests()
				.antMatchers("/user/**").access("hasRole('USER')")
				.antMatchers("/questionnaire/**").access("hasRole('USER')")
				.antMatchers("/therapist/**").access("hasRole('USER')")

				//Deny access to "/admin"
				.and().requestMatchers().antMatchers("/admin")
				.and().authorizeRequests()
				.antMatchers("/admin").access("hasRole('ADMIN')")
//				.and().exceptionHandling()
		;
	}

}

