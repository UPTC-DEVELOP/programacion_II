import java.awt.Color;
import javax.swing.JPanel;
public class PanelExpresion extends JPanel {
    private static final long serialVersionUID = 1L;
    public PanelExpresion() {
        setBackground(Color.LIGHT_GRAY);
    }
    public void mostrarCorrecto() {
        setBackground(Color.GREEN);
    }
    public void mostrarIncorrecto() {
        setBackground(Color.RED);
    }
}
