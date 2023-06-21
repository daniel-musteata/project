package org.example.frontend.add;

import org.example.db.DbService;
import org.example.db.PersonService;
import org.example.validators.EmailValidator;

import javax.swing.*;
import java.awt.*;

public class AddPersonForm extends JFrame {

    private final JTextField nameTextField;
    private final JTextField emailTextField;

    public AddPersonForm(JTable personTable) {
        super("Add Person");

        nameTextField = new JTextField(20);
        emailTextField = new JTextField(20);
        JButton addButton = new JButton("Add");

        setLayout(new FlowLayout());

        add(new JLabel("Name:"));
        add(nameTextField);
        add(new JLabel("Email:"));
        add(emailTextField);
        add(addButton);

        addButton.addActionListener(e -> {
            String name = nameTextField.getText();
            String email = emailTextField.getText();

            if (EmailValidator.isValidEmail(email)) {
                if (!PersonService.isPersonWithEmail(email)) {
                    PersonService.save(name, email);
                    loadPeopleData(personTable);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(AddPersonForm.this,
                            "This email is taken! Please choose another.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(AddPersonForm.this,
                        "Invalid email format! Please enter a valid email.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setSize(300, 150);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void loadPeopleData(JTable personTable) {
        DbService.setDataOnTable(personTable, new String[]{},"SELECT * FROM person");
    }
}
