/*This is a small java GUI application,
* for calculating the precise PE-score according to the given critera.
*
* All the code is in this file.
*
* Author: zhendong.nus@gmail.com
* License: MIT
* Date: Jan 9 2017
*
* */


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.util.*;
import java.util.List;

public class Main extends JPanel implements ActionListener {

    private JTextField display = new JTextField("0");
    private double result = 0;
    private String operator = "=";
    private boolean calculating = true;

    private JTextArea textArea1 = new JTextArea("100 2.29\n" +
            "96 2.26 \n" +
            "90 2.2\n" +
            "87 2.17\n" +
            "81 2.10\n" +
            "75 1.95\n" +
            "72 1.92\n" +
            "66 1.81\n" +
            "60 1.67");
    private JTextArea textArea2 = new JTextArea("1\n" +
            "1.67\n" +
            "1.81\n" +
            "2.27\n" +
            "2.29\n" +
            "3.5");
    private JTextArea textArea3 = new JTextArea("");

    private String[] criteraName;
    private String[] criteraContent;
    private JPanel buttonPanel;
    private int countTop;
    public Main() {
        setLayout(new BorderLayout());



        textArea1.setEditable(true);

        // Set textArea border, now its thickness is 0, so invisible.
        textArea1.setBorder(new LineBorder(new java.awt.Color(127,157,185), 0, true));
        textArea2.setBorder(new LineBorder(new java.awt.Color(127,157,185), 0, true));
        textArea3.setBorder(new LineBorder(new java.awt.Color(127,157,185), 0, true));
        textArea1.setMargin(new Insets(5,5,5,5));
        textArea2.setMargin(new Insets(5,5,5,5));
        textArea3.setMargin(new Insets(5,5,5,5));

        // Add each textArea a scroll bar.
        JScrollPane scroll1 = new JScrollPane ( textArea1 );
        scroll1.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );
        JScrollPane scroll2 = new JScrollPane ( textArea2 );
        scroll2.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );
        JScrollPane scroll3 = new JScrollPane ( textArea3 );
        scroll3.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );

        // The main panel.
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3, 5, 5));

        // The left panel. Y-box-layout
        JPanel innerLeftPanel = new JPanel();
        innerLeftPanel.setLayout(new BoxLayout(innerLeftPanel, BoxLayout.Y_AXIS));

        // The panel contains all item panels. Y-box-layout
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        // Load all the criteras from disk.
        String a = readFromFile();

        if (!a.equals("")) {
            String load[] = a.split("@@@");
            criteraName = new String[load.length - 1];
            criteraContent = new String[load.length - 1];

            // Dynamically add buttons.
            countTop = criteraName.length;
            for (int i = 1; i < load.length; i++) {
                String temp[] = load[i].split("&&&");
                String name = temp[0].trim();
                String content;
                if (temp.length >= 2)
                    content = temp[1].trim();
                else
                    content = new String("");
                criteraName[i - 1] = name;
                criteraContent[i - 1] = content;

                // Create a button with the critera name.
                JButton cre1Button = new JButton(name);
                cre1Button.setMaximumSize(new Dimension(Integer.MAX_VALUE, cre1Button.getMinimumSize().height));

                // Create a small size delete botton.
                JButton delete1Button = new JButton("删除" + i);
                delete1Button.setMaximumSize(new Dimension(68, 26));
                delete1Button.setPreferredSize(new Dimension(68, 26));
                delete1Button.setMinimumSize(new Dimension(68, 26));

                // Listen to the two buttons action.
                cre1Button.addActionListener(this);
                delete1Button.addActionListener(this);

                // The panel contains a button and a delete. X-box-layout
                JPanel itemPanel = new JPanel();
                itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.X_AXIS));

                itemPanel.add(cre1Button);
                itemPanel.add(delete1Button);

                buttonPanel.add(itemPanel);
            }

            for (int i = 0; i < criteraContent.length; i++) {
                System.out.println(criteraName[i]);
                System.out.println(criteraContent[i]);
            }
        }
        JScrollPane buttonScroll = new JScrollPane(buttonPanel);
        innerLeftPanel.add(buttonScroll);
        innerLeftPanel.add(scroll1);

        JButton saveButton = new JButton("保存评分表");
        saveButton.addActionListener(this);
        saveButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, saveButton.getMinimumSize().height));
        innerLeftPanel.add(saveButton);

        panel.add(innerLeftPanel);
        panel.add(scroll2);
        panel.add(scroll3);

        add(panel, "Center");

        JButton b = new JButton("点我计算");
        b.addActionListener(this);
        add(b, "South");

        JTextArea info = new JTextArea("本程序根据评分表和对学生的测量结果，计算出精确的体育单项成绩分数。" +
                "\n\n使用步骤：" +
                "\n1. 将评分表输入第一个输入栏，每行两个值，第一个是分数，第二个是分数对应的测量值" +
                "\n2. 将测量结果复制到第二个输入栏，每行一个值。 (复制黏贴请使用键盘Ctrl+c 和 Ctrl+v)" +
                "\n3. 点击计算按钮，结果将显示在第三栏中" +
                "\n4. 请注意输入数据除数字，小数点，空格和换行符外，请勿掺杂其他字符！" +
                "\n\n作者：zhendong.nus@gmail.com");
        info.setEditable(false);
        info.setBackground(new Color(238, 238, 238));
        info.setMargin(new Insets(5,5,5,5));
        add(info, "North");
    }

    public void actionPerformed(ActionEvent evt) {
        String cmd = evt.getActionCommand();
        if (cmd.equals("点我计算")) {
            String content1 = textArea1.getText();
            String content2 = textArea2.getText();
            String result = calculate(content1, content2);
            textArea3.setText(result);
        } else if (cmd.equals("保存评分表")) {
            String c = textArea1.getText();
            String userInput = new String("未命名");
            userInput = JOptionPane.showInputDialog( "给评分表起个名字吧" );
            // Handle cancelled dialog.
            if (userInput == null) return;
            // Name can be empty.
            while (userInput.startsWith("删除") || userInput.equals("")) {
                userInput = JOptionPane.showInputDialog( "评分表名不能为空" );
            }
            // Add the new critera name into critera name list.
            if (criteraName == null) criteraName = new String[0];
            String temp[] = new String[0];
            temp = criteraName.clone();
            criteraName = new String[temp.length + 1];
            for (int i = 0; i < temp.length; i++) {
                criteraName[i] = temp[i];
            }
            criteraName[criteraName.length - 1] = userInput;

            // Add the new critera content into critera content list.
            if (criteraContent == null) criteraContent = new String[0];
            temp = criteraContent.clone();
            criteraContent = new String[temp.length + 1];
            for (int i = 0; i < temp.length; i++) {
                criteraContent[i] = temp[i];
            }
            criteraContent[criteraContent.length - 1] = c;

            // Add a button item for this critera.
            // Create a button with the critera name.
            JButton cre1Button = new JButton(userInput);
            cre1Button.setMaximumSize(new Dimension(Integer.MAX_VALUE, cre1Button.getMinimumSize().height));

            // Create a small size delete botton.
            JButton delete1Button = new JButton("删除" + (++countTop));
            delete1Button.setMaximumSize(new Dimension(68, 26));
            delete1Button.setPreferredSize(new Dimension(68, 26));
            delete1Button.setMinimumSize(new Dimension(68, 26));

            // Listen to the two buttons action.
            cre1Button.addActionListener(this);
            delete1Button.addActionListener(this);

            // The panel contains a button and a delete. X-box-layout
            JPanel itemPanel = new JPanel();
            itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.X_AXIS));

            itemPanel.add(cre1Button);
            itemPanel.add(delete1Button);

            buttonPanel.add(itemPanel);
            // Still don't know whether needs this.
            buttonPanel.invalidate();

            // Refresh the panel UI.
            buttonPanel.updateUI();
            appendToFile("@@@" + userInput + "\n&&&" + c);

        } else if (cmd.startsWith("删除")) {
            int index = Integer.parseInt(cmd.substring(cmd.length() - 1));

            // Since index in the delete button is start from 1.
            index--;

            System.out.println(index);
            // Delete the critera name from name list.
            String temp[] = new String[0];
            temp = criteraName.clone();
            criteraName = new String[temp.length - 1];
            int j = 0;
            for (int i = 0; i < temp.length; i++) {
                if (i != index) {
                    criteraName[j++] = temp[i];
                }
            }

            // Delete the critera content from content list.
            temp = criteraContent.clone();
            criteraContent = new String[temp.length - 1];
            j = 0;
            for (int i = 0; i < temp.length; i++) {
                if (i != index) {
                    criteraContent[j++] = temp[i];
                }
            }
            // Clean the button panel for later re-load.
            buttonPanel.removeAll();

            // Reload the button items.
            countTop = criteraName.length;
            for (int i = 0; i < criteraContent.length; i++) {
                String name = criteraName[i];

                // Create a button with the critera name.
                JButton cre1Button = new JButton(name);
                cre1Button.setMaximumSize(new Dimension(Integer.MAX_VALUE, cre1Button.getMinimumSize().height));

                // Create a small size delete botton.
                JButton delete1Button = new JButton("删除" + (i + 1));
                delete1Button.setMaximumSize(new Dimension(40, 20));
                delete1Button.setPreferredSize(new Dimension(40, 20));
                delete1Button.setMinimumSize(new Dimension(40, 20));

                // Listen to the two buttons action.
                cre1Button.addActionListener(this);
                delete1Button.addActionListener(this);

                // The panel contains a button and a delete. X-box-layout
                JPanel itemPanel = new JPanel();
                itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.X_AXIS));

                itemPanel.add(cre1Button);
                itemPanel.add(delete1Button);

                buttonPanel.add(itemPanel);
            }
            buttonPanel.invalidate();
            buttonPanel.updateUI();

            // Empty the textArea1.
            textArea1.setText("");

            // Resfresh the critera file on disk.
            String out = new String();
            for (int i = 0; i < criteraName.length; i++) {
                out += "@@@" + criteraName[i] + "\n" + "&&&" + criteraContent[i] + "\n";
            }
            // Save to file without the Last '\n'.
            writeToFile(out.trim());

        } else {
            // find the cmd in critera names and set textArea1
            for (int i = 0; i < criteraName.length; i++) {
                if (cmd.equals(criteraName[i])) {
                    textArea1.setText(criteraContent[i]);
                    break;
                }
            }
        }

    }

    private String calculate(String critera, String measure) {
        // Get the critera from textArea1
        String lines[] = critera.trim().split("\\n+");
        double[][] criteraList = new double[lines.length][2];
        for (int i = 0; i < lines.length; i++) {
            String items[] = lines[i].trim().split("\\s+");
            if (items.length < 2) {
                JOptionPane.showMessageDialog(this, "评分表输入格式有误。正确格式为每行两个数。");
                return "";
            }
            criteraList[i][0] = Double.parseDouble(items[0]);
            criteraList[i][1] = Double.parseDouble(items[1]);
        }
        Comparator<double[]> doubleComparator = new Comparator<double[]>() {
            public int compare(double[] a, double[] b) {
                return Double.compare(a[0], b[0]);
            }
        };
        Arrays.sort(criteraList, doubleComparator);
        // Get the measurement data from textArea2
        lines = measure.split("\\n");
        double[] measureList = new double[lines.length];
        int countEmptyLines = 0;
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].trim().matches("[-+]?\\d*\\.?\\d+"))
                measureList[i] = Double.parseDouble(lines[i].trim());
            else if (lines[i].trim().matches("\\s*")){
                measureList[i] = 0;
                countEmptyLines++;
            } else {
                JOptionPane.showMessageDialog(this, "输入包含数字和空格以外的内容！");
                return "";
            }
        }
        if (0 != countEmptyLines)
            JOptionPane.showMessageDialog(this, "请注意您的第二栏输入有" +
                            Integer.toString(countEmptyLines) + "处空行，将在对应行输出结果0。");
        // Calculate the result
        double ret[] = new double[measureList.length];
        for (int i = 0; i < measureList.length; i++) {
            double ans = 0, x = measureList[i];
            int j = 0;
            while (j < criteraList.length && criteraList[j][1] <= x) j++;
            if (0 == j) {
                ans = 0;
            } else if (criteraList.length == j) {
                ans = criteraList[criteraList.length - 1][0];
            } else {
                ans = criteraList[j - 1][0] + (criteraList[j][0] - criteraList[j - 1][0])
                        /(criteraList[j][1] - criteraList[j - 1][1]) * (x - criteraList[j - 1][1]);
            }
            ret[i] = ans;
        }
        String retString = new String();
        for (int i = 0; i < ret.length; i++) {
            retString += String.format(new String("%.4f"), ret[i]) + "\n";
        }
        return retString;
    }

    private void appendToFile(String in) {
        List<String> lines = new ArrayList<String>();
        lines.add(in);
        try {
            Files.write(Paths.get("data.txt"), lines, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToFile(String in) {
        List<String> lines = new ArrayList<String>();
        lines.add(in);
        try {
            Files.write(Paths.get("data.txt"), lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readFromFile() {
        List<String> lines;
        String ret = new String();
        try {
            lines = Files.readAllLines(Paths.get("data.txt"));
            //textArea1.setText(lines.get(0));
            for (String line : lines) {
                ret += line + '\n';
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return ret;
    }

    public static void main(String[] args) {
        //JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame();
        frame.setTitle("体育分数计算器");
        frame.setSize(600, 600);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        Container contentPane = frame.getContentPane();
        contentPane.add(new Main());
        frame.show();
        //frame.invalidate();
    }
}