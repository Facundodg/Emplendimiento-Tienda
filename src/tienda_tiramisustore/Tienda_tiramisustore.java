package tienda_tiramisustore;

import Controlador.Crl_producto;
import Controlador.Crl_provedor;
import Controlador.Crl_ventas;
import Modelo.Crud_producto;
import Modelo.Crud_provedor;
import Modelo.Crud_ventas;
import Modelo.Producto;
import Modelo.Provedor;
import Modelo.Ventas;
import Vista.Frm_menu_principal;
import Vista.PantallaCarga;

public class Tienda_tiramisustore {

    public static void main(String[] args) {

        //-----------alta producto-------------------------
        Crud_producto mod_crud_producto = new Crud_producto();
        Producto mod_producto = new Producto();

        Frm_menu_principal menu = new Frm_menu_principal();
        //----------alta provedores------------------------
        Crud_provedor mod_crud_provedor = new Crud_provedor();
        Provedor mod_provedor = new Provedor();
        //----------------venta----------------------------
        Crud_ventas mod_crud_ventas = new Crud_ventas();
        Ventas mod_ventas = new Ventas();

        //--------INICIO DE LOS CONTROLADORES--------------
        Crl_ventas crl_ventas = new Crl_ventas(mod_crud_ventas, mod_ventas, menu, mod_producto, mod_crud_producto);
        Crl_provedor crl_provedor = new Crl_provedor(mod_crud_provedor, mod_provedor, menu);
        Crl_producto crl_producto = new Crl_producto(mod_crud_producto, mod_producto, menu);

        //--------agrego pantalla de carga-----------------
        PantallaCarga pantallaCarga = new PantallaCarga();
        
        pantallaCarga.setVisible(true);
        
        crl_producto.iniciar();
        //menu.setVisible(true);

    }

}
