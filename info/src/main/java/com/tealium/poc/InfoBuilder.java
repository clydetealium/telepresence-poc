package com.tealium.poc;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.tealium.poc.client.personality.Personality;
import com.tealium.poc.client.locality.Locality;

public class InfoBuilder {
    private List<Personality> personalities;
    private Map<String, Locality> localities = new HashMap<>();

    public InfoBuilder withPersonalities(List<Personality> personalities) {
        this.personalities = personalities;
        return this;
    }

    public InfoBuilder withLocalities(List<Locality> localities) {
        this.localities = localities.stream()
            .collect(Collectors.toMap(Locality::getName, Function.identity()));
        return this;
    }

    public Set<Info> build() {
        return personalities.stream()
            .map(this::getInfo)
            .collect(Collectors.toSet());
    }

    private Info getInfo(Personality personality) {
        Locality locality = localities.get(personality.getLocality());
        Mood mood = (locality == null || locality.getTemperature() <= personality.getThreshold()) ?
            Mood.GOOD : Mood.BAD;
        return new Info(personality.getName(), mood);
    }
}
