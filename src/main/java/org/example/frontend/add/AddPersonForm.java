package org.example.frontend.add;

import org.example.db.DbService;
import org.example.db.PersonService;

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

            PersonService.save(name, email);
            loadPeopleData(personTable);
            dispose();
        });

        setSize(300, 150);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void loadPeopleData(JTable personTable) {
        DbService.setDataOnTable(personTable, new String[]{},"SELECT * FROM person");
    }
}
