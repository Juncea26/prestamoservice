package com.sampi.prestamoservice.dto;

import java.math.BigDecimal;

public class ResumenCarteraResponse {

    private BigDecimal capitalPrestado;
    private BigDecimal totalPorCobrar;
    private BigDecimal gananciaEsperada;
    private long cantidadPendientes;
    private long cantidadCobrados;

    public ResumenCarteraResponse() {
    }

    public ResumenCarteraResponse(BigDecimal capitalPrestado, BigDecimal totalPorCobrar,
                                  BigDecimal gananciaEsperada, long cantidadPendientes,
                                  long cantidadCobrados) {
        this.capitalPrestado = capitalPrestado;
        this.totalPorCobrar = totalPorCobrar;
        this.gananciaEsperada = gananciaEsperada;
        this.cantidadPendientes = cantidadPendientes;
        this.cantidadCobrados = cantidadCobrados;
    }

    public BigDecimal getCapitalPrestado() {
        return capitalPrestado;
    }

    public void setCapitalPrestado(BigDecimal capitalPrestado) {
        this.capitalPrestado = capitalPrestado;
    }

    public BigDecimal getTotalPorCobrar() {
        return totalPorCobrar;
    }

    public void setTotalPorCobrar(BigDecimal totalPorCobrar) {
        this.totalPorCobrar = totalPorCobrar;
    }

    public BigDecimal getGananciaEsperada() {
        return gananciaEsperada;
    }

    public void setGananciaEsperada(BigDecimal gananciaEsperada) {
        this.gananciaEsperada = gananciaEsperada;
    }

    public long getCantidadPendientes() {
        return cantidadPendientes;
    }

    public void setCantidadPendientes(long cantidadPendientes) {
        this.cantidadPendientes = cantidadPendientes;
    }

    public long getCantidadCobrados() {
        return cantidadCobrados;
    }

    public void setCantidadCobrados(long cantidadCobrados) {
        this.cantidadCobrados = cantidadCobrados;
    }
}
