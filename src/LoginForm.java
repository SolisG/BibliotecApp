import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
public class LoginForm extends JDialog{
    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JButton btnOk;
    private JButton btnCancel;
    private JPanel loginPanel;

    public LoginForm(JFrame parent){
        super(parent);
        setTitle("Login");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(450,474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = tfEmail.getText();
                String password = String.valueOf(pfPassword.getPassword());

// funcion para verificar si credenciales coinciden con las almacenadas
               user =  getAuthenticatedUser(email, password);

               if (user != null){
                   dispose();
               }
               else {
                   JOptionPane.showMessageDialog(LoginForm.this,
                           "Email o Contraseña incorrectos",
                           "Intente nuevamente",
                           JOptionPane.ERROR_MESSAGE);
               }
            }
        });
        //boton de cancelar
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }
    public User user;
    private  User getAuthenticatedUser(String email, String password) {
        User user = null;
//conexion a la base de datos
        final String DB_URL = "jdbc:mysql://sql10.freesqldatabase.com:3306/sql10520671";
        final String USERNAME = "sql10520671";
        final String PASSWORD = "LvqIGT8pxr";
// se compara lo ingresado en los campos con lo almacenado en la base de datos
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM usuarios WHERE email=? AND contraseña=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                user.name = resultSet.getString("nombre");
                user.email = resultSet.getString("email");
                user.password = resultSet.getString("contraseña");

            }

            stmt.close();
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return user;
    }

    public static void main(String[] args) {
        LoginForm loginForm = new LoginForm(null);
        User user =loginForm.user;
        if (user != null){
            System.out.println("Autentificacion exitosa de: " + user.name );
            System.out.println("email: " + user.email);
        }
        else{
            System.out.println("Autentificacion cancelada");
        }
    }
}