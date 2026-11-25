package com.sampi.prestamoservice.dto;

import com.sampi.prestamoservice.entity.Prestamo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class PrestamoResponse {

    private static final BigDecimal CIEN = BigDecimal.valueOf(100);

    private Long id;
    private String cliente;
    private String dni;
    private BigDecimal monto;
    private BigDecimal interes;
    private BigDecimal totalACobrar;
    private LocalDate fechaPrestamo;
    private LocalDate fechaVencimiento;
    private Boolean pagado;

    public PrestamoResponse() {
    }

    public PrestamoResponse(Long id, String cliente, String dni, BigDecimal monto, BigDecimal interes,
                            BigDecimal totalACobrar, LocalDate fechaPrestamo, LocalDate fechaVencimiento,
                            Boolean pagado) {
        this.id = id;
        this.cliente = cliente;
        this.dni = dni;
        this.monto = monto;
        this.interes = interes;
        this.totalACobrar = totalACobrar;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaVencimiento = fechaVencimiento;
        this.pagado = pagado;
    }

    public static PrestamoResponse fromEntity(Prestamo p) {
        BigDecimal monto = p.getMonto() != null ? p.getMonto() : BigDecimal.ZERO;
        BigDecimal interes = p.getInteres() != null ? p.getInteres() : BigDecimal.ZERO;
        BigDecimal ganancia = monto.multiply(interes).divide(CIEN, 2, RoundingMode.HALF_UP);
        BigDecimal totalACobrar = monto.add(ganancia).setScale(2, RoundingMode.HALF_UP);

        return new PrestamoResponse(
                p.getId(),
                p.getCliente(),
                p.getDni(),
                p.getMonto(),
                p.getInteres(),
                totalACobrar,
                p.getFechaPrestamo(),
                p.getFechaVencimiento(),
                p.getPagado()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public BigDecimal getInteres() {
        return interes;
    }

    public void setInteres(BigDecimal interes) {
        this.interes = interes;
    }

    public BigDecimal getTotalACobrar() {
        return totalACobrar;
    }

    public void setTotalACobrar(BigDecimal totalACobrar) {
        this.totalACobrar = totalACobrar;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Boolean getPagado() {
        return pagado;
    }

    public void setPagado(Boolean pagado) {
        this.pagado = pagado;
    }
}
