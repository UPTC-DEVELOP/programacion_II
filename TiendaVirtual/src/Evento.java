import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JTextField;
public class Evento implements ActionListener {
    private JTextField campoTexto;
    private JButton botonValidar;
    private PanelExpresion panel;
    public Evento(JTextField campoTexto,
                  JButton botonValidar,
                  PanelExpresion panel) {
        this.campoTexto = campoTexto;
        this.botonValidar = botonValidar;
        this.panel = panel;
        botonValidar.addActionListener(this);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String texto = campoTexto.getText();
        if (Pattern.matches("^[a-zA-Z0-9]+$", texto)) {
            panel.mostrarCorrecto();
        } else {
            panel.mostrarIncorrecto();
        }
    }
}