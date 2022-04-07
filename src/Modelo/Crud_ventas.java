package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Crud_ventas extends Conexion {

    // GUARDAR
    public boolean guardar(Ventas vent) {

        PreparedStatement ps = null;
        Connection con = getConexion();

        String sql = "INSERT INTO tabla_venta (Codigo,Nombre,Importe,Ganancia,Cantidad,Fecha) VALUES (?,?,?,?,?,?)";

        try {

            ps = con.prepareStatement(sql);

            ps.setString(1, vent.getCodigo());
            ps.setString(2, vent.getNombre());
            ps.setDouble(3, vent.getImporte());
            ps.setDouble(4, vent.getGanancia());
            ps.setInt(5, vent.getCantidad());
            ps.setString(6, vent.getFecha());

            ps.execute();

            return true;

        } catch (SQLException e) {

            System.out.println(e);

            return false;

        } finally {

            try {

                con.close();

            } catch (SQLException e) {

                System.out.println(e);

            }
        }
    }

    // MODIFICAR
    public boolean modificar(Ventas vent) {

        PreparedStatement ps = null;
        Connection con = getConexion();

        String sql = "UPDATE tabla_venta SET Codigo=?,Nombre=?,Importe=?,Ganancia=?,Cantidad=?,Fecha=? WHERE Id=?";

        try {

            ps = con.prepareStatement(sql);

            ps.setString(1, vent.getCodigo());
            ps.setString(2, vent.getNombre());
            ps.setDouble(3, vent.getImporte());
            ps.setDouble(4, vent.getGanancia());
            ps.setInt(5, vent.getCantidad());
            ps.setString(6, vent.getFecha());

            ps.setInt(7, vent.getId());

            ps.execute();

            return true;

        } catch (SQLException e) {

            System.out.println(e);

            return false;

        } finally {

            try {

                con.close();

            } catch (SQLException e) {

                System.out.println(e);

            }
        }

    }

    //ELIMINAR
    public boolean eliminar(Ventas vent) {

        PreparedStatement ps = null;
        Connection con = getConexion();

        String sql = "DELETE FROM tabla_venta WHERE Id=?";

        try {

            ps = con.prepareStatement(sql);

            ps.setInt(1, vent.getId());

            ps.execute();

            return true;

        } catch (SQLException e) {

            System.out.println(e);

            return false;

        } finally {

            try {

                con.close();

            } catch (SQLException e) {

                System.out.println(e);

            }
        }

    }

    //BUSCAR
    public boolean buscar(Ventas vent) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConexion();

        String sql = "SELECT * FROM tabla_venta WHERE Id = ?";

        try {

            ps = con.prepareStatement(sql);

            ps.setString(1, vent.getCodigo());

            rs = ps.executeQuery();

            if (rs.next()) {

                vent.setId(Integer.parseInt(rs.getString("Id")));
                vent.setNombre(rs.getString("Nombre"));
                vent.setImporte(Double.parseDouble(rs.getString("Importe")));
                vent.setGanancia(Double.parseDouble(rs.getString("Ganancia")));
                vent.setCantidad(Integer.parseInt(rs.getString("Cantidad")));
                vent.setFecha(rs.getString("Fecha"));

                return true;
            }

            return false;

        } catch (SQLException e) {

            System.out.println(e);

            return false;

        } finally {

            try {

                con.close();

            } catch (SQLException e) {

                System.out.println(e);

            }
        }

    }
   

    //ELIMINAR VENTA YA ECHA
//    public boolean eliminarVenta(Ventas vent) {
//
//        PreparedStatement ps = null;
//        Connection con = getConexion();
//
//        String sql = "DELETE FROM tabla_venta WHERE Id=?";
//
//        try {
//
//            ps = con.prepareStatement(sql);
//
//            ps.setInt(1, vent.getId());
//
//            ps.execute();
//
//            return true;
//
//        } catch (SQLException e) {
//
//            System.out.println(e);
//
//            return false;
//
//        } finally {
//
//            try {
//
//                con.close();
//
//            } catch (SQLException e) {
//
//                System.out.println(e);
//
//            }
//        }
//
//    }

}
