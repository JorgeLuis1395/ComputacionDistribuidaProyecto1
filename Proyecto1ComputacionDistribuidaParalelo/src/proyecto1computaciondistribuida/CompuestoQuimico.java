package proyecto1computaciondistribuida;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompuestoQuimico implements Comparable<CompuestoQuimico> {
    private String NombreCompuesto;
    private int ValorCompuesto;
    HashMap<String,Integer> listaCompuesto = new HashMap<String,Integer>();
    public CompuestoQuimico(){
        ValorCompuesto=0;
        NombreCompuesto="";
    }

    public String getNombreCompuesto() {
        return NombreCompuesto;
    }

    public void setNombreCompuesto(String NombreCompuesto) {
        this.NombreCompuesto = NombreCompuesto;
    }

    public int getValorCompuesto() {
        return ValorCompuesto;
    }

    private void setValorCompuesto() {
        Iterator it = listaCompuesto.keySet().iterator();
        while(it.hasNext()){
            String clave = (String) it.next();
            this.ValorCompuesto+=listaCompuesto.get(clave);
        }
    }
    
    public void setListaCompuesto(String codigoCompuesto) {
        String caracter;
        Pattern pat = Pattern.compile("[a-zA-Z]");
        for(int i=0;i<codigoCompuesto.length();i++){
            caracter=codigoCompuesto.substring(i,i+1);
            Matcher mat = pat.matcher(caracter);
            if (mat.matches())
                if (listaCompuesto.containsKey(caracter))
                    listaCompuesto.put(caracter, listaCompuesto.get(caracter)+1);
                else
                    listaCompuesto.put(caracter, 1);
            else
                if(caracter.equals("@"))
                    listaCompuesto.put(caracter, 1);
        }
        setValorCompuesto();
    }

    @Override
    public int compareTo(CompuestoQuimico o) {
        String a=new String(this.getNombreCompuesto());
        String b=new String(o.getNombreCompuesto());
        return a.compareTo(b);
    }
}
