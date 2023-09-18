package ru.job4j.grabber.utils;

import org.junit.Test;

import java.time.format.DateTimeParseException;

import static org.assertj.core.api.Assertions.*;

public class HabrCareerDateTimeParserTest {

    @Test
    public void whenParseDate1() {
        HabrCareerDateTimeParser hcdt = new HabrCareerDateTimeParser();
        String in = "2023-09-17T20:27:03+03:00";
        assertThat(hcdt.parse(in)).isEqualTo("2023-09-17T20:27:03");
    }

    @Test
    public void whenParseDate2() {
        HabrCareerDateTimeParser hcdt = new HabrCareerDateTimeParser();
        String in = "2011-12-03T10:15:30+01:00";
        assertThat(hcdt.parse(in)).isEqualTo("2011-12-03T10:15:30");
    }
}