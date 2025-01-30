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
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author USER
 */
public class adminPanel extends javax.swing.JFrame {

    /**
     * Creates new form adminPanel
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
        nametextField = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner(new SpinnerNumberModel(1,1,31,1));
        jLabel11 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner(new SpinnerNumberModel(2000, 1950, 2025, 1));
        jSpinner3 = new javax.swing.JSpinner(new SpinnerNumberModel(01, 01, 12, 1));
        jComboBox1 = new javax.swing.JComboBox<>();
        emailtextField = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        emailtextField1 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
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
        jLabel4.setText("THIS WEEK'S REVENUE:");

        jLabel5.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel5.setText("TODAY'S REVENUE:");

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
                    .addComponent(chart, javax.swing.GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dailyRev))
                        .addGap(37, 37, 37)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(weeklyRev))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 121, Short.MAX_VALUE)
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
        jPanel5.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 40, 210, 31));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setText("Birthdate:");
        jPanel5.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 340, -1, -1));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel18.setText("Name:");
        jPanel5.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 120, -1, -1));

        nametextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nametextFieldActionPerformed(evt);
            }
        });
        jPanel5.add(nametextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 110, 150, 30));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel20.setText("Department:");
        jPanel5.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 170, -1, -1));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setText("Password:");
        jPanel5.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 280, -1, -1));
        jPanel5.add(jSpinner1, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 410, 90, 30));

        jLabel11.setText("Year:");
        jPanel5.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 340, -1, 10));

        jLabel16.setText("Month:");
        jPanel5.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 380, -1, 10));

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Day:");
        jPanel5.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 420, 30, 10));
        jPanel5.add(jSpinner2, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 330, 90, 30));

        jSpinner3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner3StateChanged(evt);
            }
        });
        jPanel5.add(jSpinner3, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 370, 90, 30));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Dry Goods", "Home Improvement", "Grocery" }));
        jPanel5.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 170, 150, -1));

        emailtextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailtextFieldActionPerformed(evt);
            }
        });
        jPanel5.add(emailtextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 280, 150, 30));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel19.setText("Email:");
        jPanel5.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 220, -1, -1));

        emailtextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailtextField1ActionPerformed(evt);
            }
        });
        jPanel5.add(emailtextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 220, 150, 30));

        jTabbedPane1.addTab("Add Employees", jPanel5);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 677, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(302, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(88, Short.MAX_VALUE))
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

    private void nametextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nametextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nametextFieldActionPerformed

    private void jSpinner3StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner3StateChanged
        int selectedMonth = (int) jSpinner3.getValue();
        int year = (int) jSpinner2.getValue();
        boolean isLeapYear = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
        SpinnerNumberModel dateModel = (SpinnerNumberModel) jSpinner1.getModel();

        if (selectedMonth == 1 || selectedMonth == 3 || selectedMonth == 5 || selectedMonth == 7 || selectedMonth == 8 || selectedMonth == 10 || selectedMonth == 12) {
            dateModel.setMaximum(31);
        } else if (selectedMonth == 2) {
            dateModel.setMaximum(isLeapYear ? 29 : 28);
        } else {
            dateModel.setMaximum(30);
        }

    }//GEN-LAST:event_jSpinner3StateChanged

    private void emailtextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailtextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailtextFieldActionPerformed

    private void emailtextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailtextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailtextField1ActionPerformed

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
    private com.raven.chart.Chart chart;
    private javax.swing.JTextField dailyRev;
    private javax.swing.JTextField emailtextField;
    private javax.swing.JTextField emailtextField1;
    private javax.swing.JComboBox<String> jComboBox1;
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
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JSpinner jSpinner3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField nametextField;
    private javax.swing.JLabel topDryG;
    private javax.swing.JLabel topGrocery;
    private javax.swing.JLabel topHomeImp;
    private javax.swing.JTextField totalRev;
    private javax.swing.JTextField weeklyRev;
    // End of variables declaration//GEN-END:variables
}
