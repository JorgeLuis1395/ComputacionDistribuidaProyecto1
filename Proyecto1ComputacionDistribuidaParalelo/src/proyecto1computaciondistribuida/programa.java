package proyecto1computaciondistribuida;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.*;
public class programa {
    private ArrayList<CompuestoQuimico> ListaCompuestosQuimicos = new ArrayList<CompuestoQuimico>();
    private CompuestoQuimico Compuesto;
    private float[][] MatrizdeCoeficientesJaccardTanimoto;
    private int tamanoColeccionCompuestosQuimicos;
    private int numeroHilos=7, cantidadDatosImprimir=210 ;
    private String archivoSalida="solution.tsv";
    DecimalFormat formatoDosDecimales = new DecimalFormat("0.00");
    private long tiempoInicial,tiempoFinal,tiempototal;
    //CONSTRUCTOR
    public programa(String archivo){
        try {
            tiempoInicial=System.currentTimeMillis();
            LecturaCompuestosQuimicos(archivo);
            tamanoColeccionCompuestosQuimicos=ListaCompuestosQuimicos.size();
            MatrizdeCoeficientesJaccardTanimoto=new float[tamanoColeccionCompuestosQuimicos][tamanoColeccionCompuestosQuimicos];
            Collections.sort(ListaCompuestosQuimicos); //Nos permite ordenar la lista de acuerdo a su nombre
            CoeficienteJaccardTanimotoParalelo();
            System.out.println("SALIDA DEL PROGRAMA");
            EscrituraArchivoSalida();
            ImprimirSimilitudQuimica();
            tiempoFinal=System.currentTimeMillis();
            tiempototal=((tiempoFinal-tiempoInicial)/1000);
            System.out.println("\n El tiempo de ejecucion en segundos es: "+(tiempototal/60)+" minutos y "+(tiempototal%60)+" segundos");
        } catch (IOException ex) {
            Logger.getLogger(programa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void LecturaCompuestosQuimicos(String archivo) throws FileNotFoundException, IOException {
        String cadena;
        String palabra;
        int contador;
        FileReader archivoLeido = new FileReader(archivo);
        BufferedReader bufferArchivo = new BufferedReader(archivoLeido);
        cadena = bufferArchivo.readLine();
        while((cadena = bufferArchivo.readLine())!=null) {
            Compuesto=new CompuestoQuimico();
            StringTokenizer trozos=new StringTokenizer(cadena,"\t");
            contador=0;
            while(trozos.hasMoreTokens()){
                palabra=trozos.nextToken();
                if(contador==1)
                   Compuesto.setNombreCompuesto(palabra);
                if(contador==3)
                   Compuesto.setListaCompuesto(palabra);
                contador++;
            }
            ListaCompuestosQuimicos.add(Compuesto);
        }
        bufferArchivo.close();
    }  
    
   private void CoeficienteJaccardTanimotoParalelo(){
       EjecucionParalelo hilo[]=new EjecucionParalelo[numeroHilos];
       for(int i=0; i<numeroHilos;i++)
           if(i==0)
              hilo[i]=new EjecucionParalelo(ListaCompuestosQuimicos,0,(tamanoColeccionCompuestosQuimicos/(numeroHilos-(i+1))) , tamanoColeccionCompuestosQuimicos, MatrizdeCoeficientesJaccardTanimoto);
           else
               if(i==(numeroHilos-1))
                   hilo[i]=new EjecucionParalelo(ListaCompuestosQuimicos,(tamanoColeccionCompuestosQuimicos/(numeroHilos-i)),tamanoColeccionCompuestosQuimicos-1, tamanoColeccionCompuestosQuimicos, MatrizdeCoeficientesJaccardTanimoto);
               else
                   hilo[i]=new EjecucionParalelo(ListaCompuestosQuimicos,(tamanoColeccionCompuestosQuimicos/(numeroHilos-i)),(tamanoColeccionCompuestosQuimicos/(numeroHilos-(i+1))), tamanoColeccionCompuestosQuimicos, MatrizdeCoeficientesJaccardTanimoto);
       for(int i=0;i<numeroHilos;i++)
           hilo[i].start();
       try {
            for (int i=0; i<numeroHilos; i++) hilo[i].join();
        } catch (InterruptedException e) {
            System.out.println (e.getMessage());
        }
   }
   
   private void ImprimirSimilitudQuimica(){
       int contador=0;
       for(int i=0; i<tamanoColeccionCompuestosQuimicos-1 && contador<cantidadDatosImprimir ;i++)
           for(int j=i+1;j<tamanoColeccionCompuestosQuimicos && contador<cantidadDatosImprimir;j++)
               if(MatrizdeCoeficientesJaccardTanimoto[i][j]>=0.5){
                   System.out.println(ListaCompuestosQuimicos.get(i).getNombreCompuesto()+"\t"+ListaCompuestosQuimicos.get(j).getNombreCompuesto()+"\t"+formatoDosDecimales.format(MatrizdeCoeficientesJaccardTanimoto[i][j]));
                   contador++;
               }
   }
   
   private void EscrituraArchivoSalida(){
       FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter(archivoSalida);
            pw = new PrintWriter(fichero);
            for(int i=0; i<tamanoColeccionCompuestosQuimicos-1;i++)
                for(int j=i+1;j<tamanoColeccionCompuestosQuimicos;j++)
                pw.println(ListaCompuestosQuimicos.get(i).getNombreCompuesto()+"\t"+ListaCompuestosQuimicos.get(j).getNombreCompuesto()+"\t"+formatoDosDecimales.format(MatrizdeCoeficientesJaccardTanimoto[i][j])+"\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
   }
}
