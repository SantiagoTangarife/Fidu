-- Crear la tabla 'negocio'
CREATE TABLE negocio (
                         idnedocio BIGINT(20) NOT NULL,
                         nombre VARCHAR(255),
                         descripcion TEXT,
                         fechaInicio DATE,
                         fechaFin DATE,
                         PRIMARY KEY (idnedocio)
);

-- Crear la tabla 'obligacion'
CREATE TABLE obligacion (
                            idobligacion BIGINT(20) NOT NULL,
                            descripcion TEXT,
                            monto FLOAT,
                            fechaVencimiento DATE,
                            PRIMARY KEY (idobligacion)
);

-- Crear la tabla 'persona'
CREATE TABLE persona (
                         personaId BIGINT(20) NOT NULL AUTO_INCREMENT,
                         numerodocumento BIGINT(20) UNIQUE,
                         nombre VARCHAR(255),
                         apellido VARCHAR(255),
                         tipoDocumento VARCHAR(255),
                         PRIMARY KEY (personaId)
);

-- Crear la tabla de unión 'negocio_obligacion'
CREATE TABLE negocio_obligacion (
                                    negocioId BIGINT(20) NOT NULL,
                                    obligacionId BIGINT(20) NOT NULL,
                                    PRIMARY KEY (negocioId, obligacionId),
                                    FOREIGN KEY (negocioId) REFERENCES negocio(idnedocio) ON DELETE CASCADE,
                                    FOREIGN KEY (obligacionId) REFERENCES obligacion(idobligacion) ON DELETE CASCADE
);

-- Crear la tabla de unión 'persona_negocio'
CREATE TABLE persona_negocio (
                                 personaId BIGINT(20) NOT NULL,
                                 negocioId BIGINT(20) NOT NULL,
                                 PRIMARY KEY (personaId, negocioId),
                                 FOREIGN KEY (personaId) REFERENCES persona(personaId) ON DELETE CASCADE,
                                 FOREIGN KEY (negocioId) REFERENCES negocio(idnedocio) ON DELETE CASCADE
);