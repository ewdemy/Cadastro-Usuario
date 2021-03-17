package com.mrcruz.api.controller;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.mrcruz.api.model.Usuario;
import com.mrcruz.api.repository.UsuarioRepository;


@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
	
	@Autowired
	private UsuarioRepository repository;
	SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Usuario adicionar(@Valid @RequestBody Usuario usuario) {
		return repository.save(usuario);
	}
	
	@GetMapping("/{id}")
	public Usuario buscar(@PathVariable Long id) {
		Usuario usuario = repository.findById(id).orElseThrow(() -> new EntityNotFoundException());
		
		return usuario;
	}
	
	@GetMapping
	public List<Usuario> listar(){
		return repository.findAll();
	}
	
	@PutMapping("/{id}")
	public Usuario alterar(@PathVariable Long id, @RequestBody Usuario usuario) {
		Optional<Usuario> usuarioExiste = repository.findById(id);
		if(usuarioExiste.isPresent()) {
			if(id.equals(usuario.getId())) {
				return repository.save(usuario);
			} else {
				throw new UnsupportedOperationException("Id informado diferente do Usuario!");
			}
		} else {
			throw new EntityNotFoundException();
		}
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Long id) {
		if(repository.existsById(id)) {
			repository.deleteById(id);
		} else {
			throw new EntityNotFoundException();
		}
	}
	
	
	
	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public void notFound() {}
	
	@ExceptionHandler(UnsupportedOperationException.class)
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	public String unsuported(UnsupportedOperationException ex) {
		return ex.getMessage();
	}
	

}
