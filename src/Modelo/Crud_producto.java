package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Crud_producto extends Conexion {

    // GUARDAR
    public boolean guardar(Producto pro) {

        PreparedStatement ps = null;
        Connection con = getConexion();

        String sql = "INSERT INTO tabla_producto (Codigo,Nombre,Compra,Venta,Cantidad,Fecha) VALUES (?,?,?,?,?,?)";

        try {

            ps = con.prepareStatement(sql);

            ps.setString(1, pro.getCodigo());
            ps.setString(2, pro.getNombre());
            ps.setDouble(3, pro.getCompra());
            ps.setDouble(4, pro.getVenta());
            ps.setInt(5, pro.getCantidad());
            ps.setString(6, pro.getFecha());

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
    public boolean modificar(Producto pro) {

        PreparedStatement ps = null;
        Connection con = getConexion();

        String sql = "UPDATE tabla_producto SET Codigo=?,Nombre=?,Compra=?,Venta=?,Fecha=?,Cantidad=? WHERE Id=?";

        try {

            ps = con.prepareStatement(sql);

            ps.setString(1, pro.getCodigo());
            ps.setString(2, pro.getNombre());
            ps.setDouble(3, pro.getCompra());
            ps.setDouble(4, pro.getVenta());
            ps.setString(5, pro.getFecha());
            ps.setInt(6, pro.getCantidad());
            ps.setInt(7, pro.getId());

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
    public boolean eliminar(Producto pro) {

        PreparedStatement ps = null;
        Connection con = getConexion();

        String sql = "DELETE FROM tabla_producto WHERE Id=?";

        try {

            ps = con.prepareStatement(sql);

            ps.setInt(1, pro.getId());

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
    public boolean buscar(Producto pro) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConexion();

        String sql = "SELECT * FROM tabla_producto WHERE Codigo = ?";

        try {

            ps = con.prepareStatement(sql);

            ps.setString(1, pro.getCodigo());

            rs = ps.executeQuery();

            if (rs.next()) {

                pro.setId(Integer.parseInt(rs.getString("Id")));
                pro.setNombre(rs.getString("Nombre"));
                pro.setCompra(Double.parseDouble(rs.getString("Compra")));
                pro.setVenta(Double.parseDouble(rs.getString("Venta")));
                pro.setFecha(rs.getString("Fecha"));
                pro.setCantidad(Integer.parseInt(rs.getString("Cantidad")));

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

    //DESCONTAR STOCK
    public boolean DescuentoStock(Producto pro) {

        PreparedStatement ps = null;
        Connection con = getConexion();

        String sql = "UPDATE tabla_producto SET Cantidad=? WHERE Codigo=?";

        try {

            ps = con.prepareStatement(sql);

            ps.setInt(1, pro.getCantidad());
            ps.setString(2, pro.getCodigo());

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
    
    //MODIFICO STOCK LUEGO DE QUE ME CANCELAN VENTA

    public boolean ModificaStock(Producto pro) {

        PreparedStatement ps = null;
        Connection con = getConexion();

        String sql = "UPDATE tabla_producto SET Cantidad=? WHERE Codigo=?";

        try {

            ps = con.prepareStatement(sql);
            
            ps.setInt(1, pro.getCantidad());
            ps.setString(2, pro.getCodigo());
            

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

}
