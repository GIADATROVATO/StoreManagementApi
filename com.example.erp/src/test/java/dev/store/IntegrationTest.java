package dev.store;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import dev.store.entity.Cliente;
import dev.store.repository.ClienteRepository;

@SpringBootTest					
@AutoConfigureMockMvc
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class) 	se volessi avere un ordine nei test 
public class IntegrationTest {
	@Autowired					
	private MockMvc mockMvc; 
	@Autowired
	private ClienteRepository clienteRepo;
	
	@BeforeEach
	void clean() {
		clienteRepo.deleteAll();
	}
	
	@Test
	void shouldReturnClienteWhenIdExists() throws Exception{
		
		
		Cliente c= new Cliente();
		c.setNome("Mario");
		c.setEmail("mario@test.com");
		  
		Cliente saved= clienteRepo.save(c);
		
		mockMvc.perform(get("/api/clienti/" +saved.getId()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.nome").value("Mario"))
			.andExpect(jsonPath("$.email").value("mario@test.com"));
	}
	@Test
	void shouldReturn404WhenClienteDoesNotExists() throws Exception {
		mockMvc.perform(get("/api/clienti/99"))
			.andExpect(status().isNotFound());
	}
	@Test
	void shouldCreateCliente() throws Exception{

	    String json = """
	        {
	            "nome": "Mario",
	            "email": "mario@test.com"
	        }
	    """;

	    mockMvc.perform(post("/api/clienti")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(json))
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$.nome").value("Mario"))
	        .andExpect(jsonPath("$.email").value("mario@test.com"));
	}
	
	@Test
	void shouldReturnAllClienti() throws Exception {
		Cliente c1= new Cliente();
		c1.setNome("Mario");
		Cliente c= new Cliente();
		c.setNome("Matteo");
		  
		clienteRepo.save(c);
		clienteRepo.save(c1);
		
		mockMvc.perform(get("/api/clienti"))
			.andExpect(status().isOk())
		    .andExpect(jsonPath("$.length()").value(2));
		
	}
	
	
	@Test
	void shouldDeleteCliente() throws Exception {

	    Cliente c = new Cliente();
	    c.setNome("Mario");
	    Cliente saved= clienteRepo.save(c);
	    
	    mockMvc.perform(delete("/api/clienti/"+saved.getId()))
	    		.andExpect(status().isOk());
	}
	
	/*
	 * properties :
	 * 	 	spring.datasource.url=jdbc:h2:mem:testdb
	 * 		spring.jpa.hibernate.ddl-auto=create-drop
	 */
}
