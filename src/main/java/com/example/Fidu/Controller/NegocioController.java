package com.example.Fidu.Controller;

import com.example.Fidu.Entity.Negocio;
import com.example.Fidu.Entity.Obligacion;
import com.example.Fidu.Entity.Persona;
import com.example.Fidu.Service.NegocioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/negocios")
public class NegocioController {
    @Autowired
    private NegocioService negocioService;

    @GetMapping("/all")
    public List<Negocio> getAllNegocios() {
        return negocioService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Negocio> getNegocioById(@PathVariable Long id) {
        Optional<Negocio> negocio = Optional.ofNullable(negocioService.findById(id));
        return negocio.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/obligaciones/{id}")
    public ResponseEntity<Set<Obligacion>> getObligacionesByNegocioId(@PathVariable Long id) {
        Optional<Negocio> opt=Optional.ofNullable((Negocio) negocioService.getObligacionesByNegocioId(id));

        return opt.map(negocio -> ResponseEntity.ok(negocio.getObligaciones()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptySet()));

    }
    @PostMapping("/{negocioId}/obligaciones/{obligacionId}")
    public ResponseEntity<Negocio> addObligacionToNegocio(@PathVariable Long negocioId, @PathVariable Long obligacionId) {
        return negocioService.addObligacionToNegocio(negocioId, obligacionId);
    }

    @PostMapping("/save")
    public Negocio createNegocio(@RequestBody Negocio negocio) {
        return negocioService.save(negocio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Negocio> updateNegocio(@PathVariable Long id, @RequestBody Negocio negocioDetails) {
        Negocio negocioOptional = negocioService.findById(id);

        if (negocioOptional!=null) {
            Negocio negocio = negocioOptional;
            negocio.setNombre(negocioDetails.getNombre());
            negocio.setDescripcion(negocioDetails.getDescripcion());
            negocio.setFechaInicio(negocioDetails.getFechaInicio());
            negocio.setFechaFin(negocioDetails.getFechaFin());

            Negocio updatedNegocio = negocioService.save(negocio);
            return ResponseEntity.ok(updatedNegocio);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNegocio(@PathVariable Long id) {
        if (negocioService.findById(id)!=null) {
            negocioService.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}
