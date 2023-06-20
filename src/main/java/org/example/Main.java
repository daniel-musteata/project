package org.example;

import org.example.frontend.AppUi;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(AppUi::new);
    }
}