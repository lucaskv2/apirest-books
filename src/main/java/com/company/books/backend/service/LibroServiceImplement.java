package com.company.books.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.books.backend.model.Libro;
import com.company.books.backend.model.dao.ILibroDao;
import com.company.books.backend.response.LibroResponseRest;

@Service
public class LibroServiceImplement implements ILibroService{
	private static final Logger Log = LoggerFactory.getLogger(LibroServiceImplement.class);
	
	@Autowired
	private ILibroDao libroDao;

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<LibroResponseRest> buscarLibros() {
		
		Log.info("Inicio metodo buscarCategorias()");
		
		LibroResponseRest response = new LibroResponseRest();
		
		try {
			
			List<Libro> libro = (List<Libro>) libroDao.findAll();
			
			response.getLibroResponse().setLibro(libro);
			
			response.setMetadata("Respuesta ok ", "00", "Respuesta Exitosa");
			
		} catch (Exception e) {
			response.setMetadata("Respuesta no ok", "-1", "Error al consultar categorias");
			Log.error("Error al consultar categorias: ", e.getMessage());
			e.getStackTrace();
			return new ResponseEntity<LibroResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<LibroResponseRest>(response, HttpStatus.OK); 
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<LibroResponseRest> buscarPorId(Long id) {
		Log.info("Inicio de metodo buscarPorId");
		
		LibroResponseRest response = new LibroResponseRest();
		
		List<Libro> list = new ArrayList<>();
		
		try {
			Optional<Libro> libro = libroDao.findById(id);
			
			if(libro.isPresent()) {
				list.add(libro.get());
				response.getLibroResponse().setLibro(list);
			}else {
				Log.error("Error al consultar libro");
				response.setMetadata("Respuesta no ok", "-1", "Libro no encontrado");
				return new ResponseEntity<LibroResponseRest>(response, HttpStatus.NOT_FOUND);
				
			}
		} catch (Exception e) {
			Log.error("Error al consultar libro");
			response.setMetadata("Respuesta no ok", "-1", "Error al consultar libro");
			return new ResponseEntity<LibroResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<LibroResponseRest>(response, HttpStatus.OK);
	}

	@Override
	@Transactional
	public ResponseEntity<LibroResponseRest> crear(Libro libro) {
		Log.info("Inicio de metodo crear Libro");
		
		LibroResponseRest response = new LibroResponseRest();
		List<Libro> list = new ArrayList<>();
		
		try {
			Libro libroGuardado = libroDao.save(libro);
			if(libroGuardado != null) {
				list.add(libroGuardado);
				response.getLibroResponse().setLibro(list);
			}else {
				Log.error("Error al grabar libro");
				response.setMetadata("Respuesta no ok", "-1", "Libro no guaradado");
				return new ResponseEntity<LibroResponseRest>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			Log.error("Error al grabar libro");
			response.setMetadata("Respuesta no ok", "-1", "Libro no guaradado");
			return new ResponseEntity<LibroResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.setMetadata("Respuesta ok", "00", "Libro guaradado");

		return new ResponseEntity<LibroResponseRest>(response, HttpStatus.OK);
	}

	@Override
	@Transactional
	public ResponseEntity<LibroResponseRest> actualizar(Libro libro, Long id) {
		Log.info("Inicio metodo actualizar");
		LibroResponseRest response = new LibroResponseRest();
		List<Libro> list = new ArrayList<>();
		
		try {
			Optional<Libro> libroBuscado = libroDao.findById(id);
			if(libroBuscado.isPresent()) {
				libroBuscado.get().setName(libro.getName());
				libroBuscado.get().setDescription(libro.getDescription());
				
				Libro libroActualizar = libroDao.save(libroBuscado.get());
				if(libroActualizar != null) {
					response.setMetadata("Respuesta ok", "00", "Libro actualizado");
					list.add(libroActualizar);
					response.getLibroResponse().setLibro(list);
				}else {
					Log.error("Error en actualizar Libro");
					response.setMetadata("Respuesta no ok", "-1", "Libro no actualizado");
					return new ResponseEntity<LibroResponseRest>(response, HttpStatus.BAD_REQUEST);
				}
			}else {
				Log.error("Error en actualizar Libro");
				response.setMetadata("Respuesta no ok", "-1", "Libro no actualizado");
				return new ResponseEntity<LibroResponseRest>(response, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			Log.error("Error en actualizar Libro", e.getMessage());
			e.getStackTrace();
			response.setMetadata("Respuesta no ok", "-1", "Libro no actualizado");
			return new ResponseEntity<LibroResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
		return new ResponseEntity<LibroResponseRest>(response, HttpStatus.OK);
	}

	@Override
	@Transactional
	public ResponseEntity<LibroResponseRest> eliminar(Long id) {
		Log.info("Inicio metodo actualizar");
		LibroResponseRest response = new LibroResponseRest();

		
		try {
			libroDao.deleteById(id);
			response.setMetadata("Respuesta ok", "00", "Libro ELIMINADO");
		} catch (Exception e) {
			Log.error("Error en ELIMINAR Libro", e.getMessage());
			e.getStackTrace();
			response.setMetadata("Respuesta no ok", "-1", "Libro no ELIMINADO");
			return new ResponseEntity<LibroResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<LibroResponseRest>(response, HttpStatus.OK);
	}
}
