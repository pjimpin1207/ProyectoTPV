package ui;

import java.awt.*;
import java.awt.event.*;
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

public class VentanaAdministrador extends Frame {

    private ProductoDAO productoDAO = new ProductoDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public VentanaAdministrador() {
        setTitle("Panel de Control - Administrador");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { System.exit(0); }
        });

        Label lblTitulo = new Label("GESTIÓN DEL RESTAURANTE", Label.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        add(lblTitulo, BorderLayout.NORTH);

        Panel panelBotones = new Panel(new GridLayout(2, 3, 20, 20));

        Button btnAñadirProd = crearBoton("Añadir Producto", Color.decode("#4CAF50"));
        Button btnGestionarProd = crearBoton("Modificar Producto", Color.decode("#FF9800"));
        Button btnCierreCaja = crearBoton("CIERRE DE CAJA", Color.decode("#607D8B"));
        Button btnAñadirCam = crearBoton("Añadir Camarero", Color.decode("#2196F3"));
        Button btnEliminarCam = crearBoton("Eliminar Camarero", Color.decode("#9C27B0"));

        btnAñadirProd.addActionListener(e -> {
            Dialog d = new Dialog(this, "Nuevo Producto", true);
            d.setLayout(new GridLayout(4, 2));
            d.add(new Label("Nombre:")); TextField txtNombre = new TextField(); d.add(txtNombre);
            d.add(new Label("Categoría:")); Choice cbCat = new Choice();
            cbCat.add("BEBIDA"); cbCat.add("COMIDA"); cbCat.add("POSTRE"); d.add(cbCat);
            d.add(new Label("Precio (€):")); TextField txtPrecio = new TextField(); d.add(txtPrecio);
            Button btnOk = new Button("Guardar");
            btnOk.addActionListener(ev -> {
                try {
                    float precio = Float.parseFloat(txtPrecio.getText().replace(",", "."));
                    productoDAO.insertarProducto(new Producto(0, txtNombre.getText().toUpperCase(), Categoria.valueOf(cbCat.getSelectedItem()), precio));
                    MensajesAWT.mostrarMensaje(this, "Producto añadido.", "Éxito");
                    d.dispose();
                } catch(Exception ex) { MensajesAWT.mostrarMensaje(this, "Datos inválidos", "Error"); }
            });
            Button btnCancel = new Button("Cancelar");
            btnCancel.addActionListener(ev -> d.dispose());
            d.add(btnOk); d.add(btnCancel);
            d.setSize(300, 200); d.setLocationRelativeTo(this);
            d.setVisible(true);
        });

        btnGestionarProd.addActionListener(e -> {
            List<Producto> lista = productoDAO.obtenerTodos();
            if (lista.isEmpty()) return;
            String[] nombres = lista.stream().map(Producto::getNombre).toArray(String[]::new);
            String seleccionado = MensajesAWT.pedirOpcion(this, "Selecciona el producto:", "Gestión", nombres);

            if (seleccionado != null) {
                Producto p = lista.stream().filter(prod -> prod.getNombre().equals(seleccionado)).findFirst().orElse(null);
                String[] opciones = {"Modificar Nombre", "Modificar Precio", "Eliminar Producto"};
                String eleccion = MensajesAWT.pedirOpcion(this, "Acción para: " + seleccionado, "Opciones", opciones);

                if ("Modificar Nombre".equals(eleccion)) {
                    String nName = MensajesAWT.pedirInput(this, "Nuevo nombre:", "Renombrar", p.getNombre());
                    if (nName != null && !nName.trim().isEmpty()) {
                        productoDAO.modificarNombreProducto(seleccionado, nName.toUpperCase());
                        MensajesAWT.mostrarMensaje(this, "Nombre actualizado.", "Info");
                    }
                } else if ("Modificar Precio".equals(eleccion)) {
                    String nPrecio = MensajesAWT.pedirInput(this, "Nuevo precio (€):", "Precio", String.valueOf(p.getPrecio()));
                    if (nPrecio != null) {
                        try {
                            productoDAO.modificarProducto(seleccionado, Float.parseFloat(nPrecio.replace(",", ".")));
                            MensajesAWT.mostrarMensaje(this, "Precio actualizado.", "Info");
                        } catch(Exception ex) { MensajesAWT.mostrarMensaje(this, "Formato incorrecto", "Error"); }
                    }
                } else if ("Eliminar Producto".equals(eleccion)) {
                    if (MensajesAWT.pedirConfirmacion(this, "¿Seguro que quieres eliminarlo?", "Eliminar")) {
                        productoDAO.eliminarProducto(seleccionado);
                        MensajesAWT.mostrarMensaje(this, "Producto eliminado.", "Info");
                    }
                }
            }
        });

        btnAñadirCam.addActionListener(e -> {
            String nombre = MensajesAWT.pedirInput(this, "Nombre del camarero:", "Añadir", "");
            if (nombre != null && !nombre.trim().isEmpty()) {
                usuarioDAO.insertarCamarero(nombre.toUpperCase());
                MensajesAWT.mostrarMensaje(this, "Camarero añadido.", "Info");
            }
        });

        btnEliminarCam.addActionListener(e -> {
            List<String> cams = usuarioDAO.obtenerNombresCamareros();
            if(!cams.isEmpty()){
                String sel = MensajesAWT.pedirOpcion(this, "Selecciona camarero:", "Despedir", cams.toArray(new String[0]));
                if (sel != null && MensajesAWT.pedirConfirmacion(this, "¿Eliminar a " + sel + "?", "Confirmar")) {
                    usuarioDAO.eliminarCamarero(sel);
                    MensajesAWT.mostrarMensaje(this, "Camarero eliminado.", "Info");
                }
            }
        });

        btnCierreCaja.addActionListener(e -> realizarCierreDeCaja());

        panelBotones.add(btnAñadirProd); panelBotones.add(btnGestionarProd); panelBotones.add(btnCierreCaja);
        panelBotones.add(btnAñadirCam); panelBotones.add(btnEliminarCam);
        add(panelBotones, BorderLayout.CENTER);

        Button btnSalir = new Button("VOLVER A SALA");
        btnSalir.addActionListener(e -> { new VentanaMesas(new Date()).setVisible(true); this.dispose(); });
        Panel pSur = new Panel(); pSur.add(btnSalir);
        add(pSur, BorderLayout.SOUTH);
    }

    private Button crearBoton(String texto, Color color) {
        Button btn = new Button(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        return btn;
    }

    private void realizarCierreDeCaja() {
        TicketObjectDBDAO tDao = new TicketObjectDBDAO();
        List<Ticket> ticketsHoy = tDao.obtenerTicketsHoy();

        if (ticketsHoy.isEmpty()) {
            MensajesAWT.mostrarMensaje(this, "No hay tickets hoy.", "Cierre");
            return;
        }

        float totalCaja = 0;
        for (Ticket t : ticketsHoy) {
            totalCaja += t.getTotal();
        }

        String ruta = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "Historial_Cierres_Caja.txt";

        try (FileWriter fw = new FileWriter(ruta, true)) {
            String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
            fw.write("=======================================\nCIERRE DE CAJA - " + fecha + "\n");
            fw.write("TOTAL FACTURADO: " + String.format("%.2f", totalCaja) + "€\nTickets: " + ticketsHoy.size() + "\n=======================================\n\n");
        } catch (Exception ex) {
            MensajesAWT.mostrarMensaje(this, "Error al guardar el archivo", "Error");
        }

        MensajesAWT.mostrarMensaje(this, "TOTAL FACTURADO HOY: " + String.format("%.2f", totalCaja) + "€\nGuardado en Escritorio.", "Cierre Diario");
    }
}