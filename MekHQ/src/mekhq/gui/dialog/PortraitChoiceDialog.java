/*
 * CamoChoiceDialog.java
 *
 * Created on October 1, 2009, 3:10 PM
 */

package mekhq.gui.dialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import megamek.common.Crew;
import megamek.common.util.DirectoryItems;

/**
 *
 * @author  Jay Lawson <jaylawson39 at yahoo.com>
 */
public class PortraitChoiceDialog extends javax.swing.JDialog {

     /**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/**
     * The categorized camo patterns.
     */
    private DirectoryItems portraits;
    private PortraitTableModel portraitModel = new PortraitTableModel();
    private String category;
    private String filename;
    private PortraitTableMouseAdapter portraitMouseAdapter;
    private boolean force = false;


    /** Creates new form CamoChoiceDialog */
    public PortraitChoiceDialog(Frame parent, boolean modal, String category, String file, DirectoryItems portraits) {
        this(parent, modal, category, file, portraits, false);
    }


    /** Creates new form CamoChoiceDialog */
    public PortraitChoiceDialog(java.awt.Frame parent, boolean modal, String category, String file, DirectoryItems portraits, boolean force) {
        super(parent, modal);
        this.category = category;
        filename = file;
        portraitMouseAdapter = new PortraitTableMouseAdapter();
        this.portraits = portraits;
        this.force = force;
        initComponents();
        fillTable((String) comboCategories.getSelectedItem());
        int rowIndex = 0;
        for(int i = 0; i < portraitModel.getRowCount(); i++) {
            if(((String) portraitModel.getValueAt(i, 0)).equals(filename)) {
                rowIndex = i;
                break;
            }
        }
        tablePortrait.setRowSelectionInterval(rowIndex, rowIndex);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        GridBagConstraints gridBagConstraints;

        scrPortrait = new JScrollPane();
        tablePortrait = new JTable();
        comboCategories = new JComboBox<String>();
        btnSelect = new JButton();
        btnCancel = new JButton();
        JPanel portraitPanel = new JPanel();

        ResourceBundle resourceMap = ResourceBundle.getBundle("mekhq.resources.PortraitChoiceDialog");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); // NOI18N
        setTitle(resourceMap.getString("Form.title"));
        portraitPanel.setLayout(new GridBagLayout());

        scrPortrait.setName("jScrollPane1"); // NOI18N

        tablePortrait.setModel(portraitModel);
        tablePortrait.setName("tablePortrait"); // NOI18N
        tablePortrait.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablePortrait.setRowHeight(76);
        tablePortrait.getColumnModel().getColumn(0).setCellRenderer(portraitModel.getRenderer());
        tablePortrait.addMouseListener(portraitMouseAdapter);
        scrPortrait.setViewportView(tablePortrait);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        portraitPanel.add(scrPortrait, gridBagConstraints);

        DefaultComboBoxModel<String> categoryModel = new DefaultComboBoxModel<String>();
        String match = null;
        categoryModel.addElement(Crew.ROOT_PORTRAIT);
        if (portraits != null) {
            Iterator<String> names = portraits.getCategoryNames();
            while (names.hasNext()) {
                String name = names.next();
                if (!"".equals(name)) { //$NON-NLS-1$
                    categoryModel.addElement(name);
                    if(category.equals(name)) {
                        match = name;
                    }
                }
            }
        }
        if(null != match) {
            categoryModel.setSelectedItem(match);
        } else {
            categoryModel.setSelectedItem(Crew.ROOT_PORTRAIT);
        }
        comboCategories.setModel(categoryModel);
        comboCategories.setName("comboCategories"); // NOI18N
        comboCategories.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
                comboCategoriesItemStateChanged(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        portraitPanel.add(comboCategories, gridBagConstraints);

        btnSelect.setText(resourceMap.getString("btnSelect.text")); // NOI18N
        btnSelect.setName("btnSelect"); // NOI18N
        btnSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnSelectActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 0.5;
        portraitPanel.add(btnSelect, gridBagConstraints);

        btnCancel.setText(resourceMap.getString("btnCancel.text")); // NOI18N
        btnCancel.setName("btnCancel"); // NOI18N
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 0.5;
        portraitPanel.add(btnCancel, gridBagConstraints);

        if (force) {
            JTabbedPane tabbedPane = new JTabbedPane();
            JPanel layeredPanel = new JPanel();
            // TODO: Coming Soon!
            tabbedPane.addTab("Single Portrait", portraitPanel);
            tabbedPane.addTab("Layered Portrait", layeredPanel);
        } else {
            getContentPane().add(portraitPanel);
        }

        pack();
    }

	private void btnCancelActionPerformed(ActionEvent evt) {
	    setVisible(false);
	}

	private void btnSelectActionPerformed(ActionEvent evt) {
	    category = portraitModel.getCategory();
	    if(tablePortrait.getSelectedRow() != -1) {
	        filename = (String) portraitModel.getValueAt(tablePortrait.getSelectedRow(), 0);
	    } else {
	        filename = Crew.PORTRAIT_NONE;
	    }
	    setVisible(false);
	}

	private void comboCategoriesItemStateChanged(ItemEvent evt) {
	    if (evt.getStateChange() == ItemEvent.SELECTED) {
	        fillTable((String) evt.getItem());
	    }
	}//GEN-LAST:event_comboCategoriesItemStateChanged

    public String getCategory() {
        return category;
    }

    public String getFileName() {
        return filename;
    }

     private void fillTable(String category) {
        portraitModel.reset();
        portraitModel.setCategory(category);
        // Translate the "root camo" category name.
        Iterator<String> portraitNames;
        if (Crew.ROOT_PORTRAIT.equals(category)) {
            portraitModel.addPortrait(Crew.PORTRAIT_NONE);
            portraitNames = portraits.getItemNames(""); //$NON-NLS-1$
        } else {
            portraitNames = portraits.getItemNames(category);
        }

        // Get the camo names for this category.
        while (portraitNames.hasNext()) {
                portraitModel.addPortrait(portraitNames.next());
        }
        if(portraitModel.getRowCount() > 0) {
            tablePortrait.setRowSelectionInterval(0, 0);
        }
    }

     /**
        * A table model for displaying camos
     */
    public class PortraitTableModel extends AbstractTableModel {

        /**
		 *
		 */
		private static final long serialVersionUID = 1L;
		private String[] columnNames;
        private String category;
        private ArrayList<String> names;
        private ArrayList<Image> images;

        public PortraitTableModel() {
            columnNames = new String[] {"Portraits"};
            category = Crew.ROOT_PORTRAIT;
            names = new ArrayList<String>();
            images = new ArrayList<Image>();
        }

        public int getRowCount() {
            return names.size();
        }

        public int getColumnCount() {
            return 1;
        }

        public void reset() {
            category = Crew.ROOT_PORTRAIT;
            names = new ArrayList<String>();
            images = new ArrayList<Image>();
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        public Object getValueAt(int row, int col) {
            return names.get(row);
        }

        public Object getImageAt(int row) {
            return images.get(row);
        }

        public void setCategory(String c) {
            category = c;
        }

        public String getCategory() {
            return category;
        }

        public void addPortrait(String name) {
            names.add(name);
            fireTableDataChanged();
        }

        @Override
        public Class<? extends Object> getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }

        public PortraitTableModel.Renderer getRenderer() {
            return new PortraitTableModel.Renderer(portraits);
        }


        public class Renderer extends PortraitPanel implements TableCellRenderer {

        	public Renderer(DirectoryItems portraits) {
				super(portraits);
			}

			private static final long serialVersionUID = -6025788865509594987L;

			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = this;
                setOpaque(true);
                String name = getValueAt(row, column).toString();
                setText(getValueAt(row, column).toString());
                setImage(category, name);
                if(isSelected) {
                    setBackground(new Color(220,220,220));
                } else {
                    setBackground(Color.WHITE);
                }

                return c;
            }
       }
    }

    public class PortraitTableMouseAdapter extends MouseInputAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {

            if (e.getClickCount() == 2) {
                int row = tablePortrait.rowAtPoint(e.getPoint());
                if(row < portraitModel.getRowCount()) {
                    category = portraitModel.getCategory();
                    filename = (String) portraitModel.getValueAt(row, 0);
                    setVisible(false);
                }
            }
        }
    }

    public class PortraitPanel extends JPanel {

        /**
    	 *
    	 */
    	private static final long serialVersionUID = -3724175393116586310L;
    	private DirectoryItems portraits;

        /** Creates new form CamoPanel */
        public PortraitPanel(DirectoryItems portraits) {
            this.portraits = portraits;
            initComponents();
        }

        private void initComponents() {
            GridBagConstraints gridBagConstraints;

            lblImage = new JLabel();

            setName("Form"); // NOI18N
            setLayout(new GridBagLayout());

            lblImage.setText(""); // NOI18N
            lblImage.setName("lblImage"); // NOI18N
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 1.0;
            add(lblImage, gridBagConstraints);
        }// </editor-fold>//GEN-END:initComponents

        public void setText(String text) {
            lblImage.setText(text);
        }

        public void setImage(String category, String name) {

            if (null == category
                    || name.equals(Crew.PORTRAIT_NONE)) {
                return;
            }

            // Try to get the portrait file.
            try {
                // Translate the root portrait directory name.
                if (Crew.ROOT_PORTRAIT.equals(category))
                    category = ""; //$NON-NLS-1$
                Image portrait = (Image) portraits.getItem(category, name);
                if(null != portrait) {
                    portrait = portrait.getScaledInstance(-1, 76, Image.SCALE_DEFAULT);
                }
                lblImage.setIcon(new ImageIcon(portrait));
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
        // Variables declaration - do not modify//GEN-BEGIN:variables
        private JLabel lblImage;
        // End of variables declaration//GEN-END:variables

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton btnCancel;
    private JButton btnSelect;
    private JComboBox<String> comboCategories;
    private JScrollPane scrPortrait;
    private JTable tablePortrait;
    // End of variables declaration//GEN-END:variables

}
