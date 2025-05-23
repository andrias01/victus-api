package co.edu.uco.easy.victusresidencias.victus_api.dao;

import java.util.List;

interface RetrieveDAO<T,I> {
	T fingByID(I id);
	List<T> findAll();
	List<T> findByFilter(T filter);

}
