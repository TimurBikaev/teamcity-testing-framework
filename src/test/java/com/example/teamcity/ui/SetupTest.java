package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.ui.pages.AgentPage;
import com.example.teamcity.ui.pages.StartUpPage;
import com.example.teamcity.ui.pages.UnauthAgentsPage;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

public class SetupTest extends BaseUiTest {

    public final static String AGENTS_ENDPOINT = "/app/rest/agents";
    public final static String AGENTS_ENDPOINT_BY_ID = AGENTS_ENDPOINT + "/id:%s";
    public final static String AUTHORIZE_INFO_AGENTS_ENDPOINT = AGENTS_ENDPOINT_BY_ID + "/authorizedInfo";
    public final static String AGENTS_TYPES_ENDPOINT = "/app/rest/agentTypes";

    protected SoftAssertions softAssert; // Переменная для мягких проверок, доступная в этом классе и его наследниках

    @Test
    public void StartUpPage() {
        new StartUpPage()
                .open()
                .setupTeamCityServer()
                .getCreateAccountHeader()
                .shouldHave(Condition.text("Create Administrator Account"));
    }

    @Test
    public void setupTeamCityAgentTest() {
        //    Перейти в папку с агентом и там запустить агента (указав адрес):
        //    docker run -e SERVER_URL="http://192.168.200.253:8111" -v $PWD/conf:/data/teamcity_agent/conf jetbrains/teamcity-agent

        //Переход в неавторизованные агенты
        new UnauthAgentsPage()
                .open();
        //Подключение агента
        new AgentPage()
                .connectAgent();
  }



}
