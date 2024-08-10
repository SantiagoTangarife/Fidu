package com.example.Fidu.Repository;

import com.example.Fidu.Entity.Obligacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObligacionRepository extends JpaRepository<Obligacion,Long> {
}
