package com.example.demo.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.CarreraRequest;
import com.example.demo.dto.CarreraResponse;
import com.example.demo.entity.Carrera;
import com.example.demo.exception.ConflictoException;
import com.example.demo.repository.CarreraRepository;

@Service
public class CarreraService {

    private final CarreraRepository carreraRepository;

    public CarreraService(CarreraRepository carreraRepository) {
        this.carreraRepository = carreraRepository;
    }

    @Transactional(readOnly = true)
    public List<CarreraResponse> listar() {
        return carreraRepository.findAll().stream()
                .sorted(Comparator.comparing(Carrera::getId))
                .map(this::aRespuesta)
                .toList();
    }

    @Transactional
    public CarreraResponse crear(CarreraRequest request) {
        String nombre = request.nombre().trim();
        if (carreraRepository.findByNombreIgnoreCase(nombre).isPresent()) {
            throw new ConflictoException("Ya existe una carrera con ese nombre.");
        }
        return aRespuesta(carreraRepository.save(new Carrera(nombre)));
    }

    private CarreraResponse aRespuesta(Carrera carrera) {
        return new CarreraResponse(carrera.getId(), carrera.getNombre(), carrera.getAlumnos().size());
    }
}
