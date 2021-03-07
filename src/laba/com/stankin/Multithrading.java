package laba.com.stankin;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Multithrading {
    private final static int THREAD_COUNT = 4;

    public static void main(String[] args) throws IOException {
        List<File> fileList = Collections.synchronizedList(new ArrayList<>());
        searchFiles(new File("C:\\TXT\\"), fileList);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Введите строку для поиска");
        String findline = reader.readLine();
        ExecutorService service = Executors.newFixedThreadPool(THREAD_COUNT);
        for (int i = 0; i < THREAD_COUNT; i++){
            service.execute(new Runnable() {
                public void run() {
                    while (true){
                        try {
                            File file = fileList.remove(0);
                            readLiner(findline,file);

                        } catch (IndexOutOfBoundsException e) {
                            if(fileList != null)
                                break;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            Thread.sleep(500);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        service.shutdown();
        System.out.println(fileList);
    }

    public static <file> void readLiner(String findline, File file) throws Exception
    {
        String line;
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        while ((line = bufferedReader.readLine()) != null) {
            if (line.equals(findline)) {                         //проверка на строку
                System.out.println("Строка "+line +" найдена в "+ file.getPath() + " при помощи " + Thread.currentThread().getName());
            }
        }
    }

    public static void searchFiles(File rootFile, List<File> fileList) //Поиск файлов
    {
        if (rootFile.isDirectory()) {
            File[] directoryFiles = rootFile.listFiles();
            if (directoryFiles != null) {
                for (File file : directoryFiles) {
                    if (file.isDirectory()) {
                        try {
                            searchFiles(file, fileList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (file.getName().toLowerCase().endsWith(".txt")) {
                            fileList.add(file);// Проверка на txt
                        }
                    }
                }
            }
        }
    }
}
