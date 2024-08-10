package com.example.Fidu.Service;

import com.example.Fidu.Entity.Obligacion;
import com.example.Fidu.Repository.ObligacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ObligacionService {
    @Autowired
    private ObligacionRepository obligacionRepository;

    public List<Obligacion> getAllObligaciones() {
        return obligacionRepository.findAll();
    }

    public Obligacion getObligacionById(Long obligacionId) {
        return obligacionRepository.findById(obligacionId).orElse(null);
    }


    public Obligacion createObligacion(Obligacion obligacion) {
        return obligacionRepository.save(obligacion);
    }

    public ResponseEntity<Obligacion> updateObligacion(Long obligacionId, Obligacion obligacionDetails) {
        Obligacion obligacion = obligacionRepository.findById(obligacionId)
                .orElseThrow();

        obligacion.setDescripcion(obligacionDetails.getDescripcion());
        obligacion.setMonto(obligacionDetails.getMonto());
        obligacion.setFechaVencimiento(obligacionDetails.getFechaVencimiento());

        return ResponseEntity.ok(obligacionRepository.save(obligacion));
    }

    public  void deleteObligacion(Long obligacionId) {
        Obligacion obligacion = obligacionRepository.findById(obligacionId)
                .orElseThrow();

        obligacionRepository.delete(obligacion);
    }
}