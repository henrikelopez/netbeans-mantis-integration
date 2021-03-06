package eu.doppel_helix.netbeans.mantisintegration.issue;

import biz.futureware.mantisconnect.ObjectRef;
import eu.doppel_helix.netbeans.mantisintegration.repository.MantisRepository;
import eu.doppel_helix.netbeans.mantisintegration.swing.ObjectRefListCellRenderer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

public class AddRelationshipDialog extends javax.swing.JDialog {
    private ActionListener okEnabler = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            okButton.setEnabled(checkValidity());
        }
    };
    
    private DocumentListener okEnabler2 = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            okButton.setEnabled(checkValidity());
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            okButton.setEnabled(checkValidity());
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            okButton.setEnabled(checkValidity());
        }
    };
    
    public AddRelationshipDialog(java.awt.Frame parent, final MantisIssue issue) {
        super(parent, true);
        setLocationByPlatform(true);
        initComponents();
        final MantisRepository mr = issue.getMantisRepository();
        DefaultComboBoxModel<ObjectRef> types = new DefaultComboBoxModel<ObjectRef>(mr.getRelationships());
        types.insertElementAt(null, 0);
        types.setSelectedItem(null);
        typeComboBox.setModel(types);
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddRelationshipDialog.this.dispose();
            }
        });
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!checkValidity()) {
                    return;
                }
                issue.getMantisRepository().getRequestProcessor().submit(new Runnable() {
                    public void run() {
                        try {
                            issue.addRelationship((ObjectRef) typeComboBox.getSelectedItem(), new BigInteger(idTextField.getText()));
                        } catch (Exception ex) {
                            NotifyDescriptor nd = new NotifyDescriptor.Exception(ex,
                                    "Failed add relationship to issue");
                            DialogDisplayer.getDefault().notifyLater(nd);
                        }
                    }
                });
                AddRelationshipDialog.this.dispose();
            }
        });
        okButton.setEnabled(false);
        typeComboBox.addActionListener(okEnabler);
        idTextField.getDocument().addDocumentListener(okEnabler2);
    }

    protected boolean checkValidity() {
        if(typeComboBox.getSelectedItem() == null) {
            return false;
        }
        try {
            BigInteger value = new BigInteger(idTextField.getText());
            if(!(value.compareTo(BigInteger.ZERO) > 0)) {
                return false;
            }
        } catch(NumberFormatException ex) {
            return false;
        }
        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        typeLabel = new javax.swing.JLabel();
        typeComboBox = new javax.swing.JComboBox();
        idLabel = new javax.swing.JLabel();
        idTextField = new javax.swing.JFormattedTextField();
        jPanel1 = new javax.swing.JPanel();
        closeButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(AddRelationshipDialog.class, "AddRelationshipDialog.title")); // NOI18N
        setMinimumSize(new java.awt.Dimension(325, 125));
        setPreferredSize(new java.awt.Dimension(325, 125));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(typeLabel, org.openide.util.NbBundle.getMessage(AddRelationshipDialog.class, "AddRelationshipDialog.typeLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(typeLabel, gridBagConstraints);

        typeComboBox.setRenderer(new ObjectRefListCellRenderer());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(typeComboBox, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(idLabel, org.openide.util.NbBundle.getMessage(AddRelationshipDialog.class, "AddRelationshipDialog.idLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(idLabel, gridBagConstraints);

        idTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        idTextField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        idTextField.setText(org.openide.util.NbBundle.getMessage(AddRelationshipDialog.class, "AddRelationshipDialog.idTextField.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(idTextField, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(closeButton, org.openide.util.NbBundle.getMessage(AddRelationshipDialog.class, "AddRelationshipDialog.closeButton.text")); // NOI18N
        jPanel1.add(closeButton);

        org.openide.awt.Mnemonics.setLocalizedText(okButton, org.openide.util.NbBundle.getMessage(AddRelationshipDialog.class, "AddRelationshipDialog.okButton.text")); // NOI18N
        jPanel1.add(okButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        getContentPane().add(jPanel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(filler1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel idLabel;
    private javax.swing.JFormattedTextField idTextField;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton okButton;
    private javax.swing.JComboBox typeComboBox;
    private javax.swing.JLabel typeLabel;
    // End of variables declaration//GEN-END:variables
}
