import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import java.awt.BorderLayout;
public class VentanaPrincipal extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField campoTexto;
    private JButton botonValidar;
    private PanelExpresion panel;
    public VentanaPrincipal() {
        setTitle("Expresión Regular");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        campoTexto = new JTextField();
        botonValidar = new JButton("Validar");
        panel = new PanelExpresion();
        add(campoTexto, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        add(botonValidar, BorderLayout.SOUTH);
        new Evento(
                campoTexto,
                botonValidar,
                panel);
    }
    public static void main(String[] args) {
        VentanaPrincipal ventana =
                new VentanaPrincipal();
        ventana.setVisible(true);
    }
}
