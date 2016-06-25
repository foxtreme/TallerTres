/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.util.Random;
import util.Gem;

/**
 *
 * @author andre
 */
public class Cliente implements Comparable<Cliente> {

    private String evento;
    private Integer tiempo;
    private Integer empleado;
    private Integer utilidad;

    public Cliente(String evento, Integer tiempo, Integer caja) {
        this.evento = evento;
        this.tiempo = tiempo;
        this.empleado = caja;
    }

    public Cliente(String evento, Integer tiempo, Integer caja, Integer utilidad) {
        this.evento = evento;
        this.tiempo = tiempo;
        this.empleado = caja;
        this.utilidad = utilidad;
    }

    public String getEvento() {
        return evento;
    }

    public Integer getTiempo() {
        return tiempo;
    }

    public Integer getEmpleado() {
        return empleado;
    }

    public Integer getUtilidad() {
        return utilidad;
    }

    public boolean equals(Cliente otro) {
        return this.getTiempo() == otro.getTiempo();
    }

    @Override
    public int compareTo(Cliente otro) {
        if (this.equals(otro)) {
            return 0;
        } else if (getTiempo() > otro.getTiempo()) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public String toString() {
        return "" + tiempo;
    }

}
