package shippingstore.shippingstore.gui;

import shippingstore.*;
import shippingstore.Box;
import shippingstore.Package;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class ShowPackages extends JFrame{
    JTable packagesTable;
    String[] columnNames = {"Tracking #", "Package Type", "Specification", "Mailing Class", "Other Details"};
    ArrayList<Package> packages;

    ShippingStore ss;

    ShowPackages() {
        setSize(1400, 500);
        setTitle("Package List");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        ss = new ShippingStore().readDatabase();
        packages = (ArrayList) ss.getPackages();

        packagesTable = new JTable();
        packagesTable.setModel(new DefaultTableModel(new Object[][] {}, columnNames));

        addRowToJTable();

        add(new JScrollPane(packagesTable));

        setVisible(true);
    }

    public void addRowToJTable() {

        DefaultTableModel tableModel = (DefaultTableModel) packagesTable.getModel();
        Object rowData[] = new Object[5];

            for (Package p: packages) {
                rowData[0] = p.getPtn();
                rowData[1] = splitFormattedText(p.getFormattedText(), 0);
                rowData[2] = p.getSpecification();
                rowData[3] = p.getMailingClass();
                rowData[4] = getOtherPackageInfo(p);

                tableModel.addRow(rowData);
            }
    }

    public String splitFormattedText(String line, int index) {
        String temp[] = line.trim().split(" ");

        return temp[index];
    }

    public String getOtherPackageInfo(Package p) {
        if (p instanceof Crate) {
            return "Load Weight: " + ((Crate) p).getLoadWeight() + ", Content: " + ((Crate) p).getContent();

        }else if (p instanceof Box) {
            return "Dimension: " + ((Box) p).getDimension() +  ", Volume: " + ((Box) p).getVolume();

        } else if (p instanceof Drum) {
            return "Material: " + ((Drum) p).getMaterial() + ", Diameter: " + ((Drum) p).getDiameter();

        } else if (p instanceof Envelope) {
            return "Height: "+ ((Envelope) p).getHeight() + ", Width: " + ((Envelope) p).getWidth();

        } else {
            return "";
        }
    }
}


//   String temp[] = line.trim().split(" ");

//        switch (temp[0]) {
//            case "Envelope":
//                return "Height: " + temp[4] + ", Width: " + temp[5];
//            case "Box":
//                return "Dimension: " + temp[4] + ", Volume: " + temp[5];
//            case "Crate":
//                return "Load Weight: " + temp[4] + ", Content: " + temp[5];
//            case "Drum":
//                return "Material: " + temp[4] + ", Diameter: " + temp[5];
//        }
