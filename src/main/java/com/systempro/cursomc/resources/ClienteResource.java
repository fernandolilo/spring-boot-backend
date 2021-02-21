package com.systempro.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.systempro.cursomc.domain.Cliente;
import com.systempro.cursomc.dto.ClienteDTO;
import com.systempro.cursomc.dto.ClienteNewDTO;
import com.systempro.cursomc.services.ClienteService;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {
	@Autowired
	private ClienteService service;

	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public ResponseEntity<Cliente> find(@PathVariable Integer id) {		
		Cliente obj = service.find(id);			
		return ResponseEntity.ok().body(obj);
		
	}
	
	// metodo de inseriri dados
		@RequestMapping(method = RequestMethod.POST) //
		public ResponseEntity<Void> insert(@Valid @RequestBody ClienteNewDTO objDto) {
			Cliente obj = service.fromDTO(objDto);
			obj = service.insert(obj);
			URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
			return ResponseEntity.created(uri).build();
		}

		// metodo de atualização... UPDATE.
		@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
		public ResponseEntity<Void> update(@RequestBody @Valid ClienteNewDTO objDto, @PathVariable Integer id) {
			Cliente obj = service.fromDTO(objDto);
			obj.setId(id);
			obj = service.update(obj);
			return ResponseEntity.noContent().build();
		}

		// metodo de busca por dados
		@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
		public ResponseEntity<Void> delete(@PathVariable Integer id) {
			service.delete(id);
			return ResponseEntity.noContent().build();
		}

		// metodo de busca por todas categorias
		@RequestMapping(method = RequestMethod.GET)
		public ResponseEntity<List<ClienteDTO>> findAll() {
			List<Cliente> obj = service.findAll();
			List<ClienteDTO> listadto = obj.stream().map(dados -> new ClienteDTO(dados)).collect(Collectors.toList());
			return ResponseEntity.ok().body(listadto);
		}

		// metodo de busca por paginas
		@RequestMapping(value = "/page", method = RequestMethod.GET)
	    public ResponseEntity<Page<ClienteDTO>> findPage(
	            @RequestParam(value="page", defaultValue="0") Integer page,
	            @RequestParam(value="linesPerPAge", defaultValue="24") Integer linesPerPAge,
	            @RequestParam(value="orderBy", defaultValue="nome") String orderBy,
	            @RequestParam(value="direction", defaultValue="ASC") String direction) {
	        Page<Cliente> list = service.findPage(page, linesPerPAge, orderBy, direction);
	        Page<ClienteDTO> listDTO = list.map(obj -> new ClienteDTO(obj));

	        return ResponseEntity.ok().body(listDTO);
	    }
		
}
