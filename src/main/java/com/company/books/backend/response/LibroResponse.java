package com.company.books.backend.response;

import java.util.List;

import com.company.books.backend.model.Libro;

public class LibroResponse {
	private List<Libro> Libro;

	public List<Libro> getLibro() {
		return Libro;
	}

	public void setLibro (List<Libro> Libro) {
		this.Libro = Libro;
	}
}
