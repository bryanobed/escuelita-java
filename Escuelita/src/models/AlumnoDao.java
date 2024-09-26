package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class AlumnoDao {

    Conexion cn = new Conexion();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;

    //Variables para enviar datos entre interfaces
    public static int id_alumno = 0;
    public static String nombre_alumno = "";
    public static String apellido_alumno = "";
    public static String edad_alumno = "";

    ///Método registrar Alumno
    public boolean registrarAlumno(Alumno alumno) {
        String query = "INSERT INTO alumnos(nombre, apellido, edad) VALUES (?, ?, ?)";

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setString(1, alumno.getNombre());
            pst.setString(2, alumno.getApellido());
            pst.setInt(3, alumno.getEdad());
            pst.execute();
            return true;

        } catch (SQLException e) {
            return false;
        }
    }

    //Método listar Alumno
    public List listarAlumnos(String value) {
        List<Alumno> lista_alumnos = new ArrayList();
        String query = "SELECT * FROM alumnos";
        String query_search_category = "SELECT * FROM alumnos WHERE nombre LIKE '%" + value + "%'";

        try {
            conn = cn.getConnection();
            if (value.equalsIgnoreCase("")) {
                pst = conn.prepareStatement(query);
                rs = pst.executeQuery();
            } else {
                pst = conn.prepareStatement(query_search_category);
                rs = pst.executeQuery();
            }

            while (rs.next()) {
                Alumno alumno = new Alumno();
                alumno.setId(rs.getInt("id"));
                alumno.setNombre(rs.getString("nombre"));
                alumno.setApellido(rs.getString("apellido"));
                alumno.setEdad(rs.getInt("edad"));
                lista_alumnos.add(alumno);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
        }
        return lista_alumnos;
    }

    public boolean actualizarAlumno(Alumno alumno) {
        String query = "UPDATE alumnos SET nombre = ?, apellido = ?, edad= ? WHERE id = ?";

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setString(1, alumno.getNombre());
            pst.setString(2, alumno.getApellido());
            pst.setInt(3, alumno.getEdad());
            pst.setInt(4, alumno.getId());
            pst.execute();
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar los datos de la categoría");
            return false;
        }
    }

    public boolean eliminarAlumno(int id) {
        String query = "DELETE FROM alumnos WHERE id = " + id;

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.execute();
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
            return false;
        }

    }

}
