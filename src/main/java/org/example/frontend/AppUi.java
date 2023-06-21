package org.example.frontend;

import org.example.db.DbService;
import org.example.db.ItemService;
import org.example.db.PersonService;
import org.example.frontend.add.AddItemForm;
import org.example.frontend.add.AddPersonForm;
import org.example.frontend.find.FindPersonForm;
import org.example.frontend.update.UpdateItemForm;
import org.example.frontend.update.UpdatePersonForm;
import org.example.models.Person;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AppUi extends JFrame {

    private final JTable personTable;
    private final JTable itemTable;
    private final JTable itemTable2;
    private JPanel personButtonPanel;
    private JPanel itemButtonPanel;
    private JPanel itemButtonPanel2;
    private JButton personAddButton;
    private JButton personUpdateButton;
    private JButton personDeleteButton;
    private JButton personFindButton;
    private JButton itemAddButton;
    private JButton itemUpdateButton;
    private JButton itemDeleteButton;
    private JTextField itemNameTextField;
    private JButton findItemButton;

    public AppUi() {
        super("App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DefaultTableModel personTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        DefaultTableModel itemTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        personTable = new JTable(personTableModel);
        itemTable = new JTable(itemTableModel);
        itemTable2 = new JTable(itemTableModel);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Person", new JScrollPane(personTable));
        tabbedPane.addTab("Item", new JScrollPane(itemTable));
        tabbedPane.addTab("Find item by name", new JScrollPane(itemTable2));
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 0) {
                add(personButtonPanel, BorderLayout.SOUTH);
                remove(itemButtonPanel);
                remove(itemButtonPanel2);
                loadPeopleData();
            } else if (tabbedPane.getSelectedIndex() == 1) {
                add(itemButtonPanel, BorderLayout.SOUTH);
                remove(personButtonPanel);
                remove(itemButtonPanel2);
                loadItemsData();
            } else if (tabbedPane.getSelectedIndex() == 2) {

                add(itemButtonPanel2, BorderLayout.NORTH);
                itemNameTextField.setText("");
                remove(personButtonPanel);
                remove(itemButtonPanel);
                loadItemsByName("");
            }
            revalidate();
            repaint();
        });

        buildButtons();
        loadPeopleData();
        getButtonActions();
        getTableListener();

        setLayout(new BorderLayout());
        add(personButtonPanel, BorderLayout.SOUTH);
        add(tabbedPane, BorderLayout.CENTER);
        setSize(800, 600);
        setVisible(true);
    }

    private void buildButtons() {
        personAddButton = new JButton("Add (Person)");
        personUpdateButton = new JButton("Update (Person)");
        personDeleteButton = new JButton("Delete (Person)");
        personFindButton = new JButton("Find (Person)");

        itemAddButton = new JButton("Add (Item)");
        itemUpdateButton = new JButton("Update (Item)");
        itemDeleteButton = new JButton("Delete (Item)");

        itemNameTextField = new JTextField(20);
        findItemButton = new JButton("Find item");

        personUpdateButton.setEnabled(false);
        personDeleteButton.setEnabled(false);
        itemUpdateButton.setEnabled(false);
        itemDeleteButton.setEnabled(false);

        personButtonPanel = new JPanel();
        personButtonPanel.add(personAddButton);
        personButtonPanel.add(personUpdateButton);
        personButtonPanel.add(personDeleteButton);
        personButtonPanel.add(personFindButton);

        itemButtonPanel = new JPanel();
        itemButtonPanel.add(itemAddButton);
        itemButtonPanel.add(itemUpdateButton);
        itemButtonPanel.add(itemDeleteButton);

        itemButtonPanel2 = new JPanel();
        itemButtonPanel2.add(findItemButton);
        itemButtonPanel2.add(itemNameTextField);
    }

    private void getButtonActions() {
        personAddButton.addActionListener(e -> {
            new AddPersonForm(personTable);
        });

        itemAddButton.addActionListener(e -> {
            new AddItemForm(itemTable);
        });

        personUpdateButton.addActionListener(e -> {
            int selectedRow = personTable.getSelectedRow();
            if (selectedRow != -1) {
                new UpdatePersonForm((int)personTable.getValueAt(selectedRow, 0), personTable);
            }
        });

        itemUpdateButton.addActionListener(e -> {
            int selectedRow = itemTable.getSelectedRow();
            if (selectedRow != -1) {
                new UpdateItemForm((int)itemTable.getValueAt(selectedRow, 0), itemTable);
            }
        });

        personDeleteButton.addActionListener(e -> {
            int selectedRow = personTable.getSelectedRow();
            if (selectedRow != -1) {
                int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this person?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    Person person = PersonService.findById((int)personTable.getValueAt(selectedRow, 0));
                    if (ItemService.getItemsForPerson(person).size() > 0) {
                        JOptionPane.showMessageDialog(null,
                                "You can not delete this person. He has items yet",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        PersonService.deleteById(person.getId());
                        loadPeopleData();
                    }
                }
            }
        });

        itemDeleteButton.addActionListener(e -> {
            int selectedRow = itemTable.getSelectedRow();
            if (selectedRow != -1) {
                int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this item?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    ItemService.deleteById((int)itemTable.getValueAt(selectedRow, 0));
                    loadItemsData();
                }
            }
        });

        personFindButton.addActionListener(e -> {
            new FindPersonForm();
        });

        findItemButton.addActionListener(e -> {
            loadItemsByName(itemNameTextField.getText());
        });
    }

    private void getTableListener() {
        personTable.getSelectionModel().addListSelectionListener(e -> {
            boolean rowSelected = personTable.getSelectedRow() != -1;
            personUpdateButton.setEnabled(rowSelected);
            personDeleteButton.setEnabled(rowSelected);
        });

        itemTable.getSelectionModel().addListSelectionListener(e -> {
            boolean rowSelected = itemTable.getSelectedRow() != -1;
            itemUpdateButton.setEnabled(rowSelected);
            itemDeleteButton.setEnabled(rowSelected);
        });
    }

    private void loadPeopleData() {
        DbService.setDataOnTable(personTable, new String[]{},"SELECT * FROM person");
    }

    private void loadItemsData() {
        DbService.setDataOnTable(itemTable, new String[] {"id", "name", "price", "owner"}, """
                SELECT item.id, item.name, item.price, person.name AS owner
                FROM item
                left JOIN person ON item.owner_id = person.id
                """);
    }

    private void loadItemsByName(String name) {
        DbService.setDataOnTableItemsByName(itemTable2, name);
    }
}
