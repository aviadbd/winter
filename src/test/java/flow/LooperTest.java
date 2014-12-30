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

    private final BlockingQueue<WorkUnit<Data>> blockingQueue = new LinkedBlockingQueue<>();
    private ExecutorService workUnitThreadPool;
    private ExecutorService looperExecutorService;
    private LogicUnit<Data> lu_increase;
    private LogicUnit<Data> double_lu_increase;


    @BeforeTest
    @SuppressWarnings("unchecked")
    public void beforeAll(){
        lu_increase = Mockito.mock(LogicUnit.class);
        double_lu_increase = Mockito.mock(LogicUnit.class);
        Mockito.doAnswer(new Answer<Data[]>() {
            @Override
            public Data[] answer(InvocationOnMock invocation) throws Throwable {
                Data data = (Data) invocation.getArguments()[0];
                data.increaseCounter();
                Data[] datas = new Data[1];
                Collections.singleton(data).toArray(datas);
                return datas;
            }}).when(lu_increase).execute(Mockito.any(Data.class));
        Mockito.doAnswer(new Answer<Data[]>() {
            @Override
            public Data[] answer(InvocationOnMock invocation) throws Throwable {
                Data data = (Data) invocation.getArguments()[0];
                data.increaseCounter();
                data.increaseCounter();
                Data[] datas = new Data[1];
                Collections.singleton(data).toArray(datas);
                return datas;
            }}).when(double_lu_increase).execute(Mockito.any(Data.class));
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
        EntryRouteUnit<Data> entry = Mockito.mock(EntryRouteUnit.class);
        Looper<Data> looper = new Looper<>(entry, blockingQueue, workUnitThreadPool);
        looperExecutorService.submit(looper);
        Thread.sleep(100);
        looper.setShutdown();
        looperExecutorService.shutdown();
        looperExecutorService.awaitTermination(5000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void singleRouteUnit() throws InterruptedException {
        RouteUnit<Data> ru = new RouteUnit<Data>(lu_increase) {
            @Override
            public Collection<RouteUnit<Data>> calculateRoutesFor(WorkUnit<Data> work) {
                return Collections.emptyList();
            }
        };
        EntryRouteUnit<Data> entry = new EntryRouteUnit<Data>() {
            @Override
            public Collection<RouteUnit<Data>> calculateRoutesFor(WorkUnit<Data> work) {
                return Collections.singleton(ru);
            }
        };
        Looper<Data> looper = new Looper<>(entry, blockingQueue, workUnitThreadPool);
        Data data = new Data(0);
        blockingQueue.add(new WorkUnit<>(data));
        looperExecutorService.submit(looper);
        Thread.sleep(100);
        looper.setShutdown();
        looperExecutorService.shutdown();
        looperExecutorService.awaitTermination(5000, TimeUnit.MILLISECONDS);
        Assert.assertEquals(data.getCounter(), 1);
    }

    @Test
    public void multipleRouteUnit_sameLogicUnit() throws InterruptedException {
        RouteUnit<Data> ru2 = new RouteUnit<Data>(lu_increase) {
            @Override
            public Collection<RouteUnit<Data>> calculateRoutesFor(WorkUnit<Data> work) {
                return Collections.emptyList();
            }
        };
        RouteUnit<Data> ru = new RouteUnit<Data>(lu_increase) {
            @Override
            public Collection<RouteUnit<Data>> calculateRoutesFor(WorkUnit<Data> work) {
                return Collections.singleton(ru2);
            }
        };
        EntryRouteUnit<Data> entry = new EntryRouteUnit<Data>() {
            @Override
            public Collection<RouteUnit<Data>> calculateRoutesFor(WorkUnit<Data> work) {
                return Collections.singleton(ru);
            }
        };
        Looper<Data> looper = new Looper<>(entry, blockingQueue, workUnitThreadPool);
        Data data = new Data(0);
        blockingQueue.add(new WorkUnit<>(data));
        looperExecutorService.submit(looper);
        Thread.sleep(100);
        looper.setShutdown();
        looperExecutorService.shutdown();
        looperExecutorService.awaitTermination(5000, TimeUnit.MILLISECONDS);
        Assert.assertEquals(data.getCounter(), 2);
    }

    @Test
    public void multipleRouteUnit_differentLogicUnit() throws InterruptedException {
        RouteUnit<Data> ru2 = new RouteUnit<Data>(double_lu_increase) {
            @Override
            public Collection<RouteUnit<Data>> calculateRoutesFor(WorkUnit<Data> work) {
                return Collections.emptyList();
            }
        };
        RouteUnit<Data> ru = new RouteUnit<Data>(lu_increase) {
            @Override
            public Collection<RouteUnit<Data>> calculateRoutesFor(WorkUnit<Data> work) {
                return Collections.singleton(ru2);
            }
        };
        EntryRouteUnit<Data> entry = new EntryRouteUnit<Data>() {
            @Override
            public Collection<RouteUnit<Data>> calculateRoutesFor(WorkUnit<Data> work) {
                return Collections.singleton(ru);
            }
        };
        Looper<Data> looper = new Looper<>(entry, blockingQueue, workUnitThreadPool);
        Data data = new Data(0);
        blockingQueue.add(new WorkUnit<>(data));
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
        RouteUnit<Data> ru2 = new RouteUnit<Data>(double_lu_increase) {
            @Override
            public Collection<RouteUnit<Data>> calculateRoutesFor(WorkUnit<Data> work) {
                return Collections.emptyList();
            }
        };
        RouteUnit<Data> ru = new RouteUnit<Data>(lu_increase) {
            @Override
            public Collection<RouteUnit<Data>> calculateRoutesFor(WorkUnit<Data> work) {
                return Collections.emptyList();
            }
        };
        EntryRouteUnit<Data> entry = new EntryRouteUnit<Data>() {
            @Override
            public Collection<RouteUnit<Data>> calculateRoutesFor(WorkUnit<Data> work) {
                return Arrays.asList(ru, ru2);
            }
        };
        Looper<Data> looper = new Looper<>(entry, blockingQueue, workUnitThreadPool);
        Data data = new Data(0);
        blockingQueue.add(new WorkUnit<>(data));
        looperExecutorService.submit(looper);
        Thread.sleep(100);
        looper.setShutdown();
        looperExecutorService.shutdown();
        looperExecutorService.awaitTermination(5000, TimeUnit.MILLISECONDS);
        Assert.assertEquals(data.getCounter(), 3);
    }

    @Test
    public void singleRouteUnit_emptyResults() throws InterruptedException {
        EntryRouteUnit<Data> entry = new EntryRouteUnit<Data>() {
            @Override
            public Collection<RouteUnit<Data>> calculateRoutesFor(WorkUnit<Data> work) {
                return Collections.emptyList();
            }
        };
        Looper<Data> looper = new Looper<>(entry, blockingQueue, workUnitThreadPool);
        Data data = new Data(0);
        blockingQueue.add(new WorkUnit<>(data));
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
