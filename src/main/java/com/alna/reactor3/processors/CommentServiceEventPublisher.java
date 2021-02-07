package com.alna.reactor3.processors;

import com.alna.reactor3.utility.MutableReference;
import com.alna.reactor3.utility.ThreadHelper;
import reactor.core.publisher.Flux;

import java.util.Observable;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class CommentServiceEventPublisher {

  private static final String[] authorEmails = new String[]{

      "mrresponsible@aracnid.net",
      "cheater@worldreverse.org",
      "hook@calzonecorner.net",
      "toughguy@unitard.com"
  };

  public static Flux<EventBase> commentServiceEventGenerator() {

    return Flux.generate(
        () -> new MutableReference<Integer>(0),
        (offset, eventBaseEmitter) -> {

          // Make sure we haven't run off the end of our list.
          if( offset.getValue() >= authorEmails.length) {

            // We have sent all the messages...send the
            // onComplete event.
            eventBaseEmitter.complete();
          }
          else {

            int nextValue = (offset.getValue() + 1) % authorEmails.length;

            // Send out the next message
            eventBaseEmitter.next(new NewCommentPostedEvent(
                authorEmails[offset.getValue()],
                authorEmails[nextValue],
                randomString(64)
            ));
          }

          // Increment our offset counter
          offset.setValue( offset.getValue() + 1 );

          // Slow things down
          ThreadHelper.sleep(1500, TimeUnit.MILLISECONDS);
          return offset;
        });
  }

  private static Random random = new Random();

  private static String randomString(int characterCount) {

    String letters = "abcdefghijklmnopqrstuvwxyz ";

    StringBuilder returnBuffer = new StringBuilder(characterCount);

    for(int i = 0 ; i < characterCount ; i++ ) {
      returnBuffer.append(letters.charAt(random.nextInt(letters.length())));
    }

    return returnBuffer.toString();
  }
}