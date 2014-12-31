package flow;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.*;

/**
 * Created by Tzachi on 12/27/14.
 *
 */
public class LooperTest {

    private BlockingQueue<WorkUnit<Data>> blockingQueue;
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
        }).when(doNothing).execute(Mockito.any(Data.class));
        Mockito.doAnswer(new Answer<Data[]>() {
            @Override
            public Data[] answer(InvocationOnMock invocation) throws Throwable {
                Data data = (Data) invocation.getArguments()[0];
                data.increaseCounter();
                return new Data[]{data};
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
        blockingQueue = new LinkedBlockingQueue<WorkUnit<Data>>();
    }

    @SuppressWarnings("unchecked")
    @Test(timeOut = 4000)
    public void noWork_shutdown() throws InterruptedException {
        RouteUnit<Data> entry = Mockito.mock(RouteUnit.class);
        Looper<Data> looper = new Looper<Data>(entry, blockingQueue, workUnitThreadPool);
        submitAndTerminate(looper);
    }

    @Test
    public void singleRouteUnit() throws InterruptedException {
        final RouteUnit<Data> ru = createFinalRoute(increaseOnce);
        Looper<Data> looper = new Looper<Data>(ru, blockingQueue, workUnitThreadPool);
        Data data = new Data(0);
        blockingQueue.add(new WorkUnit<Data>(data));
        submitAndTerminate(looper);
        Assert.assertEquals(data.getCounter(), 1);
    }

    @Test
    public void exceptionOnRoutingUnit_noCrushes() throws InterruptedException {
        final RouteUnit<Data> entry = new RouteUnit<Data>(increaseOnce) {
            @Override
            public Collection<RouteUnit<Data>> calculateRoutesFor(WorkUnit<Data> work) {
                throw new RuntimeException();
            }
        };
        Looper<Data> looper = new Looper<Data>(entry, blockingQueue, workUnitThreadPool);
        Data data = new Data(0);
        Data data2 = new Data(0);
        blockingQueue.add(new WorkUnit<Data>(data));
        looperExecutorService.submit(looper);
        Thread.sleep(100);
        blockingQueue.add(new WorkUnit<Data>(data2));
        Thread.sleep(100);
        looper.setShutdown();
        looperExecutorService.shutdown();
        looperExecutorService.awaitTermination(5000, TimeUnit.MILLISECONDS);
        Assert.assertEquals(data.getCounter(), 1);
        Assert.assertEquals(data2.getCounter(), 1);
    }

    @Test
    public void multipleRouteUnit_sameLogicUnit() throws InterruptedException {
        final RouteUnit<Data> ru2 = createFinalRoute(increaseOnce);
        final RouteUnit<Data> ru = createRoute(increaseOnce, ru2);

        Looper<Data> looper = new Looper<Data>(ru, blockingQueue, workUnitThreadPool);
        Data data = new Data(0);
        blockingQueue.add(new WorkUnit<Data>(data));
        submitAndTerminate(looper);
        Assert.assertEquals(data.getCounter(), 2);
    }

    @Test
    public void multipleRouteUnit_differentLogicUnit() throws InterruptedException {
        final RouteUnit<Data> ru2 = createFinalRoute(increaseTwice);
        final RouteUnit<Data> ru = createRoute(increaseOnce, ru2);
        Looper<Data> looper = new Looper<Data>(ru, blockingQueue, workUnitThreadPool);
        Data data = new Data(0);
        blockingQueue.add(new WorkUnit<Data>(data));
        submitAndTerminate(looper);
        Assert.assertEquals(data.getCounter(), 3);
    }

    @Test
    // this test is valid only when workUnitThreadPool limited to 1
    public void singleRouteUnit_multipleResults() throws InterruptedException {
        final RouteUnit<Data> ru2 = createFinalRoute(increaseTwice);
        final RouteUnit<Data> ru = createFinalRoute(increaseOnce);

        RouteUnit<Data> entry = createRoute(doNothing, ru, ru2);

        Looper<Data> looper = new Looper<Data>(entry, blockingQueue, workUnitThreadPool);
        Data data = new Data(0);
        blockingQueue.add(new WorkUnit<Data>(data));
        submitAndTerminate(looper);
        Assert.assertEquals(data.getCounter(), 3);
    }

    @Test
    public void singleRouteUnit_emptyResults() throws InterruptedException {
        RouteUnit<Data> entry = createFinalRoute(doNothing);
        Looper<Data> looper = new Looper<Data>(entry, blockingQueue, workUnitThreadPool);
        Data data = new Data(0);
        blockingQueue.add(new WorkUnit<Data>(data));
        submitAndTerminate(looper);
        Assert.assertEquals(data.getCounter(), 0);
    }

    @Test(timeOut = 10000L)
    public void looperThreadInterrupted_looperExited() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(2);
        Looper<Data> looper = new Looper<Data>(createFinalRoute(increaseOnce), blockingQueue, service);
        Future<?> future = looperExecutorService.submit(looper);

        // first we add some work
        Data data = new Data(0);
        blockingQueue.add(new WorkUnit<Data>(data));
        // we wait for it to be done
        Thread.sleep(1000);
        Assert.assertEquals(data.getCounter(), 1);
        // then we cancel the looper thread - this should send an interrupt exception in its blocking methods
        future.cancel(true);
        // we wait for the exception to happen
        Thread.sleep(1000);

        // we add the work again
        blockingQueue.add(new WorkUnit<Data>(data));
        // we wait for the work to be "done" (it shouldn't do anything)
        Thread.sleep(1000);
        Assert.assertEquals(data.getCounter(), 1);

        // cleanup
        looper.setShutdown();
        looperExecutorService.shutdown();
        looperExecutorService.awaitTermination(5, TimeUnit.SECONDS);
    }

    private RouteUnit<Data> createFinalRoute(final LogicUnit<Data> logicUnit) {
        return new RouteUnit<Data>(logicUnit) {
            @Override
            public Collection<RouteUnit<Data>> calculateRoutesFor(WorkUnit<Data> work) {
                return Collections.emptyList();
            }
        };
    }

    private void submitAndTerminate(Looper<Data> looper) throws InterruptedException {
        looperExecutorService.submit(looper);
        Thread.sleep(100);
        looper.setShutdown();
        looperExecutorService.shutdown();
        looperExecutorService.awaitTermination(5000, TimeUnit.MILLISECONDS);
    }

    private RouteUnit<Data> createRoute(final LogicUnit<Data> logicUnit, final RouteUnit<Data>... nextRoute) {
        return new RouteUnit<Data>(logicUnit) {
            @Override
            public Collection<RouteUnit<Data>> calculateRoutesFor(WorkUnit<Data> work) {
                return Arrays.asList(nextRoute);
            }
        };
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
