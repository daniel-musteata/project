package org.example.frontend.add;

import org.example.db.DbService;
import org.example.db.ItemService;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddItemForm extends JFrame{

    private final JTextField nameTextField;
    private final JTextField priceTextField;

    public AddItemForm(JTable itemTable) {
        super("Add Item");

        nameTextField = new JTextField(20);
        priceTextField = new JTextField(10);
        JButton addButton = new JButton("Add");

        setLayout(new FlowLayout());

        add(new JLabel("Name:"));
        add(nameTextField);
        add(new JLabel("Price:"));
        add(priceTextField);
        add(addButton);

        addButton.addActionListener(e -> {
            String name = nameTextField.getText();
            if (isValidPrice(priceTextField.getText())) {
                double price = Double.parseDouble(priceTextField.getText());
                ItemService.addItem(name, price);
                loadItemsData(itemTable);
                dispose();
            } else {
                JOptionPane.showMessageDialog(AddItemForm.this,
                        "Invalid price! Please enter a valid number.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setSize(300, 150);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private boolean isValidPrice(String price) {
        String regex = "^\\d+(\\.\\d+)?$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(price);
        return matcher.matches();
    }

    private void loadItemsData(JTable itemTable) {
        DbService.setDataOnTable(itemTable, new String[] {"id", "name", "price", "owner"}, """
                SELECT item.id, item.name, item.price, person.name AS owner
                FROM item
                left JOIN person ON item.owner_id = person.id
                """);
    }
}
