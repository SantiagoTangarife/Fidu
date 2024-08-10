package com.example.Fidu.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="negocio" )

public class Negocio {
    @Id
    @Column(name = "idnedocio", length = 20)

    Long negocioId;
    String nombre;
    String descripcion;
    Date fechaInicio;
    Date fechaFin;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "negocio_obligacion",
            joinColumns = @JoinColumn(name = "negocioId"),
            inverseJoinColumns = @JoinColumn(name = "obligacionId")
    )
    private Set<Obligacion> obligaciones = new HashSet<>();

}
