package Controlador;

import Modelo.Conexion;
import Modelo.Crud_provedor;
import Modelo.Provedor;
import Vista.Frm_menu_principal;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
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

public class Crl_provedor implements ActionListener, KeyListener {

    Crud_provedor mod_crud_provedor;
    Provedor mod_provedor;
    Frm_menu_principal menu;

    //METODO CONSTRUCTOR
    public Crl_provedor(Crud_provedor mod_crud_provedor, Provedor mod_provedor, Frm_menu_principal menu) {

        this.mod_crud_provedor = mod_crud_provedor;
        this.mod_provedor = mod_provedor;

        this.menu = menu;

        menu.btnGuardarProvedor.addActionListener(this);
        menu.btnModificarProvedor.addActionListener(this);
        menu.btnBuscarProvedor.addActionListener(this);
        menu.btnEliminarProvedor.addActionListener(this);
        menu.btnLimpiarProvedor.addActionListener(this);
        menu.btnPDFProvedor.addActionListener(this);

        this.menu.txtNombreProvedor.addKeyListener(this);
        this.menu.txtNumeroProvedor.addKeyListener(this);

    }

    //METODO QUE ESCUCHA LOS BOTONES
    public void actionPerformed(ActionEvent e) {

        //BOTON QUE GUARDA EL PROVEDOR
        if (e.getSource() == menu.btnGuardarProvedor) {

            mod_provedor.setNombre(menu.txtNombreProvedor.getText());
            mod_provedor.setTelefono(menu.txtNumeroProvedor.getText());
            mod_provedor.setCorreo(menu.txtCorreoProvedor.getText());
            mod_provedor.setDescripicion(menu.txtDescripcionProvedor.getText());

            if (mod_crud_provedor.guardar(mod_provedor)) { // if true / false else

                JOptionPane.showMessageDialog(null, "Se guardo correctamente.");
                limpiar();
                refrescarTablaProvedor();

            } else {

                JOptionPane.showMessageDialog(null, "No se pudo guardar datos.");

            }
        }

        //BOTON QUE MODIFICA EL PROVEDOR 
        if (e.getSource() == menu.btnModificarProvedor) {

            mod_provedor.setNombre(menu.txtNombreProvedor.getText());
            mod_provedor.setTelefono(menu.txtNumeroProvedor.getText());
            mod_provedor.setCorreo(menu.txtCorreoProvedor.getText());
            mod_provedor.setDescripicion(menu.txtDescripcionProvedor.getText());

            if (mod_crud_provedor.modificar(mod_provedor)) {

                JOptionPane.showMessageDialog(null, "Se modifico con exito.");
                limpiar();
                refrescarTablaProvedor();

            } else {

                JOptionPane.showMessageDialog(null, "No se pudo modificar.");

            }

        }

        //BOTON DE ELIMINAR PROVEDOR
        if (e.getSource() == menu.btnEliminarProvedor) {

            mod_provedor.setId(Integer.parseInt(menu.txtIdProvedor.getText()));

            if (mod_crud_provedor.eliminar(mod_provedor)) {

                JOptionPane.showMessageDialog(null, "Se pudo eliminar los datos");
                limpiar();
                refrescarTablaProvedor();

            } else {

                JOptionPane.showMessageDialog(null, "No se pudo eliminar");

            }

        }

        //BOTON DE BUSCAR PROVEDOR
        if (e.getSource() == menu.btnBuscarProvedor) {

            mod_provedor.setNombre(menu.txtBuscarProvedor.getText());

            if (mod_crud_provedor.buscar(mod_provedor)) {

                menu.txtIdProvedor.setText(String.valueOf(mod_provedor.getId()));
                menu.txtNombreProvedor.setText(String.valueOf(mod_provedor.getNombre()));
                menu.txtNumeroProvedor.setText(String.valueOf(mod_provedor.getTelefono()));
                menu.txtCorreoProvedor.setText(String.valueOf(mod_provedor.getCorreo()));
                menu.txtDescripcionProvedor.setText(String.valueOf(mod_provedor.getDescripicion()));

            } else {

                JOptionPane.showMessageDialog(null, "No se pudo encontrar el provedor.");

            }

        }

        //BOTON PARA LIMPIAR LOS TXT DE LA ALTA DE PROVEDOR
        if (e.getSource() == menu.btnLimpiarProvedor) {

            limpiar();

        }
        //BOTON QUE GENERA EL REPORTE DEL PROVEEDOR
        if (e.getSource() == menu.btnPDFProvedor) {

            GenerarReporteStock();

        }

    }

    //--------------FONA DE METODOS BASICOS------------------
    //METODO QUE LIMPIA LOS TXT DE ALTA PROVEDOR 
    public void limpiar() {

        menu.txtNombreProvedor.setText("");
        menu.txtNumeroProvedor.setText("");
        menu.txtCorreoProvedor.setText("");
        menu.txtDescripcionProvedor.setText("");
        menu.txtBuscarProvedor.setText("");
        menu.lbNombreProvedorTest.setText("");
        menu.lbTelefonoProvedorTest.setText("");

    }

    //METODO PARA VERIFICAR EL NUMBRE DEL PROVEDOR
    public boolean nombreProvedor(String nombre) {

        int nombreNumero = nombre.length();

        if (nombreNumero < 3) {

            return false;

        } else {

            return true;

        }
    }

    //METODO PARA REVISAR NUMERO DE TELEFONO
    public boolean telefonoProvedor(String numeroString) {

        int numero = Integer.parseInt(numeroString);
        
        if (numeroString.length() == 10) {

            if (numero < 1) {

                return false;

            } else {

                return true;

            }

        } else {

            return false;

        }

    }

    //METODO DE REFRESCAR TABLA DE PROVEDORES
    public void refrescarTablaProvedor() {

        try {

            DefaultTableModel modelo = new DefaultTableModel();

            menu.tlbProvedores.setModel(modelo);

            PreparedStatement ps = null;

            ResultSet rs = null;

            Conexion conn = new Conexion();

            Connection con = conn.getConexion();

            String sql = "SELECT Nombre,Telefono,Correo,Descripcion FROM tabla_provedores";

            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();

            ResultSetMetaData rsMd = rs.getMetaData(); //le damos los datos de la consulta a la tabla.

            int cantidadColumnas = rsMd.getColumnCount(); // nos dira la cantidad de columnas.

            modelo.addColumn("Nombre"); //cada vuelta que reinicie la tabla perdera los nombres de las columnas
            modelo.addColumn("Telefono");//por eso los tenemos que reasignar.
            modelo.addColumn("Correo");
            modelo.addColumn("Descripcion");

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

    //METODO PARA GENERAR EL REPORTE PDF DEL STOCK
    public void GenerarReporteStock() {

        try {
            Conexion con = new Conexion();
            Connection conn = con.getConexion();
            System.out.println("entro");
            JasperReport reporte = null;
            //C:\\Users\\FLORPC\\Desktop\\FLOR\\Tienda_tiramisustore\\src\\Report\\ReporteStock.jasper
            String path = "C:\\Users\\FLORPC\\Desktop\\FLOR\\Tienda_tiramisustore\\src\\Report\\ReporteProvedores.jasper";
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

        //REVISA NOMBRE PROVEEDOR
        if (menu.txtNombreProvedor.getText().equals("")) {

            menu.lbNombreProvedorTest.setText("");

        } else if (nombreProvedor(menu.txtNombreProvedor.getText()) == false) {

            menu.lbNombreProvedorTest.setText("El Nombre es muy corto");
            menu.lbNombreProvedorTest.setForeground(new Color(255, 51, 51));

        } else if (nombreProvedor(menu.txtNombreProvedor.getText()) == true) {

            menu.lbNombreProvedorTest.setText("El Nombre esta correcto");
            menu.lbNombreProvedorTest.setForeground(new Color(76, 213, 54));

        }

        //REVISA NUMERO DE TELEFONO
        if (menu.txtNumeroProvedor.getText().equals("")) {

            menu.lbTelefonoProvedorTest.setText("");

        }

        else if (telefonoProvedor(menu.txtNumeroProvedor.getText()) == false) {

            menu.lbTelefonoProvedorTest.setText("El Numero de telefono no esta completo");
            menu.lbTelefonoProvedorTest.setForeground(new Color(255, 51, 51));

        } else if (telefonoProvedor(menu.txtNumeroProvedor.getText()) == true) {

            menu.lbTelefonoProvedorTest.setText("El Numero de telefono es correcto");
            menu.lbTelefonoProvedorTest.setForeground(new Color(76, 213, 54));

        }

    }
}
