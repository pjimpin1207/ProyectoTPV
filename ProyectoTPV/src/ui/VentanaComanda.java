package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import modelo.Mesa;
import modelo.Ticket;
import modelo.Producto;
import dao.ProductoDAO;

public class VentanaComanda extends JFrame {
    private JTable tablaTicket;
    private DefaultTableModel modeloTabla;
    private JLabel lblTotal;
    private Ticket ticket;
    private Mesa mesa;
    private List<List<Producto>> lineasVisuales;

    public VentanaComanda(Mesa mesa, String camarero, Ticket ticket) {
        this.mesa = mesa;
        this.ticket = ticket;
        this.lineasVisuales = new ArrayList<>();

        // NUEVO: Registramos al camarero actual en la cuenta de la mesa
        this.ticket.añadirCamarero(camarero);

        // El título muestra todos los camareros que han pasado por la mesa
        setTitle("Mesa Nº " + mesa.getNumero() + " - Atendida por: " + ticket.getNombresCamareros());
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.setPreferredSize(new Dimension(400, 0));
        panelIzquierdo.setBorder(BorderFactory.createTitledBorder("PRODUCTOS DEL TICKET"));

        String[] columnas = {"Cant.", "Producto", "P. Unit", "Total"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tablaTicket = new JTable(modeloTabla);
        tablaTicket.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaTicket.setFont(new Font("Arial", Font.PLAIN, 14));
        tablaTicket.setRowHeight(25);
        tablaTicket.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaTicket.getColumnModel().getColumn(1).setPreferredWidth(150);

        panelIzquierdo.add(new JScrollPane(tablaTicket), BorderLayout.CENTER);
        add(panelIzquierdo, BorderLayout.WEST);

        JPanel panelProductos = new JPanel(new GridLayout(0, 3, 10, 10));
        panelProductos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        ProductoDAO dao = new ProductoDAO();
        List<Producto> carta = dao.obtenerTodos();

        for (Producto p : carta) {
            JButton btnProd = new JButton(p.getNombre());
            btnProd.addActionListener(e -> {
                Producto prodClonado = new Producto(p.getId(), p.getNombre(), p.getCategoria(), p.getPrecio());
                ticket.añadirProducto(prodClonado);
                actualizarResumen();
            });
            panelProductos.add(btnProd);
        }
        add(new JScrollPane(panelProductos), BorderLayout.CENTER);

        JPanel panelSur = new JPanel(new BorderLayout());
        JPanel panelControles = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnTicket = new JButton("TICKET");
        JButton btnModificar = new JButton("MODIFICAR");
        JButton btnCobrar = new JButton("COBRAR");
        JButton btnSalir = new JButton("SALIR");

        // ACTUALIZADO: El ticket impreso ya contiene todos los datos y camareros
        btnTicket.addActionListener(e -> {
            if (ticket.getProductos().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El ticket está vacío.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                String nombreFichero = "Ticket_Mesa_" + mesa.getNumero() + ".txt";
                FileWriter fw = new FileWriter(nombreFichero);
                fw.write(ticket.toString()); // Escribe todo de una vez
                fw.write("\n¡Gracias por su visita!");
                fw.close();
                JOptionPane.showMessageDialog(this, "Ticket impreso correctamente en:\n" + nombreFichero);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al generar el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnModificar.addActionListener(e -> {
            int filaSel = tablaTicket.getSelectedRow();
            if (filaSel != -1) {
                List<Producto> productosLinea = lineasVisuales.get(filaSel);
                Producto prodReferencia = productosLinea.get(0);

                JTextField txtCantidad = new JTextField(String.valueOf(productosLinea.size()));
                JTextField txtPrecio = new JTextField(String.valueOf(prodReferencia.getPrecio()));

                Object[] mensaje = { "Nueva cantidad:", txtCantidad, "Nuevo precio unitario (€):", txtPrecio };

                int opcion = JOptionPane.showConfirmDialog(this, mensaje, "Modificar " + prodReferencia.getNombre(), JOptionPane.OK_CANCEL_OPTION);
                if (opcion == JOptionPane.OK_OPTION) {
                    try {
                        int nuevaCantidad = Integer.parseInt(txtCantidad.getText());
                        float nuevoPrecio = Float.parseFloat(txtPrecio.getText().replace(",", "."));
                        ticket.getProductos().removeAll(productosLinea);
                        for (int i = 0; i < nuevaCantidad; i++) {
                            ticket.añadirProducto(new Producto(prodReferencia.getId(), prodReferencia.getNombre(), prodReferencia.getCategoria(), nuevoPrecio));
                        }
                        ticket.calcularTotal();
                        actualizarResumen();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Introduce valores numéricos correctos.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona un producto de la tabla.", "Atención", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnSalir.addActionListener(e -> {
            new VentanaMesas(new java.util.Date()).setVisible(true);
            this.dispose();
        });

        btnCobrar.addActionListener(e -> {
            new DialogoCobro(this, mesa, camarero, ticket).setVisible(true);
        });

        panelControles.add(btnTicket);
        panelControles.add(btnModificar);
        panelControles.add(btnCobrar);
        panelControles.add(btnSalir);

        lblTotal = new JLabel("TOTAL: 0.00€  ");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 24));
        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);

        panelSur.add(panelControles, BorderLayout.WEST);
        panelSur.add(lblTotal, BorderLayout.EAST);
        add(panelSur, BorderLayout.SOUTH);

        actualizarResumen();
    }

    private void actualizarResumen() {
        modeloTabla.setRowCount(0);
        lineasVisuales.clear();

        if (ticket.getProductos().isEmpty()) {
            mesa.cambiarEstado(modelo.EstadoMesa.LIBRE);
        } else {
            mesa.cambiarEstado(modelo.EstadoMesa.OCUPADA);
        }

        Map<String, List<Producto>> agrupados = new LinkedHashMap<>();
        for (Producto p : ticket.getProductos()) {
            String clave = p.getNombre() + "_" + p.getPrecio();
            agrupados.computeIfAbsent(clave, k -> new ArrayList<>()).add(p);
        }

        for (List<Producto> lista : agrupados.values()) {
            lineasVisuales.add(lista);
            int cantidad = lista.size();
            Producto referencia = lista.get(0);
            float totalLinea = cantidad * referencia.getPrecio();

            modeloTabla.addRow(new Object[]{
                    cantidad,
                    referencia.getNombre(),
                    String.format("%.2f €", referencia.getPrecio()),
                    String.format("%.2f €", totalLinea)
            });
        }
        lblTotal.setText("TOTAL: " + String.format("%.2f", ticket.getTotal()) + "€  ");
    }
}