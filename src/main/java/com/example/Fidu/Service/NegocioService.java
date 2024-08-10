package com.example.Fidu.Service;

import com.example.Fidu.Entity.Negocio;
import com.example.Fidu.Entity.Obligacion;
import com.example.Fidu.Entity.Persona;
import com.example.Fidu.Repository.NegocioRepository;
import com.example.Fidu.Repository.ObligacionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NegocioService {
    @Autowired
    NegocioRepository negocioRepository;
    @Autowired
    ObligacionRepository obligacionRepository;
    @Transactional
    public List<Negocio> findAll() {
        return negocioRepository.findAll();
    }

    public Negocio findById(Long id) {
        return negocioRepository.findById(id).orElse(null);
    }


    @Transactional
    public Negocio save(Negocio negocio) {
        return negocioRepository.save(negocio);
    }

    @Transactional
    public void deleteById(Long id) {
        negocioRepository.deleteById(id);
    }
    @Transactional
    public ResponseEntity<Negocio> addObligacionToNegocio(Long negocioId, Long obligacionId) {

        Optional<Obligacion> obligacionOpt = obligacionRepository.findById(obligacionId);
        Optional<Negocio> negocioOpt = negocioRepository.findById(negocioId);

        if (obligacionOpt.isPresent() && negocioOpt.isPresent()) {
            Obligacion obligacion = obligacionOpt.get();
            Negocio negocio = negocioOpt.get();

            negocio.getObligaciones().add(obligacion);
            Negocio updatedNegocio = negocioRepository.save(negocio);
            return ResponseEntity.ok(updatedNegocio);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public Set<Obligacion> getObligacionesByNegocioId(Long negocioId) {
        Optional<Negocio> opt = negocioRepository.findById(negocioId);
        return opt.map(Negocio ::getObligaciones)
                .orElse(Collections.emptySet());
    }
    public Negocio updateNegocio(Long id, Negocio negocioDetails) {
        Negocio existingNegocio = findById(id);

        existingNegocio.setNombre(negocioDetails.getNombre());
        existingNegocio.setDescripcion(negocioDetails.getDescripcion());
        existingNegocio.setFechaInicio(negocioDetails.getFechaInicio());
        existingNegocio.setFechaFin(negocioDetails.getFechaFin());

        return negocioRepository.save(existingNegocio);
    }


}
