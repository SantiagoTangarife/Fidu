package com.example.Fidu.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="persona" )

public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "personaId")
    Long personaId;
    @Column(name = "numerodocumento", length = 20, unique = true)
    Long numeroDocumento;

    String nombre;
    String apellido;
    String tipoDocumento;

    @ManyToMany(fetch = FetchType.EAGER) // Cambio a EAGER por velocidad
    @JoinTable(
            name = "persona_negocio",
            joinColumns = @JoinColumn(name = "personaId", referencedColumnName = "personaId"),
            inverseJoinColumns = @JoinColumn(name = "negocioId")
    )

    private Set<Negocio> negocios=new HashSet<>();


}
