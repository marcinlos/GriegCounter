package pl.edu.agh.ki.grieg;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.io.AudioException;
import pl.edu.agh.ki.grieg.processing.core.Processor;

public class Scheduler {

    private static final Logger logger = LoggerFactory
            .getLogger(Scheduler.class);
    
    private final Deque<Future<?>> taskQueue;

    private final ExecutorService executor;
    private final ErrorHandler errorHandler;


    public Scheduler(ErrorHandler errorHandler) {
        this.taskQueue = new ArrayDeque<>();
        this.executor = Executors.newSingleThreadExecutor();
        this.errorHandler = errorHandler;
    }

    public void enqueue(Runnable action) {
        Future<?> handler = executor.submit(action);
        taskQueue.add(handler);
    }
    
    public void asyncPreAnalysis(Processor proc) {
        enqueue(new PreAnalysis(proc));
    }
    
    public void asyncAnalysis(Processor proc) {
        enqueue(new Analysis(proc));
    }

    private final class PreAnalysis implements Runnable {
        private final Processor proc;

        private PreAnalysis(Processor proc) {
            this.proc = proc;
        }

        @Override
        public void run() {
            logger.info("Gathering metadata");
            try {
                proc.preAnalyze();
                logger.info("Metadata gathered");
            } catch (AudioException e) {
                errorHandler.equals(e);
                logger.error("Error during preliminary analysis", e);
            } catch (IOException e) {
                errorHandler.equals(e);
                logger.error("Error during preliminary analysis", e);
            }
        }
    }

    private final class Analysis implements Runnable {
        private final Processor proc;

        private Analysis(Processor proc) {
            this.proc = proc;
        }

        @Override
        public void run() {
            try {
                logger.info("Beginning audio analysis");
                proc.analyze();
                logger.info("Audio analysis finished");
            } catch (AudioException e) {
                errorHandler.equals(e);
                logger.error("Error during the main analysis phase", e);
            } catch (IOException e) {
                errorHandler.equals(e);
                logger.error("Error during the main analysis phase", e);
            }
        }
    }

}
