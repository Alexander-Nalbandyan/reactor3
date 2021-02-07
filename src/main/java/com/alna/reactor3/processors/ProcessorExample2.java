package com.alna.reactor3.processors;

import com.alna.reactor3.DemoSubscriber;
import com.alna.reactor3.utility.ThreadHelper;
import com.alna.reactor3.utility.processors.NamedProcessor;
import com.alna.reactor3.utility.processors.ProcessorManager;
import reactor.core.publisher.DirectProcessor;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.TimeUnit;

public class ProcessorExample2 {
  public static void main(String[] args) {
    String USER_SERVICE_PROCESSOR_NAME = "userService";
    String COMMENT_SERVICE_PROCESSOR_NAME = "commentService";

    final ProcessorManager processorManager = new ProcessorManager();

    processorManager.registerProcessor(new NamedProcessor(USER_SERVICE_PROCESSOR_NAME, DirectProcessor.create()));
    processorManager.registerProcessor(new NamedProcessor(COMMENT_SERVICE_PROCESSOR_NAME, DirectProcessor.create()));

    processorManager.addEventConsumer(USER_SERVICE_PROCESSOR_NAME, new DemoSubscriber("sub1"));
    processorManager.addEventConsumer(COMMENT_SERVICE_PROCESSOR_NAME, new DemoSubscriber("sub2"));


    processorManager.addEventProducer(USER_SERVICE_PROCESSOR_NAME,
                                      UserServiceEventPublisher.userServiceEventGenerator()
                                          .subscribeOn(Schedulers.parallel()));

    processorManager.addEventProducer(COMMENT_SERVICE_PROCESSOR_NAME,
                                      CommentServiceEventPublisher.commentServiceEventGenerator()
                                          .subscribeOn(Schedulers.parallel()));

    ThreadHelper.sleep(10000, TimeUnit.MILLISECONDS);


  }
}
