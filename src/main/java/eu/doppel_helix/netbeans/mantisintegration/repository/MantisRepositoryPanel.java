package eu.doppel_helix.netbeans.mantisintegration.repository;

import java.awt.TextComponent;
import javax.swing.text.JTextComponent;

public class MantisRepositoryPanel extends javax.swing.JPanel {

    /**
     * Creates new form MantisRepositoryPanel
     */
    public MantisRepositoryPanel() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nameLabel = new javax.swing.JLabel();
        urlLabel = new javax.swing.JLabel();
        usernameLabel = new javax.swing.JLabel();
        passwordLabel = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        urlTextField = new javax.swing.JTextField();
        usernameTextField = new javax.swing.JTextField();
        checkResult = new javax.swing.JLabel();
        checkButton = new javax.swing.JButton();
        passwordTextField = new javax.swing.JPasswordField();

        nameLabel.setText("Name:");

        urlLabel.setText("Mantis-URL:");

        usernameLabel.setText("Username:");

        passwordLabel.setText("Password:");

        nameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                selectAllOnFocusGain(evt);
            }
        });

        urlTextField.setText("http://<host>/<mantis-basepath>");
        urlTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                selectAllOnFocusGain(evt);
            }
        });

        usernameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                selectAllOnFocusGain(evt);
            }
        });

        checkResult.setFont(checkResult.getFont().deriveFont(checkResult.getFont().getStyle() & ~java.awt.Font.BOLD));

        checkButton.setText("Check");
        checkButton.setEnabled(false);

        passwordTextField.setText("jPasswordField1");
        passwordTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                selectAllOnFocusGain(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(urlLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nameLabel)
                    .addComponent(usernameLabel)
                    .addComponent(passwordLabel)
                    .addComponent(checkButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(urlTextField)
                    .addComponent(usernameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
                    .addComponent(nameTextField)
                    .addComponent(checkResult, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(passwordTextField))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(urlLabel)
                    .addComponent(urlTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameLabel)
                    .addComponent(usernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordLabel)
                    .addComponent(passwordTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(checkButton)
                    .addComponent(checkResult))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void selectAllOnFocusGain(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_selectAllOnFocusGain
        if (evt.getComponent() instanceof JTextComponent) {
            ((JTextComponent) evt.getComponent()).selectAll();
        } else if (evt.getComponent() instanceof TextComponent) {
            ((TextComponent) evt.getComponent()).selectAll();
        }
    }//GEN-LAST:event_selectAllOnFocusGain
    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JButton checkButton;
    javax.swing.JLabel checkResult;
    javax.swing.JLabel nameLabel;
    javax.swing.JTextField nameTextField;
    javax.swing.JLabel passwordLabel;
    javax.swing.JPasswordField passwordTextField;
    javax.swing.JLabel urlLabel;
    javax.swing.JTextField urlTextField;
    javax.swing.JLabel usernameLabel;
    javax.swing.JTextField usernameTextField;
    // End of variables declaration//GEN-END:variables
}
