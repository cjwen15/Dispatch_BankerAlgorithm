package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import tools.Context;

//进行安全性检查
public class BankFrame2 extends JFrame {

    private static final long serialVersionUID = 1L;
    //用于构造函数初始化数据
    private Map<String, Map<String, Map<String, Integer>>> datas = null;
    private Map<String, Integer> availableRs = null;
    private List<String> safeOrder = new ArrayList<String>();

    private Vector<String> tableColumnNames = null;
    private JButton safeCheck = null;
    private JButton exit = null;

    JTable table;

    public BankFrame2(Map<String, Map<String, Map<String, Integer>>> datas,
            Map<String, Integer> availableRs, List<String> safeOrder) {
        this.datas = datas;
        this.availableRs = availableRs;
        if (safeOrder != null) {
            this.safeOrder = safeOrder;
        }

        Vector<Vector<Object>> initTableValues = update();

        addTable(initTableValues);

        // 为退出按钮添加点击事件
        exit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                BankFrame2.this.dispose();

            }
        });

        //为执行算法添加点击事件
        safeCheck.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (checkSafety()) {
                    System.out.println("存在安全序列....");
                    String safeOrder_result = "";
                    for (int i = 0; i < BankFrame2.this.safeOrder.size(); i++) {
                        safeOrder_result += BankFrame2.this.safeOrder.get(i)
                                + " ";
                    }
                    JOptionPane.showMessageDialog(null, "安全序列为：" + safeOrder_result);
                } else {
                    JOptionPane.showMessageDialog(null, "无安全序列！");
                }
                new BankFrame2(BankFrame2.this.datas,
                        BankFrame2.this.availableRs, BankFrame2.this.safeOrder);
                BankFrame2.this.dispose();
            }
        });
        setWindowLocation();
    }

    // 设置窗口显示的位置
    public void setWindowLocation() {
        int h = 500;
        int w = 350;
        Toolkit tk = this.getToolkit();
        Dimension dm = tk.getScreenSize();
        this.setLocation((int) (dm.getWidth() - w) / 2,
                (int) (dm.getHeight() - h) / 2);
    }

    // 添加中间显示的数据表格
    public void addTable(Vector<Vector<Object>> tableValues) {

        JPanel centerPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane();

        tableColumnNames = new Vector<String>();
        tableColumnNames.add(Context.PROCESS);
        tableColumnNames.add(Context.WORK);
        tableColumnNames.add(Context.NEED);
        tableColumnNames.add(Context.ALLOCATION);
        tableColumnNames.add(Context.WORK_ALLOCATION);
        tableColumnNames.add(Context.FINISH);

        DefaultTableModel defaultTableModel = new DefaultTableModel(
                tableValues, tableColumnNames);

        table = new JTable(defaultTableModel); // 创建table对象

        scrollPane.setViewportView(table);
        System.out.println("tableValues..." + tableValues);
        centerPanel.add(scrollPane);

        centerPanel.revalidate();
        this.add(centerPanel, BorderLayout.CENTER);

        JLabel main_title = new JLabel("系统在T0时刻的安全序列：");
        this.add(main_title, BorderLayout.NORTH);

        JPanel bottom_panel = new JPanel(new GridLayout(1, 3));
        JLabel bottom_title = new JLabel("检测T0时刻的系统安全性：");
        safeCheck = new JButton("执行算法");
        exit = new JButton("退出程序");
        bottom_panel.add(bottom_title);
        bottom_panel.add(safeCheck);
        bottom_panel.add(exit);
        this.add(bottom_panel, BorderLayout.SOUTH);

        this.setTitle("银行家算法");
        this.pack();
        this.setSize(500, 500);
        this.setVisible(true); // Display the window.
    }

    /*
     * 数据更新到表格
     */
    private Vector<Vector<Object>> update() {

        Vector<Vector<Object>> tableValues = new Vector<Vector<Object>>();

        if (datas != null) {

            Set<String> keys = datas.keySet();
            Map<String, Map<String, Integer>> secondLeverTemp;
            Vector<Object> vectorTemp = null;
            Map<String, Integer> rs = null;

            List<String> temp_keys = new ArrayList<String>(safeOrder);
            int safeOrder_size = safeOrder.size();
            if (safeOrder != null && safeOrder_size > 0) {
                for (String key : keys) {       //遍历keys的集合，取出每一个元素
                    if (!checkIfChecked(key)) {
                        temp_keys.add(key);
                    }
                }
                for (int i = 0; i < temp_keys.size(); i++) {
                    String key = temp_keys.get(i);
                    vectorTemp = new Vector<Object>();
                    vectorTemp.add(key);
                    secondLeverTemp = datas.get(key);

                    rs = secondLeverTemp.get(Context.WORK);
                    vectorTemp.add(buildRsString(rs)); // Work

                    rs = secondLeverTemp.get(Context.NEED);
                    vectorTemp.add(buildRsString(rs)); // Need

                    rs = secondLeverTemp.get(Context.ALLOCATION);
                    vectorTemp.add(buildRsString(rs)); // Allocation

                    rs = secondLeverTemp.get(Context.WORK_ALLOCATION);
                    vectorTemp.add(buildRsString(rs)); // WORK+ALLOCATION

                    if (i < safeOrder_size) {
                        vectorTemp.add("true"); // Finish
                    } else {
                        vectorTemp.add("");
                    }
                    tableValues.add(vectorTemp);
                }
                return tableValues;
            }

            for (String key : keys) {
                vectorTemp = new Vector<Object>();
                vectorTemp.add(key);
                secondLeverTemp = datas.get(key);

                rs = secondLeverTemp.get(Context.WORK);
                vectorTemp.add(buildRsString(rs)); // Work

                rs = secondLeverTemp.get(Context.NEED);
                vectorTemp.add(buildRsString(rs)); // Need

                rs = secondLeverTemp.get(Context.ALLOCATION);
                vectorTemp.add(buildRsString(rs)); // Allocation

                rs = secondLeverTemp.get(Context.WORK_ALLOCATION);
                vectorTemp.add(buildRsString(rs)); // WORK+ALLOCATION

                rs = secondLeverTemp.get(Context.FINISH);
                vectorTemp.add(buildRsString(rs)); // Finish
                tableValues.add(vectorTemp);
            }
        }
        return tableValues;
    }

    /*
     * buildRsString( 用于构建资源字符串)
     */
    public String buildRsString(Map<String, Integer> rs) {
        String temp = "";
        if (rs != null) {
            for (int i = 1; i < 5; i++) {
                temp += rs.get("r" + i) + " ";
            }
            return temp;
        } else {
            return "";
        }
    }

    /*
     * checkSafety( 利用银行家算法检测安全性及安全序列 ) return: true表示有安全序列，FALSE表示没有安全序列
     */
    public boolean checkSafety() {
        if (datas != null && availableRs != null) {

            Map<String, Map<String, Integer>> secondLeverTemp;
            Map<String, Integer> rs = null;
            int compute_times = 0;
            while (safeOrder.size() < datas.size()) {
                Set<String> keys = datas.keySet();
                for (String key : keys) { // 遍历每一个进程
                    secondLeverTemp = datas.get(key);
                    // Need
                    rs = secondLeverTemp.get(Context.NEED);
                    if (rs != null) {
                        if (!checkIfChecked(key)) {
                            //取得可用的资源
                            int a_r1 = availableRs.get(Context.R1);
                            int a_r2 = availableRs.get(Context.R2);
                            int a_r3 = availableRs.get(Context.R3);
                            int a_r4 = availableRs.get(Context.R4);

                            //取得需要的资源数
                            int n_r1 = rs.get(Context.R1);
                            int n_r2 = rs.get(Context.R2);
                            int n_r3 = rs.get(Context.R3);
                            int n_r4 = rs.get(Context.R4);

                            if (a_r1 >= n_r1 && a_r2 >= n_r2 && a_r3 >= n_r3
                                    && a_r4 >= n_r4) {
                                System.out.println("资源可被进程" + key + "使用....");
                                safeOrder.add(key);

                                // Work
                                Map<String, Integer> rs_temp = new HashMap<String, Integer>();
                                rs_temp.put(Context.R1, a_r1);
                                rs_temp.put(Context.R2, a_r2);
                                rs_temp.put(Context.R3, a_r3);
                                rs_temp.put(Context.R4, a_r4);
                                secondLeverTemp.put(Context.WORK, rs_temp);

                                // 得到已分配的资源Allocation
                                rs = secondLeverTemp.get(Context.ALLOCATION);
                                int allo_r1 = rs.get(Context.R1);
                                int allo_r2 = rs.get(Context.R2);
                                int allo_r3 = rs.get(Context.R3);
                                int allo_r4 = rs.get(Context.R4);

                                // WORK+ALLOCATION
                                rs_temp = new HashMap<String, Integer>();
                                rs_temp.put(Context.R1, a_r1 + allo_r1);
                                rs_temp.put(Context.R2, a_r2 + allo_r2);
                                rs_temp.put(Context.R3, a_r3 + allo_r3);
                                rs_temp.put(Context.R4, a_r4 + allo_r4);

                                secondLeverTemp.put(Context.WORK_ALLOCATION,
                                        rs_temp);
                                //修改剩下资源
                                availableRs.put(Context.R1, a_r1 + allo_r1);
                                availableRs.put(Context.R2, a_r2 + allo_r2);
                                availableRs.put(Context.R3, a_r3 + allo_r3);
                                availableRs.put(Context.R4, a_r4 + allo_r4);

                                //踢出已经加入安全序列的进程
                                datas.remove(key);

                                System.out.println("remove success+"
                                        + datas.containsKey(key));

                                datas.put(key, secondLeverTemp);

                                table.repaint();
                                break;
                            } else {
                                System.out.println("资源不足进程" + key + "使用....");
                            }
                        }
                    } else {
                        System.out.println("系统无可用的资源!");
                        return false;
                    }
                    // Finish
                }
                compute_times++;
                if (compute_times > 20) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /*
     * checkIfChecked(检测当前进程是否添加到安全序列中)
     * @return 存在返回true 否则返回false
     */
    public boolean checkIfChecked(String key) {
        if (safeOrder != null) {
            for (int i = 0; i < safeOrder.size(); i++) {
                if (safeOrder.get(i).equals(key)) {
                    return true;
                }
            }
        }
        return false;
    }

}
