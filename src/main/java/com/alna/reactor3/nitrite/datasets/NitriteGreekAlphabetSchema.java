package com.alna.reactor3.nitrite.datasets;

import com.alna.reactor3.nitrite.NitriteSchema;
import com.alna.reactor3.nitrite.entity.LetterPair;
import com.alna.reactor3.utility.datasets.GreekAlphabet;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public class NitriteGreekAlphabetSchema implements NitriteSchema {

    @Override
    public void applySchema(Nitrite db) {

        // Make a collection to hold the greek alphabet
        ObjectRepository<LetterPair> letterRepo = db.getRepository(LetterPair.class);

        // See if it's already populated
        if( letterRepo.find().totalCount() == 0 ) {

            // Make a LetterPair for each letter in the greek alphabet
            List<LetterPair> letterList = Flux.zip(
                GreekAlphabet.greekAlphabetInGreekFlux(),
                GreekAlphabet.greekAlphabetInEnglishFlux(),
                LetterPair::new)
                    .collectList()
                    .block();

            letterRepo.insert(letterList.toArray(new LetterPair[letterList.size()]));
        }
    }
}
