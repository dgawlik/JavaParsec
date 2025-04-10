package org.jparsec.test;

import org.jparsec.combinator.Seq;
import org.jparsec.containers.Context;
import org.jparsec.containers.Ok;
import org.jparsec.containers.seq.Tuple7;
import org.junit.jupiter.api.Test;

import static org.jparsec.combinator.Strings.c;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class SeqTest {


    @Test
    public void test_id() {
        var longSeq = Seq.seq(c("quick "),
                c("brown "),
                c("fox "),
                c("jumped "),
                c("over "),
                c("lazy "),
                c("dog"));

        var ctx = Context.of("quick brown fox jumped over lazy dog");
        var result = longSeq.parse(ctx);

        if (result instanceof Ok(Tuple7(String s1, String s2, String s3,
                                        String s4, String s5, String s6, String s7),
                                 Context newCtx)){
            assertEquals("quick brown fox jumped over lazy dog",
                    s1 + s2 + s3 + s4 + s5 + s6 + s7);
        } else {
            fail();
        }
    }
}