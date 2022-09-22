import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class RegistrationForm extends JDialog{
    private JTextField tfName;
    private JTextField tfEmail;
    private JTextField pfPassword;
    private JTextField pfConfirmPassword;
    private JButton btnRegister;
    private JButton btnCancel;
    private JPanel registerPanel;

    public RegistrationForm(JFrame parent){
        super(parent);
        setTitle("Crear una nueva cuenta");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // boton para confirmar el registro de un nuevo usuario
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        // boton de cierre de ventana
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void registerUser() {
        String name = tfName.getText();
        String email = tfEmail.getText();
        String password = String.valueOf(pfPassword.getText());
        String confirmPassword = String.valueOf(pfConfirmPassword.getText());
// se verifica que no existan campos vacios
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, ingrese todos los campos",
                    "Intente de nuevo",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
// se verifica que las contraseñas coincidan
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                    "Las contraseñas no coinciden",
                    "Intente nuevamente",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
// si no hay inconvenientes se cierra la ventana
        user = addUserToDatabase(name, email, password);
        if (user != null){
            dispose();
        }
        // en caso de cualquier inconveniente en el registro, se muestra mensaje de alerta
        else{
            JOptionPane.showMessageDialog(this,
                    "Fallo al intentar crear un nuevo usuario",
                    "Intente nuevamente",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    // se insertan los datos registrados en la tabla usuarios de la base de datos
    public User user;
    private User addUserToDatabase(String name, String email, String password) {
        User user = null;
        final String DB_URL = "jdbc:mysql://sql10.freesqldatabase.com:3306/sql10520671";
        final String USERNAME = "sql10520671";
        final String PASSWORD = "LvqIGT8pxr";

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO usuarios (nombre, email, contraseña)" +
                    "VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);

            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0){
                user = new User();
                user.name = name;
                user.email = email;
                user.password = password;
            }

            stmt.close();
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return user;
    }

    public static void  main(String[] args){
        RegistrationForm myForm = new RegistrationForm(null);
        User user = myForm.user;
        if (user != null){
            System.out.println("Registro exitoso de: " + user.name);
        }
        else {
            System.out.println("Registro cancelado");
        }
    }
}
