package Controlador;

import Modelo.Conexion;
import Modelo.Crud_producto;
import Modelo.Producto;
import Vista.Frm_menu_principal;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class Crl_producto implements ActionListener, KeyListener {

    Crud_producto mod_crud_producto;
    Producto mod_producto;
    Frm_menu_principal menu;

    //METODO CONSTRUCTOR
    public Crl_producto(Crud_producto mod_crud_producto, Producto mod_producto, Frm_menu_principal menu) {

        this.mod_crud_producto = mod_crud_producto;
        this.mod_producto = mod_producto;

        this.menu = menu;

        DecimalFormat df = new DecimalFormat("#.00");
        String capitalDouble = df.format(capital());
        menu.txtCapital.setText(capitalDouble + "$");

//        menu.txtCapital.setText(capital().toString() + "$"); //ME DA EL CAPITAL
        menu.btnGuardar.addActionListener(this);
        menu.btnModificar.addActionListener(this);
        menu.btnEliminar.addActionListener(this);
        menu.btnBuscar.addActionListener(this);
        menu.btnLimpiar.addActionListener(this);
        menu.btnPDFStock.addActionListener(this);
        menu.btnRefrescarTablaStock.addActionListener(this);

        //METODO PARA QUE ESCUCHEN EL TECLADO LOS CAMPOS DE TEXTO
        this.menu.txtCantidad.addKeyListener(this);
        this.menu.txtBuscarTiempoReal.addKeyListener(this);

        //METODOS PARA CAMBIAR DE COLOR CASILLAS
    }

    //METODO DE EVENTO DE BOTONES
    public void actionPerformed(ActionEvent e) {

        //BOTON DE GUARDAR
        if (e.getSource() == menu.btnGuardar) {

            mod_producto.setCodigo(menu.txtClave.getText());
            mod_producto.setNombre(menu.txtNombre.getText());
            mod_producto.setCompra(Double.parseDouble(menu.txtCompra.getText()));
            mod_producto.setVenta(Double.parseDouble(menu.txtVenta.getText()));
            mod_producto.setFecha(menu.txtFecha.getText());
            mod_producto.setCantidad(Integer.parseInt(menu.txtCantidad.getText()));

            if (mod_crud_producto.guardar(mod_producto)) { // if true / false else

                JOptionPane.showMessageDialog(null, "Se guardo correctamente.");
                limpiar();
                refrescarTabla();
                cantidadProducto();
                //ColorCelda();
                RedondeCapital();

            } else {

                JOptionPane.showMessageDialog(null, "No se pudo guardar datos.");
                limpiar();

            }
        }
        //BOTON DE MODIFICAR
        if (e.getSource() == menu.btnModificar) {

            mod_producto.setId(Integer.parseInt(menu.txtId.getText()));
            mod_producto.setCodigo(menu.txtClave.getText());
            mod_producto.setNombre(menu.txtNombre.getText());
            mod_producto.setCompra(Double.parseDouble(menu.txtCompra.getText()));
            mod_producto.setVenta(Double.parseDouble(menu.txtVenta.getText()));
            mod_producto.setFecha(menu.txtFecha.getText());
            mod_producto.setCantidad(Integer.parseInt(menu.txtCantidad.getText()));

            if (mod_crud_producto.modificar(mod_producto)) {

                JOptionPane.showMessageDialog(null, "Se modifico con exito.");
                limpiar();
                refrescarTabla();
                cantidadProducto();
                RedondeCapital();


            } else {

                JOptionPane.showMessageDialog(null, "No se pudo modificar.");

            }

        }
        //BOTON ELIMINAR
        if (e.getSource() == menu.btnEliminar) {

            int respuesta = JOptionPane.showConfirmDialog(null, "¿Seguro que quieres eliminar este producto?", "Eliminar Producto", JOptionPane.WARNING_MESSAGE);

            if (respuesta != 0) {

                limpiar();

            } else {

                mod_producto.setId(Integer.parseInt(menu.txtId.getText()));

                if (mod_crud_producto.eliminar(mod_producto)) {

                    JOptionPane.showMessageDialog(null, "Se pudo eliminar los datos.");
                    limpiar();
                    refrescarTabla();
                    cantidadProducto();
                    RedondeCapital();

                } else {

                    JOptionPane.showMessageDialog(null, "No se pudo eliminar.");

                }

            }

        }

        //BOTON DE BUSCAR
        if (e.getSource() == menu.btnBuscar) {

            mod_producto.setCodigo(menu.txtBuscar.getText());

            if (mod_crud_producto.buscar(mod_producto)) {

                menu.txtId.setText(String.valueOf(mod_producto.getId()));
                menu.txtClave.setText(String.valueOf(mod_producto.getCodigo()));
                menu.txtNombre.setText(String.valueOf(mod_producto.getNombre()));
                menu.txtCompra.setText(String.valueOf(mod_producto.getCompra()));
                menu.txtVenta.setText(String.valueOf(mod_producto.getVenta()));
                menu.txtFecha.setText(String.valueOf(mod_producto.getFecha()));
                menu.txtCantidad.setText(String.valueOf(mod_producto.getCantidad()));

            } else {

                JOptionPane.showMessageDialog(null, "No se pudo encontrar el producto.");
                limpiar();

            }

        }

        //BOTON QUE LIMPIA LOS TXT DEL ALATA PRODUCTO
        if (e.getSource() == menu.btnLimpiar) {

            limpiar();

        }
        //BOTON PARA GENERAR PDF DEL LA TABLA STOCK
        if (e.getSource() == menu.btnPDFStock) {

            GenerarReporteStock();

        }
        //REFRESCAR LA TABLASTOCK
        if (e.getSource() == menu.btnRefrescarTablaStock) {

            refrescarTabla();

        }

    }

    //--------------FONA DE METODOS BASICOS------------------
    //METODO QUE LIMPIA LOS TXT DE ALTA PRODUCTO
    public void limpiar() {

        menu.txtBuscar.setText("");
        menu.txtCantidad.setText("");
        menu.txtClave.setText("");
        menu.txtCompra.setText("");
        menu.txtFecha.setText("");
        menu.txtId.setText("");
        menu.txtNombre.setText("");
        menu.txtVenta.setText("");
        menu.txtId.setText("");

    }

    //METODO PARA REFRESCAR LA TABLA DE ALTA PRODUCTO
    public void refrescarTabla() {

        try {

            DefaultTableModel modelo = new DefaultTableModel();

            menu.tlbStock.setModel(modelo);
            menu.tlbStockVentas.setModel(modelo);

            PreparedStatement ps = null;

            ResultSet rs = null;

            Conexion conn = new Conexion();

            Connection con = conn.getConexion();

            String sql = "SELECT Codigo,Nombre,Compra,Venta,Fecha,Cantidad FROM tabla_producto ORDER BY Codigo";

            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();

            ResultSetMetaData rsMd = rs.getMetaData(); //le damos los datos de la consulta a la tabla.

            int cantidadColumnas = rsMd.getColumnCount(); // nos dira la cantidad de columnas.

            modelo.addColumn("Código"); //cada vuelta que reinicie la tabla perdera los nombres de las columnas
            modelo.addColumn("Nombre");//por eso los tenemos que reasignar.
            modelo.addColumn("Costo");
            modelo.addColumn("Venta");
            modelo.addColumn("Fecha");
            modelo.addColumn("Cantidad");

            int[] anchoColumnas = {50, 200, 50, 50, 50, 50}; //poniendole un ancho a las tablas.

            for (int i = 0; i < 6; i++) {

                menu.tlbStock.getColumnModel().getColumn(i).setPreferredWidth(anchoColumnas[i]);

            }

            while (rs.next()) { //va a ir recorriendo los datos y los ira trayendo fila por fila el ciclo while.

                Object[] filas = new Object[cantidadColumnas];

                for (int i = 0; i < cantidadColumnas; i++) {

                    filas[i] = rs.getObject(i + 1); //trae fila por fila

                }

                modelo.addRow(filas); //guarda en la tabla el vector que guardo todos los datos de la base de datos

            }

        } catch (SQLException e) {

            System.out.println(e);

        }

    }

    //METODO QUE VERA EL CAPITAL
    public Double capital() {

        int contar = menu.tlbStock.getRowCount();

        double capital = 0.0;
        double valorDeCompra = 0.0;
        int cantidad = 0;
        String nombre;
        System.out.println("contar: " + contar);

        for (int i = 0; i < contar; i++) {

            nombre = menu.tlbStock.getValueAt(i, 1).toString();
            valorDeCompra = Double.parseDouble(menu.tlbStock.getValueAt(i, 2).toString());
            cantidad = Integer.parseInt(menu.tlbStock.getValueAt(i, 5).toString());
            capital = capital + valorDeCompra * cantidad;

            System.out.println("-----sumando capital-----");
            System.out.println("nombre:" + nombre);
            System.out.println("valorDeCompra: " + valorDeCompra);
            System.out.println("cantidad: " + cantidad);
            System.out.println("capital:" + capital);

        }
        System.out.println("........................");

        return capital;

    }

    //METODO PARA CONTAR LA CANTIDAD DE PRODUCTOS
    public void cantidadProducto() {

        int contar = menu.tlbStock.getRowCount();
        String contarString = String.valueOf(contar);
        menu.txtCantidadProductos.setText(contarString);

    }

    //METODO INICIAR
    public void iniciar() {

        menu.setLocationRelativeTo(null);
        menu.setTitle("Sistema Tiramisu Store V4.4");
//        JOptionPane.showMessageDialog(null, "Actualizaciones: " + "\n"
//                + "-Solucion de bugs." + "\n"
//                + "-Implementacion de datos de Venta Basicos." + "\n"
//                + "-Agregado un nuevo Reloj." + "\n"
//                + "-Busqueda Tiempo Real." + "\n"
//                + "-Agregado un evento de MouseClick en Tabla." + "\n"
//                + "-Aumento en el tamaño de las fuentes." + "\n"
//                + "-Boton elimina venta cancelada." + "\n"
//                + "-Mejoras a calculadora.");
        menu.setResizable(false);

    }

    //REDONDEO DE CAPITAL
    public void RedondeCapital() {
        
        DecimalFormat df = new DecimalFormat("#.00");
        String capitalDouble = df.format(capital());
        menu.txtCapital.setText(capitalDouble + "$");
        
    }

    //METODO PARA GENERAR EL REPORTE PDF DEL STOCK
    public void GenerarReporteStock() {

        try {
            Conexion con = new Conexion();
            Connection conn = con.getConexion();
            System.out.println("entro");
            JasperReport reporte = null;
            //src\\\\Report\\\\ReporteStock.jasper
            //C:\\Users\\FLORPC\\Desktop\\FLOR\\Tienda_tiramisustore\\src\\Report\\ReporteStock.jasper
            String path = "src\\\\Report\\\\ReporteStock.jasper";
            reporte = (JasperReport) JRLoader.loadObjectFromFile(path);
            JasperPrint jprint = JasperFillManager.fillReport(reporte, null, conn);

            JasperViewer view = new JasperViewer(jprint, false);

            view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            view.setVisible(true);

            JOptionPane.showMessageDialog(null, path);

        } catch (JRException ex) {
            Logger.getLogger(Crl_provedor.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
        }

    }

    //METODO PARA BUSCADOR A TIEMPO REAL EN LA TABLA STOCK
    public void FiltrarDatos(String valor) {

        String[] titulos = {"Codigo", "Nombre", "Costo", "Venta", "Fecha", "Cantidad"};
        String[] registros = new String[6];

        PreparedStatement ps = null;
        Conexion conn = new Conexion();
        Connection con = conn.getConexion();

        DefaultTableModel modelo = new DefaultTableModel(null, titulos);

        String sql = "select * from tabla_producto where Nombre like '%" + valor + "%'    ";

        try {

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                registros[0] = rs.getString("Codigo");
                registros[1] = rs.getString("Nombre");
                registros[2] = rs.getString("Compra");
                registros[3] = rs.getString("Venta");
                registros[4] = rs.getString("Fecha");
                registros[5] = rs.getString("Cantidad");

                modelo.addRow(registros);

            }

            menu.tlbStock.setModel(modelo);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, e);

        }

    }

    //METODO PARA IDENTIFICAR LA CANTIDAD DE PRODUCTOS A TERMINARSE
    public void ColorCelda() {

        int cantidad;
        int contar = menu.tlbStock.getRowCount();

        for (int i = 0; i < contar; i++) {

            cantidad = Integer.parseInt(menu.tlbStock.getValueAt(i, 5).toString());

            if (cantidad <= 10) {

                menu.tlbStock.setBackground(Color.ORANGE);
                menu.tlbStock.setForeground(Color.BLACK);
                menu.tlbStockVentas.setBackground(Color.ORANGE);
                menu.tlbStockVentas.setForeground(Color.BLACK);

                if (cantidad <= 3) {

                    menu.tlbStock.setBackground(Color.RED);
                    menu.tlbStock.setForeground(Color.BLACK);
                    menu.tlbStockVentas.setBackground(Color.RED);
                    menu.tlbStockVentas.setForeground(Color.BLACK);

                }

            } else {

                menu.tlbStock.setBackground(Color.white);
                menu.tlbStock.setForeground(Color.BLACK);
                menu.tlbStockVentas.setBackground(Color.white);
                menu.tlbStockVentas.setForeground(Color.BLACK);

            }

        }

    }

    //-------------FIN DE LOS METODOS BASICOS----------------
    //--------------INICIO DE LAS EVENTOS--------------------
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        // ENTER CODIGO SECRETO
        if (e.getKeyCode() == com.sun.glass.events.KeyEvent.VK_ENTER) {

            mod_producto.setCodigo(menu.txtClave.getText());
            mod_producto.setNombre(menu.txtNombre.getText());
            mod_producto.setCompra(Double.parseDouble(menu.txtCompra.getText()));
            mod_producto.setVenta(Double.parseDouble(menu.txtVenta.getText()));
            mod_producto.setFecha(menu.txtFecha.getText());
            mod_producto.setCantidad(Integer.parseInt(menu.txtCantidad.getText()));

            if (mod_crud_producto.guardar(mod_producto)) { // if true / false else

                JOptionPane.showMessageDialog(null, "Se guardo correctamente.");
                limpiar();
                refrescarTabla();
                menu.txtCapital.setText(capital().toString() + "$");

            } else {

                JOptionPane.showMessageDialog(null, "No se pudo guardar datos.");
                limpiar();

            }

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        FiltrarDatos(menu.txtBuscarTiempoReal.getText());

    }

    //----------FIN DE LOS EVENTOS DE BOTONES----------------
}
