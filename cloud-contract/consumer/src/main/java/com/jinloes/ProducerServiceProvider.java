package com.jinloes;

import org.apache.commons.lang3.tuple.Pair;

public interface ProducerServiceProvider {
  Pair<String, Integer> getDetails();
}
