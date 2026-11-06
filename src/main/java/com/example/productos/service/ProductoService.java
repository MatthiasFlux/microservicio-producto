package com.example.productos.service;

import com.example.productos.model.Producto;
import com.example.productos.repository.ProductoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producto no encontrado con el id: " + id));
    }

    public Producto crear(Producto producto) {
        validarProducto(producto);
        return productoRepository.save(producto);
    }

    public Producto actualizar(Long id, Producto productoActualizado) {
        Producto productoExistente = obtenerPorId(id);

        validarProducto(productoActualizado);

        productoExistente.setNombre(productoActualizado.getNombre());
        productoExistente.setPrecio(productoActualizado.getPrecio());
        productoExistente.setStock(productoActualizado.getStock());

        return productoExistente;
    }

    public void eliminar(Long id) {
        boolean eliminado = productoRepository.deleteById(id);
        if (!eliminado) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Producto no encontrado con el id: " + id);
        }
    }

    private void validarProducto(Producto producto) {
        if (producto == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El cuerpo de la petición no puede estar vacío.");
        }
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El nombre del producto no puede estar vacío.");
        }
        if (producto.getPrecio() == null || producto.getPrecio() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El precio debe ser mayor que 0.");
        }
        if (producto.getStock() == null || producto.getStock() < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El stock no puede ser negativo.");
        }
    }
}