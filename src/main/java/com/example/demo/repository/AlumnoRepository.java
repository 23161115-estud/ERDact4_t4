package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Alumno;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

    Page<Alumno> findByCarreraId(Long carreraId, Pageable pageable);

    Optional<Alumno> findByCorreoIgnoreCase(String correo);
}
