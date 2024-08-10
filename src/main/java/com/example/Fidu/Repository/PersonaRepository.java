package com.example.Fidu.Repository;

import com.example.Fidu.Entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends JpaRepository<Persona,Long> {
    Persona findByNumeroDocumento(Long numeroDocumento);
}
