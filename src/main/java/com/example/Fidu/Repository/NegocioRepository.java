package com.example.Fidu.Repository;

import com.example.Fidu.Entity.Negocio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NegocioRepository extends JpaRepository<Negocio,Long> {

}
