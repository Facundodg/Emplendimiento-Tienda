package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Crud_provedor extends Conexion {

    // GUARDAR
    public boolean guardar(Provedor prove) {

        PreparedStatement ps = null;
        Connection con = getConexion();

        String sql = "INSERT INTO tabla_provedores (Nombre,Telefono,Correo,Descripcion) VALUES (?,?,?,?)";

        try {

            ps = con.prepareStatement(sql);

            ps.setString(1, prove.getNombre());
            ps.setString(2, prove.getTelefono());
            ps.setString(3, prove.getCorreo());
            ps.setString(4, prove.getDescripicion());

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
    public boolean modificar(Provedor prove) {

        PreparedStatement ps = null;
        Connection con = getConexion();

        String sql = "UPDATE tabla_provedores SET Nombre=?,Telefono=?,Correo=?,Descripcion=? WHERE Id=?";

        try {

            ps = con.prepareStatement(sql);

            ps.setString(1, prove.getNombre());
            ps.setString(2, prove.getTelefono());
            ps.setString(3, prove.getCorreo());
            ps.setString(4, prove.getDescripicion());
            ps.setInt(5, prove.getId());

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

    // ELIMINAR
    public boolean eliminar(Provedor prove) {

        PreparedStatement ps = null;
        Connection con = getConexion();

        String sql = "DELETE FROM tabla_provedores WHERE Id=?";

        try {

            ps = con.prepareStatement(sql);

            ps.setInt(1, prove.getId());

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

    // BUSCAR
    public boolean buscar(Provedor prove) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = getConexion();

        String sql = "SELECT * FROM tabla_provedores WHERE Nombre = ?";

        try {

            ps = con.prepareStatement(sql);

            ps.setString(1, prove.getNombre());

            rs = ps.executeQuery();

            if (rs.next()) {
                
                prove.setId(Integer.parseInt(rs.getString("Id")));
                prove.setNombre(rs.getString("Nombre"));
                prove.setTelefono(rs.getString("Telefono"));
                prove.setCorreo(rs.getString("Correo"));
                prove.setDescripicion(rs.getString("Descripcion"));

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

}
