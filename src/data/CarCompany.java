/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import util.Gem;

/**
 *
 * @author chris
 */
public class CarCompany {
    
    private Gem gem;
    private List<Double> tiemposFalla;//lista de todos los tiempos de falla de m *10 vehiculos
    private List<Integer> fallasAnuales; //lista de cantidad fallas anuales por m anhos
    private int m; //tiempo simulado
    private double promedioFalla;//tiempo promedio de fallo cuando se producen m*10 vehiculos
    private double costoTotalGarantia;//cuanto cuesta a la empresa pagar por garantias en m anhos
    private int cantTiemposGarantia;
    
    /**
     * Constructor de CarCompany
     */
    public CarCompany(){
        long range = 12345678L;
        Random rand = new Random();
        long r_seed = (long)(rand.nextDouble()*range);
        this.gem = new Gem(r_seed);
    }
    
    /**
     * se encarga de generar la lista con los datos aleatorios de los tiempos de falla
     * correrpondientes a m * 10 vehiculos con distribucion exponencial y evaluarlos para
     * obtener las variables de desempenho
     * @param m
     */
    public void procesoVehiculos(int m){
        this.m = m;
        //se producen m * 10 vehiculos en m anhos, con distribucion exponencial
        //se aplica bernoulli con parametro m para evaluar si un vehiculo falla o no la garantia
        gem.produccion(this.m);
        //se establecen las fallas por garantia de los vehiculos producidos
        evaluacion();
    }
    
    /**
     * Evalua cada vehiculo producido y calcula las variables de desempenho con
     * los resultados de la evaluacion
     * @param m tiempo en anhos simulados
     */
    public void evaluacion() {
        this.fallasAnuales = gem.getBinomialFallas(); //lista con la cantidad de fallas en m anhos
        this.tiemposFalla = gem.getTiemposFalla(); // lista con todos los tiempos aleatorios de m*10 datos
        double sumTiemposGarantia =  gem.getSumTiemposGarantia(); //sumatoria de los tiempos que fallaron por garantia
        this.cantTiemposGarantia = gem.getCantFallas(); // cantidad de vehiculos que fallaron por garantia
        this.promedioFalla = sumTiemposGarantia/cantTiemposGarantia;
        this.costoTotalGarantia = cantTiemposGarantia * 6.5; //cuesta 6.5 cada fallo por garantia
    }
    
    /**
     * Imprime en pantalla los parametros de simulacion y los resultados obtenidos
     * @param m tiempo en anhos que son simulados
     */
    public void printMetricasSimulacion(){
        for(int i=0; i<tiemposFalla.size();i++){
            if(tiemposFalla.get(i)<= m){
                System.out.print(" # 1 "+tiemposFalla.get(i));
            }
            if(tiemposFalla.get(i)> m){
                System.out.print(" # 0 "+tiemposFalla.get(i));
            }
            System.out.println();
            
        }
        DecimalFormat df = new DecimalFormat("#.##");
        System.out.println("Costo total por concepto de garantias: "+this.costoTotalGarantia);
        System.out.println("tiempo promedio de falla: "+df.format(this.promedioFalla));
        
    }
    
    public  List<String> resultado(int idx){
        List <String> result = new ArrayList<String>();
        System.out.println("Ejecucion: "+idx+"\nPeriodo simulado: "+m+"\nCantidad vehiculos producidos: "+
                (this.m*10)+"\nCosto Total por Garantia: "+costoTotalGarantia+"\nTiempo promedio de fallo: "+
                this.promedioFalla+"\nCantidad de fallas por garantia: "+this.cantTiemposGarantia);
        return result;
    }
    
    /**
     * Retorna el gem usado por CarCompany
     * @return Generador de estandar Minimo
     */
    public Gem getGem() {
        return gem;
    }
    
    public static void main(String args[]){
        CarCompany cc = new CarCompany();
        int m = 2;
        cc.procesoVehiculos(m);
        //descomentar printMetricas para mas detalles
////        cc.printMetricasSimulacion();
        cc.resultado(1);
    }

    

    
   
}
