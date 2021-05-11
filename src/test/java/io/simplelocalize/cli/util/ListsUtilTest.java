package io.simplelocalize.cli.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class ListsUtilTest {

  @Test
  public void shouldCombineTwoLists() throws Exception {
    //given
    List<Integer> lhs = Arrays.asList(1, 5);
    List<Integer> rhs = Arrays.asList(2, 8);

    //when
    List<Integer> sum = ListsUtil.combine(lhs, rhs);

    //then
    Assertions.assertThat(sum).contains(1, 2, 5, 8);
    Assertions.assertThat(sum).hasSize(4);
  }
}
