package view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import tools.Context;

//初始化各个进程的数据
public class BankFrame extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    JPanel jp_top, jp_bottom;
    JLabel jl_top, jl_bottom;
    JButton jb_check, jb_exit;
    JTable table_init;
    JScrollPane jsp;

    private final Map<String, Map<String, Map<String, Integer>>> datas = new HashMap<String, Map<String, Map<String, Integer>>>();
    private final Map<String, Integer> availableResource = new HashMap<String, Integer>();

    public BankFrame() {
        initDatas();

        Vector<String> tableColumnNames = new Vector<String>();
        tableColumnNames.add(Context.PROCESS);
        tableColumnNames.add(Context.ALLOCATION);
        tableColumnNames.add(Context.NEED);
        tableColumnNames.add(Context.AVAILABLE);

        Vector<Vector<Object>> tableValues = new Vector<Vector<Object>>();

        if (datas != null) {
            //keySet()遍历map是无序的
            Set<String> keys = datas.keySet();
            Map<String, Map<String, Integer>> secondLeverTemp;
            Vector<Object> vectorTemp = null;
            Map<String, Integer> rs = null;
            boolean showAvailable = true;

            //将每一个进程放入Vector中
            for (String key : keys) {
                String temp = " ";
                vectorTemp = new Vector<Object>();
                vectorTemp.add(key);

                //先取每个进程
                secondLeverTemp = datas.get(key);

                //每个进程中的资源情况
                rs = secondLeverTemp.get(Context.ALLOCATION);
                for (int i = 1; i < 5; i++) {
                    temp += rs.get("r" + i) + " ";
                }
                vectorTemp.add(temp);//ALLOCATION

                temp = "";
                rs = secondLeverTemp.get(Context.NEED);
                for (int i = 1; i < 5; i++) {
                    temp += rs.get("r" + i) + " ";
                }
                vectorTemp.add(temp);//NEED

                if (showAvailable) {
                    temp = "";
                    for (int i = 1; i < 5; i++) {
                        temp += availableResource.get("r" + i) + " ";
                    }
                    vectorTemp.add(temp);//Available
                    //设置只输入一次
                    showAvailable = false;
                }
                tableValues.add(vectorTemp);
            }

            //构建表的类型
            final DefaultTableModel defaultTableModel = new DefaultTableModel(tableValues, tableColumnNames);

            //创建table对象
            //初始化北部组件
            table_init = new JTable(defaultTableModel);
            jl_top = new JLabel("系统在T0时刻的资源分配");
            this.add(jl_top, "North");

            //初始化中部组件
            jp_top = new JPanel();
            jsp = new JScrollPane(table_init);
            jp_top.add(jsp);
            this.add(jp_top, "Center");

            //初始化南部组件
            jp_bottom = new JPanel(new GridLayout(1, 3));
            jl_bottom = new JLabel("检查T0时刻系统的安全性：");
            jb_check = new JButton("安全检查");

            //注册监听
            jb_check.addActionListener(this);
            jb_exit = new JButton("退出程序");

            //注册监听
            jb_exit.addActionListener(this);
            jp_bottom.add(jl_bottom);
            jp_bottom.add(jb_check);
            jp_bottom.add(jb_exit);
            this.add(jp_bottom, "South");

            //设置窗口
            setWindowLocation();
            this.setSize(600, 350);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setVisible(true);
        }

    }

    private void setWindowLocation() {
        int h = 600;
        int w = 350;

        Toolkit tk = this.getToolkit();
        Dimension dm = tk.getScreenSize();
        this.setLocation((int) (dm.getWidth() - w) / 2, (int) (dm.getHeight() - h) / 2);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jb_check) {
            //新建一个窗口
            new BankFrame2(datas, availableResource, null);
        } else if (e.getSource() == jb_exit) {
            //关闭窗口
            this.dispose();
        }
    }

    private void initDatas() {  //初始化所有资源，进程的数据

        //进程p0的初始化数据 allocation,need
        Map<String, Map<String, Integer>> pMap = new HashMap<String, Map<String, Integer>>();
        Map<String, Integer> rs = new HashMap<String, Integer>();
        rs.put(Context.R1, 0);
        rs.put(Context.R2, 0);
        rs.put(Context.R3, 3);
        rs.put(Context.R4, 2);
        pMap.put(Context.ALLOCATION, rs);

        rs = new HashMap<String, Integer>();
        rs.put(Context.R1, 0);
        rs.put(Context.R2, 0);
        rs.put(Context.R3, 1);
        rs.put(Context.R4, 2);
        pMap.put(Context.NEED, rs);
        datas.put(Context.P0, pMap);

        //进程p1的初始化数据 allocation,need
        pMap = new HashMap<String, Map<String, Integer>>();
        rs = new HashMap<String, Integer>();
        rs.put(Context.R1, 1);
        rs.put(Context.R2, 0);
        rs.put(Context.R3, 0);
        rs.put(Context.R4, 0);
        pMap.put(Context.ALLOCATION, rs);

        rs = new HashMap<String, Integer>();
        rs.put(Context.R1, 1);
        rs.put(Context.R2, 7);
        rs.put(Context.R3, 5);
        rs.put(Context.R4, 0);
        pMap.put(Context.NEED, rs);
        datas.put(Context.P1, pMap);

        // 进行p2的初始化数据    allocation,need
        pMap = new HashMap<String, Map<String, Integer>>();
        rs = new HashMap<String, Integer>();
        rs.put(Context.R1, 1);
        rs.put(Context.R2, 3);
        rs.put(Context.R3, 5);
        rs.put(Context.R4, 4);
        pMap.put(Context.ALLOCATION, rs);

        rs = new HashMap<String, Integer>();
        rs.put(Context.R1, 2);
        rs.put(Context.R2, 3);
        rs.put(Context.R3, 5);
        rs.put(Context.R4, 6);
        pMap.put(Context.NEED, rs);
        datas.put(Context.P2, pMap);

        // 进行p3的初始化数据    allocation,need
        pMap = new HashMap<String, Map<String, Integer>>();
        rs = new HashMap<String, Integer>();
        rs.put(Context.R1, 0);
        rs.put(Context.R2, 3);
        rs.put(Context.R3, 3);
        rs.put(Context.R4, 2);
        pMap.put(Context.ALLOCATION, rs);

        rs = new HashMap<String, Integer>();
        rs.put(Context.R1, 0);
        rs.put(Context.R2, 6);
        rs.put(Context.R3, 5);
        rs.put(Context.R4, 2);
        pMap.put(Context.NEED, rs);
        datas.put(Context.P3, pMap);

        // 进行p4的初始化数据    allocation,need
        pMap = new HashMap<String, Map<String, Integer>>();
        rs = new HashMap<String, Integer>();
        rs.put(Context.R1, 0);
        rs.put(Context.R2, 0);
        rs.put(Context.R3, 1);
        rs.put(Context.R4, 4);
        pMap.put(Context.ALLOCATION, rs);

        rs = new HashMap<String, Integer>();
        rs.put(Context.R1, 0);
        rs.put(Context.R2, 6);
        rs.put(Context.R3, 5);
        rs.put(Context.R4, 6);
        pMap.put(Context.NEED, rs);
        datas.put(Context.P4, pMap);

        //定义系统当前可用的资源数量
        availableResource.put(Context.R1, 1);
        availableResource.put(Context.R2, 6);
        availableResource.put(Context.R3, 2);
        availableResource.put(Context.R4, 2);

    }

}
