package com.tealium.poc;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.tealium.poc.client.personality.Personality;
import com.tealium.poc.client.locality.Locality;


public class InfoBuilderTest {

    @Test
    public void testInfoBuildNoLocality() {
        // given Beaker
        Info expected = new Info("Beaker", Mood.GOOD);

        // when we don;t know Smalltown's tempereture
        Personality beaker = new Personality("Beaker", 105, "Smalltown");
        List<Personality> personalities = List.of(beaker);
        List<Info> infos = new InfoBuilder()
          .withPersonalities(personalities)
          .build();
        Info actual = infos.get(0);
        
        // then we assume Beaker's in a good mood
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testInfoBuildWithLocalityTooHot() {
        // given Beaker
        Info expected = new Info("Beaker", Mood.BAD);

        // when Smalltown is too hot
        Personality beaker = new Personality("Beaker", 105, "Smalltown");
        Locality smallTown = new Locality("Smalltown", 106);
        List<Info> infos = new InfoBuilder()
          .withPersonalities(List.of(beaker))
          .withLocalities(List.of(smallTown))
          .build();
        Info actual = infos.get(0);
        
        // then Beaker's in a bad mood
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testInfoBuildWithLocalityNotTooHot() {
        // given Beaker
        Info expected = new Info("Beaker", Mood.GOOD);

        // when Smalltown is NOT too hot
        Personality beaker = new Personality("Beaker", 105, "Smalltown");
        Locality smallTown = new Locality("Smalltown", 75);
        List<Info> infos = new InfoBuilder()
          .withPersonalities(List.of(beaker))
          .withLocalities(List.of(smallTown))
          .build();
        Info actual = infos.get(0);
        
        // then Beaker's in a good mood
        Assertions.assertEquals(expected, actual);
    }

}