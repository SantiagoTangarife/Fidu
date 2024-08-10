package com.example.Fidu.Service;

import com.example.Fidu.Entity.Negocio;
import com.example.Fidu.Entity.Obligacion;
import com.example.Fidu.Entity.Persona;
import com.example.Fidu.Repository.NegocioRepository;
import com.example.Fidu.Repository.PersonaRepository;
import jakarta.transaction.Transactional;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.*;

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
        return personaRepository.findByNumeroDocumento(personaId);

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

    public List<String> exportar(Persona usuario) {
        List<String> resultado = new ArrayList<>();

        // Obtener la lista de negocios asociados a la persona
        Set<Negocio> listaNegocios = getNegociosByPersonaId(usuario.getPersonaId());
        // Crear un nuevo libro de trabajo de Excel
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Negocios y Obligaciones");

        // Crear la fila de encabezados
        String[] encabezados = {
                "Negocio ID", "Nombre", "Descripción", "Fecha Inicio", "Fecha Fin",
                "Obligación ID", "Monto", "Descripción Obligación", "Fecha Vencimiento"
        };
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < encabezados.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(encabezados[i]);
        }
        // Iterar sobre cada negocio
        int rowIndex = 1;
        for (Negocio negocio : listaNegocios) {
            // Obtener las obligaciones asociadas a este negocio
            Set<Obligacion> listaObligaciones = negocio.getObligaciones();
            // Iterar sobre cada obligación
            for (Obligacion obligacion : listaObligaciones) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(negocio.getNegocioId().toString());
                row.createCell(1).setCellValue(negocio.getNombre());
                row.createCell(2).setCellValue(negocio.getDescripcion());
                row.createCell(3).setCellValue(negocio.getFechaInicio().toString());
                row.createCell(4).setCellValue(negocio.getFechaFin().toString());
                row.createCell(5).setCellValue(obligacion.getObligacionId().toString());
                row.createCell(6).setCellValue(obligacion.getMonto().toString());
                row.createCell(7).setCellValue(obligacion.getDescripcion());
                row.createCell(8).setCellValue(obligacion.getFechaVencimiento().toString());
            }
        }

        // Especificar la ruta donde se guardará el archivo
        String projectPath = System.getProperty("user.dir");
        File documentosFolder = new File(projectPath, "documentos_datos_de_excel");

        // Crear la carpeta si no existe
        if (!documentosFolder.exists()) {
            documentosFolder.mkdirs();
        }

        // Nombre del archivo
        String nombreArchivo = String.format("negocios_y_obligaciones_de_%s_%s.xlsx", usuario.getNombre().toUpperCase(), usuario.getApellido().toUpperCase());
        String filePath = Paths.get(documentosFolder.getPath(), nombreArchivo).toString();



        // Guardar el archivo Excel con protección
        try (POIFSFileSystem fs = new POIFSFileSystem()) {
            EncryptionInfo info = new EncryptionInfo(EncryptionMode.agile);
            Encryptor encryptor = info.getEncryptor();
            encryptor.confirmPassword(usuario.getNumeroDocumento().toString());

            try (OutputStream os = encryptor.getDataStream(fs)) {
                workbook.write(os);
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            }

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fs.writeFilesystem(fos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Cerrar el libro de trabajo
        try {
            workbook.close();
            resultado.add("Exito");
            resultado.add("El documento fue creado exitosamente, búscalo en la carpeta: documentos_datos_de_excel");
        } catch (IOException e) {
            resultado.add("Falla");
            resultado.add("El documento no pudo ser creado. Revisa las condiciones de tu solicitud.");

        }
        return resultado;
    }

}




