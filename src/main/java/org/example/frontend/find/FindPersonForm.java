package org.example.frontend.find;

import org.example.db.PersonService;
import org.example.models.Person;

import javax.swing.*;
import java.awt.*;
public class FindPersonForm extends JFrame {

    public FindPersonForm() {
        super("Find person");

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailTextField = new JTextField();
        JButton findButton = new JButton("Find");
        JLabel resultLabel = new JLabel("");

        panel.add(emailLabel);
        panel.add(emailTextField);
        panel.add(findButton);


        findButton.addActionListener(e -> {
            String email = emailTextField.getText();
            Person person = PersonService.findByEmail(email);

            if (person.getEmail() != null) {
                resultLabel.setText("Person found: " + person.getName() + " " + person.getEmail());
            } else {
                resultLabel.setText("Person not found.");
            }
        });

        JPanel resultPanel = new JPanel();
        resultPanel.add(resultLabel);


        add(panel, BorderLayout.NORTH);
        add(resultPanel, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 150);
        setVisible(true);
    }
}
