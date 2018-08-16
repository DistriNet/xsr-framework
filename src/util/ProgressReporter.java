package util;

import TestFunctionality.RemoteBrowserThread;
import TestFunctionality.Tests.TestName;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.List;

public class ProgressReporter {

    private Map<RemoteBrowserThread, JProgressBar> progressBars;
    private Map<RemoteBrowserThread, Integer> progress;
    private Map<RemoteBrowserThread, Integer> numberOfTests;
    private Map<RemoteBrowserThread, String> status;
    private DefaultTableModel table;
    private JFrame frame;

    public ProgressReporter(Collection<RemoteBrowserThread> threads) {
        progressBars = new LinkedHashMap<>();
        progress = new LinkedHashMap<>();
        numberOfTests = new LinkedHashMap<>();
        status = new LinkedHashMap<>();
        for (RemoteBrowserThread browserThread : threads) {
            progress.put(browserThread, 0);
            numberOfTests.put(browserThread, browserThread.getNumberOfTests());
            status.put(browserThread, "REGISTERED");
        }
        createInfoWindow(threads);
    }

    /*public void addThread(RemoteBrowserThread browserThread) {
        JProgressBar progressBar = new JProgressBar(0, 100);
        this.progressBars.put(browserThread, progressBar);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        Border border = BorderFactory.createTitledBorder(browserThread.toString());
        progressBar.setBorder(border);
        frame.getContentPane().add(progressBar, BorderLayout.NORTH);

        int columns = table.getColumnCount();
        Vector<String> row = new Vector<>();
        for (int i = 0; i < columns - 1; i++)
            row.add("-");
        row.add(0, browserThread.toString());
        table.addRow(row);

        // Update other variables
        numberBrowserThreads++;
        progress.put(browserThread, 0);
        numberOfTests.put(browserThread, browserThread.getNumberOfTests());
        status.put(browserThread, "REGISTERED");
    }*/

    private void createInfoWindow(Collection<RemoteBrowserThread> threads) {
        this.frame = new JFrame("Framework: progress and intermediary results");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        Container content = frame.getContentPane();

        // Progress bars
        for (RemoteBrowserThread browserThread : threads) {
            JProgressBar progressBar = new JProgressBar(0, 100);
            this.progressBars.put(browserThread, progressBar);
            progressBar.setValue(0);
            progressBar.setStringPainted(true);
            Border border = BorderFactory.createTitledBorder(browserThread.toString());
            progressBar.setBorder(border);
            content.add(progressBar, BorderLayout.NORTH);
        }

        // Table
        JTable table = new JTable();
        JScrollPane pane = new JScrollPane(table);
        pane.setBounds(0, 0, 880, 200);
        frame.add(pane);
        Object[] columns = { "Extension", "appcache", "header-link", "header-csp", "pdf", "redirect", "script", "static", "sw" };
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        table.setModel(model);
        table.getColumnModel().getColumn(0).setMinWidth(600);
        pane.setPreferredSize(new Dimension(1200, 500));

        table.setBackground(Color.LIGHT_GRAY);
        table.setForeground(Color.black);
        Font font = new Font("",1,18);
        table.setFont(font);
        table.setRowHeight(30);

        Vector<String> row;

        for (RemoteBrowserThread browserThread : threads) {
            row = new Vector<>();
            for (int i = 0; i < columns.length - 1; i++)
                row.add("-");
            row.add(0, browserThread.toString());
            model.addRow(row);

        }

        // Sorter for table
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);

        List<RowSorter.SortKey> sortKeys = new ArrayList<>(1);
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);

        this.table = model;

        frame.setSize(1200, 500);
        frame.setVisible(true);
    }

    public synchronized void started (RemoteBrowserThread thread) {
        status.put(thread, "STARTED");
    }

    public synchronized void testDone(RemoteBrowserThread thread, TestName test, int numberOfLeaks) {
        // Update progress bar
        progress.put(thread, progress.get(thread) + 1);
        JProgressBar progressBar = progressBars.get(thread);
        int newValue = (int) ( (double) progress.get(thread) / (double) numberOfTests.get(thread) * 100);
        progressBar.setValue(newValue);

        // Update table
        outer:
        for (int i = 0; i < table.getRowCount(); i++) {
            if (thread.toString().equals(table.getValueAt(i, 0))) {
                for (int j = 1; j < table.getColumnCount(); j++) {
                    if (test.toString().equals(table.getColumnName(j))) {
                        table.setValueAt(numberOfLeaks, i, j);
                        break outer;
                    }
                }
            }
        }
    }

    public synchronized void finished(RemoteBrowserThread thread) {
        status.put(thread, "FINISHED");
        if (isCompletelyFinished())
            exportTableToCSV(thread.getRegister().getBaseFolder());
    }

    private boolean isCompletelyFinished() {
        for (String status : status.values())
            if (!status.equals("FINISHED"))
                return false;
        return true;
    }

    public boolean exportTableToCSV(File baseFolder) {
        try {
            File file = new File(baseFolder + "/_overview.csv");
            if (!file.exists())
                Files.createFile(file.toPath());

            FileWriter csv = new FileWriter(file);

            for (int i = 0; i < table.getColumnCount(); i++) {
                csv.write(table.getColumnName(i) + ",");
            }

            csv.write("\n");

            ArrayList<String> overview = new ArrayList<>();

            String line;
            for (int i = 0; i < table.getRowCount(); i++) {
                line = "";
                for (int j = 0; j < table.getColumnCount(); j++) {
                    line += table.getValueAt(i, j).toString() + ",";
                }
                line += '\n';
                overview.add(line);
            }

            Collections.sort(overview);

            for (String toWrite : overview)
                csv.write(toWrite);

            csv.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
