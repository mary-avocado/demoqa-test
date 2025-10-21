package org.example;

import com.codeborne.selenide.Condition;
import org.example.metrics.TestMetricsExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selectors.byText;

@ExtendWith(TestMetricsExtension.class)
public class ElementsPageTest {
    @BeforeAll static void setup() { SelenideConfig.apply(); }
    @Test @DisplayName("Elements: виден пункт 'Text Box'")
    void elementsPageHasTextBox() {
        open("https://demoqa.com/elements");
        $(byText("Text Box")).shouldBe(Condition.visible);
    }
}
