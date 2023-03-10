package src.GUI.UserSide;

import src.users.User;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import src.GUI.UserPage;
import src.bank.BankManager;
import src.bank.Bank;
import java.util.regex.Pattern;

public class ModifyDetails extends JFrame implements ActionListener {
    JFrame frame = new JFrame();
    JLabel createuser = new JLabel("Create New User!");
    JLabel first = new JLabel("First Name: ");
    JLabel last = new JLabel("Last Name: ");
    JLabel emailid = new JLabel("Email : ");
    JLabel phno = new JLabel("Phone No : ");
    JTextField firstname = new JTextField();
    JTextField lastname = new JTextField();
    JTextField email = new JTextField();
    JTextField phoneno = new JTextField();
    JTextField address = new JTextField();
    JTextField emiratesidno = new JTextField();
    JButton createbutton = new JButton("Modify");
    JButton cancelbutton = new JButton("Cancel");
    User u;
    BankManager bm;
    Bank b;

    ModifyDetails() {

        // For User

        first.setBounds(50, 50, 75, 25);
        last.setBounds(50, 100, 75, 25);
        emailid.setBounds(50, 150, 75, 25);
        phno.setBounds(50, 200, 75, 25);

        firstname.setBounds(150, 50, 150, 25);
        lastname.setBounds(150, 100, 150, 25);
        email.setBounds(150, 150, 150, 25);
        phoneno.setBounds(150, 200, 150, 25);

        createbutton.setBounds(125, 250, 100, 25);
        createbutton.setFocusable(false);
        createbutton.addActionListener(this);

        cancelbutton.setBounds(225, 250, 100, 25);
        cancelbutton.addActionListener(this);
        cancelbutton.setFocusable(false);

        frame.add(firstname);
        frame.add(lastname);
        frame.add(email);
        frame.add(phoneno);
        frame.add(createbutton);
        frame.add(createuser);
        frame.add(cancelbutton);
        frame.add(first);
        frame.add(last);
        frame.add(emailid);
        frame.add(phno);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 420);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    public ModifyDetails(User u, Bank b, BankManager bm) {
        this();
        this.u = u;
        this.b = b;
        this.bm = bm;
        firstname.setText(u.getFirstName());
        lastname.setText(u.getLastName());
        email.setText(u.getEmail());
        phoneno.setText(String.valueOf(u.getPhoneNumber()));
    }

    public static boolean isValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == cancelbutton) {
            new UserPage(u, bm, b);
            frame.dispose();
        }

        if (e.getSource() == createbutton) {
            String first = firstname.getText();
            String last = lastname.getText();
            String email = this.email.getText();
            String phone = phoneno.getText();

            if (first.equals("") || last.equals("") || email.equals("") || phone.equals("")) {
                JOptionPane.showMessageDialog(null, "Please fill all the fields");
            } else if (!isValid(email)) {
                JOptionPane.showMessageDialog(null, "Please enter a valid email");
                this.email.setText("");
            } else if (phone.length() != 10) {
                JOptionPane.showMessageDialog(null, "Please enter a valid phone number");
                phoneno.setText("");
            } else {
                try {
                    int oldAccno = u.accountNumber;
                    int oldPin = u.getPin();
                    b.deleteUser(oldAccno);
                    b.addUser(new User(first, last, email, Integer.parseInt(phone), oldAccno));
                    b.getUserByAccountId(oldAccno).setPin(oldPin);
                    bm.save(b);
                    JOptionPane.showMessageDialog(null, "Account Modified Successfully");
                    new UserPage(b.getUserByAccountId(oldAccno), bm, b);
                    frame.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Please enter valid details");
                    System.out.println(ex);
                }
            }
        }
    }
}