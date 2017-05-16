package proyecto1computaciondistribuida;


import java.util.*;

public class EjecucionParalelo extends Thread{
    private ArrayList<CompuestoQuimico> ListaCompuestosQuimicos = new ArrayList<CompuestoQuimico>();
    private int tamanoColeccionCompuestosQuimicos,iInicial,iFin;
    private float[][] MatrizdeCoeficientesJaccardTanimoto;
    public EjecucionParalelo(ArrayList<CompuestoQuimico> ListaCompuestos,int inicio, int fin,int cantidadCompuestos, float[][] matriz) {
        ListaCompuestosQuimicos=ListaCompuestos;
        iInicial=inicio;
        iFin=fin;
        tamanoColeccionCompuestosQuimicos=cantidadCompuestos;
        MatrizdeCoeficientesJaccardTanimoto=matriz;
    }
    public void run() {
        CoeficienteJaccardTanimotoParalelo();
    }
    
    private void CoeficienteJaccardTanimotoParalelo(){
       float Coeficiente;
       for(int i=iInicial; i<iFin;i++)
           for(int j=i+1;j<tamanoColeccionCompuestosQuimicos;j++){
               Coeficiente=calculoTab(ListaCompuestosQuimicos.get(i),ListaCompuestosQuimicos.get(j));
               MatrizdeCoeficientesJaccardTanimoto[i][j]=Coeficiente;
               MatrizdeCoeficientesJaccardTanimoto[j][i]=Coeficiente;
            }
   }
    
    private float calculoTab(CompuestoQuimico a, CompuestoQuimico b){
        int Nc=CalculoNc(a, b);
        float valor=(Nc/(float)(a.getValorCompuesto()+b.getValorCompuesto()-Nc));
        return valor;
    }
    
    private int CalculoNc(CompuestoQuimico a, CompuestoQuimico b){
        int Nc=0;
        Iterator it = b.listaCompuesto.keySet().iterator();
        while(it.hasNext()){
            String clave = (String) it.next();
            if (a.listaCompuesto.containsKey(clave))
                 if(a.listaCompuesto.get(clave)<=b.listaCompuesto.get(clave))
                     Nc+=a.listaCompuesto.get(clave);
                 else
                     Nc+=b.listaCompuesto.get(clave);
        }
        return Nc;
    }
}
