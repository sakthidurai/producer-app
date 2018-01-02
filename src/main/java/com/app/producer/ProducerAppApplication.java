package com.app.producer;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@EnableEurekaClient
@ComponentScan("com.app.*")
class ProducerAppApplication {
	
	@Bean	
	CommandLineRunner commandLineRunner(ProducerRepository repository) {
		return args -> {
			Arrays.asList("sakthi,anush,nila".split(",")).forEach(value -> repository.save(new Producer((String)value)));
			repository.findAll().forEach(System.out::println);
		};
		
	}
	
	public static void main(String[] args) {
		SpringApplication.run(ProducerAppApplication.class, args);
	}
}

@RestController
@RequestMapping("/producer")
class ProducerAppController {

	@Autowired
	private ProducerAppService producerAppService;
	
	@RequestMapping(value = "/receiveMessage", method= RequestMethod.GET, produces ="application/JSON")
	public List<Producer> getEmployee() throws Exception {
		return producerAppService.getInfo();
	}
}

@Service
class ProducerAppService {
	
	@Autowired
	ProducerRepository repository;
	
	public List<Producer> getInfo() throws Exception {
		
		return repository.findAll();

	}
}

@Entity
@Table(name = "PRODUCER")
class Producer{
	
	public Producer(){
		
	}
	public Producer(String name){
		this.name = name;
	}

	
	@Id
	@GeneratedValue
	@Column (name = "ID")
	Long id;
	
	@Column (name = "NAME")
	String name;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "Producer [id=" + id + ", name=" + name + "]";
	}
	
}

@RepositoryRestResource
interface ProducerRepository extends JpaRepository<Producer, Long>{

	
}
