import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


// Clase para la nueva ventana
public class Clientes extends JFrame implements ActionListener {

    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;
    private JButton boton1;

    public Clientes() {
        setTitle("Clientes");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra solo esta ventana
        setLocationRelativeTo(null); // Centrar en la pantalla después de definir el tamaño
        getContentPane().setBackground(Color.WHITE);


        // 📌 Panel de contenido con Layout
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);


        // 📌 Definir los nombres de las columnas
        String[] columnas = {"ID_CLIENTE", "NOMBRE", "APELLIDOS", "NIF", "TELEFONO"};

        // 📌 Crear el DefaultTableModel para manejar dinámicamente los datos
        modeloTabla = new DefaultTableModel(columnas, 0); //cero filas al inciio
        tablaClientes = new JTable(modeloTabla);
        tablaClientes.setFillsViewportHeight(true); //crear la tabla con el modelo
        tablaClientes.setRowHeight(30);  // Cambia la altura de todas las filas a 30 píxeles
        tablaClientes.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID_CLIENTE más pequeño
        tablaClientes.getColumnModel().getColumn(1).setPreferredWidth(150); // NOMBRE más ancho
        tablaClientes.getColumnModel().getColumn(2).setPreferredWidth(200); // APELLIDOS más ancho
        tablaClientes.getColumnModel().getColumn(3).setPreferredWidth(100); // NIF tamaño medio
tablaClientes.getColumnModel().getColumn(4).setPreferredWidth(100); // TELEFONO tamaño medio


        // 📌 Añadir la tabla al panel dentro de un JScrollPane
        JScrollPane scrollPane = new JScrollPane(tablaClientes);
        panel.add(scrollPane, BorderLayout.CENTER);

        boton1 = new JButton("Agregar Cliente");
        boton1.setBounds(10,300,150,30);
        add(boton1);
        boton1.addActionListener(this);

        // 📌 Llenar la tabla con datos de la base de datos
        obtenerClientes();  
        
        add(panel);

        // 📌 Añadir el panel al JFrame
        add(panel);

        // Mostrar la ventana
        setVisible(true);

    }

     // Método para obtener los datos de los clientes desde la base de datos
     private void obtenerClientes() {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;   

        try {
            // 🔗 Conectar a la base de datos
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tiendainformatica", "root", "");
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT ID_CLIENTE, NOMBRE, APELLIDOS, NIF, TELEFONO FROM clientes");

            // 🔄 Limpiar modelo antes de llenarlo (para evitar duplicados)
            modeloTabla.setRowCount(0);

            // 📥 Llenar la tabla con datos
            while (rs.next()) {
                Object[] fila = {
                    rs.getString("ID_CLIENTE"),
                    rs.getString("NOMBRE"),
                    rs.getString("APELLIDOS"),
                    rs.getString("NIF"),
                    rs.getString("TELEFONO")
                };
                modeloTabla.addRow(fila);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 🔴 Cerrar la conexión correctamente
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Clientes();  // Crear y mostrar la ventana de Clientes
    }
}