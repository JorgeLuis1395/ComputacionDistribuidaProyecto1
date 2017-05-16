package proyecto1computaciondistribuida;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;
public class LeerArchivo {
    public static String NombreArchivo="";
    private JFrame Ventana = new JFrame("ARCHIVO DE ENTRADA");
    public LeerArchivo(){
        JFileChooser fileChooser = new JFileChooser(".");
	FileNameExtensionFilter filtro = new FileNameExtensionFilter("*.tsv", "tsv");
        fileChooser.setFileFilter(filtro);
        fileChooser.setControlButtonsAreShown(true);
        Ventana.add(fileChooser, BorderLayout.CENTER);
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser theFileChooser = (JFileChooser) actionEvent.getSource();	        
                String command = actionEvent.getActionCommand();
                theFileChooser.setMultiSelectionEnabled(true);
                if (command.equals(JFileChooser.APPROVE_SELECTION)) {
                    File selectedFile = theFileChooser.getSelectedFile();
                    NombreArchivo=selectedFile.getParent()+"\\" +selectedFile.getName();
                } else if (command.equals(JFileChooser.CANCEL_SELECTION))
                    System.out.println(JFileChooser.CANCEL_SELECTION);
                cerrar();
                if(!NombreArchivo.equals(""))
                        new programa(NombreArchivo);
                else
                    System.exit(0);
            }
        };
        fileChooser.addActionListener(actionListener);
        Ventana.pack();
        Ventana.setVisible(true);
    }
    private void cerrar(){
	Ventana.setVisible(false);
    }
}
