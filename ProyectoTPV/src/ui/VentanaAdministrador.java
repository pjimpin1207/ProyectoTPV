package ui;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import modelo.Producto;
import modelo.Categoria;
import modelo.Ticket;
import dao.ProductoDAO;
import dao.UsuarioDAO;
import dao.TicketObjectDBDAO;

public class VentanaAdministrador extends JFrame {

    private ProductoDAO productoDAO = new ProductoDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public VentanaAdministrador() {
        setTitle("Panel de Control - Administrador");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitulo = new JLabel("GESTIÓN DEL RESTAURANTE", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // Panel de botones organizado en 3 columnas
        JPanel panelBotones = new JPanel(new GridLayout(0, 3, 20, 20));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));

        JButton btnAñadirProd = crearBoton("Añadir Producto", Color.decode("#4CAF50"));
        JButton btnGestionarProd = crearBoton("Modificar Producto", Color.decode("#FF9800"));
        JButton btnCierreCaja = crearBoton("CIERRE DE CAJA", Color.decode("#607D8B"));
        JButton btnAñadirCam = crearBoton("Añadir Camarero", Color.decode("#2196F3"));
        JButton btnEliminarCam = crearBoton("Eliminar Camarero", Color.decode("#9C27B0"));

        // Lógica de Añadir Producto
        btnAñadirProd.addActionListener(e -> {
            JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
            JTextField txtNombre = new JTextField();
            JComboBox<Categoria> cbCat = new JComboBox<>(Categoria.values());
            JTextField txtPrecio = new JTextField();
            panel.add(new JLabel("Nombre:"));
            panel.add(txtNombre);
            panel.add(new JLabel("Categoría:"));
            panel.add(cbCat);
            panel.add(new JLabel("Precio (€):"));
            panel.add(txtPrecio);
            if (JOptionPane.showConfirmDialog(this, panel, "Nuevo Producto", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    float precio = Float.parseFloat(txtPrecio.getText().replace(",", "."));
                    productoDAO.insertarProducto(new Producto(0, txtNombre.getText().toUpperCase(), (Categoria) cbCat.getSelectedItem(), precio));
                    JOptionPane.showMessageDialog(this, "Producto añadido.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Datos inválidos.");
                }
            }
        });

        // Lógica unificada de gestión de producto
        btnGestionarProd.addActionListener(e -> {
            List<Producto> lista = productoDAO.obtenerTodos();
            if (lista.isEmpty()) return;
            String[] nombres = lista.stream().map(Producto::getNombre).toArray(String[]::new);
            String seleccionado = (String) JOptionPane.showInputDialog(this, "Selecciona:", "Gestión", JOptionPane.QUESTION_MESSAGE, null, nombres, nombres[0]);
            if (seleccionado != null) {
                Producto p = lista.stream().filter(prod -> prod.getNombre().equals(seleccionado)).findFirst().orElse(null);
                String[] opciones = {"Modificar Nombre", "Modificar Precio", "Eliminar Producto"};
                int eleccion = JOptionPane.showOptionDialog(this, "Gestión de: " + seleccionado, "Opciones", 0, 1, null, opciones, opciones[0]);
                if (eleccion == 0) {
                    String nuevoNombre = JOptionPane.showInputDialog(this, "Nuevo nombre:", p.getNombre());
                    if (nuevoNombre != null)
                        productoDAO.modificarNombreProducto(seleccionado, nuevoNombre.toUpperCase());
                } else if (eleccion == 1) {
                    String nuevoPrecio = JOptionPane.showInputDialog(this, "Nuevo precio:", p.getPrecio());
                    if (nuevoPrecio != null)
                        productoDAO.modificarProducto(seleccionado, Float.parseFloat(nuevoPrecio.replace(",", ".")));
                } else if (eleccion == 2) {
                    productoDAO.eliminarProducto(seleccionado);
                }
                JOptionPane.showMessageDialog(this, "Acción realizada.");
            }
        });

        // Añadir/Eliminar camareros
        btnAñadirCam.addActionListener(e -> {
            String nombre = JOptionPane.showInputDialog(this, "Nombre:");
            if (nombre != null) usuarioDAO.insertarCamarero(nombre.toUpperCase());
        });

        btnEliminarCam.addActionListener(e -> {
            List<String> cams = usuarioDAO.obtenerNombresCamareros();
            String sel = (String) JOptionPane.showInputDialog(this, "Selecciona:", "Eliminar Camarero", 2, null, cams.toArray(), null);
            if (sel != null) usuarioDAO.eliminarCamarero(sel);
        });

        btnCierreCaja.addActionListener(e -> realizarCierreDeCaja());

        panelBotones.add(btnAñadirProd);
        panelBotones.add(btnGestionarProd);
        panelBotones.add(btnCierreCaja);
        panelBotones.add(btnAñadirCam);
        panelBotones.add(btnEliminarCam);

        add(panelBotones, BorderLayout.CENTER);

        JButton btnSalir = new JButton("VOLVER A SALA");
        btnSalir.addActionListener(e -> {
            new VentanaMesas(new Date()).setVisible(true);
            this.dispose();
        });
        add(btnSalir, BorderLayout.SOUTH);
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        return btn;
    }

    private void realizarCierreDeCaja() {
        TicketObjectDBDAO tDao = new TicketObjectDBDAO();
        List<Ticket> ticketsHoy = tDao.obtenerTicketsHoy();

        if (ticketsHoy.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay tickets registrados en el día de hoy.",
                    "Cierre de Caja",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        float totalCaja = 0;

        // Sumamos el total de todos los tickets del día DIRECTAMENTE
        // (Hemos quitado el 'if' problemático que daba 0)
        for (Ticket t : ticketsHoy) {
            totalCaja += t.getTotal();
        }

        // --- GUARDADO EN ARCHIVO HISTÓRICO (MODO APPEND) ---
        String ruta = System.getProperty("user.home") + java.io.File.separator + "Desktop" + java.io.File.separator;
        String nombreFichero = ruta + "Historial_Cierres_Caja.txt";

        try (java.io.FileWriter fw = new java.io.FileWriter(nombreFichero, true)) {
            String fechaActual = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date());

            fw.write("=======================================\n");
            fw.write("CIERRE DE CAJA - " + fechaActual + "\n");
            fw.write("TOTAL FACTURADO: " + String.format("%.2f", totalCaja) + "€\n");
            fw.write("Tickets procesados: " + ticketsHoy.size() + "\n");
            fw.write("=======================================\n\n");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al escribir en el histórico: " + ex.getMessage(), "Error de Archivo", JOptionPane.ERROR_MESSAGE);
        }

        // --- AVISO RÁPIDO EN PANTALLA ---
        JOptionPane.showMessageDialog(this,
                "TOTAL FACTURADO HOY: " + String.format("%.2f", totalCaja) + "€\n\n(Registro añadido a Historial_Cierres_Caja.txt en tu Escritorio)",
                "Resumen de Caja Diario",
                JOptionPane.INFORMATION_MESSAGE);
    }
}