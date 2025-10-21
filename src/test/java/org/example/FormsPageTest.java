package org.example;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.example.metrics.TestMetricsExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selectors.byText;

@ExtendWith(TestMetricsExtension.class)
public class FormsPageTest {

    @BeforeAll
    static void setup() {
        SelenideConfig.apply();
    }

    @Test
    @DisplayName("Forms: видна форма 'Practice Form'")
    void formsPageHasPracticeForm() {
        open("https://demoqa.com/forms");
        $(byText("Practice Form")).shouldBe(Condition.visible);
    }
}

