package org.jparsec.test;

import org.jparsec.combinator.Seq;
import org.jparsec.containers.Ok;
import org.jparsec.containers.ParseContext;
import org.jparsec.containers.Tuple7;
import org.junit.jupiter.api.Test;

import static org.jparsec.combinator.Strings.string;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class SeqTest {


    @Test
    public void test_id() {
        var longSeq = Seq.seq(string("quick "),
                string("brown "),
                string("fox "),
                string("jumped "),
                string("over "),
                string("lazy "),
                string("dog"));

        var ctx = ParseContext.of("quick brown fox jumped over lazy dog");
        var result = longSeq.parse(ctx);

        if (result instanceof Ok(Tuple7(String s1, String s2, String s3,
                                        String s4, String s5, String s6, String s7),
                                 ParseContext newCtx)){
            assertEquals("quick brown fox jumped over lazy dog",
                    s1 + s2 + s3 + s4 + s5 + s6 + s7);
        } else {
            fail();
        }
    }
}