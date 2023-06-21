package org.example.frontend.update;

import org.example.db.DbService;
import org.example.db.PersonService;
import org.example.models.Person;
import org.example.validators.EmailValidator;

import javax.swing.*;
import java.awt.*;

public class UpdatePersonForm extends JFrame {

    private final JTextField nameTextField;
    private final JTextField emailTextField;

    public UpdatePersonForm(int personId, JTable personTable) {
        super("Update Person");

        nameTextField = new JTextField(20);
        emailTextField = new JTextField(20);
        JButton updateButton = new JButton("Update");

        Person person = PersonService.findById(personId);
        nameTextField.setText(person.getName());
        emailTextField.setText(person.getEmail());

        setLayout(new FlowLayout());

        add(new JLabel("Name:"));
        add(nameTextField);
        add(new JLabel("Email:"));
        add(emailTextField);
        add(updateButton);

        updateButton.addActionListener(e -> {
            String name = nameTextField.getText();
            String email = emailTextField.getText();

            if (EmailValidator.isValidEmail(email)) {
                PersonService.updatePerson(personId, name, email);
                loadPeopleData(personTable);
                dispose();
            } else  {
                JOptionPane.showMessageDialog(UpdatePersonForm.this,
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
