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
                    "No hay tickets registrados hoy.",
                    "Cierre de caja",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        float totalCaja = 0;
        Map<String, Integer> conteoProductos = new HashMap<>();
        Set<String> trabajadoresHoy = new TreeSet<>();


        for (Ticket t : ticketsHoy) {

            if (t.getProductos() != null) {

                totalCaja += t.getTotal();

                for (modelo.Producto p : t.getProductos()) {

                    conteoProductos.put(
                            p.getNombre(),
                            conteoProductos.getOrDefault(p.getNombre(), 0) + 1
                    );
                }
            }


            if (t.getNombresCamareros() != null &&
                    !t.getNombresCamareros().equals("Ninguno")) {

                String[] nombres = t.getNombresCamareros().split(", ");

                for (String nombre : nombres) {
                    trabajadoresHoy.add(nombre.trim());
                }
            }
        }


        List<Map.Entry<String, Integer>> ranking =
                new ArrayList<>(conteoProductos.entrySet());

        ranking.sort((a, b) ->
                b.getValue().compareTo(a.getValue()));


        // PANEL PRINCIPAL SWING

        JPanel panel = new JPanel(new BorderLayout(10, 10));


        JLabel titulo = new JLabel(
                "RESUMEN DE CAJA DIARIO",
                SwingConstants.CENTER
        );

        titulo.setFont(new Font("Arial", Font.BOLD, 18));


        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));


        area.append("---------------------------------------\n");
        area.append("          RESUMEN DE CAJA\n");
        area.append("---------------------------------------\n\n");

        area.append("TOTAL GANADO HOY: "
                + String.format("%.2f", totalCaja)
                + " €\n\n");


        area.append("PERSONAL EN TURNO:\n");

        if (trabajadoresHoy.isEmpty()) {

            area.append("- Sin asignar\n");

        } else {

            for (String empleado : trabajadoresHoy) {
                area.append("- " + empleado + "\n");
            }
        }


        area.append("\nPRODUCTOS VENDIDOS:\n");


        for (Map.Entry<String, Integer> e : ranking) {

            area.append(
                    "- " + e.getKey()
                            + ": "
                            + e.getValue()
                            + " uds\n"
            );
        }


        JScrollPane scroll = new JScrollPane(area);


        JButton btnExportar = new JButton("Exportar TXT");
        JButton btnCerrar = new JButton("Cerrar");


        JPanel botones = new JPanel();

        botones.add(btnExportar);
        botones.add(btnCerrar);


        panel.add(titulo, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);


        JDialog ventana = new JDialog(
                this,
                "Cierre de caja",
                true
        );


        ventana.setSize(500, 450);
        ventana.setLocationRelativeTo(this);


        ventana.add(panel);


        // EXPORTAR

        btnExportar.addActionListener(e -> {

            String ruta =
                    System.getProperty("user.home")
                            + File.separator
                            + "Desktop"
                            + File.separator;


            String fecha =
                    new SimpleDateFormat("yyyyMMdd")
                            .format(new Date());


            String fichero =
                    ruta + "Cierre_" + fecha + ".txt";


            try (FileWriter fw = new FileWriter(fichero)) {


                fw.write(area.getText());


                fw.write(
                        "\n---------------------------------------\n"
                );

                fw.write(
                        "DESGLOSE DE TICKETS\n"
                );


                for (Ticket t : ticketsHoy) {

                    fw.write("\n");
                    fw.write(t.toString());

                }


                JOptionPane.showMessageDialog(
                        ventana,
                        "Informe exportado correctamente:\n"
                                + fichero
                );


            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        ventana,
                        "Error al exportar: "
                                + ex.getMessage()
                );
            }

        });


        btnCerrar.addActionListener(e -> ventana.dispose());


        ventana.setVisible(true);
    }
}