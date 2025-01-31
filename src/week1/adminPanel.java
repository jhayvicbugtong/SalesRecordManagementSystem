/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package week1;

import com.raven.chart.ModelChart;
import java.awt.Color;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javaswingdev.chart.ModelPieChart;
import javaswingdev.chart.PieChart;
import javax.swing.JOptionPane;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author USER
 */
public class adminPanel extends javax.swing.JFrame {

    /**
     * Creates new form adminPanel
     * @throws java.lang.ClassNotFoundException
     */
    public adminPanel() throws ClassNotFoundException {
        initComponents();
        chart();
        pieChartOne();
        pieChartTwo();
    }

    final void pieChartOne() throws ClassNotFoundException {
        List<SaleData> dailySalesData = fetchDailySalesDataFromDatabase();
        Map<String, Integer> departmentSales = new HashMap<>();

        for (SaleData sale : dailySalesData) {
            String departmentName = sale.getDepartmentName();
            int quantitySold = sale.getQuantity();
            departmentSales.put(departmentName, departmentSales.getOrDefault(departmentName, 0) + quantitySold);
        }

        for (Map.Entry<String, Integer> entry : departmentSales.entrySet()) {
            String departmentName = entry.getKey();
            int quantitySold = entry.getValue();
            Color color;

            switch (departmentName) {
                case "Dry Goods":
                    color = new Color(23, 126, 238);
                    break;
                case "Home Improvement":
                    color = new Color(221, 65, 65);
                    break;
                case "Grocery":
                    color = new Color(47, 157, 64);
                    break;
                default:
                    color = new Color(196, 151, 58);
                    break;
            }

            DailyPieChart.addData(new ModelPieChart(departmentName, quantitySold, color));
        }

        DailyPieChart.setChartType(PieChart.PeiChartType.DONUT_CHART);
    }

    private List<SaleData> fetchDailySalesDataFromDatabase() throws ClassNotFoundException {
        List<SaleData> dailySalesData = new ArrayList<>();
        String sql = "SELECT s.sales_date, s.quantity, d.DepartmentName " +
                     "FROM sales s " +
                     "JOIN product p ON s.product_id = p.product_id " +
                     "JOIN department d ON p.DepartmentID = d.DepartmentID " +
                     "WHERE DATE(s.sales_date) = CURDATE()";
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String url = "jdbc:mysql://localhost:3306/srm_db";
        String user = "root";
        String pass = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, pass);
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                Date salesDate = rs.getDate("sales_date");
                int quantity = rs.getInt("quantity");
                String departmentName = rs.getString("DepartmentName");
                dailySalesData.add(new SaleData(salesDate, quantity, departmentName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return dailySalesData;
    }

    
    final void pieChartTwo() throws ClassNotFoundException {
        List<SaleData> weeklySalesData = fetchWeeklySalesDataFromDatabase();
        Map<String, Integer> departmentSales = new HashMap<>();

        for (SaleData sale : weeklySalesData) {
            String departmentName = sale.getDepartmentName();
            int quantitySold = sale.getQuantity();
            departmentSales.put(departmentName, departmentSales.getOrDefault(departmentName, 0) + quantitySold);
        }

        for (Map.Entry<String, Integer> entry : departmentSales.entrySet()) {
            String departmentName = entry.getKey();
            int quantitySold = entry.getValue();
            Color color;
            switch (departmentName) {
                case "Dry Goods":
                    color = new Color(23, 126, 238);
                    break;
                case "Home Improvement":
                    color = new Color(221, 65, 65);
                    break;
                case "Grocery":
                    color = new Color(47, 157, 64);
                    break;
                default:
                    continue;
            }
            WeeklyPieChart.addData(new ModelPieChart(departmentName, quantitySold, color));
        }

        WeeklyPieChart.setChartType(PieChart.PeiChartType.DONUT_CHART);
    }
    
    private List<SaleData> fetchWeeklySalesDataFromDatabase() throws ClassNotFoundException {
        List<SaleData> weeklySalesData = new ArrayList<>();
        String sql = "SELECT s.sales_date, s.quantity, d.DepartmentName " +
                     "FROM sales s " +
                     "JOIN product p ON s.product_id = p.product_id " +
                     "JOIN department d ON p.DepartmentID = d.DepartmentID " +
                     "WHERE WEEK(s.sales_date) = WEEK(CURDATE()) AND YEAR(s.sales_date) = YEAR(CURDATE())";
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String url = "jdbc:mysql://localhost:3306/srm_db";
        String user = "root";
        String pass = "";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, pass);
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                Date salesDate = rs.getDate("sales_date");
                int quantity = rs.getInt("quantity");
                String departmentName = rs.getString("DepartmentName");
                weeklySalesData.add(new SaleData(salesDate, quantity, departmentName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return weeklySalesData;
    }


    private void chart() throws ClassNotFoundException {
        List<SaleData> salesData = fetchSalesDataFromDatabase();
        Map<String, double[]> monthlyData = new HashMap<>(); // Map to store monthly data

        // Populate the data for each month
        for (SaleData sale : salesData) {
            String month = getMonthFromDate(sale.getSalesDate());
            String departmentName = sale.getDepartmentName();

            // Initialize the monthly data if not already present
            if (!monthlyData.containsKey(month)) {
                monthlyData.put(month, new double[4]); // Assuming 4 departments: Dry Goods, Home Improvement, Grocery, All
            }

            double[] data = monthlyData.get(month);

            // Based on the department, increment the corresponding value
            if ("Dry Goods".equals(departmentName)) {
                data[0] += sale.getQuantity();
            } else if ("Home Improvement".equals(departmentName)) {
                data[1] += sale.getQuantity();
            } else if ("Grocery".equals(departmentName)) {
                data[2] += sale.getQuantity();
            }

            // Add the total quantity sold to "All" category
            data[3] += sale.getQuantity();
        }

        // Add data for each month to the chart
        for (Map.Entry<String, double[]> entry : monthlyData.entrySet()) {
            String month = entry.getKey();
            double[] data = entry.getValue();
            chart.addData(new ModelChart(month, data)); // Using the month as the label
        }

        // Add legends for departments
        chart.addLegend("Dry Goods", new Color(245, 189, 135));
        chart.addLegend("Home Improvement", new Color(135, 189, 245));
        chart.addLegend("Grocery", new Color(189, 135, 245));
        chart.addLegend("All", new Color(139, 229, 222));
    }

    private String getMonthFromDate(Date salesDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM");  // Format as full month name (e.g., January)
        return sdf.format(salesDate);
    }

    class SaleData {
        private Date salesDate;
        private int quantity;
        private String departmentName;

        public SaleData(Date salesDate, int quantity, String departmentName) {
            this.salesDate = salesDate;
            this.quantity = quantity;
            this.departmentName = departmentName;
        }

        public Date getSalesDate() {
            return salesDate;
        }

        public int getQuantity() {
            return quantity;
        }

        public String getDepartmentName() {
            return departmentName;
        }
    }

    private List<SaleData> fetchSalesDataFromDatabase() throws ClassNotFoundException {
        List<SaleData> salesData = new ArrayList<>();
        String sql = "SELECT s.sales_date, s.quantity, d.DepartmentName " +
                     "FROM sales s " +
                     "JOIN product p ON s.product_id = p.product_id " +
                     "JOIN department d ON p.DepartmentID = d.DepartmentID";
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String url = "jdbc:mysql://localhost:3306/srm_db";
        String user = "root";
        String pass = "";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, pass);
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                Date salesDate = rs.getDate("sales_date");
                int quantity = rs.getInt("quantity");
                String departmentName = rs.getString("DepartmentName");
                salesData.add(new SaleData(salesDate, quantity, departmentName));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return salesData;
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField4 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        chart = new com.raven.chart.Chart();
        WeeklyPieChart = new javaswingdev.chart.PieChart();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        DailyPieChart = new javaswingdev.chart.PieChart();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        dailyRev = new javax.swing.JTextField();
        weeklyRev = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        totalRev = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        topGrocery = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        topDryG = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        topHomeImp = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtPass = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        yearSpinner = new javax.swing.JSpinner(new SpinnerNumberModel(2000, 1950, 2025, 1));
        monthSpinner = new javax.swing.JSpinner(new SpinnerNumberModel(01, 01, 12, 1));
        daySpinner = new javax.swing.JSpinner(new SpinnerNumberModel(1,1,31,1));
        jLabel11 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        addEmployee = new javax.swing.JButton();
        roleBox = new javax.swing.JComboBox<>();
        deptBox = new javax.swing.JComboBox<>();
        jLabel25 = new javax.swing.JLabel();
        txtPersonnel = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        inventoryTable = new javax.swing.JTable();
        jLabel19 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        txtProductName = new javax.swing.JTextField();
        txtStock = new javax.swing.JTextField();
        txtPrice = new javax.swing.JTextField();
        categoryBox = new javax.swing.JComboBox<>();
        addButton = new javax.swing.JButton();
        confrimEdit = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        txtProductID = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        jTextField4.setText("jTextField4");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationByPlatform(true);

        WeeklyPieChart.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jLabel2.setText("WEEKLY SALES:");

        jLabel3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jLabel3.setText("DAILY SALES:");

        DailyPieChart.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel4.setText("WEEKLY REVENUE:");

        jLabel5.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel5.setText("DAILY REVENUE:");

        dailyRev.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N

        weeklyRev.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        weeklyRev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                weeklyRevActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jLabel6.setText("TOTAL REVENUE:");

        totalRev.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        totalRev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalRevActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI Semibold", 0, 20)); // NOI18N
        jLabel7.setText("TOP EMPLOYEE ");

        jLabel8.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel8.setText("PER DEPARTMENT");

        topGrocery.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        topGrocery.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        topGrocery.setText("NAME PLACEHOLDER");

        jLabel10.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("GROCERY");

        topDryG.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        topDryG.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        topDryG.setText("DRY GOODS");

        jLabel12.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("NAME PLACEHOLDER");

        jLabel13.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("NAME PLACEHOLDER");

        topHomeImp.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        topHomeImp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        topHomeImp.setText("HOME IMPROVEMENT");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chart, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(dailyRev, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(37, 37, 37)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(weeklyRev))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 159, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(totalRev, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(WeeklyPieChart, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                        .addComponent(DailyPieChart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(jLabel8)))
                        .addGap(38, 38, 38))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(topGrocery, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(topDryG, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(topHomeImp, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addGap(24, 24, 24))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(chart, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(weeklyRev)
                                    .addComponent(dailyRev)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(totalRev, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(WeeklyPieChart, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(DailyPieChart, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(topGrocery)
                        .addGap(3, 3, 3)
                        .addComponent(jLabel10)
                        .addGap(37, 37, 37)
                        .addComponent(jLabel12)
                        .addGap(3, 3, 3)
                        .addComponent(topDryG)
                        .addGap(33, 33, 33)
                        .addComponent(jLabel13)
                        .addGap(3, 3, 3)
                        .addComponent(topHomeImp)))
                .addContainerGap(89, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Performance Overview", jPanel2);

        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel9.setText("ADD EMPLOYEE:");
        jPanel5.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 80, 210, 31));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setText("Birthdate:");
        jPanel5.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 360, -1, -1));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel18.setText("Personnell ID:");
        jPanel5.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 160, -1, -1));

        txtPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPassActionPerformed(evt);
            }
        });
        jPanel5.add(txtPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 320, 150, 30));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel20.setText("Department:");
        jPanel5.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 210, -1, -1));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setText("Password:");
        jPanel5.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 320, -1, -1));
        jPanel5.add(yearSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 360, -1, -1));

        monthSpinner.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                monthSpinnerPropertyChange(evt);
            }
        });
        jPanel5.add(monthSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 360, -1, -1));
        jPanel5.add(daySpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 360, -1, -1));

        jLabel11.setText("Day");
        jPanel5.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 390, 40, -1));

        jLabel16.setText("Year");
        jPanel5.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 390, 30, -1));

        jLabel17.setText("Month");
        jPanel5.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 390, 40, -1));

        addEmployee.setText("ADD");
        addEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addEmployeeActionPerformed(evt);
            }
        });
        jPanel5.add(addEmployee, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 420, -1, -1));

        roleBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Admin", "Sale Staff" }));
        roleBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roleBoxActionPerformed(evt);
            }
        });
        jPanel5.add(roleBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 240, 150, 30));

        deptBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Home Improvement", "Grocery", "Dry Goods" }));
        deptBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deptBoxActionPerformed(evt);
            }
        });
        jPanel5.add(deptBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 200, 150, 30));

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel25.setText("Role:");
        jPanel5.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 240, -1, -1));

        txtPersonnel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPersonnelActionPerformed(evt);
            }
        });
        jPanel5.add(txtPersonnel, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 160, 150, 30));

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel26.setText("Email:");
        jPanel5.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 280, -1, -1));

        txtEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailActionPerformed(evt);
            }
        });
        jPanel5.add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 280, 150, 30));

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel27.setText("Name:");
        jPanel5.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 120, -1, -1));

        txtName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameActionPerformed(evt);
            }
        });
        jPanel5.add(txtName, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 120, 150, 30));

        jTabbedPane1.addTab("Add Employees", jPanel5);

        inventoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Product Name", "Category", "Price", "Stock"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(inventoryTable);
        if (inventoryTable.getColumnModel().getColumnCount() > 0) {
            inventoryTable.getColumnModel().getColumn(0).setResizable(false);
            inventoryTable.getColumnModel().getColumn(1).setResizable(false);
            inventoryTable.getColumnModel().getColumn(2).setResizable(false);
            inventoryTable.getColumnModel().getColumn(3).setResizable(false);
        }

        jLabel19.setText("Category:");

        jLabel21.setText("Product Name:");

        jLabel22.setText("Price:");

        jLabel23.setText("Stock:");

        txtProductName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProductNameActionPerformed(evt);
            }
        });

        categoryBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Fruits", "Dairy", "Tools", "Hygiene", "Grains", "Consumables", "Cleaning", "Gardening" }));
        categoryBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categoryBoxActionPerformed(evt);
            }
        });

        addButton.setText("ADD");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        confrimEdit.setText("CONFIRM EDIT");
        confrimEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confrimEditActionPerformed(evt);
            }
        });

        jLabel24.setText("Product ID:");

        txtProductID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProductIDActionPerformed(evt);
            }
        });
        txtProductID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtProductIDKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 954, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(confrimEdit)
                                .addGap(18, 18, 18)
                                .addComponent(addButton))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtProductID, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(categoryBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtProductName, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(txtProductID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(categoryBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel23)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addButton)
                    .addComponent(confrimEdit))
                .addContainerGap(78, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Inventory", jPanel3);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        jLabel1.setText("ADMIN DASHBOARD");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 985, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 556, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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
    }// </editor-fold>//GEN-END:initComponents

    private void weeklyRevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_weeklyRevActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_weeklyRevActionPerformed

    private void totalRevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalRevActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalRevActionPerformed

    private void txtPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPassActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPassActionPerformed

    private void monthSpinnerPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_monthSpinnerPropertyChange
        int selectedMonth = (int) monthSpinner.getValue();
        int year = (int) yearSpinner.getValue();
        boolean isLeapYear = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
        SpinnerNumberModel dateModel = (SpinnerNumberModel) daySpinner.getModel();

        if (selectedMonth == 1 || selectedMonth == 3 || selectedMonth == 5 || selectedMonth == 7 || selectedMonth == 8 || selectedMonth == 10 || selectedMonth == 12) {
            dateModel.setMaximum(31);
        } else if (selectedMonth == 2) {
            dateModel.setMaximum(isLeapYear ? 29 : 28);
        } else {
            dateModel.setMaximum(30);
        }
    }//GEN-LAST:event_monthSpinnerPropertyChange

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
    // Getting values from UI components
    String product = txtProductID.getText();
    String productname = txtProductName.getText();
    String category = categoryBox.getSelectedItem().toString();
    String role = roleBox.getSelectedItem().toString();
    String stock = txtStock.getText();

    // Validate input
    if (productname.isEmpty() || category.isEmpty() || role.isEmpty() || stock.isEmpty()) {
        JOptionPane.showMessageDialog(this, "All fields must be filled out.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Database connection parameters
    String url = "jdbc:mysql://localhost:3306/srm_db"; // Ensure database name is correct
    String user = "root";
    String pass = "";

    Connection conn = null;
    PreparedStatement pst = null;

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(url, user, pass);

        // SQL query to insert new product record
        String insertProductSql = "INSERT INTO product (product_id, product_name, category, price, stock) VALUES (?, ?, ?, ?, ?)";
        pst = conn.prepareStatement(insertProductSql);

        // Setting values in the prepared statement
        pst.setInt(1, Integer.parseInt(product));
        pst.setString(2, productname);
        pst.setString(3, category);
        pst.setString(4, role); 
        pst.setInt(5, Integer.parseInt(stock)); 

        int rowsInserted = pst.executeUpdate();

        if (rowsInserted > 0) {
            JOptionPane.showMessageDialog(this, "Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Refresh the table to show the new record
            refreshInventoryTable();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add product.", "Error", JOptionPane.ERROR_MESSAGE);
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

    private void refreshInventoryTable() {
    DefaultTableModel model = (DefaultTableModel) inventoryTable.getModel();
    model.setRowCount(0); // Clear existing rows

    String url = "jdbc:mysql://localhost:3306/srm_db";
    String user = "root";
    String pass = "";

    try (Connection conn = DriverManager.getConnection(url, user, pass);
         PreparedStatement pst = conn.prepareStatement("SELECT sales_id, product_name, quantity, sales_date, total_price FROM sales");
         ResultSet rs = pst.executeQuery()) {

        Class.forName("com.mysql.cj.jdbc.Driver");

        // Populate the JTable with fresh data
        while (rs.next()) {
            int salesId = rs.getInt("sales_id");
            String productName = rs.getString("product_name");
            int quantity = rs.getInt("quantity");
            Date salesDate = rs.getDate("sales_date"); // Correctly retrieving date
            double totalPrice = rs.getDouble("total_price");

            // Add row to the table model
            model.addRow(new Object[]{salesId, productName, quantity, salesDate, totalPrice});
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } catch (ClassNotFoundException e) {
        JOptionPane.showMessageDialog(this, "Driver not found: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private void categoryBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categoryBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_categoryBoxActionPerformed

    private void txtProductNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProductNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProductNameActionPerformed

    private void confrimEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confrimEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_confrimEditActionPerformed

    private void txtProductIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProductIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProductIDActionPerformed

    private void txtProductIDKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProductIDKeyReleased
        String product = txtProductID.getText();
        if(product.isEmpty()){
            txtProductName.setText("");
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
                String productName = rs.getString("product_name");
                txtProductName.setText(productName);
            }else{
            txtProductName.setText("");
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
    }//GEN-LAST:event_txtProductIDKeyReleased

    private void addEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addEmployeeActionPerformed
           // Getting values from the UI components
int year = (int) yearSpinner.getValue();
int month = (int) monthSpinner.getValue();
int day = (int) daySpinner.getValue();

String ename = txtName.getText();
String personnell = txtPersonnel.getText(); // Assuming this is PersonnelID
String dept = deptBox.getSelectedItem().toString(); // DepartmentID should be handled according to your DB design
String role = roleBox.getSelectedItem().toString();
String email = txtEmail.getText();
String stock = txtPass.getText(); // Assuming this is the password
String birthdate = String.format("%04d-%02d-%02d", year, month, day);

// Validate input
if (personnell.isEmpty() || ename.isEmpty() || role.isEmpty() || dept.isEmpty() || email.isEmpty() || stock.isEmpty()) {
    JOptionPane.showMessageDialog(this, "All fields must be filled out.", "Error", JOptionPane.ERROR_MESSAGE);
    return;
}

// Database connection parameters
String url = "jdbc:mysql://localhost:3306/srm_db"; // Ensure database name is correct
String user = "root";
String pass = "";

Connection conn = null;
PreparedStatement pst = null;

try {
    Class.forName("com.mysql.cj.jdbc.Driver");
    conn = DriverManager.getConnection(url, user, pass);

    // SQL query to insert new user record
    String insertUserSql = "INSERT INTO user (PersonnelID, name, role, DepartmentID, email, BirthDate, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
    pst = conn.prepareStatement(insertUserSql);

    // Setting values in the prepared statement
    pst.setString(1, personnell); // Set PersonnelID
    pst.setString(2, ename); // Set name
    pst.setString(3, role); // Set role
    pst.setString(4, dept); // Set DepartmentID (ensure dept contains valid ID)
    pst.setString(5, email); // Set email
    pst.setString(6, birthdate); // Set BirthDate
    pst.setString(7, stock); // Set password

    int rowsInserted = pst.executeUpdate();

    if (rowsInserted > 0) {
        JOptionPane.showMessageDialog(this, "User added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    } else {
        JOptionPane.showMessageDialog(this, "Failed to add user.", "Error", JOptionPane.ERROR_MESSAGE);
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


    }//GEN-LAST:event_addEmployeeActionPerformed

    private void roleBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roleBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_roleBoxActionPerformed

    private void deptBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deptBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_deptBoxActionPerformed

    private void txtPersonnelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPersonnelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPersonnelActionPerformed

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailActionPerformed

    private void txtNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNameActionPerformed

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
            java.util.logging.Logger.getLogger(adminPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(adminPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(adminPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(adminPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new adminPanel().setVisible(true);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(adminPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javaswingdev.chart.PieChart DailyPieChart;
    private javaswingdev.chart.PieChart WeeklyPieChart;
    private javax.swing.JButton addButton;
    private javax.swing.JButton addEmployee;
    private javax.swing.JComboBox<String> categoryBox;
    private com.raven.chart.Chart chart;
    private javax.swing.JButton confrimEdit;
    private javax.swing.JTextField dailyRev;
    private javax.swing.JSpinner daySpinner;
    private javax.swing.JComboBox<String> deptBox;
    private javax.swing.JTable inventoryTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JSpinner monthSpinner;
    private javax.swing.JComboBox<String> roleBox;
    private javax.swing.JLabel topDryG;
    private javax.swing.JLabel topGrocery;
    private javax.swing.JLabel topHomeImp;
    private javax.swing.JTextField totalRev;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPass;
    private javax.swing.JTextField txtPersonnel;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtProductID;
    private javax.swing.JTextField txtProductName;
    private javax.swing.JTextField txtStock;
    private javax.swing.JTextField weeklyRev;
    private javax.swing.JSpinner yearSpinner;
    // End of variables declaration//GEN-END:variables
}
