/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package week1;

import com.raven.chart.ModelChart;
import java.awt.Color;
import java.sql.DriverManager;
import java.util.ArrayList;
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
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.NumberFormat;
import java.time.LocalDate;
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
        dashboard();
        InventoryTable();
    }
    void dashboard(){
        getDailyRevenue();
        getWeeklyRevenue();
        getTotalRevenue();
        getTopDryGoods();
        getTopHomeImp();
        getTopGrocery();
    }
    void getDailyRevenue() {
        String DB_URL = "jdbc:mysql://localhost:3306/srm_db";
        String DB_USER = "root";
        String DB_PASSWORD = "";
        String query = "SELECT SUM(total_price) AS dRevenue FROM sales WHERE sales_date = CURRENT_DATE";
        double revenue = 0.0;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                revenue = rs.getDouble("dRevenue");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);
        String formattedRevenue = String.format("₱%s", numberFormat.format(revenue));
        dailyRev.setText(formattedRevenue);
    }  
    void getWeeklyRevenue() {
        String DB_URL = "jdbc:mysql://localhost:3306/srm_db";
        String DB_USER = "root";
        String DB_PASSWORD = "";
        String query = "SELECT SUM(total_price) AS wRevenue FROM sales WHERE sales_date >= DATE_SUB(CURRENT_DATE, INTERVAL 7 DAY)";
        double revenue = 0.0;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                revenue = rs.getDouble("wRevenue");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);
        String formattedRevenue = String.format("₱%s", numberFormat.format(revenue));
        weeklyRev.setText(formattedRevenue);
        
    }   
    void getTotalRevenue() {
        String DB_URL = "jdbc:mysql://localhost:3306/srm_db";
        String DB_USER = "root";
        String DB_PASSWORD = "";
        String query = "SELECT SUM(total_price) AS tRevenue FROM sales";
        double revenue = 0.0;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                revenue = rs.getDouble("tRevenue");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);
        String formattedRevenue = String.format("₱%s", numberFormat.format(revenue));  
        totalRev.setText(formattedRevenue);
        
    }
    void getTopDryGoods() {
        String DB_URL = "jdbc:mysql://localhost:3306/srm_db";
        String DB_USER = "root";
        String DB_PASSWORD = "";
        String query = "SELECT u.name " +
                       "FROM sales s " +
                       "JOIN user u ON s.user_id = u.user_id " +
                       "WHERE u.DepartmentID = 1 " +
                       "GROUP BY u.name " +
                       "ORDER BY SUM(s.quantity) DESC " +
                       "LIMIT 1;";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                String name = rs.getString("name");
                name = name.toUpperCase();
                jLabel12.setText(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    void getTopHomeImp() {
        String DB_URL = "jdbc:mysql://localhost:3306/srm_db";
        String DB_USER = "root";
        String DB_PASSWORD = "";
        String query = "SELECT u.name " +
                       "FROM sales s " +
                       "JOIN user u ON s.user_id = u.user_id " +
                       "WHERE u.DepartmentID = 2 " +
                       "GROUP BY u.name " +
                       "ORDER BY SUM(s.quantity) DESC " +
                       "LIMIT 1;";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                String name = rs.getString("name");
                name = name.toUpperCase();
                jLabel13.setText(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    void getTopGrocery() {
        String DB_URL = "jdbc:mysql://localhost:3306/srm_db";
        String DB_USER = "root";
        String DB_PASSWORD = "";
        String query = "SELECT u.name " +
                       "FROM sales s " +
                       "JOIN user u ON s.user_id = u.user_id " +
                       "WHERE u.DepartmentID = 3 " +
                       "GROUP BY u.name " +
                       "ORDER BY SUM(s.quantity) DESC " +
                       "LIMIT 1;";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                String name = rs.getString("name");
                name = name.toUpperCase();
                topGrocery.setText(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        jTextField1 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        topDryG1 = new javax.swing.JLabel();
        topHomeImp1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
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
        jLabel26 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        inventoryTable = new javax.swing.JTable();
        txtProductName = new javax.swing.JTextField();
        txtStock = new javax.swing.JTextField();
        addButton = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        txtProductID = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        LocalDate today = LocalDate.now();         int currentYear = today.getYear();         int currentMonth = today.getMonthValue(); int currentDay = today.getDayOfMonth();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        SalesRecordTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        yearSpinner1 = new javax.swing.JSpinner(new SpinnerNumberModel(currentYear, 2024, currentYear, 1));
        monthSpinner1 = new javax.swing.JSpinner(new SpinnerNumberModel(currentMonth, 01, 12, 1));
        daySpinner1 = new javax.swing.JSpinner(new SpinnerNumberModel(currentDay,1,31,1));
        jLabel28 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        dltRecord = new javax.swing.JButton();
        jTextField5 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        jTextField4.setText("jTextField4");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationByPlatform(true);

        jPanel1.setBackground(new java.awt.Color(125, 125, 125));

        jPanel2.setBackground(new java.awt.Color(173, 173, 173));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel2.add(chart, new org.netbeans.lib.awtextra.AbsoluteConstraints(27, 0, 538, 408));

        WeeklyPieChart.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jPanel2.add(WeeklyPieChart, new org.netbeans.lib.awtextra.AbsoluteConstraints(583, 54, 182, 163));

        jLabel3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jLabel3.setText("DAILY SALES:");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 220, 133, 32));

        DailyPieChart.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jPanel2.add(DailyPieChart, new org.netbeans.lib.awtextra.AbsoluteConstraints(583, 267, 183, 165));

        jLabel4.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel4.setText("WEEKLY REVENUE:");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(219, 416, -1, 32));

        jLabel5.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel5.setText("DAILY REVENUE:");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(49, 416, -1, 32));

        dailyRev.setEditable(false);
        dailyRev.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        dailyRev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dailyRevActionPerformed(evt);
            }
        });
        jPanel2.add(dailyRev, new org.netbeans.lib.awtextra.AbsoluteConstraints(44, 456, 113, -1));

        weeklyRev.setEditable(false);
        weeklyRev.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        weeklyRev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                weeklyRevActionPerformed(evt);
            }
        });
        jPanel2.add(weeklyRev, new org.netbeans.lib.awtextra.AbsoluteConstraints(219, 456, 121, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jLabel6.setText("TOTAL REVENUE:");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(421, 414, -1, 32));

        totalRev.setEditable(false);
        totalRev.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        totalRev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalRevActionPerformed(evt);
            }
        });
        jPanel2.add(totalRev, new org.netbeans.lib.awtextra.AbsoluteConstraints(424, 454, 141, 36));

        jLabel7.setFont(new java.awt.Font("Segoe UI Semibold", 0, 20)); // NOI18N
        jLabel7.setText("TOP EMPLOYEE ");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 36, -1, 21));

        jLabel8.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel8.setText("PER DEPARTMENT");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(814, 63, -1, 14));

        topGrocery.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        topGrocery.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        topGrocery.setText("NAME PLACEHOLDER");
        jPanel2.add(topGrocery, new org.netbeans.lib.awtextra.AbsoluteConstraints(771, 117, 181, -1));

        jLabel10.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("GROCERY");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(798, 410, 181, -1));

        topDryG.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        topDryG.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        topDryG.setText("DRY GOODS");
        jPanel2.add(topDryG, new org.netbeans.lib.awtextra.AbsoluteConstraints(771, 220, 181, -1));

        jLabel12.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("NAME PLACEHOLDER");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(773, 197, 181, -1));

        jLabel13.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("NAME PLACEHOLDER");
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(773, 273, 181, -1));

        topHomeImp.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        topHomeImp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        topHomeImp.setText("HOME IMPROVEMENT");
        jPanel2.add(topHomeImp, new org.netbeans.lib.awtextra.AbsoluteConstraints(771, 296, 181, -1));

        jTextField1.setEditable(false);
        jTextField1.setBackground(new java.awt.Color(255, 0, 0));
        jPanel2.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(772, 442, 14, 16));

        jTextField3.setEditable(false);
        jTextField3.setBackground(new java.awt.Color(0, 51, 255));
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });
        jPanel2.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(772, 480, 14, 16));

        jLabel19.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        jLabel19.setText("WEEKLY SALES:");
        jPanel2.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(616, 16, 133, 32));

        jTextField2.setEditable(false);
        jTextField2.setBackground(new java.awt.Color(0, 255, 0));
        jPanel2.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(772, 410, 14, 16));

        jLabel22.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("GROCERY");
        jPanel2.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(771, 155, 181, -1));

        topDryG1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        topDryG1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        topDryG1.setText("DRY GOODS");
        jPanel2.add(topDryG1, new org.netbeans.lib.awtextra.AbsoluteConstraints(798, 480, 181, -1));

        topHomeImp1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        topHomeImp1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        topHomeImp1.setText("HOME IMPROVEMENT");
        jPanel2.add(topHomeImp1, new org.netbeans.lib.awtextra.AbsoluteConstraints(798, 442, 181, -1));

        jTabbedPane1.addTab("Performance Overview", jPanel2);

        jPanel5.setBackground(new java.awt.Color(173, 173, 173));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel9.setText("ADD EMPLOYEE:");
        jPanel5.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 30, 210, 31));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setText("Birthdate:");
        jPanel5.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 310, -1, -1));

        txtPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPassActionPerformed(evt);
            }
        });
        jPanel5.add(txtPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 270, 150, 30));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel20.setText("Department:");
        jPanel5.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 170, -1, -1));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setText("Password:");
        jPanel5.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 270, -1, -1));
        jPanel5.add(yearSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 310, -1, -1));

        monthSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                monthSpinnerStateChanged(evt);
            }
        });
        jPanel5.add(monthSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 310, -1, -1));
        jPanel5.add(daySpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 310, -1, -1));

        jLabel11.setText("Day");
        jPanel5.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 340, 40, -1));

        jLabel16.setText("Year");
        jPanel5.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 340, 30, -1));

        jLabel17.setText("Month");
        jPanel5.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 340, 40, -1));

        addEmployee.setText("ADD");
        addEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addEmployeeActionPerformed(evt);
            }
        });
        jPanel5.add(addEmployee, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 420, -1, -1));

        roleBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sales Staff", "Admin" }));
        roleBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                roleBoxItemStateChanged(evt);
            }
        });
        roleBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roleBoxActionPerformed(evt);
            }
        });
        jPanel5.add(roleBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 120, 150, 30));

        deptBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Home Improvement", "Grocery", "Dry Goods" }));
        deptBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deptBoxActionPerformed(evt);
            }
        });
        jPanel5.add(deptBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 170, 150, 30));

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel25.setText("Role:");
        jPanel5.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 120, -1, -1));

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel26.setText("Email:");
        jPanel5.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 220, -1, -1));

        txtEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailActionPerformed(evt);
            }
        });
        jPanel5.add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 220, 150, 30));

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel27.setText("Name:");
        jPanel5.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 70, -1, -1));

        txtName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameActionPerformed(evt);
            }
        });
        jPanel5.add(txtName, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 70, 150, 30));

        jLabel18.setFont(new java.awt.Font("Segoe UI Semibold", 0, 24)); // NOI18N
        jLabel18.setText("PERSONNEL ID");
        jPanel5.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 370, 170, 30));

        jButton1.setText("GENERATE");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 370, 100, -1));

        jTabbedPane1.addTab("Add Employees", jPanel5);

        jPanel3.setBackground(new java.awt.Color(173, 173, 173));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        inventoryTable.setAutoCreateRowSorter(true);
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
                "Product ID", "Product Name", "Price", "Stock"
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

        jPanel3.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, 954, 253));

        txtProductName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProductNameActionPerformed(evt);
            }
        });
        jPanel3.add(txtProductName, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 330, 220, 30));

        txtStock.setText("0");
        jPanel3.add(txtStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 420, 230, 40));

        addButton.setText("UPDATE");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
        jPanel3.add(addButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 490, -1, -1));

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel24.setText("Stock Count:");
        jPanel3.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 450, 129, -1));

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
        jPanel3.add(txtProductID, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 280, 220, 30));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Add Stock", "Reduce Stock" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jPanel3.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 370, 160, 40));

        jButton2.setText("Notify Low Stock");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(819, 452, 141, -1));

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel29.setText("Product ID:");
        jPanel3.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 280, -1, -1));

        jLabel30.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel30.setText("Product Name:");
        jPanel3.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 330, 130, -1));

        jTabbedPane1.addTab("Inventory", jPanel3);

        jPanel4.setBackground(new java.awt.Color(173, 173, 173));

        SalesRecordTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Product Name", "Sales Staff", "Quantity", "Department", "Sales Date", "Total Sales"
            }
        ));
        jScrollPane2.setViewportView(SalesRecordTable);
        loadSalesRecordTable();

        jLabel2.setFont(new java.awt.Font("Segoe UI Semibold", 2, 18)); // NOI18N
        jLabel2.setText("SALES RECORD PER DAY:");

        yearSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                yearSpinner1StateChanged(evt);
            }
        });

        monthSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                monthSpinner1StateChanged(evt);
            }
        });

        daySpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                daySpinner1StateChanged(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel28.setText("YEAR:");

        jLabel31.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel31.setText("MONTH:");

        jLabel32.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel32.setText("DAY:");

        dltRecord.setText("DELETE RECORD");
        dltRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dltRecordActionPerformed(evt);
            }
        });

        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });
        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField5KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(yearSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(jLabel31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(monthSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(daySpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 186, Short.MAX_VALUE)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(dltRecord)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(yearSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28)
                    .addComponent(jLabel31)
                    .addComponent(jLabel32)
                    .addComponent(monthSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(daySpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dltRecord)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Sales Record", jPanel4);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        jLabel1.setText("ADMIN DASHBOARD");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 985, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        setLocationRelativeTo(null);
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

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
    String product = txtProductID.getText();
    String productname = txtProductName.getText();
    String stock = txtStock.getText();
    if (productname.isEmpty() || stock.isEmpty()) {
        JOptionPane.showMessageDialog(this, "All fields must be filled out.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    int Stock = Integer.parseInt(stock);
    if (Stock < 0) {
        JOptionPane.showMessageDialog(this, "No negative numbers.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Database connection parameters
    String url = "jdbc:mysql://localhost:3306/srm_db"; // Ensure database name is correct
    String user = "root";
    String pass = "";
    String selected = (String) jComboBox1.getSelectedItem();
    Connection conn = null;
    PreparedStatement pst = null;

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(url, user, pass);
        String insertProductSql;
        // SQL query to insert new product record
        if("Add Stock".equals(selected)){
        insertProductSql = "UPDATE product SET stock = stock + ? WHERE product_id = ?";}
        else{
        insertProductSql = "UPDATE product SET stock = stock - ? WHERE product_id = ?";}    
        pst = conn.prepareStatement(insertProductSql);

        // Setting values in the prepared statement
        pst.setInt(2, Integer.parseInt(product));
        pst.setInt(1, Integer.parseInt(stock)); 

        int rowsInserted = pst.executeUpdate();

        if (rowsInserted > 0) {
            JOptionPane.showMessageDialog(this, "Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            InventoryTable();
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

    private void InventoryTable() {
        DefaultTableModel model = (DefaultTableModel) inventoryTable.getModel();
        model.setRowCount(0); // Clear existing rows

        String url = "jdbc:mysql://localhost:3306/srm_db";
        String user = "root";
        String pass = "";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement pst = conn.prepareStatement("SELECT product_id, product_name, price, stock FROM product");
             ResultSet rs = pst.executeQuery()) {

            Class.forName("com.mysql.cj.jdbc.Driver");

            // Populate the JTable with fresh data
            while (rs.next()) {
                int salesId = rs.getInt("product_id");
                String productName = rs.getString("product_name");
                int price = rs.getInt("price");
                int stock = rs.getInt("stock");

                // Add row to the table model
                model.addRow(new Object[]{salesId, productName, price, stock});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Driver not found: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void loadSalesRecordTable() {
        DefaultTableModel model = (DefaultTableModel) SalesRecordTable.getModel();
        model.setRowCount(0);
        int year = (int) yearSpinner1.getValue();
        int month = (int) monthSpinner1.getValue();
        int day = (int) daySpinner1.getValue();
        String date = String.format("%04d-%02d-%02d", year, month, day);
        String url = "jdbc:mysql://localhost:3306/srm_db";
        String user = "root";
        String pass = "";
        String qry = "SELECT p.product_name, u.name, s.quantity, d.DepartmentName, "
                   + "s.sales_date, s.total_price FROM sales s "
                   + "JOIN user u ON u.user_id = s.user_id "
                   + "JOIN department d ON d.DepartmentID = u.DepartmentID "
                   + "JOIN product p ON p.product_id = s.product_id "
                   + "WHERE s.sales_date = ?";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(url, user, pass);
                 PreparedStatement pst = conn.prepareStatement(qry)) {
                pst.setString(1, date);

                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        String pname = rs.getString("product_name");
                        String name = rs.getString("name");
                        int quant = rs.getInt("quantity");
                        String dept = rs.getString("DepartmentName");
                        String Date = rs.getString("sales_date");
                        double price = rs.getDouble("total_price");
                        model.addRow(new Object[]{pname, name, quant, dept, Date, price});
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Driver not found: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void txtProductNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProductNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProductNameActionPerformed

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
        int year = (int) yearSpinner.getValue();
        int month = (int) monthSpinner.getValue();
        int day = (int) daySpinner.getValue();

        String ename = txtName.getText();
        String dept = deptBox.getSelectedItem().toString();
        int deptID;
        String role = roleBox.getSelectedItem().toString();
        String email = txtEmail.getText();
        String stock = txtPass.getText(); // Assuming this is the password
        String birthdate = String.format("%04d-%02d-%02d", year, month, day);

        // Validate input
        if (ename.isEmpty() || role.isEmpty() || dept.isEmpty() || email.isEmpty() || stock.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if("Dry Goods".equals(dept)){
            deptID = 1;
        }else if("Home Improvement".equals(dept)){
            deptID = 2;
        }else{
            deptID = 3;
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
            String personnelID = generatePersonnelID(conn, birthdate);
            // SQL query to insert new user record
            String insertUserSql = "INSERT INTO user (name, PersonnelID, role, DepartmentID, email, BirthDate, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
            pst = conn.prepareStatement(insertUserSql);

            pst.setString(1, ename);
            pst.setString(2, personnelID); 
            pst.setString(3, role); 
            pst.setInt(4, deptID); 
            pst.setString(5, email); 
            pst.setString(6, birthdate); 
            pst.setString(7, stock); 

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

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailActionPerformed

    private void txtNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNameActionPerformed

    private void roleBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_roleBoxItemStateChanged
        String role = (String) roleBox.getSelectedItem();
        if("Admin".equals(role)){
            jLabel20.setVisible(false);
            deptBox.setVisible(false);
            deptBox.setSelectedItem("0");
        }
        else{
            jLabel20.setVisible(true);
            deptBox.setVisible(true);
        }
    }//GEN-LAST:event_roleBoxItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int year = (int) yearSpinner.getValue();
        int month = (int) monthSpinner.getValue();
        int day = (int) daySpinner.getValue();
        String url = "jdbc:mysql://localhost:3306/srm_db";
        String user = "root";
        String pass = "";
        String birthdate = String.format("%04d-%02d-%02d", year, month, day);
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, pass);
            String personnelID = generatePersonnelID(conn, birthdate);
            jLabel18.setText(personnelID);
        }catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(adminPanel.class.getName()).log(Level.SEVERE, null, ex);
        }     
            
    }//GEN-LAST:event_jButton1ActionPerformed

    private void dailyRevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dailyRevActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dailyRevActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    int selectedRow = inventoryTable.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a product to create a notification.", 
                                      "No Row Selected", JOptionPane.WARNING_MESSAGE);
    } else {
        int productId = (int) inventoryTable.getValueAt(selectedRow, 0);

        String url = "jdbc:mysql://localhost:3306/srm_db";
        String user = "root";
        String pass = "";

        String query = "INSERT INTO notification (product_id, notification_date) VALUES (?,CURDATE());";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setInt(1, productId);
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Notification created for product ID: " + productId, 
                                              "Notification Added", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error inserting notification: " + e.getMessage(),
                                          "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void monthSpinner1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_monthSpinner1StateChanged
        loadSalesRecordTable();
        int selectedMonth = (int) monthSpinner1.getValue();
        int year = (int) yearSpinner1.getValue();
        boolean isLeapYear = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
        SpinnerNumberModel dateModel = (SpinnerNumberModel) daySpinner1.getModel();

        if (selectedMonth == 1 || selectedMonth == 3 || selectedMonth == 5 || selectedMonth == 7 || selectedMonth == 8 || selectedMonth == 10 || selectedMonth == 12) {
            dateModel.setMaximum(31);
        } else if (selectedMonth == 2) {
            dateModel.setMaximum(isLeapYear ? 29 : 28);
        } else {
            dateModel.setMaximum(30);
        }
    }//GEN-LAST:event_monthSpinner1StateChanged

    private void monthSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_monthSpinnerStateChanged
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
    }//GEN-LAST:event_monthSpinnerStateChanged

    private void daySpinner1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_daySpinner1StateChanged
        loadSalesRecordTable();
    }//GEN-LAST:event_daySpinner1StateChanged

    private void yearSpinner1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_yearSpinner1StateChanged
        loadSalesRecordTable();
    }//GEN-LAST:event_yearSpinner1StateChanged

    private void dltRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dltRecordActionPerformed
        int selectedRow = SalesRecordTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.", 
                                          "No Row Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int salesId = searchSalesID(); 
        if (salesId == -1) {
            return;
        }

        int quantity = (int) SalesRecordTable.getValueAt(selectedRow, 2);
        String productName = SalesRecordTable.getValueAt(selectedRow, 0).toString();

        String url = "jdbc:mysql://localhost:3306/srm_db";
        String user = "root";
        String pass = "";

        String deleteQuery = "DELETE FROM sales WHERE sales_id = ?";
        String updateStockQuery = "UPDATE product SET stock = stock + ? WHERE product_name = ?";

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstDelete = conn.prepareStatement(deleteQuery);
                 PreparedStatement pstUpdateStock = conn.prepareStatement(updateStockQuery)) {

                pstDelete.setInt(1, salesId);
                int rowsDeleted = pstDelete.executeUpdate();

                if (rowsDeleted > 0) {
                    pstUpdateStock.setInt(1, quantity);
                    pstUpdateStock.setString(2, productName);
                    pstUpdateStock.executeUpdate();

                    conn.commit();
                    JOptionPane.showMessageDialog(this, "Record deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadSalesRecordTable(); 
                } else {
                    conn.rollback();
                    JOptionPane.showMessageDialog(this, "Deletion failed. Sales record not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                conn.rollback();
                JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_dltRecordActionPerformed
    private void jTextField5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyReleased
        searchSalesRecord(jTextField5.getText().trim());
    }//GEN-LAST:event_jTextField5KeyReleased

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed
    private void searchSalesRecord(String keyword) {
        DefaultTableModel model = (DefaultTableModel) SalesRecordTable.getModel();
        model.setRowCount(0);

        int year = (int) yearSpinner1.getValue();
        int month = (int) monthSpinner1.getValue();
        int day = (int) daySpinner1.getValue();
        String date = String.format("%04d-%02d-%02d", year, month, day);  // Format date as YYYY-MM-DD

        String url = "jdbc:mysql://localhost:3306/srm_db";
        String user = "root";
        String pass = "";

        String query = "SELECT p.product_name, u.name, s.quantity, d.DepartmentName, "
                     + "s.sales_date, s.total_price FROM sales s "
                     + "JOIN user u ON u.user_id = s.user_id "
                     + "JOIN department d ON d.DepartmentID = u.DepartmentID "
                     + "JOIN product p ON p.product_id = s.product_id "
                     + "WHERE (p.product_name LIKE ? OR u.name LIKE ?) AND s.sales_date = ?";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(url, user, pass);
                 PreparedStatement pst = conn.prepareStatement(query)) {

                String searchParam = "%" + keyword + "%";
                pst.setString(1, searchParam); // Product name filter
                pst.setString(2, searchParam); // User name filter
                pst.setString(3, date);       // Fixed sales date

                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        String pname = rs.getString("product_name");
                        String name = rs.getString("name");
                        int quant = rs.getInt("quantity");
                        String dept = rs.getString("DepartmentName");
                        String salesDate = rs.getString("sales_date");
                        double price = rs.getDouble("total_price");

                        model.addRow(new Object[]{pname, name, quant, dept, salesDate, price});
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Driver not found: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public int searchSalesID() {
        int selectedRow = SalesRecordTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a row first!", "Warning", JOptionPane.WARNING_MESSAGE);
            return -1;
        }

        String productName = SalesRecordTable.getValueAt(selectedRow, 0).toString();
        String userName = SalesRecordTable.getValueAt(selectedRow, 1).toString();
        String salesDate = SalesRecordTable.getValueAt(selectedRow, 4).toString();
        double totalPrice = Double.parseDouble(SalesRecordTable.getValueAt(selectedRow, 5).toString());

        String url = "jdbc:mysql://localhost:3306/srm_db";
        String user = "root";
        String pass = "";

        String qry = "SELECT s.sales_id FROM sales s "
                   + "JOIN user u ON u.user_id = s.user_id "
                   + "JOIN product p ON p.product_id = s.product_id "
                   + "WHERE p.product_name = ? AND u.name = ? AND s.sales_date = ? AND s.total_price = ?";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(url, user, pass);
                 PreparedStatement pst = conn.prepareStatement(qry)) {

                pst.setString(1, productName);
                pst.setString(2, userName);
                pst.setString(3, salesDate);
                pst.setDouble(4, totalPrice);

                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("sales_id");
                    } else {
                        JOptionPane.showMessageDialog(null, "No matching sales record found!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Driver not found: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return -1;
    }
    public static String generatePersonnelID(Connection conn, String birthdate) {
        String formattedDate = formatBirthdate(birthdate);
        int nextUserID = getNextUserID(conn);
        return formattedDate + String.format("%03d", nextUserID);
    }
    private static String formatBirthdate(String birthdate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMdd");
            Date date = inputFormat.parse(birthdate);
            return outputFormat.format(date);  // Returns MMDD
        } catch (ParseException e) {
            e.printStackTrace();
            return "0000";  // Default in case of error
        }
    }
    private static int getNextUserID(Connection conn) {
        int nextID = 1; // Default ID if no records exist
        String sql = "SELECT COALESCE(MAX(user_id) + 1, 1) AS nextID FROM user";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                nextID = rs.getInt("nextID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nextID;
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
    private javax.swing.JTable SalesRecordTable;
    private javaswingdev.chart.PieChart WeeklyPieChart;
    private javax.swing.JButton addButton;
    private javax.swing.JButton addEmployee;
    private com.raven.chart.Chart chart;
    private javax.swing.JTextField dailyRev;
    private javax.swing.JSpinner daySpinner;
    private javax.swing.JSpinner daySpinner1;
    private javax.swing.JComboBox<String> deptBox;
    private javax.swing.JButton dltRecord;
    private javax.swing.JTable inventoryTable;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
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
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JSpinner monthSpinner;
    private javax.swing.JSpinner monthSpinner1;
    private javax.swing.JComboBox<String> roleBox;
    private javax.swing.JLabel topDryG;
    private javax.swing.JLabel topDryG1;
    private javax.swing.JLabel topGrocery;
    private javax.swing.JLabel topHomeImp;
    private javax.swing.JLabel topHomeImp1;
    private javax.swing.JTextField totalRev;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPass;
    private javax.swing.JTextField txtProductID;
    private javax.swing.JTextField txtProductName;
    private javax.swing.JTextField txtStock;
    private javax.swing.JTextField weeklyRev;
    private javax.swing.JSpinner yearSpinner;
    private javax.swing.JSpinner yearSpinner1;
    // End of variables declaration//GEN-END:variables
}
