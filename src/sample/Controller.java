package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Controller {


        /* TO DO:
        - Why aren't the proxies added right before being tested?
        */

        @FXML private TextArea TEXTAREAProxy;
        @FXML private TextArea TEXTAREAWorkingProxy;
        @FXML private TextField TEXTFIELDtimeOut;
        @FXML private TextField TEXTFIELDnumberThreads;
        @FXML private TextField TEXTFIELDurl;

        public void doSomething(){

            Loop l = new Loop();

            int numOfThreads = Integer.parseInt(TEXTFIELDnumberThreads.getText());
            int timeOut = Integer.parseInt(TEXTFIELDtimeOut.getText());

            String url = TEXTFIELDurl.getText();
            String urlF = "https://";


            if (!url.contains("https") && !(url.contains("http"))) {
                urlF = urlF.concat(url);
                System.out.println(urlF);
            } else {
                urlF = url;
            }

            final String urlFINAL = urlF;

            class Processor implements Runnable {

                private volatile int id;

                public Processor(int id) {
                    this.id = id;
                }

                @Override
                public void run() {
                    try {
                        l.executeLoop(TEXTAREAProxy, TEXTAREAWorkingProxy, id, (numOfThreads - 1), urlFINAL, timeOut); // 4 = ThreadsNum - 1
                        Thread.sleep(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            ExecutorService executor = Executors.newFixedThreadPool(numOfThreads);

            for (int i = 1; i <= numOfThreads; i++) {
                executor.submit(new Processor(i));
            }

            executor.shutdown();
            try {
                executor.awaitTermination(1, TimeUnit.DAYS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
}
