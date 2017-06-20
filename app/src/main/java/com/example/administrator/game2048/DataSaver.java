package com.example.administrator.game2048;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Administrator on 2017/04/27.
 */

public class DataSaver {

    private static DataSaver dataSaver = null;

    public static DataSaver getDataSaver() {
        return dataSaver;
    }

    public String readFile() {

        String tempString = null;
        String filename = "data.txt";
        BufferedReader reader = null;
        //File file = new File(filename);

        try {
            reader = new BufferedReader(new FileReader(filename));
            //以行读取数据
            tempString = reader.readLine();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    //一定要注意关闭文件流
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return tempString;
    }

}
