package ua.edu.ukma.storeapp;

import javafx.scene.control.ListCell;

public class groupCell extends ListCell<group> {
    @Override
    protected void updateItem(group item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
        }
        else {
            setText(item.getName());
        }
    }
}
