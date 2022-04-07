package Controlador;

import Modelo.Conexion;
import Modelo.Crud_producto;
import Modelo.Crud_ventas;
import Modelo.Producto;
import Modelo.Ventas;
import Vista.Frm_menu_principal;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import static java.lang.Math.round;
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

public class Crl_ventas implements ActionListener, KeyListener {

    Crud_ventas mod_crud_ventas;
    Ventas ventas;
    Frm_menu_principal menu;
    Producto mod_producto;
    Crud_producto mod_crud_producto;

    //METODO CONSTRUCTOR
    public Crl_ventas(Crud_ventas mod_crud_ventas, Ventas ventas, Frm_menu_principal menu, Producto mod_producto, Crud_producto mod_crud_producto) {

        this.mod_crud_ventas = mod_crud_ventas;
        this.ventas = ventas;
        this.menu = menu;
        this.mod_producto = mod_producto;
        this.mod_crud_producto = mod_crud_producto;

        menu.btnBuscarVentas.addActionListener(this);
        menu.btnAgregarCarrito.addActionListener(this);
        menu.btnEliminarCarrito.addActionListener(this);
        menu.btnVender.addActionListener(this);
        menu.btnRefrescarTablaVentas.addActionListener(this);
        menu.btnLimpiarCarritoVentas.addActionListener(this);
        menu.btnCrearPDFReporteVentas.addActionListener(this);
        menu.btnEliminarProductoReporteVentas.addActionListener(this);

        //METODO PARA QUE ESCUCHEN EL TECLADO LOS CAMPOS DE TEXTO
        menu.txtBuscadorTimepoRealVentas.addKeyListener(this);
        menu.txtBuscador_Prueba.addKeyListener(this);

        //METODOS QUE TIENEN QUE INICIAR CON LA CLASE
        CantidadDeVentas();
        CantidadDeVentasMes();
        refrescatTablaReporteVenta();

    }

    //METODO QUE ESCUCHA LOS BOTONES
    public void actionPerformed(ActionEvent e) {

        //BOTON DE BUSCAR PRODUCTO
        if (e.getSource() == menu.btnBuscarVentas) {

            BuscarProductoParaVenta();

        }

        //BOTON DE AGREGAR AL CARRITO
        if (e.getSource() == menu.btnAgregarCarrito) {

            AgregarProductoCarrito();

        }

        //BOTON DE ELIMINAR DEL CARRITO
        if (e.getSource() == menu.btnEliminarCarrito) {

            String total;
            EliminarProductosCarritos();
            total = String.valueOf(ActualizaTotal());
            menu.txtTotalVentas.setText(total + "$");

        }

        //BOTON DE GENERAR VENTA 
        if (e.getSource() == menu.btnVender) {

            int respuesta = JOptionPane.showConfirmDialog(null, "Seguro que quiere realizar la compra?", "Seguro?", JOptionPane.WARNING_MESSAGE);

            if (respuesta != 0) {

            } else {

                refrescarTabla();
                Vender();

            }

        }

        //BOTON PARA LIMPIAR LA LISTA DE STOCK DE LA PARTE DE VENTAS 
        if (e.getSource() == menu.btnRefrescarTablaVentas) {

            refrescarTabla();

        }

        //BOTON PARA LIMPIAR EL CARRITO Y EL CAMPO DE TEXTO DE TOTAL Y GANANCIA
        if (e.getSource() == menu.btnLimpiarCarritoVentas) {

            limpiaCarrito();

        }

        //BOTON QUE DA EL PDF DE REPORTE DE VENTAS
        if (e.getSource() == menu.btnCrearPDFReporteVentas) {

            GenerarReporteStock();

        }

        //BOTON PARA ELIMINAR PRODUCTO DEL REPORTE DE VENTAS
        if (e.getSource() == menu.btnEliminarProductoReporteVentas) {

            RetornoDeStock();

        }

    }

    //---------------METODOS BASICOS------------------
    //METODO PARA BUSCAR UN PRODUCTO EN UNA TABLA
    public void BuscarProductoParaVenta() { //cambiar nombre a buscar para carrito 

        String campo = menu.txtBuscarVenta.getText();

        String where = "";

        if (!"".equals(campo)) {

            where = " WHERE Nombre = '" + campo + "'";

        }

        try {

            DefaultTableModel modelo = new DefaultTableModel();

            //frm_producto.jtVentas.setModel(modelo);
            menu.tlbStockVentas.setModel(modelo);

            PreparedStatement ps = null;

            ResultSet rs = null;

            Conexion conn = new Conexion();

            Connection con = conn.getConexion();

            String sql = "SELECT Codigo,Nombre,Compra,Venta,Fecha,Cantidad FROM tabla_producto" + where;

            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();

            ResultSetMetaData rsMd = rs.getMetaData(); //le damos los datos de la consulta a la tabla.

            int cantidadColumnas = 6;

            modelo.addColumn("Codigo"); //cada vuelta que reinicie la tabla perdera los nombres de las columnas
            modelo.addColumn("Nombre");//por eso los tenemos que reasignar.
            modelo.addColumn("Compra");
            modelo.addColumn("Venta");
            modelo.addColumn("Fecha");
            modelo.addColumn("Cantidad");

            Object[] filas = new Object[6];

            while (rs.next()) { //va a ir recorriendo los datos y los ira trayendo fila por fila el ciclo while.

                for (int i = 0; i < cantidadColumnas; i++) {

                    filas[i] = rs.getObject(i + 1); //trae columna por columna

                }

//                modelo.addRow(filas); //guarda en la tabla el vector que guardo todos los datos de la base de datos
            }

            modelo.addRow(filas);

        } catch (SQLException e) {

            System.out.println(e);

        }

    }

    //METODO PARA BUSCAR A TIEMPO REAL LOS PRODUCTOS
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

            menu.tlbStockVentas.setModel(modelo);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, e);

        }

    }

    //METODO PARA AGREGAR UN PRODUCTO AL CARRITO
    public void AgregarProductoCarrito() {

        DefaultTableModel m;

        int fsel = menu.tlbStockVentas.getSelectedRow();

        String codigo, nombre, compra, venta, fecha, cantidad, cant, importe;

        double x = 0.0;
        double y = 0.0;
        double j = 0.0;
        double t = 0.0;
        double total; //variable que trae el valor que retorna la funcion Total.
        String totalstri; //variable que usamos para pasar a cadena el total y mostrarlo en el txt.
        double importeGanancia; //variable que usamos para tener el importe de ganancia.
        String importeGananciaString;

        int cantidadRestante = 0;

        if (fsel == -1) {

            JOptionPane.showMessageDialog(null, "Selecciona una fila.");

        } else {

            m = (DefaultTableModel) menu.tlbStockVentas.getModel();

            codigo = menu.tlbStockVentas.getValueAt(fsel, 0).toString();
            nombre = menu.tlbStockVentas.getValueAt(fsel, 1).toString();
            compra = menu.tlbStockVentas.getValueAt(fsel, 2).toString();
            venta = menu.tlbStockVentas.getValueAt(fsel, 3).toString();
            fecha = menu.tlbStockVentas.getValueAt(fsel, 4).toString();
            cantidad = menu.tlbStockVentas.getValueAt(fsel, 5).toString();

            cant = menu.txtCantidadVentas.getText();

            t = Double.parseDouble(compra); //COMPRA
            y = Double.parseDouble(venta); //VENTA
            j = Integer.parseInt(cant); //CANTIDAD A LLEVAR

            x = j * y;

            importe = String.valueOf(x);

            int cantidad2 = Integer.parseInt(cantidad);
            int cant2 = Integer.parseInt(cant);

            if (cantidad2 >= cant2) {

                cantidadRestante = cantidad2 - cant2;
                System.out.println("-----------------------");
                System.out.println("Producto:" + nombre);
                System.out.println("Venta:" + venta);
                System.out.println("Cantidad a llevar: " + cant);
                System.out.println("Cantidad restante: " + cantidadRestante);
                System.out.println("Importe:" + x);

                m = (DefaultTableModel) menu.tlbCarrito.getModel();

                String filaelementos[] = {codigo, nombre, cant, compra, venta, importe};
                m.addRow(filaelementos);

                //sacar el total a tiempo real
                total = ActualizaTotal();
                totalstri = String.valueOf(total);
                menu.txtTotalVentas.setText(totalstri + "$");
                menu.txtCantidadVentas.setText("1");

            } else {

                JOptionPane.showMessageDialog(null, "Falta Stock." + nombre);

            }

            //SEGUIR MODIFICANDO
        }

    }

    //METODO DE ACTUALIZAR TOTAL
    public double ActualizaTotal() {

        int contar = menu.tlbCarrito.getRowCount();

        double suma = 0.0;

        for (int i = 0; i < contar; i++) {

            suma = suma + Double.parseDouble(menu.tlbCarrito.getValueAt(i, 5).toString());

        }

        return suma;

    }

    //METODO PARA SACAR LA CANTIDAD DE VENTAS DESDE QUE SE INSTALO EL SISTEMA
    public void CantidadDeVentas() {

        int contar = menu.tlbReporteVentas.getRowCount();
        String contarString = String.valueOf(contar);
        menu.txtCantidadDeVentas.setText(contarString);

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

    } // {SACAR}

    //METODO PARA SACAR LA CANTIDAD DE VENTAS DEL MES.
    public void CantidadDeVentasMes() {

        DefaultTableModel m = new DefaultTableModel();

        int contarReporteVentas = menu.tlbReporteVentas.getRowCount();

        String codigoReporte, nombreReporte, importeReporte, gananciaReporte, cantidadReporte, fechaReporte, idReporte;
        String FechaActual;

        m.addColumn("Codigo");
        m.addColumn("Nombre");
        m.addColumn("Importe");
        m.addColumn("Ganancia");
        m.addColumn("Canatidad");
        m.addColumn("Fecha");

        int contador = 0;

        for (int i = 0; i < contarReporteVentas; i++) {

            idReporte = menu.tlbReporteVentas.getValueAt(i, 0).toString();
            codigoReporte = menu.tlbReporteVentas.getValueAt(i, 1).toString();
            nombreReporte = menu.tlbReporteVentas.getValueAt(i, 2).toString();
            importeReporte = menu.tlbReporteVentas.getValueAt(i, 3).toString();
            gananciaReporte = menu.tlbReporteVentas.getValueAt(i, 4).toString();
            cantidadReporte = menu.tlbReporteVentas.getValueAt(i, 5).toString();
            fechaReporte = menu.tlbReporteVentas.getValueAt(i, 6).toString();
            FechaActual = menu.lbFecha.getText();

            System.out.println("--------------------------");
            System.out.println("Codigo: " + codigoReporte);
            System.out.println("Nombre: " + nombreReporte);
            System.out.println("Importe: " + importeReporte);
            System.out.println("Ganancia: " + gananciaReporte);
            System.out.println("cantidad: " + cantidadReporte);
            System.out.println("Fecha de compra: " + fechaReporte);
            System.out.println("Fecha Actual: " + FechaActual);
            System.out.println("---------------------------");

            String SubCadenaFechaCompra = fechaReporte.substring(3, 5);
            System.out.println("subCadena Fecha de compra: " + SubCadenaFechaCompra);

            String SubCadenaFechaActual = FechaActual.substring(3, 5);
            System.out.println("subCadena Fecha Actual: " + SubCadenaFechaActual);

            if (SubCadenaFechaCompra.equals(SubCadenaFechaActual)) {

                System.out.println("Comparo: " + SubCadenaFechaCompra + " con " + SubCadenaFechaActual);

                contador++;

            }

            String contadorString = String.valueOf(contador);

            menu.txtCantidadDeVentasMes.setText(contadorString);
            GananciaMensual();

        }

    }

    //METODO PARA SABER LA CANTIDAD DE GANANCIA EN EL MES
    public void GananciaMensual() {

        DefaultTableModel m = new DefaultTableModel();

        int contarReporteVentas = menu.tlbReporteVentas.getRowCount();

        String codigoReporte, nombreReporte, importeReporte, gananciaReporte, cantidadReporte, fechaReporte, idReporte;
        String FechaActual;

        m.addColumn("Codigo");
        m.addColumn("Nombre");
        m.addColumn("Importe");
        m.addColumn("Ganancia");
        m.addColumn("Canatidad");
        m.addColumn("Fecha");

        double gananciaTotal = 0;
        double ganancia = 0;

        for (int i = 0; i < contarReporteVentas; i++) {

            idReporte = menu.tlbReporteVentas.getValueAt(i, 0).toString();
            codigoReporte = menu.tlbReporteVentas.getValueAt(i, 1).toString();
            nombreReporte = menu.tlbReporteVentas.getValueAt(i, 2).toString();
            importeReporte = menu.tlbReporteVentas.getValueAt(i, 3).toString();
            gananciaReporte = menu.tlbReporteVentas.getValueAt(i, 4).toString();
            cantidadReporte = menu.tlbReporteVentas.getValueAt(i, 5).toString();
            fechaReporte = menu.tlbReporteVentas.getValueAt(i, 6).toString();
            FechaActual = menu.lbFecha.getText();

            System.out.println("--------------------------");
            System.out.println("Codigo: " + codigoReporte);
            System.out.println("Nombre: " + nombreReporte);
            System.out.println("Importe: " + importeReporte);
            System.out.println("Ganancia: " + gananciaReporte);
            System.out.println("cantidad: " + cantidadReporte);
            System.out.println("Fecha de compra: " + fechaReporte);
            System.out.println("Fecha Actual: " + FechaActual);
            System.out.println("---------------------------");

            String SubCadenaFechaCompra = fechaReporte.substring(3, 5);
            System.out.println("subCadena Fecha de compra: " + SubCadenaFechaCompra);

            String SubCadenaFechaActual = FechaActual.substring(3, 5);
            System.out.println("subCadena Fecha Actual: " + SubCadenaFechaActual);

            ganancia = Double.parseDouble(gananciaReporte);

            if (SubCadenaFechaCompra.equals(SubCadenaFechaActual)) {

                gananciaTotal = gananciaTotal + ganancia;

            }

            DecimalFormat df = new DecimalFormat("#.00");
            String contadorGanancia = df.format(gananciaTotal);
            menu.txtGananciaMes.setText(contadorGanancia + "$");

        }

    }

    //METODO PARA LIMPIAR CARRITO
    public void limpiaCarrito() {

        DefaultTableModel m = new DefaultTableModel();

        int filas = m.getRowCount();

        m.addColumn("Codigo");
        m.addColumn("Nombre");
        m.addColumn("Cantidad");
        m.addColumn("Venta");
        m.addColumn("Compra");
        m.addColumn("Importe");

        for (int i = 0; i < filas; i++) {

            m.removeRow(0);

        }

        menu.tlbCarrito.setModel(m);

        menu.txtTotalVentas.setText("");

    }

    //METODO PARA ELIMINAR UN PRODUCTO DEL CARRITO
    public void EliminarProductosCarritos() {

        DefaultTableModel m = new DefaultTableModel();
        int filaSeleccionada;

        try {

            filaSeleccionada = menu.tlbCarrito.getSelectedRow();

            if (filaSeleccionada == -1) {

                JOptionPane.showMessageDialog(null, "Seleccione la fila a elimnar del carrito.");

                System.out.println("....................................");
                System.out.println("No seleccionaste fila para eliminar.");

            } else {

                m = (DefaultTableModel) menu.tlbCarrito.getModel();
                m.removeRow(filaSeleccionada);

            }

        } catch (Exception e) {

            System.out.println(e);

        }

    }

    //METODO PARA GENERAR VENTA
    public void Vender() {

        DefaultTableModel m = new DefaultTableModel();

        int contarCarrito = menu.tlbCarrito.getRowCount();
        int gananciaInt;

        String codigoCarrito, nombreCarrito, importeCarrito, ventaCarrito, compraCarrito, cantidadCarrito;

        m.addColumn("Codigo");
        m.addColumn("Nombre");
        m.addColumn("Cantidad");
        m.addColumn("Venta");
        m.addColumn("Compra");
        m.addColumn("Importe");

        for (int i = 0; i < contarCarrito; i++) {

            //ABSORVE DATOS DE LA TABLA CARRITO
            codigoCarrito = menu.tlbCarrito.getValueAt(i, 0).toString();
            nombreCarrito = menu.tlbCarrito.getValueAt(i, 1).toString();
            cantidadCarrito = menu.tlbCarrito.getValueAt(i, 2).toString();
            ventaCarrito = menu.tlbCarrito.getValueAt(i, 3).toString();
            compraCarrito = menu.tlbCarrito.getValueAt(i, 4).toString();
            importeCarrito = menu.tlbCarrito.getValueAt(i, 5).toString();

            System.out.println("------PRODUCTO CARRITO------");
            System.out.println("codigo: " + codigoCarrito);
            System.out.println("nombre:" + nombreCarrito);
            System.out.println("cantidad:" + cantidadCarrito);
            System.out.println("venta:" + ventaCarrito);
            System.out.println("compre:" + compraCarrito);
            System.out.println("importe:" + importeCarrito);

            double importeDouble = Double.parseDouble(importeCarrito);
            double ganancia = (Double.parseDouble(compraCarrito) - Double.parseDouble(ventaCarrito)) * Integer.parseInt(cantidadCarrito);

            System.out.println("Ganancia: " + ganancia);

            String fecha = menu.txtFecha.getText();

            DescuentaStock(codigoCarrito, nombreCarrito, importeDouble, ganancia, cantidadCarrito, fecha);
            CantidadDeVentas();
            CantidadDeVentasMes();

        }

    }

    //DESCUENTA DEL STOCK LUEGO DE CONFIRMAR UNA VENTA
    public void DescuentaStock(String codigoCarrito, String nombreCarrito, double importeDouble, double ganancia, String cantidadCarrito, String fecha) {

        DefaultTableModel n = new DefaultTableModel();
        int contadorStock = menu.tlbStockVentas.getRowCount();

        String nombreStock, codigoStock, compraStock, ventaStock, fechaStock, cantidadStock;

        n.addColumn("Codigo");
        n.addColumn("Nombre");
        n.addColumn("Compra");
        n.addColumn("Venta");
        n.addColumn("Fecha");
        n.addColumn("Cantidad");

        for (int i = 0; i < contadorStock; i++) {

            int cantidadCarritoInt = Integer.parseInt(cantidadCarrito);

            //ABSORVE DATOS DE LA TABLA STOCKVENTAS
            codigoStock = menu.tlbStockVentas.getValueAt(i, 0).toString();
            nombreStock = menu.tlbStockVentas.getValueAt(i, 1).toString();
            compraStock = menu.tlbStockVentas.getValueAt(i, 2).toString();
            ventaStock = menu.tlbStockVentas.getValueAt(i, 3).toString();
            fechaStock = menu.tlbStockVentas.getValueAt(i, 4).toString();
            cantidadStock = menu.tlbStockVentas.getValueAt(i, 5).toString();

            int cantidadStockInt = Integer.parseInt(cantidadStock);

            System.out.println("-----PRODUCTO EN STOCK-------");
            System.out.println("codigo: " + nombreStock);
            System.out.println("nombre: " + codigoStock);
            System.out.println("compra: " + compraStock);
            System.out.println("venta: " + ventaStock);
            System.out.println("fecha: " + fechaStock);
            System.out.println("cantidad: " + cantidadStock);
            System.out.println("-----PRODUCTO DE CARRITO-----");
            System.out.println("codigo: " + codigoCarrito);
            System.out.println("cantidad: " + cantidadCarrito);

            if (codigoStock.equals(codigoCarrito)) {

                System.out.println("------MODIFICAR STOCK------");
                System.out.println("cantidad a llevar: " + cantidadCarritoInt);
                System.out.println("Stock actual: " + cantidadStockInt);

                int actualizar = cantidadStockInt - cantidadCarritoInt;

                System.out.println("stock actualizado : " + actualizar);

                mod_producto.setCantidad(actualizar);
                mod_producto.setCodigo(codigoStock);

                if (mod_crud_producto.DescuentoStock(mod_producto)) {

                    System.out.println("modificado Stock.");
                    System.out.println(".......................");
                    refrescarTabla();
                    String fechaVenta = menu.lbFecha.getText();
                    AgregarReporteVentas(codigoStock, nombreStock, importeDouble, ganancia, cantidadCarritoInt, fechaVenta);

                } else {

                    System.out.println("no modificado.");
                    System.out.println(".......................");

                }

            }

        }

    }

    //AGREGAR TABLA DEL REPORTE DE VENTAS
    public void AgregarReporteVentas(String codigo, String nombre, double importe, double ganancia, int cantidad, String fecha) {

        double gananciaTotal = 0.0;
        ventas.setCodigo(codigo);
        ventas.setNombre(nombre);
        ventas.setImporte(importe);
        ventas.setGanancia(ganancia);
        ventas.setCantidad(cantidad);
        ventas.setFecha(fecha);

        if (mod_crud_ventas.guardar(ventas)) {

            System.out.println("--PRODUCTO AGREGADO AL REPORTE DE VENTAS--");
            refrescatTablaReporteVenta();

        } else {

            JOptionPane.showMessageDialog(null, "No se pudo guardar datos.");

        }

    }

    //ELIMINAR DE REPORTE DE VENTAS
    public void EliminarReporteVentas(int id) {

        ventas.setId(id);

        if (mod_crud_ventas.eliminar(ventas)) {

            JOptionPane.showMessageDialog(null, "Se pudo eliminar los datos.");
            refrescatTablaReporteVenta();
            refrescarTabla();
            CantidadDeVentas();
            CantidadDeVentasMes();
            menu.txtIdVentaEliminar.setText("");
            menu.txtCantidadDevolverStock.setText("");

        } else {

            JOptionPane.showMessageDialog(null, "No se pudo eliminar.");

        }

    }

    //MODIFICAR STOCK ACTUAL LUEGO DE CANCELAR UNA VENTA
    public void RetornoDeStock() {

        String id = menu.txtIdVentaEliminar.getText();
        int cantidad = Integer.parseInt(menu.txtCantidadDevolverStock.getText());

        System.out.println("------Retorno de stock------");
        System.out.println("Id: " + id);
        System.out.println("cantidad: " + cantidad);

        DefaultTableModel n = new DefaultTableModel();
        int contadorStock = menu.tlbReporteVentas.getRowCount();

        String IdReporte, CodigoReporte, NombreReporte, ImporteReporte, GananciaReporte, CantidadReporte, FechaReporte;

        n.addColumn("Id");
        n.addColumn("Codigo");
        n.addColumn("Nombre");
        n.addColumn("Importe");
        n.addColumn("Ganancia");
        n.addColumn("Cantidad");
        n.addColumn("Fecha");

        for (int i = 0; i < contadorStock; i++) {

            //ABSORVE DATOS DE LA TABLA REPORTE DE VENTAS
            IdReporte = menu.tlbReporteVentas.getValueAt(i, 0).toString();
            CodigoReporte = menu.tlbReporteVentas.getValueAt(i, 1).toString();
//            NombreReporte = menu.tlbReporteVentas.getValueAt(i, 2).toString();
//            ImporteReporte = menu.tlbReporteVentas.getValueAt(i, 3).toString();
//            GananciaReporte = menu.tlbReporteVentas.getValueAt(i, 4).toString();
//            CantidadReporte = menu.tlbReporteVentas.getValueAt(i, 5).toString();
//            FechaReporte = menu.tlbReporteVentas.getValueAt(i, 6).toString();

            System.out.println("-----REPORTE DE VENTAS-------");
            System.out.println("Id: " + IdReporte);
            System.out.println("Codigo: " + CodigoReporte);
//            System.out.println("Nombre: " + NombreReporte);
//            System.out.println("Importe: " + ImporteReporte);
//            System.out.println("Ganancia: " + GananciaReporte);
//            System.out.println("Cantidad: " + CantidadReporte);
//            System.out.println("Fecha: " + FechaReporte);

            if (IdReporte.equals(id)) {

                System.out.println("---------BUSQUEDA DE LA TABLA STOCK POR CANTIDAD---------");
                System.out.println("CODIGO A BUSCAR: " + CodigoReporte);

                DefaultTableModel m = new DefaultTableModel();
                int Stock = menu.tlbStockVentas.getRowCount();

                String nombreStock, codigoStock, compraStock, ventaStock, fechaStock, cantidadStock;

                n.addColumn("Codigo");
                n.addColumn("Nombre");
                n.addColumn("Compra");
                n.addColumn("Venta");
                n.addColumn("Fecha");
                n.addColumn("Cantidad");

                for (int j = 0; j < Stock; j++) {

                    //ABSORVE DATOS DE LA TABLA STOCKVENTAS
                    codigoStock = menu.tlbStockVentas.getValueAt(j, 0).toString();
                    nombreStock = menu.tlbStockVentas.getValueAt(j, 1).toString();
                    compraStock = menu.tlbStockVentas.getValueAt(j, 2).toString();
                    ventaStock = menu.tlbStockVentas.getValueAt(j, 3).toString();
                    fechaStock = menu.tlbStockVentas.getValueAt(j, 4).toString();
                    cantidadStock = menu.tlbStockVentas.getValueAt(j, 5).toString();

                    int cantidadStockInt = Integer.parseInt(cantidadStock);

                    System.out.println("-----PRODUCTO EN STOCK-------");
                    System.out.println("codigo: " + nombreStock);
                    System.out.println("nombre: " + codigoStock);
                    System.out.println("compra: " + compraStock);
                    System.out.println("venta: " + ventaStock);
                    System.out.println("fecha: " + fechaStock);
                    System.out.println("cantidad: " + cantidadStock);

                    if (codigoStock.equals(CodigoReporte)) {

                        int idInt = Integer.parseInt(id);
                        int CantidadReporteInt = Integer.parseInt(cantidadStock);
                        int CantidadTotal = CantidadReporteInt + cantidad;

                        System.out.println("---------------------------------------------------------------");
                        System.out.println("-----REPORTE DE VENTAS-------");
                        System.out.println("Id: " + IdReporte);
                        System.out.println("Codigo: " + CodigoReporte);
//                        System.out.println("Nombre: " + NombreReporte);
//                        System.out.println("Importe: " + ImporteReporte);
//                        System.out.println("Ganancia: " + GananciaReporte);
//                        System.out.println("Cantidad: " + CantidadReporte);
//                        System.out.println("Fecha: " + FechaReporte);
                        System.out.println("------RETORNO DE STOCK-------");
                        System.out.println("Id: " + id);
                        System.out.println("cantidad: " + cantidad);
                        System.out.println("CANTIDAD FINAL: " + CantidadTotal);
                        System.out.println("----------------------------------------------------------------");

                        mod_producto.setCantidad(CantidadTotal);
                        mod_producto.setCodigo(CodigoReporte);

                        if (mod_crud_producto.ModificaStock(mod_producto)) {

                            JOptionPane.showMessageDialog(null, "SE MODIFICO");
                            System.out.println(".......................");
                            EliminarReporteVentas(idInt);

                        } else {

                            System.out.println("NO SE MODIFICO");
                            System.out.println(".......................");

                        }

                    }
                }

            }

        }

    }

    //ACTUALICA LA TABLA STOCKVENTAS y TABLASTOCK
    public void refrescarTabla() {

        try {

            DefaultTableModel modelo = new DefaultTableModel();

            menu.tlbStockVentas.setModel(modelo);
            menu.tlbStock.setModel(modelo);
            menu.TlbPrueba_lector_barra.setModel(modelo);

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
                menu.tlbStockVentas.getColumnModel().getColumn(i).setPreferredWidth(anchoColumnas[i]);

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

    //ACTUALIZAR LISTA DE REPORTE DE VENTAS
    public void refrescatTablaReporteVenta() {

        try {

            DefaultTableModel modelo = new DefaultTableModel();

            menu.tlbReporteVentas.setModel(modelo);

            PreparedStatement ps = null;

            ResultSet rs = null;

            Conexion conn = new Conexion();

            Connection con = conn.getConexion();

            String sql = "SELECT Id, Codigo, Nombre, Importe, Ganancia, Cantidad, Fecha FROM tabla_venta ORDER BY Id DESC LIMIT 1000";

            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();

            ResultSetMetaData rsMd = rs.getMetaData(); //le damos los datos de la consulta a la tabla.

            int cantidadColumnas = rsMd.getColumnCount(); // nos dira la cantidad de columnas.

            modelo.addColumn("Id");
            modelo.addColumn("Código"); //cada vuelta que reinicie la tabla perdera los nombres de las columnas
            modelo.addColumn("Nombre"); //por eso los tenemos que reasignar.
            modelo.addColumn("Importe");
            modelo.addColumn("Ganancia");
            modelo.addColumn("Cantidad");
            modelo.addColumn("Fecha");

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

    //METODO QUE BUSQUE A TIEMPO REAL Y AGREGUE A CARRITO
    public void buscarCodigoDeBarra(String codigo) { // { PRUEBA }
        
        //para terminar la prueba:----------------------------------------------
        //-el sistema tiene que hacer las operaciones que hace el carrito comun-
        //-tambien tener en pauta que los procesos los tiene que hacer y proye=-
        //-ctarlos en la cabla del Carrito_Prueba                              -
        //----------------------------------------------------------------------
        
        String codig, nombre, compra, venta, fecha, cantidad, cant, importe;

        double x = 0.0;
        double y = 0.0;
        double j = 0.0;
        double t = 0.0;
        double total; //variable que trae el valor que retorna la funcion Total.
        String totalstri; //variable que usamos para pasar a cadena el total y mostrarlo en el txt.
        double importeGanancia; //variable que usamos para tener el importe de ganancia.
        String importeGananciaString;

        if (codigo.length() == 8) {

            String campo = menu.txtBuscador_Prueba.getText();

            String where = "";

            if (!"".equals(campo)) {

                where = " WHERE Codigo = '" + campo + "'";

            }

            try {

                DefaultTableModel modelo = new DefaultTableModel();

                //frm_producto.jtVentas.setModel(modelo);
                menu.tlb_Carrito_Prueba.setModel(modelo);

                PreparedStatement ps = null;

                ResultSet rs = null;

                Conexion conn = new Conexion();

                Connection con = conn.getConexion();

                String sql = "SELECT Codigo,Nombre,Compra,Venta,Fecha,Cantidad FROM tabla_producto" + where;

                ps = con.prepareStatement(sql);

                rs = ps.executeQuery();

                ResultSetMetaData rsMd = rs.getMetaData(); //le damos los datos de la consulta a la tabla.

                int cantidadColumnas = 6;

                modelo.addColumn("Codigo"); //cada vuelta que reinicie la tabla perdera los nombres de las columnas
                modelo.addColumn("Nombre");//por eso los tenemos que reasignar.
                modelo.addColumn("Compra");
                modelo.addColumn("Venta");
                modelo.addColumn("Fecha");
                modelo.addColumn("Cantidad");

                Object[] filas = new Object[6];

                while (rs.next()) { //va a ir recorriendo los datos y los ira trayendo fila por fila el ciclo while.

                    for (int i = 0; i < cantidadColumnas; i++) {

                        filas[i] = rs.getObject(i + 1); //trae columna por columna

                        codig = rs.getString("Codigo");
                        nombre = rs.getString("Nombre");
                        compra = rs.getString("Compra");
                        venta = rs.getString("Venta");
                        fecha = rs.getString("Fecha");
                        cantidad = rs.getString("Cantidad");
                        
                        System.out.println(codig);
                        System.out.println(nombre);
                        System.out.println(compra);
                        System.out.println(venta);
                        System.out.println(fecha);
                        System.out.println(cantidad);

                    }

                    modelo.addRow(filas); //guarda en la tabla el vector que guardo todos los datos de la base de datos
                    
                    
                }

                //     modelo.addRow(filas);
            } catch (SQLException e) {

                System.out.println(e);

            }

        }

        if (codigo.length() < 8) {
            refrescarTabla();
        }

    }

    //GENERAR REPORTE DE VENTAS
    public void GenerarReporteStock() {

        try {
            Conexion con = new Conexion();
            Connection conn = con.getConexion();
            System.out.println("entro");
            JasperReport reporte = null;
            //C:\\Users\\FLORPC\\Desktop\\FLOR\\Tienda_tiramisustore\\src\\Report\\ReporteVentas.jasper
            String path = "C:\\Users\\FLORPC\\Desktop\\FLOR\\Tienda_tiramisustore\\src\\Report\\ReporteVentas.jasper"; //recorda que si lo queres pasar de maquina cambiar la ruta
            reporte = (JasperReport) JRLoader.loadObjectFromFile(path);
            JasperPrint jprint = JasperFillManager.fillReport(reporte, null, conn);

            JasperViewer view = new JasperViewer(jprint, false);

            view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            view.setVisible(true);

        } catch (JRException ex) {
            Logger.getLogger(Crl_provedor.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

        FiltrarDatos(menu.txtBuscadorTimepoRealVentas.getText());
        buscarCodigoDeBarra(menu.txtBuscador_Prueba.getText());

        //EVENTO QUE BUSQUE A TIEMPÓ REAL HACER UN (METODO QUE AYUDE EN TIEMPO REAL Y TIRE EN LA LISTA DEL CARRITO)
    }

}
