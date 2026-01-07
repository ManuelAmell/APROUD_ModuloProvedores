package vista;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;

/**
 * Pantalla de carga limpia con imagen de fondo y barra de progreso.
 */
public class PantallaCarga extends JWindow {

    private static final Color BG_PRINCIPAL = new Color(25, 35, 55);
    private JProgressBar progressBar;

    public PantallaCarga() {
        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {
        setSize(600, 400);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
    }

    private void inicializarComponentes() {
        // Panel principal con imagen de fondo
        JPanel panel = new JPanel() {
            private Image imagenFondo = null;

            {
                // Cargar imagen de fondo
                String[] rutasImagen = {
                        "lib/logo_carga.png",
                        "lib/ModuloProveedores.png",
                        "logo_carga.png"
                };

                for (String ruta : rutasImagen) {
                    try {
                        File archivoImagen = new File(ruta);
                        if (archivoImagen.exists()) {
                            ImageIcon icono = new ImageIcon(ruta);
                            if (icono.getIconWidth() > 0) {
                                imagenFondo = icono.getImage();
                                if (DialogoConfiguracion.isDebugMode()) {
                                    System.out.println("✓ Imagen de fondo cargada desde: " + ruta);
                                }
                                break;
                            }
                        }
                    } catch (Exception e) {
                        // Continuar con la siguiente ruta
                    }
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Activar antialiasing para mejor calidad
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                if (imagenFondo != null) {
                    // Dibujar imagen escalada para cubrir toda la ventana
                    g2d.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
                } else {
                    // Si no hay imagen, fondo sólido
                    g2d.setColor(BG_PRINCIPAL);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };

        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(new Color(0, 120, 215), 2));

        // Panel inferior con barra de progreso
        JPanel panelInferior = new JPanel(new BorderLayout(0, 5));
        panelInferior.setOpaque(false);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Sin border

        // Barra de progreso minimalista (pegada al borde inferior o flotante abajo)
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(false); // Sin texto
        progressBar.setForeground(new Color(0, 120, 215)); // Azul corporativo
        progressBar.setBackground(new Color(40, 50, 70, 0)); // Fondo transparente
        progressBar.setBorderPainted(false);
        progressBar.setPreferredSize(new Dimension(600, 6)); // Barra muy delgada

        panelInferior.add(progressBar, BorderLayout.SOUTH);

        // Agregar componentes al panel principal
        panel.add(panelInferior, BorderLayout.SOUTH);

        setContentPane(panel);
    }

    /**
     * Actualiza el progreso de la barra
     */
    public void actualizarProgreso(int progreso) {
        SwingUtilities.invokeLater(() -> progressBar.setValue(progreso));
    }

    /**
     * Cierra la pantalla de carga con efecto de fade out
     */
    public void cerrar() {
        SwingUtilities.invokeLater(() -> {
            setVisible(false);
            dispose();
        });
    }

    /**
     * Simula una carga con progreso automático
     */
    public void simularCarga(Runnable onComplete) {
        new Thread(() -> {
            try {
                // Usar pasos de carga más rápidos ya que no hay texto que leer
                int numPasos = 10;

                for (int i = 0; i < numPasos; i++) {
                    final int progreso = (i + 1) * 100 / numPasos;

                    SwingUtilities.invokeLater(() -> {
                        actualizarProgreso(progreso);
                    });

                    // Tiempo de carga más rápido (200ms = 2 segundos total)
                    Thread.sleep(200);
                }

                // Esperar un poco antes de cerrar
                Thread.sleep(100);

                // Cerrar y ejecutar callback
                SwingUtilities.invokeLater(() -> {
                    cerrar();
                    if (onComplete != null) {
                        onComplete.run();
                    }
                });

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
