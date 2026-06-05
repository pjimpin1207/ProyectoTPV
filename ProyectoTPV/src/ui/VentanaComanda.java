package ui;

import java.awt.*;
import java.awt.event.*;
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

public class VentanaComanda extends Frame {
    private java.awt.List listaTicketVisual;
    private Label lblTotal;
    private Ticket ticket;
    private Mesa mesa;
    private List<List<Producto>> lineasVisuales;

    public VentanaComanda(Mesa mesa, String camarero, Ticket ticket) {
        this.mesa = mesa;
        this.ticket = ticket;
        this.lineasVisuales = new ArrayList<>();
        this.ticket.añadirCamarero(camarero);

        setTitle("Mesa Nº " + mesa.getNumero() + " - " + ticket.getNombresCamareros());
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { dispose(); }
        });

        Panel panelIzquierdo = new Panel(new BorderLayout());
        panelIzquierdo.setPreferredSize(new Dimension(400, 0));
        panelIzquierdo.add(new Label("PRODUCTOS DEL TICKET", Label.CENTER), BorderLayout.NORTH);

        listaTicketVisual = new java.awt.List();
        // Usamos Monospaced para poder simular una tabla
        listaTicketVisual.setFont(new Font("Monospaced", Font.PLAIN, 14));
        panelIzquierdo.add(listaTicketVisual, BorderLayout.CENTER);
        add(panelIzquierdo, BorderLayout.WEST);

        Panel panelProductos = new Panel(new GridLayout(0, 3, 5, 5));
        ProductoDAO dao = new ProductoDAO();
        List<Producto> carta = dao.obtenerTodos();

        for (Producto p : carta) {
            Button btnProd = new Button(p.getNombre());
            btnProd.addActionListener(e -> {
                Producto prodClonado = new Producto(p.getId(), p.getNombre(), p.getCategoria(), p.getPrecio());
                this.ticket.añadirProducto(prodClonado);
                actualizarResumen();
            });
            panelProductos.add(btnProd);
        }

        ScrollPane scrollProds = new ScrollPane();
        scrollProds.add(panelProductos);
        add(scrollProds, BorderLayout.CENTER);

        Panel panelSur = new Panel(new BorderLayout());
        Panel panelControles = new Panel(new FlowLayout(FlowLayout.LEFT));

        Button btnTicket = new Button("TICKET");
        Button btnModificar = new Button("MODIFICAR");
        Button btnCobrar = new Button("COBRAR");
        Button btnSalir = new Button("SALIR");

        btnTicket.addActionListener(e -> {
            if (ticket.getProductos().isEmpty()) {
                MensajesAWT.mostrarMensaje(this, "El ticket está vacío.", "Aviso");
                return;
            }
            try {
                String nombreFichero = System.getProperty("user.home") + java.io.File.separator + "Desktop" + java.io.File.separator + "Ticket_Mesa_" + mesa.getNumero() + ".txt";
                FileWriter fw = new FileWriter(nombreFichero);
                fw.write(ticket.toString());
                fw.write("\n¡Gracias por su visita!");
                fw.close();
                MensajesAWT.mostrarMensaje(this, "Ticket impreso en tu Escritorio.", "Éxito");
            } catch (IOException ex) {
                MensajesAWT.mostrarMensaje(this, "Error al generar el archivo.", "Error");
            }
        });

        btnModificar.addActionListener(e -> {
            // Restamos 2 porque las dos primeras líneas de la lista son de cabecera visual
            int filaSel = listaTicketVisual.getSelectedIndex() - 2;

            if (filaSel >= 0 && filaSel < lineasVisuales.size()) {
                List<Producto> productosLinea = lineasVisuales.get(filaSel);
                Producto prodRef = productosLinea.get(0);

                String nuevaCantStr = MensajesAWT.pedirInput(this, "Nueva cantidad para " + prodRef.getNombre() + ":", "Modificar Cantidad", String.valueOf(productosLinea.size()));
                if(nuevaCantStr != null) {
                    try {
                        int cant = Integer.parseInt(nuevaCantStr);
                        String nuevoPrecioStr = MensajesAWT.pedirInput(this, "Nuevo precio unitario (€):", "Modificar Precio", String.valueOf(prodRef.getPrecio()));
                        if(nuevoPrecioStr != null) {
                            float precio = Float.parseFloat(nuevoPrecioStr.replace(",", "."));
                            this.ticket.getProductos().removeAll(productosLinea);
                            for (int i = 0; i < cant; i++) {
                                this.ticket.añadirProducto(new Producto(prodRef.getId(), prodRef.getNombre(), prodRef.getCategoria(), precio));
                            }
                            this.ticket.calcularTotal();
                            actualizarResumen();
                        }
                    } catch (Exception ex) { MensajesAWT.mostrarMensaje(this, "Número inválido.", "Error"); }
                }
            } else {
                MensajesAWT.mostrarMensaje(this, "Selecciona un producto válido de la lista primero.", "Atención");
            }
        });

        btnSalir.addActionListener(e -> {
            new VentanaMesas(new java.util.Date()).setVisible(true);
            this.dispose();
        });

        btnCobrar.addActionListener(e -> {
            if(!ticket.getProductos().isEmpty()){
                new DialogoCobro(this, mesa, camarero, this.ticket).setVisible(true);
            } else {
                MensajesAWT.mostrarMensaje(this, "No hay productos que cobrar.", "Aviso");
            }
        });

        panelControles.add(btnTicket);
        panelControles.add(btnModificar);
        panelControles.add(btnCobrar);
        panelControles.add(btnSalir);

        lblTotal = new Label("TOTAL: 0.00€  ", Label.RIGHT);
        lblTotal.setFont(new Font("Arial", Font.BOLD, 24));

        panelSur.add(panelControles, BorderLayout.WEST);
        panelSur.add(lblTotal, BorderLayout.EAST);
        add(panelSur, BorderLayout.SOUTH);

        actualizarResumen();
    }

    private void actualizarResumen() {
        listaTicketVisual.removeAll();
        lineasVisuales.clear();

        if (ticket.getProductos().isEmpty()) { mesa.cambiarEstado(modelo.EstadoMesa.LIBRE); }
        else { mesa.cambiarEstado(modelo.EstadoMesa.OCUPADA); }

        Map<String, List<Producto>> agrupados = new LinkedHashMap<>();
        for (Producto p : ticket.getProductos()) {
            String clave = p.getNombre() + "_" + p.getPrecio();
            agrupados.computeIfAbsent(clave, k -> new ArrayList<>()).add(p);
        }

        // Falsa tabla creada con espacios
        listaTicketVisual.add(String.format("%-5s | %-15s | %-8s | %-8s", "CANT", "PRODUCTO", "P.UNIT", "TOTAL"));
        listaTicketVisual.add("--------------------------------------------------");

        for (List<Producto> lista : agrupados.values()) {
            lineasVisuales.add(lista);
            int cant = lista.size();
            Producto ref = lista.get(0);

            String textoLinea = String.format("%-5d | %-15s | %-8s | %-8s",
                    cant,
                    ref.getNombre(),
                    String.format("%.2f€", ref.getPrecio()),
                    String.format("%.2f€", cant * ref.getPrecio()));

            listaTicketVisual.add(textoLinea);
        }
        lblTotal.setText("TOTAL: " + String.format("%.2f", ticket.getTotal()) + "€  ");
    }
}