package bet.pobrissimo.core.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface BaseController<T> {

    @PostMapping
    ResponseEntity<?> create(@RequestBody @Valid T entity);

    @PutMapping
    ResponseEntity<?> update(@RequestBody @Valid T entity);

    @GetMapping
    ResponseEntity<?> findById(Long id);

    @GetMapping
    ResponseEntity<?> findAllPageable();

    @DeleteMapping
    ResponseEntity<?> delete(@RequestBody @Valid T entity);

}
