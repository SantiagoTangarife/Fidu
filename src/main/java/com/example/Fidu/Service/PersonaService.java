package com.example.Fidu.Service;

import com.example.Fidu.Entity.Negocio;
import com.example.Fidu.Entity.Persona;
import com.example.Fidu.Repository.NegocioRepository;
import com.example.Fidu.Repository.PersonaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PersonaService {
    @Autowired
    PersonaRepository personaRepository;

    @Autowired
    private NegocioRepository negocioRepository;

    public List<Persona> getAllPersonas() {
        return personaRepository.findAll();

    }

    public Persona getPersonaById(Long personaId) {
        return  personaRepository.findByNumeroDocumento(personaId);

    }

    public ResponseEntity<Persona> createPersona(Persona persona) {
        Persona createdPersona = personaRepository.save(persona);
        return ResponseEntity.ok(createdPersona);
    }

    public ResponseEntity<Persona> updatePersona(Long personaCc, Persona personaDetails) {
        Optional<Persona> opt = Optional.ofNullable(personaRepository.findByNumeroDocumento(personaCc));
        if (opt.isPresent()) {
            Persona persona = opt.get();
            persona.setNombre(personaDetails.getNombre());

            System.out.println(personaDetails.getApellido());//----------------

            persona.setApellido(personaDetails.getApellido());
            persona.setTipoDocumento(personaDetails.getTipoDocumento());
            persona.setNumeroDocumento(personaDetails.getNumeroDocumento());


            Persona updatedPersona = personaRepository.save(persona);
            return ResponseEntity.ok(updatedPersona);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> deletePersona(Long personaId) {
        Optional<Persona> opt = personaRepository.findById(personaId);
        if (opt.isPresent()) {
            personaRepository.delete(opt.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional
    public ResponseEntity<Persona> addNegocioToPersona(Long personaId, Long negocioId) {
        Optional<Persona> personaOpt = Optional.ofNullable(personaRepository.findByNumeroDocumento(personaId));

        Optional<Negocio> negocioOpt = negocioRepository.findById(negocioId);

        if (personaOpt.isPresent() && negocioOpt.isPresent()) {
            Persona persona = personaOpt.get();
            Negocio negocio = negocioOpt.get();

            persona.getNegocios().add(negocio);
            Persona updatedPersona = personaRepository.save(persona);
            return ResponseEntity.ok(updatedPersona);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public Set<Negocio> getNegociosByPersonaId(Long personaId) {
        Optional<Persona> opt = personaRepository.findById(personaId);
        return opt.map(Persona::getNegocios)
                .orElse(Collections.emptySet()); // Devuelve un conjunto vacío si la persona no se encuentra
    }
}



