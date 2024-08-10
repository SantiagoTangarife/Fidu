package com.example.Fidu.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="obligacion" )

public class Obligacion {
    @Id
    @Column(name = "idobligacion", length = 20)
    Long obligacionId;
    String descripcion;
    Float monto;
    Date fechaVencimiento;
}
