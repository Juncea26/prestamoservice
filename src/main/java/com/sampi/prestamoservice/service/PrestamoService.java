package com.sampi.prestamoservice.service;

import com.sampi.prestamoservice.dto.PrestamoRequest;
import com.sampi.prestamoservice.dto.PrestamoResponse;
import com.sampi.prestamoservice.dto.ResumenCarteraResponse;
import com.sampi.prestamoservice.entity.Prestamo;
import com.sampi.prestamoservice.entity.Usuario;
import com.sampi.prestamoservice.repository.PrestamoRepository;
import com.sampi.prestamoservice.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class PrestamoService {

    private static final BigDecimal CERO = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

    private final PrestamoRepository prestamoRepository;
    private final UsuarioRepository usuarioRepository;

    public PrestamoService(
            PrestamoRepository prestamoRepository,
            UsuarioRepository usuarioRepository) {
        this.prestamoRepository = prestamoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    private Usuario getUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }
        return usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Usuario autenticado no encontrado"
                ));
    }

    @Transactional(readOnly = true)
    public List<PrestamoResponse> listar() {
        Usuario usuario = getUsuarioAutenticado();
        return prestamoRepository.findByUsuarioIdOrderByFechaVencimientoAsc(usuario.getId())
                .stream()
                .map(PrestamoResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public PrestamoResponse obtenerPorId(Long id) {
        Usuario usuario = getUsuarioAutenticado();
        Prestamo prestamo = buscarPropioO404(id, usuario.getId());
        return PrestamoResponse.fromEntity(prestamo);
    }

    @Transactional
    public PrestamoResponse crear(PrestamoRequest req) {
        Usuario usuario = getUsuarioAutenticado();

        Prestamo prestamo = new Prestamo();
        prestamo.setCliente(req.getCliente().trim());
        prestamo.setDni(req.getDni().trim());
        prestamo.setMonto(req.getMonto());
        prestamo.setInteres(req.getInteres());
        prestamo.setFechaPrestamo(LocalDate.now());
        prestamo.setFechaVencimiento(req.getFechaVencimiento());
        prestamo.setPagado(req.getPagado() != null ? req.getPagado() : Boolean.FALSE);
        prestamo.setUsuario(usuario);

        Prestamo guardado = prestamoRepository.save(prestamo);
        return PrestamoResponse.fromEntity(guardado);
    }

    @Transactional
    public PrestamoResponse actualizar(Long id, PrestamoRequest req) {
        Usuario usuario = getUsuarioAutenticado();
        Prestamo prestamo = buscarPropioO404(id, usuario.getId());

        prestamo.setCliente(req.getCliente().trim());
        prestamo.setDni(req.getDni().trim());
        prestamo.setMonto(req.getMonto());
        prestamo.setInteres(req.getInteres());
        prestamo.setFechaVencimiento(req.getFechaVencimiento());
        if (req.getPagado() != null) {
            prestamo.setPagado(req.getPagado());
        }

        Prestamo actualizado = prestamoRepository.save(prestamo);
        return PrestamoResponse.fromEntity(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        Usuario usuario = getUsuarioAutenticado();
        Prestamo prestamo = buscarPropioO404(id, usuario.getId());
        prestamoRepository.delete(prestamo);
    }

    @Transactional(readOnly = true)
    public ResumenCarteraResponse obtenerResumen() {
        Usuario usuario = getUsuarioAutenticado();
        List<Prestamo> prestamos = prestamoRepository
                .findByUsuarioIdOrderByFechaVencimientoAsc(usuario.getId());

        BigDecimal capitalPrestado = CERO;
        BigDecimal totalPorCobrar = CERO;
        BigDecimal gananciaEsperada = CERO;
        long pendientes = 0L;
        long cobrados = 0L;

        for (Prestamo prestamo : prestamos) {
            PrestamoResponse dto = PrestamoResponse.fromEntity(prestamo);
            BigDecimal monto = dto.getMonto().setScale(2, RoundingMode.HALF_UP);
            BigDecimal total = dto.getTotalACobrar().setScale(2, RoundingMode.HALF_UP);
            BigDecimal ganancia = total.subtract(monto).setScale(2, RoundingMode.HALF_UP);

            capitalPrestado = capitalPrestado.add(monto);
            gananciaEsperada = gananciaEsperada.add(ganancia);

            if (Boolean.TRUE.equals(prestamo.getPagado())) {
                cobrados++;
            } else {
                pendientes++;
                totalPorCobrar = totalPorCobrar.add(total);
            }
        }

        return new ResumenCarteraResponse(
                capitalPrestado.setScale(2, RoundingMode.HALF_UP),
                totalPorCobrar.setScale(2, RoundingMode.HALF_UP),
                gananciaEsperada.setScale(2, RoundingMode.HALF_UP),
                pendientes,
                cobrados
        );
    }

    private Prestamo buscarPropioO404(Long id, Long usuarioId) {
        return prestamoRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Préstamo no encontrado"
                ));
    }
}
