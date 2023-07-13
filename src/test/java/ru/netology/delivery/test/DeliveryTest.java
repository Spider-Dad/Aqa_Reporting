package ru.netology.delivery.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

class DeliveryTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);

        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();
        $(byText("Успешно!")).shouldBe(visible, Duration.ofSeconds(10));

        $("[data-test-id=success-notification] .notification__content").shouldHave(text("Встреча успешно запланирована на " + firstMeetingDate)).shouldBe(visible, Duration.ofSeconds(10));

        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(secondMeetingDate);
        $(byText("Запланировать")).click();

        $("[data-test-id=replan-notification]").shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?")).shouldBe(visible, Duration.ofSeconds(10));
        $("[data-test-id=replan-notification] button").click();

        $("[data-test-id=success-notification] .notification__content").shouldHave(text("Встреча успешно запланирована на " + secondMeetingDate)).shouldBe(visible, Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Not should successful plan  meeting if not correct latin city")
    void shouldNotSuccessfulPlanMeetingNotCorrectLatinCity() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var invalidCity = "Saratov";

        $("[data-test-id=city] input").setValue(invalidCity);

        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();

        $("[data-test-id=city].input_invalid .input__sub").shouldHave(text("Доставка в выбранный город недоступна")).shouldBe(visible, Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Not should successful plan  meeting if not correct  city")
    void shouldNotSuccessfulPlanMeetingNotCorrectCity() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var invalidCity = "Париж";

        $("[data-test-id=city] input").setValue(invalidCity);
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();

        $("[data-test-id=city].input_invalid .input__sub").shouldHave(text("Доставка в выбранный город недоступна")).shouldBe(visible, Duration.ofSeconds(10));

    }

    @Test
    @DisplayName("Not should successful plan  meeting if not filled city")
    void shouldNotSuccessfulPlanMeetingNotFilledCity() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);

        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();

        $("[data-test-id=city].input_invalid .input__sub").shouldHave(text("Поле обязательно для заполнения")).shouldBe(visible);
    }

    @Test
    @DisplayName("Not should successful plan  meeting if meeting day less 3 days")
    void shouldNotSuccessfulPlanMeetingDayLessThree() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 2;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);

        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();

        $("[data-test-id=date] .input_invalid .input__sub").shouldHave(text("Заказ на выбранную дату невозможен")).shouldBe(visible, Duration.ofSeconds(10));

    }

    @Test
    @DisplayName("Not should successful plan  meeting if meeting day not correct format ")
    void shouldNotSuccessfulPlanMeetingDayNotCorrectFormat() {
        var validUser = DataGenerator.Registration.generateUser("ru");

        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue("10.10.23");
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();

        $("[data-test-id=date] .input_invalid .input__sub").shouldHave(text("Неверно введена дата")).shouldBe(visible, Duration.ofSeconds(10));

    }

    @Test
    @DisplayName("Not should successful plan  meeting if not filled phone")
    void shouldNotSuccessfulPlanMeetingNotFilledPhone() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);

        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();

        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(text("Поле обязательно для заполнения")).shouldBe(visible, Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Not should successful plan  meeting if not filled Name")
    void shouldNotSuccessfulPlanMeetingNotFilledName() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);

        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();

        $("[data-test-id=name].input_invalid .input__sub").shouldHave(text("Поле обязательно для заполнения")).shouldBe(visible, Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Not should successful plan  meeting if not filled Date")
    void shouldNotSuccessfulPlanMeetingNotFilledDate() {
        var validUser = DataGenerator.Registration.generateUser("ru");

        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();

        $("[data-test-id=date] .input_invalid").shouldHave(text("Неверно введена дата")).shouldBe(visible, Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Not should successful plan  meeting if not push agreement")
    void shouldNotSuccessfulPlanMeetingNotPushAgreement() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);

        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $(byText("Запланировать")).click();

        $("[data-test-id=agreement].input_invalid .checkbox__text").shouldHave(text("Я соглашаюсь с условиями обработки и использования моих персональных данных")).shouldBe(visible, Duration.ofSeconds(10));

    }
}
