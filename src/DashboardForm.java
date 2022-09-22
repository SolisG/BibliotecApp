import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DashboardForm extends JFrame{
    private JPanel dashboardPanel;
    private JLabel lbAdmin;
    private JButton btnRegister;

    public DashboardForm(){
        setTitle("Dashboard");
        setContentPane(dashboardPanel);
        setMinimumSize(new Dimension(500,429));
        setSize(1200,700);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // se verifica si existen usuarios creados en la base de datos
        boolean hasRegistredUsers =  connectToDatabase();

        // si existen usuarios creados, se muestra la pantalla de login para ingresar credenciales
        if (hasRegistredUsers){
            LoginForm loginForm = new LoginForm(this);
                    User user = loginForm.user;

                    // si el usuario es valido, se muestra la pantalla de dashboard
                    if (user != null){
                        lbAdmin.setText("Usuario:  " + user.name);
                        setLocationRelativeTo(null);
                        setVisible(true);
                    }
                    else{
                        dispose();
                    }
        }
        // si no existen usuarios creados, se muestra la pantalla de registro
        else{
            RegistrationForm registrationForm = new RegistrationForm(this);
            User user = registrationForm.user;

            // si el usuario es valido, la etiqueta superior izquierda mostrará el nombre del usuario
            if (user != null){
                lbAdmin.setText("Usuario:  " + user.name);
                setLocationRelativeTo(null);
                setVisible(true);
            }
            else{
                dispose();
            }
        }
    // si se hace click en el boton registrar, se muestra la pantalla de registro para añadir nuevos usuarios
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistrationForm registrationForm = new RegistrationForm(DashboardForm.this);
                User user = registrationForm.user;

                // si el registro es correcto, se muestra mensaje de confirmación
                if(user != null) {
                    JOptionPane.showMessageDialog(DashboardForm.this,
                            "Nuevo Usuario " + user.name,
                            "Registro exitoso",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    // funcion para conectarse a la base de datos
    private boolean connectToDatabase() {
        boolean hasRegistredUsers = false;

        final String DB_URL = "jdbc:mysql://sql10.freesqldatabase.com:3306/sql10520671";
        final String USERNAME = "sql10520671";
        final String PASSWORD = "LvqIGT8pxr";

        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            Statement statement = conn.createStatement();

// se cuenta la cantidad de ingresos en la tabla usuarios, si es superior a 0 se considera que existen usuarios creados
            statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM usuarios");

            if(resultSet.next()){
            int numUsers = resultSet.getInt(1);
            if(numUsers > 0){
                hasRegistredUsers = true;
            }
        }

            statement.close();
            conn.close();


    }catch (Exception e) {
            e.printStackTrace();
        }

        return hasRegistredUsers;
    }

    public static void main(String[] args) {
        DashboardForm dashboardForm = new DashboardForm();
    }
}
