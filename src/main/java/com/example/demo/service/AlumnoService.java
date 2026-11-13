package com.example.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.AlumnoRequest;
import com.example.demo.dto.AlumnoResponse;
import com.example.demo.dto.PaginaResponse;
import com.example.demo.entity.Alumno;
import com.example.demo.entity.Carrera;
import com.example.demo.exception.ConflictoException;
import com.example.demo.exception.DatosInvalidosException;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.repository.AlumnoRepository;
import com.example.demo.repository.CarreraRepository;

@Service
public class AlumnoService {

    private static final int TAMANO_MAXIMO = 50;

    private final AlumnoRepository alumnoRepository;
    private final CarreraRepository carreraRepository;

    public AlumnoService(AlumnoRepository alumnoRepository, CarreraRepository carreraRepository) {
        this.alumnoRepository = alumnoRepository;
        this.carreraRepository = carreraRepository;
    }

    @Transactional(readOnly = true)
    public PaginaResponse<AlumnoResponse> listar(Long carreraId, int pagina, int tamano) {
        Pageable pageable = PageRequest.of(Math.max(pagina, 0), Math.min(Math.max(tamano, 1), TAMANO_MAXIMO),
                Sort.by("id"));
        Page<Alumno> resultado;
        if (carreraId == null) {
            resultado = alumnoRepository.findAll(pageable);
        } else {
            if (!carreraRepository.existsById(carreraId)) {
                throw new RecursoNoEncontradoException("No existe una carrera con el id " + carreraId + ".");
            }
            resultado = alumnoRepository.findByCarreraId(carreraId, pageable);
        }
        return PaginaResponse.de(resultado.map(this::aRespuesta));
    }

    @Transactional(readOnly = true)
    public AlumnoResponse obtener(Long id) {
        return aRespuesta(buscar(id));
    }

    @Transactional
    public AlumnoResponse crear(AlumnoRequest request) {
        String correo = request.correo().trim();
        validarCorreoUnico(correo, null);
        Carrera carrera = buscarCarrera(request.carreraId());
        Alumno alumno = new Alumno(request.nombre().trim(), correo, request.semestre(), carrera);
        return aRespuesta(alumnoRepository.save(alumno));
    }

    @Transactional
    public AlumnoResponse actualizar(Long id, AlumnoRequest request) {
        Alumno alumno = buscar(id);
        String correo = request.correo().trim();
        validarCorreoUnico(correo, id);
        alumno.setNombre(request.nombre().trim());
        alumno.setCorreo(correo);
        alumno.setSemestre(request.semestre());
        alumno.setCarrera(buscarCarrera(request.carreraId()));
        return aRespuesta(alumnoRepository.save(alumno));
    }

    @Transactional
    public void eliminar(Long id) {
        alumnoRepository.delete(buscar(id));
    }

    private Alumno buscar(Long id) {
        return alumnoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un alumno con el id " + id + "."));
    }

    private Carrera buscarCarrera(Long carreraId) {
        return carreraRepository.findById(carreraId)
                .orElseThrow(() -> new DatosInvalidosException("La carrera seleccionada no existe."));
    }

    private void validarCorreoUnico(String correo, Long idActual) {
        alumnoRepository.findByCorreoIgnoreCase(correo).ifPresent(existente -> {
            if (!existente.getId().equals(idActual)) {
                throw new ConflictoException("Ya existe un alumno con ese correo.");
            }
        });
    }

    private AlumnoResponse aRespuesta(Alumno alumno) {
        return new AlumnoResponse(
                alumno.getId(),
                alumno.getNombre(),
                alumno.getCorreo(),
                alumno.getSemestre(),
                alumno.getCarrera().getId(),
                alumno.getCarrera().getNombre());
    }
}
