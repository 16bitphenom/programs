/**
 * Created by Sudhabindu on 18-Jun-17.
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
public class LineCounter {

    public static void main(String args[]){
        if (args.length < 1){
            System.out.println("Usage: java LineCount fileName1 | fileName2 | ....");
            return;
        }

        File file;
        Scanner sc;
        BufferedReader br;
        FileInputStream fs;
        int lineCount;

        for(int i = 0; i < args.length; i++){
            lineCount = 0;
            String fileName = args[i];
            file = new File(fileName);
            if (file.isDirectory()){
                System.out.println("The filename " + fileName + " refers to a directory");
            }
            else if (file.exists() == false){
                System.out.println("The file " + fileName + " doesnot exist");
            }
            else{
                try {
                    sc = new Scanner(new FileInputStream(file));
                    while(true){
                        if (sc.hasNextLine() == false){
                            break;
                        }
                        sc.nextLine();
                        lineCount++;
                    }
                    System.out.println(fileName + ": " + lineCount);

                }
                catch(IOException e){
                    System.out.println("Error: " + e);
                }
            }
        }

    }
}
