package com.alna.reactor3.schedulers;

import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.utility.GateBasedSynchronization;
import com.google.common.util.concurrent.Uninterruptibles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

public class AvailableSchedulers {
  public static void main(String[] args) {
    final GateBasedSynchronization gate = new GateBasedSynchronization();

    //By default creates scheduler with fixed pool of threads with  Number of cpu.
    //It is useful for CPU bound operations.
    Schedulers.parallel();

    //Unbounded scheduler which reuses threads from pool if there is a free one.
    //This scheduler should be used for IO bound operations but it is not preferred anymore as it can cause too many thread creations instead we should use bounded elastic.
    Schedulers.elastic();

    //For limited elastic scheduler we can use following alternatives which caps both number of thread and number of queued tasks.
    Schedulers.boundedElastic();
    Schedulers.newBoundedElastic(2, 2, "test");


    //Single thread scheduler so all operations will be run sequentially with one dedicated thread and it will be reused for all calls to scheduler.
    //All callers are guaranteed to be run by the same single thread. It is like a singletone for schedulers.
    // But if you want dedicated thread for each caller then we should use  newSingle("name-prefix").
    Schedulers.single();

    //Gives single threaded executer service for each caller of newSingle().
    Schedulers.newSingle("test");

    // We can use following methods to create scheduler from executer or executorService and also pass trampoline which guarantees order of execution for submitted tasks.
    // So if you need FIFO order pass trampoline as true.
    Executor executer = null;
    Schedulers.fromExecutor(executer, true);
//    Schedulers.fromExecutorService()

    // No scheduling is done and task,operators are executed in the same thread. It can be considered as null scheduler or no-op scheduler.
    Schedulers.immediate();

    //Pattern to use when making synchronous calls
    Mono.fromCallable(() -> {
      Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(5));
      return "asdad";
      //to execute each subscriber on a dedicated thread and also it will imply limit on number of threads instead of just elastic
      //we don't use single() or newSingle() here because in that case only thread will be provided for all subscribers.
    }).subscribeOn(Schedulers.boundedElastic());


    //Schedulers can be created also with newXXX() methods for each type specifying different params for customization.
    Schedulers.newParallel("test");

    //single() and parallel() schedulers does not support blocking operation inside threads toIterable() toStream(), blockXXX()
    //But elastic() and elasticBound() schedulers support blocking operations and they were created for transitioning from legacy code.
    //New scheduler can also be created to not support blocking operations by providing threads which implement NonBlocking marker interface.
    Flux.fromIterable(Flux.fromArray(new Integer[] {1, 2, 3}).toIterable())
        .subscribeOn(Schedulers.single())
        .doOnNext(i -> System.out.println(i))
        .subscribe(new DemoSubscriber<>(gate));

    //By default the interval method uses parallel() scheduler for counting the tick but custom scheduler can be provided as well.
    //There are other methods with same behavior which use clock ticking.
    Flux.interval(Duration.ofMillis(500), Schedulers.newSingle("test"));

    gate.waitForAny("onComplete", "onError");


  }
}
