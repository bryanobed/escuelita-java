package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import models.Alumno;
import models.AlumnoDao;
import views.Form;

public class ControladorAlumno implements ActionListener, MouseListener, KeyListener {

    Alumno alumno;
    AlumnoDao alumnoDao;
    Form form;
    DefaultTableModel model = new DefaultTableModel();

    public ControladorAlumno(Alumno alumno, AlumnoDao alumnoDao, Form form) {
        this.alumno = alumno;
        this.alumnoDao = alumnoDao;
        this.form = form;
        //Botón de registrar categoría
        //Botón de registrar categoría
        this.form.btn_registrar.addActionListener(this);
        //Botón de modificar categoría
        this.form.btn_modificar.addActionListener(this);
        //Botón de eliminar categoría
        this.form.btn_eliminar.addActionListener(this);
        //Botón de cancelar
        this.form.btn_cancelar.addActionListener(this);
        this.form.tb_alumnos.addMouseListener(this);
        this.form.txt_buscar_alumno.addKeyListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == form.btn_registrar) {
            if (form.txtNombre.getText().equals("")
                    || form.txtApellido.getText().equals("")
                    || form.txtEdad.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
                limpiarCampos();
            } else {
                //Realizar la inserción                
                alumno.setNombre(form.txtNombre.getText().trim());
                alumno.setApellido(form.txtApellido.getText().trim());
                alumno.setEdad(Integer.parseInt(form.txtEdad.getText().trim()));

                if (alumnoDao.registrarAlumno(alumno)) {
                    limpiarTabla();
                    limpiarCampos();
                    listarTablaAlumnos();
                    JOptionPane.showMessageDialog(null, "Alumno registrado con éxito");
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al registrar el alumno");
                }
            }
        } else if (e.getSource() == form.btn_modificar) {
            if (form.txtId.getText().equals("")
                    || form.txtNombre.getText().equals("")
                    || form.txtApellido.getText().equals("")
                    || form.txtEdad.getText().equals("")) {

                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");

            } else {
                alumno.setId(Integer.parseInt(form.txtId.getText()));
                alumno.setNombre(form.txtNombre.getText().trim());
                alumno.setApellido(form.txtApellido.getText().trim());
                alumno.setEdad(Integer.parseInt(form.txtEdad.getText()));

                if (alumnoDao.actualizarAlumno(alumno)) {
                    limpiarTabla();
                    limpiarCampos();
                    listarTablaAlumnos();
                    form.btn_registrar.setEnabled(true);
                    JOptionPane.showMessageDialog(null, "Datos del lumno modificados con éxito");
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al modificar los datos del alumno");
                }
            }
        } else if (e.getSource() == form.btn_eliminar) {
            int row = form.tb_alumnos.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar un alumno para eliminar");
            } else {
                int id = Integer.parseInt(form.tb_alumnos.getValueAt(row, 0).toString());
                int question = JOptionPane.showConfirmDialog(null, "¿En realidad quieres eliminar esta categoría?");

                if (question == 0 && alumnoDao.eliminarAlumno(id) != false) {
                    limpiarTabla();
                    limpiarCampos();
                    form.btn_registrar.setEnabled(true);
                    listarTablaAlumnos();
                    JOptionPane.showMessageDialog(null, "Alumno eliminado con éxito");
                }
            }
        } else if (e.getSource() == form.btn_cancelar) {
            limpiarCampos();
            form.btn_cancelar.setEnabled(true);
            form.btn_registrar.setEnabled(true);

        }
    }

    //Listar categorías
    public void listarTablaAlumnos() {
        limpiarTabla(); // Limpia la tabla antes de agregar nuevos datos
        List<Alumno> list = alumnoDao.listarAlumnos(form.txt_buscar_alumno.getText());
        model = (DefaultTableModel) form.tb_alumnos.getModel();
        Object[] row = new Object[4];
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getId();
            row[1] = list.get(i).getNombre();
            row[2] = list.get(i).getApellido();
            row[3] = list.get(i).getEdad();
            model.addRow(row);
        }
        form.tb_alumnos.setModel(model);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (alumno != null) {
            int row = form.tb_alumnos.rowAtPoint(e.getPoint());
            form.txtId.setText(form.tb_alumnos.getValueAt(row, 0).toString()); // ID
            form.txtNombre.setText(form.tb_alumnos.getValueAt(row, 1).toString()); // Nombre
            form.txtApellido.setText(form.tb_alumnos.getValueAt(row, 2).toString()); // Apellido
            form.txtEdad.setText(form.tb_alumnos.getValueAt(row, 3).toString()); // Edad           
            // Deshabilitar el botón de registrar
            form.btn_registrar.setEnabled(false);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    public void keyReleased(KeyEvent e) {
        if (e.getSource() == form.txt_buscar_alumno) {
            //Limpiar tabla
            limpiarTabla();
            //Listar categorías
            listarTablaAlumnos();

        }
    }

    public void limpiarTabla() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }

    public void limpiarCampos() {
        form.txtId.setText("");
        form.txtNombre.setText("");
        form.txtApellido.setText("");
        form.txtEdad.setText("");
    }

}
