package com.company;

import java.io.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.Collections;

public class Main {
    //public static void HprofParse();

    public static void main(String[] args) throws IOException, InterruptedException {

        JButton buttonReload = new JButton("Reload");
        buttonReload.setBounds(35,60,100, 50);
        JButton buttonClose = new JButton("Close");
        buttonClose.setBounds(160,60,100, 50);

        JFrame frame = new JFrame();
        frame.add(buttonReload);
        frame.add(buttonClose);
        frame.setSize(300,200);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buttonReload.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                HprofParse();
            }
        });

        buttonClose.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                frame.dispose();
            }
        });
    }

    public static void HprofParse(){
        File sourcePath = new File("PathOfSource.txt");
        File destination = new File("ParsedHprof.txt");
        Charset charset = Charset.forName("US-ASCII");
        try {
            BufferedReader readerPath =
                    Files.newBufferedReader(sourcePath.toPath(), charset);
            File source = new File(readerPath.readLine());

            BufferedReader reader =
                    Files.newBufferedReader(source.toPath(), charset);
            BufferedWriter writer =
                    Files.newBufferedWriter(destination.toPath(), charset);

            String line = null, tabs = null, subs = null;
            int tabNum = 0, index = 0;

            while ((line = reader.readLine()) != null) {
                subs = line.substring(0,3);
                if(subs.equals("P#C")){
                    tabs = String.join("",
                            Collections.nCopies(tabNum, "\t"));
                    index = line.indexOf("#",line.lastIndexOf("\""));
                    if (index > 4){
                        writer.write(tabs + line.substring(4, index));
                    }
                    else {
                        writer.write(tabs + line.substring(4));
                    }

                    writer.newLine();
                    tabNum++;
                }
                else if(subs.equals("P#R")){
                    tabNum--;
                }
            }
            writer.flush();
            writer.close();
            reader.close();
            readerPath.close();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }
}