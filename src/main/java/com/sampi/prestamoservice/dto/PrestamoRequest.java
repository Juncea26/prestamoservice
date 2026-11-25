package com.sampi.prestamoservice.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PrestamoRequest {

    @NotBlank
    @Size(max = 100)
    private String cliente;

    @NotBlank
    @Pattern(regexp = "^\\d{8}$", message = "El DNI debe tener 8 dígitos")
    private String dni;

    @NotNull
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal monto;

    @NotNull
    @DecimalMin(value = "0.00")
    @DecimalMax(value = "999.99")
    private BigDecimal interes;

    @NotNull
    private LocalDate fechaVencimiento;

    private Boolean pagado;

    public PrestamoRequest() {
    }

    public PrestamoRequest(String cliente, String dni, BigDecimal monto, BigDecimal interes,
                           LocalDate fechaVencimiento, Boolean pagado) {
        this.cliente = cliente;
        this.dni = dni;
        this.monto = monto;
        this.interes = interes;
        this.fechaVencimiento = fechaVencimiento;
        this.pagado = pagado;
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
