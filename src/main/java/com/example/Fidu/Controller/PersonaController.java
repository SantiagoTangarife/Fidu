package com.example.Fidu.Controller;

import com.example.Fidu.Entity.Negocio;
import com.example.Fidu.Entity.Persona;
import com.example.Fidu.Service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@RestController
@RequestMapping("/api/personas")
@Controller
public class PersonaController {
    @Autowired
    private PersonaService personaService;

    @GetMapping("/all")
    public ResponseEntity<List<Persona>> getAllPersonas() {
        return ResponseEntity.ok(personaService.getAllPersonas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Persona> getPersonaById(@PathVariable Long id) {

        Optional<Persona> opt = Optional.ofNullable(personaService.getPersonaById(id));
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Persona> createPersona(@RequestBody Persona persona) {
        return personaService.createPersona(persona);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Persona> updatePersona(@PathVariable Long id, @RequestBody Persona personaDetails) {
        return personaService.updatePersona(id, personaDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersona(@PathVariable Long id) {
        return personaService.deletePersona(id);
    }

    @PostMapping("/{personaId}/negocios/{negocioId}")
    public ResponseEntity<Persona> addNegocioToPersona(@PathVariable Long personaId, @PathVariable Long negocioId) {
        return personaService.addNegocioToPersona(personaId, negocioId);
    }

    @GetMapping("/negocios/{personaId}")
    public ResponseEntity<Set<Negocio>> getNegociosByPersonaId(@PathVariable Long personaId) {
        Optional<Persona> opt = Optional.ofNullable((Persona) personaService.getNegociosByPersonaId(personaId));

        return opt.map(persona -> ResponseEntity.ok(persona.getNegocios()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptySet()));
    }


}