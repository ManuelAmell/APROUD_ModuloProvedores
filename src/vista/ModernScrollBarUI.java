package vista;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

/**
 * ScrollBar moderna y minimalista con tema oscuro.
 * Diseño delgado que aparece al hacer hover.
 */
public class ModernScrollBarUI extends BasicScrollBarUI {
    
    private final Color THUMB_COLOR = new Color(100, 120, 150, 180);      // Azul grisáceo semi-transparente
    private final Color THUMB_HOVER_COLOR = new Color(120, 150, 200, 220); // Azul más brillante al hover
    private final Color TRACK_COLOR = new Color(30, 42, 65, 50);          // Casi transparente
    
    private final int THUMB_SIZE = 9;  // Grosor del thumb (delgado)
    private final int THUMB_RADIUS = 4; // Radio de las esquinas redondeadas
    
    private boolean isHover = false;
    
    @Override
    protected void configureScrollBarColors() {
        // No usar los colores por defecto
    }
    
    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createInvisibleButton();
    }
    
    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createInvisibleButton();
    }
    
    /**
     * Crea un botón invisible (sin flechas)
     */
    private JButton createInvisibleButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        return button;
    }
    
    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Pintar el track (fondo) - casi invisible
        g2.setColor(TRACK_COLOR);
        g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        
        g2.dispose();
    }
    
    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
            return;
        }
        
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Color según hover
        Color thumbColor = isHover ? THUMB_HOVER_COLOR : THUMB_COLOR;
        g2.setColor(thumbColor);
        
        // Dibujar thumb redondeado y delgado
        if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
            int x = thumbBounds.x + (thumbBounds.width - THUMB_SIZE) / 2;
            int y = thumbBounds.y;
            int width = THUMB_SIZE;
            int height = thumbBounds.height;
            
            g2.fillRoundRect(x, y, width, height, THUMB_RADIUS, THUMB_RADIUS);
        } else {
            int x = thumbBounds.x;
            int y = thumbBounds.y + (thumbBounds.height - THUMB_SIZE) / 2;
            int width = thumbBounds.width;
            int height = THUMB_SIZE;
            
            g2.fillRoundRect(x, y, width, height, THUMB_RADIUS, THUMB_RADIUS);
        }
        
        g2.dispose();
    }
    
    @Override
    protected void setThumbBounds(int x, int y, int width, int height) {
        super.setThumbBounds(x, y, width, height);
        scrollbar.repaint();
    }
    
    @Override
    protected void installListeners() {
        super.installListeners();
        
        // Listener para hover
        scrollbar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                isHover = true;
                scrollbar.repaint();
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                isHover = false;
                scrollbar.repaint();
            }
        });
    }
    
    /**
     * Aplica el scrollbar moderno a un JScrollPane
     */
    public static void aplicarScrollModerno(JScrollPane scrollPane) {
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(new ModernScrollBarUI());
        
        // Configuración adicional
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        
        // Hacer el scrollbar más delgado
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(12, 0));
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 12));
        
        // Sin bordes
        scrollPane.setBorder(null);
        scrollPane.setViewportBorder(null);
    }
}
