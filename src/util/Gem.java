/*
 * generador tomado de https://www.seas.gwu.edu/~drum/java/lectures/appendix/examples/uniform_random.java
 * adaptado para que evitar el overflow de acuerdo a https://en.wikipedia.org/wiki/Lehmer_random_number_generator
 */
package util;

import java.io.IOException;
import static java.lang.Math.*;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 *
 * @author chris
 */
public class Gem {

    static final long m = 2147483647L;
    static final long a = 48271L;
    static final long q = 44488L;
    static final long r = 3399L;
    
    // variable global, inicializar la semilla con algun valor arbitrario diferente de 0
    private long r_seed;
    private List<Double> tiemposFalla = new ArrayList<Double>();
    private List<Integer> binomialFallas;
    private int cantFallas;
    private double sumTiemposGarantia;
    
    /**
     *
     * Constructor clase Gem
     */
    public Gem(long r_seed) {
        this.r_seed = r_seed;
    }

    /**
     * *
     * Generador de numeros pseudoaleatorios entre 0 y 1
     * @return numero pseudoaleatorio
     */
    public double generarAleatorio() {
        double numeroGenerado = 0.0;
        //esto se hace para trucar el overflow de multiplicar numeros tan grandes
        long hi = r_seed / q;
        long lo = r_seed - q * hi;
        long t = a * lo - r * hi;
        if (t > 0) {
            r_seed = t;
        } else {
            r_seed = t + m;
        }
        numeroGenerado = ((double) r_seed / (double) m);

        return numeroGenerado;
    }

    /**
     * Genera una cantidad dada de numeros en distribucion binomial con p=m y
     * n=10
     * descomentar las partes indicadas de este metodo para ver detalles
     * @param cant cantidad de numeros aleatorios a ser transformados a
     * distribucion binomial
     */
    public void produccion(int m) {
        tiemposFalla.clear();//se limpia por si se vuelve a usar el metodo
        int cant = m*10;
        //descomentar para mas detalles por cada anho
//        System.out.println("--------Produccion y Evaluacion de Vehiculos----------");
//        System.out.println("tiempo de vida util promedio del vehiculo (anhos): " + m);
//        System.out.println("cantidad de vehiculos producidos: " + cant);
//        System.out.println("-----------------------");
        int n = 10, numBinomial = 0, cantNumerosBinomial = cant / n;
        double p = m;
        sumTiemposGarantia = 0.0;
        List<Integer> numerosBinomial = new ArrayList<Integer>();
        for (int j = 0; j < cantNumerosBinomial; j++) {//anualizar
            for (int i = 0; i < n; i++) {
                double ri = generarAleatorio();//genera un aleatorio
                double expo = (-1 * m) * Math.log(ri);//lo pasa a exponencial
                tiemposFalla.add((Double) Math.abs(expo)); //lo agrega a la lista de producidos
                if (expo <= p) {//el numero es un exito
                    numBinomial++;
                    sumTiemposGarantia = sumTiemposGarantia+expo;
                }
            }
            numerosBinomial.add(numBinomial);//agrega la cant de fallas por anho a la lista
              //descomentar para mas detalles por cada anho  
//            System.out.println("cantidad de fallas en el anho "+(j+1)+": "+ numBinomial);
            numBinomial = 0;//resetea el binomial para el proximo anho
        }
        int sum = 0;
        for(int i=0; i< numerosBinomial.size();i++){//suma las fallas anuales
           sum = sum +numerosBinomial.get(i);
        }
        cantFallas = sum;
        //descomentar para mas detalles por cada anho
//        System.out.println("cant de fallas en "+cant+" anhos: " + cantFallas);
//        System.out.println("-----------------------");
        binomialFallas = numerosBinomial;
    }

    
    public List<Double> getTiemposFalla() {
        return tiemposFalla;
    }

    public List<Integer> getBinomialFallas() {
        return binomialFallas;
    }

    public int getCantFallas() {
        return cantFallas;
    }

    public double getSumTiemposGarantia() {
        return sumTiemposGarantia;
    }
    
    
    
}
