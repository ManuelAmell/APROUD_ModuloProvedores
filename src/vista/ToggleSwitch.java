package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Componente personalizado de Toggle Switch para representar estados Activo/Inactivo.
 * - Verde cuando está Activo (true)
 * - Rojo cuando está Inactivo (false)
 */
public class ToggleSwitch extends JPanel {
    
    private boolean activo = true; // Estado por defecto: Activo
    private final Color COLOR_ACTIVO = new Color(80, 255, 120);   // Verde brillante
    private final Color COLOR_INACTIVO = new Color(255, 80, 80);  // Rojo brillante
    private final Color COLOR_FONDO_OSCURO = new Color(30, 42, 65);
    private final Color COLOR_CIRCULO = Color.WHITE;
    
    private int switchWidth = 60;
    private int switchHeight = 28;
    private int circleSize = 22;
    private int padding = 3;
    
    // Animación
    private float circleX = padding;
    private float targetX = padding;
    private Timer animationTimer;
    
    public ToggleSwitch() {
        setPreferredSize(new Dimension(switchWidth, switchHeight));
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Inicializar posición del círculo según estado inicial
        if (activo) {
            circleX = switchWidth - circleSize - padding;
            targetX = circleX;
        }
        
        // Configurar animación
        animationTimer = new Timer(10, e -> {
            float diff = targetX - circleX;
            if (Math.abs(diff) > 0.5f) {
                circleX += diff * 0.3f;
                repaint();
            } else {
                circleX = targetX;
                animationTimer.stop();
                repaint();
            }
        });
        
        // Evento de click
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggle();
            }
        });
    }
    
    /**
     * Cambia el estado del switch
     */
    public void toggle() {
        activo = !activo;
        animateToggle();
        firePropertyChange("estado", !activo, activo);
    }
    
    /**
     * Establece el estado del switch
     */
    public void setActivo(boolean activo) {
        if (this.activo != activo) {
            this.activo = activo;
            animateToggle();
            firePropertyChange("estado", !activo, activo);
        }
    }
    
    /**
     * Obtiene el estado actual
     */
    public boolean isActivo() {
        return activo;
    }
    
    /**
     * Anima el movimiento del círculo
     */
    private void animateToggle() {
        if (activo) {
            targetX = switchWidth - circleSize - padding;
        } else {
            targetX = padding;
        }
        
        if (!animationTimer.isRunning()) {
            animationTimer.start();
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        
        // Activar antialiasing para bordes suaves
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Dibujar fondo del switch (redondeado)
        Color backgroundColor = activo ? COLOR_ACTIVO : COLOR_INACTIVO;
        g2.setColor(backgroundColor);
        g2.fill(new RoundRectangle2D.Float(0, 0, switchWidth, switchHeight, switchHeight, switchHeight));
        
        // Dibujar círculo (botón del switch)
        g2.setColor(COLOR_CIRCULO);
        g2.fillOval((int) circleX, padding, circleSize, circleSize);
        
        g2.dispose();
    }
}
