/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author chris
 */
public class Simulacion {
    
    private int m;//periodo simulado
    private int cantVehiculos;
    private double costoTotalPromedioGarantia;//promedio para 50 ejecuciones
    private double tiempoTotalPromedioFallo;//promedio para 50 ejecuciones
    private List <String> filasResultados;
    /**
     * Ejecuta la simulacion 50 veces para un tiempo en anhos
     * @param m tiempo en anhos
     */
    public Simulacion(){
        filasResultados = new ArrayList<String>();
    }
    
    public void ejecutarSimulacion(int m){
        this.m = m;
        this.cantVehiculos = (m*10);
        double sumCostoTotalPromedioGarantia = 0;
        double sumTiempoTotalPromedioFallo = 0;
        for(int i=1; i<51; i++){
            CarCompany cc = new CarCompany();
            cc.procesoVehiculos(m);
            sumCostoTotalPromedioGarantia = sumCostoTotalPromedioGarantia + cc.getCostoTotalGarantia();
            sumTiempoTotalPromedioFallo = sumTiempoTotalPromedioFallo + cc.getPromedioFalla();
            filasResultados.add((String)cc.resultado(i));
        }
        tiempoTotalPromedioFallo = sumTiempoTotalPromedioFallo/50;
        costoTotalPromedioGarantia = sumCostoTotalPromedioGarantia/50;
    }
    
    public void resultadoSimulacion(){
        DecimalFormat df = new DecimalFormat("#.##");
        System.out.println("-----------------------------");
        System.out.println("-- Variables de desempenho --");
        System.out.println("Costo promedio por Garantia: "+costoTotalPromedioGarantia);
        System.out.println("Tiempo promedio de fallo Observado: "+df.format(tiempoTotalPromedioFallo));
        System.out.println("-- Detalles --");
        System.out.println("Ejecucion-Tiempo-Vehiculos producidos-Costo Garantia-Tiempo fallo-Cantidad de fallas");
        for(int i= 0; i<filasResultados.size();i++){
            System.out.println(filasResultados.get(i));
        }
    }
    
    /**
     * Calcula la diferencia entre el costo total esperado y el costo total simulado
     * @return la diferencia entre costos simulado y esperado
     */
    public double comparacionSimuladovsEsperado(){
        double diferencia=0;
        DecimalFormat df = new DecimalFormat("#.##");
        if(m==2){
            double ct2 = 302; //costo total esperado para m=2
            double cs2 = 276 + costoTotalPromedioGarantia;//costo total simulado para m=2
            diferencia = Math.abs(ct2 - cs2);
        }
        if(m==4){
            double ct4 = 1007;//costo total esperado para m=4
            double cs4 = 968 + costoTotalPromedioGarantia;//costo total simulado para m=4
            diferencia = Math.abs(ct4 - cs4);
        }
        if(m==6){
            double ct6 = 2224;//costo total esperado para m=6
            double cs6 = 2172 + costoTotalPromedioGarantia;//costo total simulado para m=6
            diferencia = Math.abs(ct6 - cs6);
        }
        System.out.println("para un tiempo de "+m+" anhos y "+cantVehiculos+" Vehiculos");
        System.out.println("La diferencia entre el valor esperado y el observado es: "+df.format(diferencia));
        return diferencia;
    }
    
    public static void main(String args[]){
        Simulacion s = new Simulacion();
        s.ejecutarSimulacion(2);
//        s.resultadoSimulacion(); //descomentar para ver los resultados de m = 2 
        double diferencia2 = s.comparacionSimuladovsEsperado();
        s.ejecutarSimulacion(4);
        double diferencia4 = s.comparacionSimuladovsEsperado();
        s.ejecutarSimulacion(6);
        double diferencia6 = s.comparacionSimuladovsEsperado();
        System.out.println();
        System.out.println("Se recomienda escoger la menor diferencia ya que es el valor\n mas"
                + " cercano al real e implica un menor costo por parte de la empresa");
        
    }
    
}
