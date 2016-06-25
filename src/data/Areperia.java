/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import util.Gem;

/**
 *
 * @author andre
 */
public class Areperia {
/**
 * Variables de entrada:
 *  llegada
 *  salida
 * Variables de estado:
 *  estado1,estado2, estado3
 *  occ1, occ2,occ3
 * Variables de desempeño
 *  colaMax
 *  utilidadFinal
 *  tiempo espera promedio
 */
    private PriorityQueue<Cliente> LEF;
    private Gem gem;
    private LinkedList<Integer> colaLlegada;
    private int tiempoEspera;
    private boolean estado1 = false;
    private boolean estado2 = false;
    private boolean estado3 = false;
    private boolean occ1 = false;
    private boolean occ2 = false;
    private boolean occ3 = false;
    private int cola;
    private List colaMax;
    private int atendidos;
    public int tiempoJornada = 21600;
    private int utilidadFinal;

    /**
     * Constructor de Areperia establece una semilla aleatoria de tipo long
     * para usar en el gem
     */
    public Areperia() {

        //Comparator<Cliente> comparador = new comparadorTiempos();
        //LEF = new PriorityQueue(11, comparador);
        //LEF2 = new PriorityQueue(2, comparador);
        //LEF3 = new PriorityQueue(2, comparador);
        long range = 12345678L;
        Random rand = new Random();
        long r_seed = (long) (rand.nextDouble() * range);
        this.gem = new Gem(r_seed);
    }

    /*
     * Jornada laboral de la areperia 
     */
    public void iniciarJornada(int numEmpleados) {
        //Inicializar LEF, colaMax, tiempoEspera, estado servidor, cola
        LEF = new PriorityQueue<Cliente>();
        colaMax = new LinkedList<>();
        colaLlegada = new LinkedList<Integer>();
        tiempoEspera = 0;
        occ1 = false;
        occ2 = false;
        occ3 = false;
        cola = 0;
        atendidos = 0;
        utilidadFinal = 0;
        //Inicializar empleados
        if (numEmpleados == 1) {
            estado1 = true;
        }
        if (numEmpleados == 2) {
            estado1 = true;
            estado2 = true;
        }
        if (numEmpleados == 3) {
            estado1 = true;
            estado2 = true;
            estado3 = true;
        }

        //Inicializar LEF con llegada
        LEF.add(new Cliente("L", 0, 1));

        //Tiempo espera tiempoEspera+= reloj-LEF.peek().getTiempo();
        int reloj = 0;
        for (; reloj < tiempoJornada; reloj++) {

            while (LEF.peek().getTiempo() == reloj) {
                if (LEF.peek().getTiempo() == reloj && LEF.peek().getEvento().equals("L")) {
                    //Generar Llegada 
                    LEF.poll();
                    int llegada = generarTiempoLlegada() + reloj;
                    LEF.add(new Cliente("L", llegada, 1));
                    //Servidores estan ocupados
                    if (occ1 && (occ2 || !estado2) && (occ3 || !estado3)) {
                        cola++;
                        colaMax.add(cola);
                        colaLlegada.add(reloj);
                        //System.out.println("Espera en cola en: " + reloj);
                    } else { //Servidor Libres
                        //Generar  salida, no espera en la cola
                        //Determinar salida
                        String tipoSalida = determinarSalida();
                        int salida = 0;
                        int utilidad = 0;
                        switch (tipoSalida) {
                            case "contodo":
                                salida = generarTiempoConTodo() + reloj;
                                utilidad = 750;
                                break;
                            case "nada":
                                salida = generarTiempoNada() + reloj;
                                break;
                            case "chicharron":
                                salida = generarTiempoChicharron() + reloj;
                                utilidad = 500;
                                break;
                        }

                        if (occ1 == false && estado1 == true) {
                            occ1 = true;
                            LEF.add(new Cliente("S", salida, 1, utilidad));
                        } else if (occ2 == false && estado2 == true) {
                            LEF.add(new Cliente("S", salida, 2, utilidad));
                            occ2 = true;
                        } else if (occ3 == false && estado3 == true) {
                            LEF.add(new Cliente("S", salida, 3, utilidad));
                            occ3 = true;
                        }
                    }
                }

                //Generar Salida
                if (LEF.peek().getTiempo() == reloj && LEF.peek().getEvento().equals("S")) {

                    //Actualizar utilidad
                    utilidadFinal += LEF.peek().getUtilidad();

                    atendidos++;
                    //Determinar salida
                    String tipoSalida = determinarSalida();
                    int salida = 0;
                    int utilidad = 0;
                    switch (tipoSalida) {
                        case "contodo":
                            salida = generarTiempoConTodo() + reloj;
                            utilidad = 750;
                            break;
                        case "nada":
                            salida = generarTiempoNada() + reloj;
                            break;
                        case "chicharron":
                            salida = generarTiempoChicharron() + reloj;
                            utilidad = 500;
                            break;
                    }

                    if (cola == 0) {
                        LEF.poll();
                        occ1 = false;
                        occ2 = false;
                        occ3 = false;
                        colaMax.add(cola);
                    } else {
                        cola--;
                        tiempoEspera += reloj - colaLlegada.removeFirst();
                        //System.out.println("Sale cola: " + reloj);
                        if (LEF.peek().getEmpleado() == 1) {
                            occ1 = true;
                            LEF.poll();
                            LEF.add(new Cliente("S", salida, 1, utilidad));
                        } else if (LEF.peek().getEmpleado() == 2) {
                            occ2 = true;
                            LEF.poll();
                            LEF.add(new Cliente("S", salida, 2, utilidad));
                        } else if (LEF.peek().getEmpleado() == 3) {
                            occ3 = true;
                            LEF.poll();
                            LEF.add(new Cliente("S", salida, 3, utilidad));
                        }
                    }
                }
            }
        }

        //Despachar los de la cola despues de 21600
        for (; cola > 0; reloj++) {
            //Discriminar nuevas llegadas despues de cierre
            if (LEF.peek().getTiempo() == reloj && "L".equals(LEF.peek().getEvento())) {
                LEF.poll();
            }
            // Salidas
            while (LEF.peek().getTiempo() == reloj && "S".equals(LEF.peek().getEvento())) {
                //Actualizar utilidad
                utilidadFinal += LEF.peek().getUtilidad();

                //Determinar salida
                String tipoSalida = determinarSalida();
                int salida = 0;
                int utilidad = 0;
                switch (tipoSalida) {
                    case "contodo":
                        salida = generarTiempoConTodo() + reloj;
                        utilidad = 750;
                        break;
                    case "nada":
                        salida = generarTiempoNada() + reloj;
                        break;
                    case "chicharron":
                        salida = generarTiempoChicharron() + reloj;
                        utilidad = 500;
                        break;
                }

                atendidos++;
                cola--;
                tiempoEspera += reloj - colaLlegada.removeFirst();
                //System.out.println("Sale cola: " + reloj);
                if (LEF.peek().getEmpleado() == 1) {
                    occ1 = true;
                    LEF.poll();
                    LEF.add(new Cliente("S", salida, 1, utilidad));
                } else if (LEF.peek().getEmpleado() == 2) {
                    occ2 = true;
                    LEF.poll();
                    LEF.add(new Cliente("S", salida, 2, utilidad));
                } else if (LEF.peek().getEmpleado() == 3) {
                    occ3 = true;
                    LEF.poll();
                    LEF.add(new Cliente("S", salida, 3, utilidad));
                }
            }

        }
        /**
         * Informacion de ejecucion
         */

        
        System.out.print("" + (Collections.max(colaMax)));
        System.out.print("\t" + tiempoEspera);
        System.out.print("\t" + atendidos);
        System.out.print("\t" + tiempoEspera / atendidos);
        System.out.print("\t" + cola);
        System.out.print("\t" + colaLlegada.size());
        System.out.print("\t" + reloj);
        System.out.print("\t" + utilidadFinal);

    }

    private int generarTiempoChicharron() {
        return (int) ((-360) * Math.log(gem.generarAleatorio()));
    }

    private int generarTiempoNada() {
        return (int) ((-90) * Math.log(gem.generarAleatorio()));
    }

    private int generarTiempoConTodo() {
        return (int) (((600 - 300) * gem.generarAleatorio()) + 300);
    }

    public String determinarSalida() {
        int random = (int) (gem.generarAleatorio() * 100 + 1);
        String salida = "";
        //50 % de probabilidad de comprar arepa con todo
        if (random > 50) {
            salida = "contodo";
        } else if (random > 25 && random <= 50) {
            //25 % prob arepa con chicharron
            salida = "chicharron";
        } else if (random <= 25) {
            //25 % prob nada
            salida = "nada";
        }
        return salida;
    }

    /**
     * Genera un numero aleatorio para la llegada del cliente
     *
     * @return
     */
    public int generarTiempoLlegada() {
        int llegada = (int) ((-240) * Math.log(gem.generarAleatorio()));
        return llegada;
    }

    public int getTiempoEspera() {
        return tiempoEspera;
    }

    public int getAtendidos() {
        return atendidos;
    }

    public int getUtilidadFinal() {
        return utilidadFinal;
    }

}
