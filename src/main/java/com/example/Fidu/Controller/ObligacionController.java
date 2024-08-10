package com.example.Fidu.Controller;

import com.example.Fidu.Entity.Negocio;
import com.example.Fidu.Entity.Obligacion;
import com.example.Fidu.Service.ObligacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/obligaciones")
public class ObligacionController {
    @Autowired
    private ObligacionService obligacionService;

    @GetMapping
    public List<Obligacion> getAllObligaciones() {
        return obligacionService.getAllObligaciones();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Obligacion> getObligacionById(@PathVariable Long id) {
        Optional<Obligacion> obligacion = Optional.ofNullable(obligacionService.getObligacionById(id));
        return obligacion.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PostMapping
    public Obligacion createObligacion(@RequestBody Obligacion obligacion) {
        return obligacionService.createObligacion(obligacion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Obligacion> updateObligacion(@PathVariable Long id, @RequestBody Obligacion obligacionDetails) {
        return obligacionService.updateObligacion(id, obligacionDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteObligacion(@PathVariable Long id) {
        obligacionService.deleteObligacion(id);
    }


}