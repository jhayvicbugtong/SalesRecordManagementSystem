
package week1;

import com.mysql.cj.jdbc.result.ResultSetMetaData;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class staffPanel extends javax.swing.JFrame {
    private int currentUserId;
    /**
     * Creates new form staffPanel
     */
    public staffPanel(int id) {
        currentUserId = id;
        initComponents();
        setInfo(id);
        displayProductStockWarning();
    }

    void setInfo(int userID) {
        String url = "jdbc:mysql://localhost:3306/srm_db";
        String user = "root";
        String pass = "";
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, pass);

            String sql = "SELECT u.name, d.DepartmentName FROM user u "
                    + "JOIN department d ON u.DepartmentID = d.DepartmentID WHERE user_id = ?;";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, userID);
            rs = pst.executeQuery();
            if (rs.next()) {
                String Name = rs.getString("name");
                String departmentName = rs.getString("DepartmentName");
                jLabel5.setText(Name);
                jLabel4.setText(departmentName + " Department");
            } else {
                JOptionPane.showMessageDialog(this, "Department not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            String weeklysql = """
                         SELECT SUM(s.quantity) AS total_sold
                         FROM sales s
                         JOIN product p ON s.product_id = p.product_id
                         JOIN department d ON p.DepartmentID = d.DepartmentID
                         WHERE WEEK(s.sales_date) = WEEK(CURDATE())
                         AND YEAR(s.sales_date) = YEAR(CURDATE())
                         AND s.user_id = ?
                         GROUP BY d.DepartmentName;""";
            pst = conn.prepareStatement(weeklysql);
            pst.setInt(1, userID);
            rs = pst.executeQuery();
            if (rs.next()) {
                String Name = rs.getString("total_sold");
                jTextField3.setText(Name);
            }else{
                jTextField3.setText("0");
            }
            
            String dailySql = "SELECT SUM(s.quantity) AS total_sold " +
                  "FROM sales s " +
                  "JOIN product p ON s.product_id = p.product_id " +
                  "JOIN department d ON p.DepartmentID = d.DepartmentID " +
                  "WHERE DATE(s.sales_date) = CURDATE() AND s.user_id = ? " +
                  "GROUP BY d.DepartmentName";

            pst = conn.prepareStatement(dailySql);
            pst.setInt(1, userID);
            rs = pst.executeQuery();
            if (rs.next()) {
                String Name = rs.getString("total_sold");
                jTextField2.setText(Name);
            }else{
                jTextField2.setText("0");
            }
            
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void displayProductStockWarning() {
        String DB_URL = "jdbc:mysql://localhost:3306/srm_db";
        String DB_USER = "root";
        String DB_PASSWORD = "";

        String query = "SELECT CONCAT('Warning: The product ', p.product_name, ' has only ', p.stock, ' units remaining.') AS Product_Stock_Warning " +
                       "FROM product p " +
                       "JOIN notification n ON n.product_id = p.product_id " +
                       "WHERE n.notification_date >= DATE_SUB(CURDATE(), INTERVAL 3 DAY);";  // Filter by last 3 days

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            StringBuilder notificationMessage = new StringBuilder();

            while (rs.next()) {
                notificationMessage.append(rs.getString("Product_Stock_Warning")).append("\n");
            }

            jTextArea1.setText(notificationMessage.toString());

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching product stock warnings: " + e.getMessage(), 
                                          "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        staffSalesTable = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        reloadButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        salesrecordTable = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        productId = new javax.swing.JTextField();
        quantities = new javax.swing.JTextField();
        stocks = new javax.swing.JTextField();
        addButton = new javax.swing.JButton();
        productName1 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(125, 125, 125));

        jTabbedPane1.setBackground(new java.awt.Color(173, 173, 173));

        jPanel2.setBackground(new java.awt.Color(173, 173, 173));

        staffSalesTable.setAutoCreateRowSorter(true);
        staffSalesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Product Name", "Quantity", "Sales Date", "Total Sales"
            }
        ));
        fetchAndDisplaySalesRecords();
        jScrollPane1.setViewportView(staffSalesTable);

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jLabel2.setText("Total Daily Sale/s:");

        jTextField2.setEditable(false);
        jTextField2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 24)); // NOI18N
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jTextField3.setEditable(false);
        jTextField3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 24)); // NOI18N
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jLabel3.setText("Total Weekly Sales:");

        jLabel4.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jLabel4.setText("Department");

        jLabel5.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jLabel5.setText("Placeholder");

        reloadButton.setText("RELOAD");
        reloadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reloadButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jTextField1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 174, Short.MAX_VALUE)
                                .addComponent(reloadButton))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(11, 11, 11)))
                        .addGap(14, 14, 14)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(reloadButton))
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        jTabbedPane1.addTab("Performance Overview", jPanel2);

        jPanel3.setBackground(new java.awt.Color(173, 173, 173));

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(7);
        jTextArea1.setBorder(null);
        jScrollPane2.setViewportView(jTextArea1);
        jTextArea1.setVisible(false);

        salesrecordTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Product Name", "Quantity", "Date", "Total Sales Amount"
            }
        ));
        fetchAndDisplayDailySalesRecords();
        jScrollPane3.setViewportView(salesrecordTable);

        jLabel6.setText("Product ID:");

        jLabel8.setText("Stocks Left:");

        jLabel9.setText("Quantity:");

        productId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                productIdActionPerformed(evt);
            }
        });
        productId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                productIdKeyReleased(evt);
            }
        });

        quantities.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quantitiesActionPerformed(evt);
            }
        });

        stocks.setEditable(false);
        stocks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stocksActionPerformed(evt);
            }
        });

        addButton.setText("ADD");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        productName1.setEditable(false);
        productName1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                productName1ActionPerformed(evt);
            }
        });

        jLabel10.setText("Product Name:");

        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/dio.jpg"))); // NOI18N
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(productId, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(31, 31, 31)
                                        .addComponent(stocks, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(productName1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(quantities, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(15, 15, 15)
                        .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 745, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(productId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addButton))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(productName1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(quantities, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(stocks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addGap(25, 25, 25))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );

        jTabbedPane1.addTab("Add Sales Record", jPanel3);

        jLabel1.setFont(new java.awt.Font("Segoe UI Semibold", 3, 24)); // NOI18N
        jLabel1.setText("SALESTAFF DASHBOARD");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 442, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed
    
   private void fetchAndDisplaySalesRecords() {
    // Database connection parameters
    String url = "jdbc:mysql://localhost:3306/srm_db";
    String user = "root";
    String pass = "";

    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(url, user, pass);

        // SQL query to fetch sales records for the current user
        String fetchSalesSql = "SELECT p.product_name AS 'Product Name', " +
                   "s.quantity AS 'Quantity', " +
                   "s.sales_date AS 'Date', " +
                   "s.total_price AS 'Total Sales Amount' " +
                   "FROM sales s " +
                   "JOIN product p ON s.product_id = p.product_id " +
                   "WHERE s.user_id = ?";
        pst = conn.prepareStatement(fetchSalesSql);
        pst.setInt(1, currentUserId); // Set the current user ID

        rs = pst.executeQuery();

        DefaultTableModel model = (DefaultTableModel) staffSalesTable.getModel();
        model.setRowCount(0); // Clear the existing rows

        // Populate the JTable with fetched data
        while (rs.next()) {
            String productName = rs.getString("Product Name");
            int quantity = rs.getInt("Quantity");
            String salesDate = rs.getString("Date");
            double totalPrice = rs.getDouble("Total Sales Amount");

            // Add row to the table model
            model.addRow(new Object[]{productName, quantity, salesDate, totalPrice});
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Driver not found: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } finally {
        try {
            if (rs != null) rs.close();
            if (pst != null) pst.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
   private void fetchAndDisplayDailySalesRecords() {
    // Database connection parameters
    String url = "jdbc:mysql://localhost:3306/srm_db";
    String user = "root";
    String pass = "";

    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(url, user, pass);

        // SQL query to fetch sales records for the current user
        String fetchDSalesSql = "SELECT p.product_name AS 'Product Name', " +
                       "s.quantity AS 'Quantity', " +
                       "s.sales_date AS 'Date', " +
                       "s.total_price AS 'Total Sales Amount' " +
                       "FROM sales s " +
                       "JOIN product p ON s.product_id = p.product_id " +
                       "WHERE s.user_id = ? AND s.sales_date = CURDATE()";
        pst = conn.prepareStatement(fetchDSalesSql);
        pst.setInt(1, currentUserId); // Set the current user ID

        rs = pst.executeQuery();

        DefaultTableModel model = (DefaultTableModel) salesrecordTable.getModel();
        model.setRowCount(0); // Clear the existing rows

        // Populate the JTable with fetched data
        while (rs.next()) {
            String productName = rs.getString("Product Name");
            int quantity = rs.getInt("Quantity");
            String salesDate = rs.getString("Date");
            double totalPrice = rs.getDouble("Total Sales Amount");

            // Add row to the table model
            model.addRow(new Object[]{productName, quantity, salesDate, totalPrice});
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Driver not found: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } finally {
        try {
            if (rs != null) rs.close();
            if (pst != null) pst.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   }
    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed

    String product = productId.getText();
    String name = productName1.getText();
    String quantity = quantities.getText();
    String Stocks = stocks.getText();
    
    if (product.isEmpty() || name.isEmpty() || quantity.isEmpty()) {
        JOptionPane.showMessageDialog(this, "All fields must be filled out.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    String url = "jdbc:mysql://localhost:3306/srm_db"; // Ensure database name is correct
    String user = "root";
    String pass = "";
    int stock = Integer.parseInt(Stocks);
    int price = getPrice(Integer.parseInt(product));
    int quant = Integer.parseInt(quantity);
    int total = quant * price;
    if(quant > stock){
        JOptionPane.showMessageDialog(this, "Not enough stocks", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    if(quant < 0){
        JOptionPane.showMessageDialog(this, "Enter appropiate quantity", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    Connection conn = null;
    PreparedStatement pst = null;
    String query = "SELECT * FROM user u JOIN product p ON p.DepartmentID = u.DepartmentID "
             + "WHERE u.user_id = ? AND p.product_id = ?;";
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(url, user, pass);
        pst = conn.prepareStatement(query);
        pst.setInt(1, currentUserId);
        pst.setInt(2, Integer.parseInt(product));

        ResultSet rs = pst.executeQuery();

        if (!rs.next()) {
            JOptionPane.showMessageDialog(this, "Not in your Department.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String insertSalesSql = "INSERT INTO sales (user_id, product_id, quantity, sales_date, total_price) VALUES (?, ?, ?, CURDATE(), ?)";
        pst = conn.prepareStatement(insertSalesSql);

        pst.setInt(1, currentUserId); 
        pst.setInt(2, Integer.parseInt(product));
        pst.setInt(3, quant);
        pst.setInt(4, total);

        int rowsInserted = pst.executeUpdate();
        
        String insertProductSql;
        insertProductSql = "UPDATE product SET stock = stock - ? WHERE product_id = ?"; 
        pst = conn.prepareStatement(insertProductSql);
        pst.setInt(2, Integer.parseInt(product));
        pst.setInt(1, quant); 
        pst.executeUpdate();

        if (rowsInserted > 0) {
            quantities.setText("");
            productId.setText("");
            productName1.setText("");
            stocks.setText("");
            JOptionPane.showMessageDialog(this, "Sales record added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            fetchAndDisplayDailySalesRecords();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add sales record.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Driver not found: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } finally {
        try {
            if (pst != null) pst.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    }//GEN-LAST:event_addButtonActionPerformed

    public int getPrice(int productId) {
        String DB_URL = "jdbc:mysql://localhost:3306/srm_db";
        String DB_USER = "root";
        String DB_PASSWORD = "";
        String query = "SELECT price FROM product WHERE product_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setInt(1, productId);  // Set the productId parameter

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("price");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;  // Return -1 if no price was found (or handle it as needed)
    }

   
    private void quantitiesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quantitiesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_quantitiesActionPerformed

    private void productIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_productIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_productIdActionPerformed

    private void productName1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_productName1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_productName1ActionPerformed

    private void productIdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_productIdKeyReleased
        
        String product = productId.getText();
        if(product.isEmpty()){
            productName1.setText("");
            stocks.setText("");
            return;
        }
        String url = "jdbc:mysql://localhost:3306/srm_db";
        String user = "root";
        String pass = "";
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, pass);

            String searchSql = "SELECT product_name FROM product WHERE product_id = ?";
            pst = conn.prepareStatement(searchSql);
            pst.setInt(1, Integer.parseInt(product));
            
            rs = pst.executeQuery();
            if (rs.next()) {
                String stock = rs.getString("product_name");
                productName1.setText(stock);
            }
            
            String productstock = "SELECT stock FROM product WHERE product_id = ?";
            pst = conn.prepareStatement(productstock);
            pst.setInt(1, Integer.parseInt(product));

            rs = pst.executeQuery();

            if (rs.next()) {
                String stock = rs.getString("stock");
                stocks.setText(stock);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_productIdKeyReleased

    private void reloadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reloadButtonActionPerformed
              fetchAndDisplaySalesRecords();
    }//GEN-LAST:event_reloadButtonActionPerformed

    private void stocksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stocksActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_stocksActionPerformed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        searchSalesRecords(jTextField1.getText().trim());
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
                if(jToggleButton1.isSelected()){
            jTextArea1.setVisible(true);}
        else{
            jTextArea1.setVisible(false);
        }
    }//GEN-LAST:event_jToggleButton1ActionPerformed
    private void searchSalesRecords(String keyword) {
        String fetchSalesSql = "SELECT p.product_name AS 'Product Name', " +
                               "s.quantity AS 'Quantity', " +
                               "s.sales_date AS 'Date', " +
                               "s.total_price AS 'Total Sales Amount' " +
                               "FROM sales s " +
                               "JOIN product p ON s.product_id = p.product_id " +
                               "WHERE s.user_id = ? AND (p.product_name LIKE ? OR s.sales_date LIKE ?)";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/srm_db", "root", "")) {
            PreparedStatement pst = conn.prepareStatement(fetchSalesSql);
            pst.setInt(1, currentUserId);

            String searchParam = "%" + keyword + "%";
            pst.setString(2, searchParam);
            pst.setString(3, searchParam);

            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = (DefaultTableModel) staffSalesTable.getModel();
            model.setRowCount(0);
            while (rs.next()) {
                String productName = rs.getString("Product Name");
                int quantity = rs.getInt("Quantity");
                String salesDate = rs.getString("Date");
                double totalPrice = rs.getDouble("Total Sales Amount");

                model.addRow(new Object[]{productName, quantity, salesDate, totalPrice});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching sales records: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(staffPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(staffPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(staffPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(staffPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new staffPanel(4).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JTextField productId;
    private javax.swing.JTextField productName1;
    private javax.swing.JTextField quantities;
    private javax.swing.JButton reloadButton;
    private javax.swing.JTable salesrecordTable;
    private javax.swing.JTable staffSalesTable;
    private javax.swing.JTextField stocks;
    // End of variables declaration//GEN-END:variables
}
