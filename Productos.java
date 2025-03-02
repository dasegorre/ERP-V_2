import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


// Clase para la nueva ventana
public class Productos extends JFrame implements ActionListener {

    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JButton boton1;

    public Productos() {
        setTitle("Productos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra solo esta ventana
        setLocationRelativeTo(null); // Centrar en la pantalla después de definir el tamaño
        getContentPane().setBackground(Color.WHITE);


        // 📌 Panel de contenido con Layout
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);


        // 📌 Definir los nombres de las columnas
        String[] columnas = {"COD_PRODUCTO", "NOMBRE", "PRECIO_COMPRA", "ID_PROVEEDOR"};

        // 📌 Crear el DefaultTableModel para manejar dinámicamente los datos
        modeloTabla = new DefaultTableModel(columnas, 0); //cero filas al inciio
        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setFillsViewportHeight(true); //crear la tabla con el modelo
        tablaProductos.setRowHeight(30);  // Cambia la altura de todas las filas a 30 píxeles
        tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(50);  // COD_PRODUCTIO más pequeño
        tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(150); // NOMBRE más ancho
        tablaProductos.getColumnModel().getColumn(2).setPreferredWidth(100); // PRECIO_COMPRA
        tablaProductos.getColumnModel().getColumn(3).setPreferredWidth(100); // ID_PROVEEDOR


        // 📌 Añadir la tabla al panel dentro de un JScrollPane
        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        panel.add(scrollPane, BorderLayout.CENTER);

        boton1 = new JButton("Agregar Producto");
        boton1.setBounds(10,300,150,30);
        add(boton1);
        boton1.addActionListener(this);

        // 📌 Llenar la tabla con datos de la base de datos
        obtenerProducto();  

        // 📌 Añadir el panel al JFrame
        add(panel);

        // Mostrar la ventana
        setVisible(true);

    }

     // Método para obtener los datos de los clientes desde la base de datos
     private void obtenerProducto() {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;   

        try {
            // 🔗 Conectar a la base de datos
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tiendainformatica", "root", "");
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT COD_PRODUCTO, NOMBRE, PRECIO_COMPRA, ID_PROVEEDOR FROM productos");

            // 🔄 Limpiar modelo antes de llenarlo (para evitar duplicados)
            modeloTabla.setRowCount(0);

            // 📥 Llenar la tabla con datos
            while (rs.next()) {
                Object[] fila = {
                    rs.getString("COD_PRODUCTO"),
                    rs.getString("NOMBRE"),
                    rs.getString("PRECIO_COMPRA"),
                    rs.getString("ID_PROVEEDOR")
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
        new Productos();  // Crear y mostrar la ventana de Clientes
    }
}