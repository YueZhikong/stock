package org.yanyang.test.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DemoApplication.class).web(WebApplicationType.NONE).run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        String basePath = "D:\\files";
        String[] list = new File(basePath).list();
        for (int i = 0; i < list.length; i++) {
            readFileByLine(basePath+"\\"+list[i]);
        }
    }

    public static void readFileByLine(String strFile){
        try {
            File file = new File(strFile);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String strLine = null;
            int lineCount = 1;
            while(null != (strLine = bufferedReader.readLine())){
                if (lineCount==1){
                    String[] aaa = strLine.split(" ");
                }
                else if (lineCount==2){

                }
                else {
                    String[] sss = strLine.split(",");
                }
                lineCount++;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
