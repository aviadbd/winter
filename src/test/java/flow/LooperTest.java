package flow;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.*;

/**
 * Created by Tzachi on 12/27/14.
 */
public class LooperTest {

    private final BlockingQueue<WorkUnit<Data>> blockingQueue = new LinkedBlockingQueue<WorkUnit<Data>>();
    private ExecutorService workUnitThreadPool;
    private ExecutorService looperExecutorService;

    private LogicUnit<Data> doNothing;
    private LogicUnit<Data> increaseOnce;
    private LogicUnit<Data> increaseTwice;


    @BeforeTest
    @SuppressWarnings("unchecked")
    public void beforeAll(){
        doNothing = Mockito.mock(LogicUnit.class);
        increaseOnce = Mockito.mock(LogicUnit.class);
        increaseTwice = Mockito.mock(LogicUnit.class);

        Mockito.doAnswer(new Answer<Data[]>() {
            @Override
            public Data[] answer(InvocationOnMock invocation) throws Throwable {
                Data data = (Data) invocation.getArguments()[0];
                return new Data[] { data };
            }
        });
        Mockito.doAnswer(new Answer<Data[]>() {
            @Override
            public Data[] answer(InvocationOnMock invocation) throws Throwable {
                Data data = (Data) invocation.getArguments()[0];
                data.increaseCounter();
                return new Data[] { data };
            }}).when(increaseOnce).execute(Mockito.any(Data.class));
        Mockito.doAnswer(new Answer<Data[]>() {
            @Override
            public Data[] answer(InvocationOnMock invocation) throws Throwable {
                Data data = (Data) invocation.getArguments()[0];
                data.increaseCounter();
                data.increaseCounter();
                return new Data[] { data };
            }}).when(increaseTwice).execute(Mockito.any(Data.class));
    }


    @BeforeMethod
    public void beforeMethod(){
        workUnitThreadPool = Executors.newFixedThreadPool(1);
        looperExecutorService = Executors.newFixedThreadPool(1);
        blockingQueue.clear();
    }

    @SuppressWarnings("unchecked")
    @Test(timeOut = 4000)
    public void noWork_shutdown() throws InterruptedException {
        RouteUnit<Data> entry = Mockito.mock(RouteUnit.class);
        Looper<Data> looper = new Looper<Data>(entry, blockingQueue, workUnitThreadPool);
        looperExecutorService.submit(looper);
        Thread.sleep(100);
        looper.setShutdown();
        looperExecutorService.shutdown();
        looperExecutorService.awaitTermination(5000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void singleRouteUnit() throws InterruptedException {
        final RouteUnit<Data> ru = new RouteUnit<Data>(increaseOnce) {
            @Override
            public Collection<RouteUnit<Data>> calculateRoutesFor(WorkUnit<Data> work) {
                return Collections.emptyList();
            }
        };
        Looper<Data> looper = new Looper<Data>(ru, blockingQueue, workUnitThreadPool);
        Data data = new Data(0);
        blockingQueue.add(new WorkUnit<Data>(data));
        looperExecutorService.submit(looper);
        Thread.sleep(100);
        looper.setShutdown();
        looperExecutorService.shutdown();
        looperExecutorService.awaitTermination(5000, TimeUnit.MILLISECONDS);
        Assert.assertEquals(data.getCounter(), 1);
    }

    @Test
    public void multipleRouteUnit_sameLogicUnit() throws InterruptedException {
        final RouteUnit<Data> ru2 = new RouteUnit<Data>(increaseOnce) {
            @Override
            public Collection<RouteUnit<Data>> calculateRoutesFor(WorkUnit<Data> work) {
                return Collections.emptyList();
            }
        };
        final RouteUnit<Data> ru = new RouteUnit<Data>(increaseOnce) {
            @Override
            public Collection<RouteUnit<Data>> calculateRoutesFor(WorkUnit<Data> work) {
                return Collections.singleton(ru2);
            }
        };
        Looper<Data> looper = new Looper<Data>(ru, blockingQueue, workUnitThreadPool);
        Data data = new Data(0);
        blockingQueue.add(new WorkUnit<Data>(data));
        looperExecutorService.submit(looper);
        Thread.sleep(100);
        looper.setShutdown();
        looperExecutorService.shutdown();
        looperExecutorService.awaitTermination(5000, TimeUnit.MILLISECONDS);
        Assert.assertEquals(data.getCounter(), 2);
    }

    @Test
    public void multipleRouteUnit_differentLogicUnit() throws InterruptedException {
        final RouteUnit<Data> ru2 = new RouteUnit<Data>(increaseTwice) {
            @Override
            public Collection<RouteUnit<Data>> calculateRoutesFor(WorkUnit<Data> work) {
                return Collections.emptyList();
            }
        };
        final RouteUnit<Data> ru = new RouteUnit<Data>(increaseOnce) {
            @Override
            public Collection<RouteUnit<Data>> calculateRoutesFor(WorkUnit<Data> work) {
                return Collections.singleton(ru2);
            }
        };
        Looper<Data> looper = new Looper<Data>(ru, blockingQueue, workUnitThreadPool);
        Data data = new Data(0);
        blockingQueue.add(new WorkUnit<Data>(data));
        looperExecutorService.submit(looper);
        Thread.sleep(100);
        looper.setShutdown();
        looperExecutorService.shutdown();
        looperExecutorService.awaitTermination(5000, TimeUnit.MILLISECONDS);
        Assert.assertEquals(data.getCounter(), 3);
    }

    @Test
    // this test is valid only when workUnitThreadPool limited to 1
    public void singleRouteUnit_multipleResults() throws InterruptedException {
        final RouteUnit<Data> ru2 = new RouteUnit<Data>(increaseTwice) {
            @Override
            public Collection<RouteUnit<Data>> calculateRoutesFor(WorkUnit<Data> work) {
                return Collections.emptyList();
            }
        };
        final RouteUnit<Data> ru = new RouteUnit<Data>(increaseOnce) {
            @Override
            public Collection<RouteUnit<Data>> calculateRoutesFor(WorkUnit<Data> work) {
                return Collections.emptyList();
            }
        };

        RouteUnit<Data> entry = new RouteUnit<Data>(doNothing) {
            @Override
            public Collection<RouteUnit<Data>> calculateRoutesFor(WorkUnit<Data> work) {
                return Arrays.asList(ru, ru2);
            }
        };

        Looper<Data> looper = new Looper<Data>(entry, blockingQueue, workUnitThreadPool);
        Data data = new Data(0);
        blockingQueue.add(new WorkUnit<Data>(data));
        looperExecutorService.submit(looper);
        Thread.sleep(100);
        looper.setShutdown();
        looperExecutorService.shutdown();
        looperExecutorService.awaitTermination(5000, TimeUnit.MILLISECONDS);
        Assert.assertEquals(data.getCounter(), 3);
    }

    @Test
    public void singleRouteUnit_emptyResults() throws InterruptedException {
        RouteUnit<Data> entry = new RouteUnit<Data>(doNothing) {
            @Override
            public Collection<RouteUnit<Data>> calculateRoutesFor(WorkUnit<Data> work) {
                return Collections.emptyList();
            }
        };
        Looper<Data> looper = new Looper<Data>(entry, blockingQueue, workUnitThreadPool);
        Data data = new Data(0);
        blockingQueue.add(new WorkUnit<Data>(data));
        looperExecutorService.submit(looper);
        Thread.sleep(100);
        looper.setShutdown();
        looperExecutorService.shutdown();
        looperExecutorService.awaitTermination(5000, TimeUnit.MILLISECONDS);
        Assert.assertEquals(data.getCounter(), 0);
    }


    static class Data{
        private int counter;
        public Data(int counter) {
            this.counter = counter;
        }
        public void increaseCounter(){
            counter++;
        }
        public int getCounter() {
            return counter;
        }
    }
}
