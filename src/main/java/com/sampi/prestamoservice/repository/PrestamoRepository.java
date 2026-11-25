package com.sampi.prestamoservice.repository;

import com.sampi.prestamoservice.entity.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    List<Prestamo> findByUsuarioIdOrderByFechaVencimientoAsc(Long usuarioId);

    Optional<Prestamo> findByIdAndUsuarioId(Long id, Long usuarioId);
}
