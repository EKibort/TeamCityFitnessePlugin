package Fitnesse.agent.tests;
import Fitnesse.agent.Results.ResultReporter;
import Fitnesse.agent.Results.TeamCityResultReporter;
import Fitnesse.agent.Results.XmlResultStreamProcessor;
import org.junit.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class XmlResultStreamProcessorTest {

    @Mock
    jetbrains.buildServer.agent.BuildProgressLogger logger;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @Ignore("just for testing")
    public void testProcessStream() {
        ResultReporter reporter = new TeamCityResultReporter(logger);
        XmlResultStreamProcessor xmlResultStreamProcessor = new XmlResultStreamProcessor(reporter, logger);
        InputStream stream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        xmlResultStreamProcessor.ProcessStream(stream);
    }

    String xml = "<?xml version=\"1.0\"?>\n" +
            "<testResults>\n" +
            "  <FitNesseVersion>v20140418</FitNesseVersion>\n" +
            "  <rootPath>AppsCloudhouseCom.ApiDocumentation.LicenseAgreementApiTests.LicenseAgreementsRun</rootPath>\n" +
            "    <result>\n" +
            "    <counts>\n" +
            "      <right>44</right>\n" +
            "      <wrong>0</wrong>\n" +
            "      <ignores>0</ignores>\n" +
            "      <exceptions>1</exceptions>\n" +
            "    </counts>\n" +
            "    <runTimeInMillis>780</runTimeInMillis>\n" +
            "    <content>&lt;div class=\"collapsible closed\"&gt;&lt;ul&gt;&lt;li&gt;&lt;a href='#' class='expandall'&gt;Expand All&lt;/a&gt;&lt;/li&gt;&lt;li&gt;&lt;a href='#' class='collapseall'&gt;Collapse All&lt;/a&gt;&lt;/li&gt;&lt;/ul&gt;\n" +
            "\t&lt;p class=\"title\"&gt;Included page: &lt;a href=\"AppsCloudhouseCom.ApiDocumentation.SetUp\"&gt;.AppsCloudhouseCom.ApiDocumentation.SetUp&lt;/a&gt; &lt;a href=\"AppsCloudhouseCom.ApiDocumentation.SetUp?edit&amp;amp;redirectToReferer=true&amp;amp;redirectAction=\" class=\"edit\"&gt;(edit)&lt;/a&gt;&lt;/p&gt;\n" +
            "\t&lt;div&gt;&lt;table&gt;\n" +
            "\t&lt;tr&gt;\n" +
            "\t\t&lt;td&gt;&lt;span class=\"fit_interpreter\"&gt;ResetCompaniesData&lt;/span&gt;&lt;/td&gt;\n" +
            "\t&lt;/tr&gt;\n" +
            "\t&lt;tr&gt;\n" +
            "\t\t&lt;td&gt;Reset&lt;/td&gt;\n" +
            "\t&lt;/tr&gt;\n" +
            "&lt;/table&gt;\n" +
            "&lt;/div&gt;\n" +
            "&lt;/div&gt;\n" +
            "&lt;br/&gt;&lt;div class=\"collapsible invisible\"&gt;&lt;ul&gt;&lt;li&gt;&lt;a href='#' class='expandall'&gt;Expand All&lt;/a&gt;&lt;/li&gt;&lt;li&gt;&lt;a href='#' class='collapseall'&gt;Collapse All&lt;/a&gt;&lt;/li&gt;&lt;/ul&gt;\n" +
            "\t&lt;p class=\"title\"&gt;Hide this section&lt;/p&gt;\n" +
            "\t&lt;div&gt;&lt;table&gt;\n" +
            "\t&lt;tr&gt;\n" +
            "\t\t&lt;td&gt;&lt;span class=\"fit_interpreter\"&gt;import&lt;/span&gt;&lt;/td&gt;\n" +
            "\t&lt;/tr&gt;\n" +
            "\t&lt;tr&gt;\n" +
            "\t\t&lt;td&gt;CloudhousePortalAcceptanceTests&lt;/td&gt;\n" +
            "\t&lt;/tr&gt;\n" +
            "&lt;/table&gt;\n" +
            "&lt;br/&gt;&lt;span class=\"meta\"&gt;variable defined: GetRunLinkXml=&amp;lt;?xml version=\"1.0\" encoding=\"utf-8\"?&amp;gt;\n" +
            "&amp;lt;InfoResultOfLicenseAgreementSession xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"&amp;gt;\n" +
            "\t&amp;lt;Result&amp;gt;\n" +
            "\t\t&amp;lt;Success&amp;gt;true&amp;lt;/Success&amp;gt;\n" +
            "\t\t&amp;lt;Message&amp;gt;Success. Link is only valid for: 90 minutes&amp;lt;/Message&amp;gt;\n" +
            "\t&amp;lt;/Result&amp;gt;\n" +
            "\t&amp;lt;Href&amp;gt;http://localhost:9990/v2/Companies/2/LicenseAgreements/1/Run.xml?SessionGuid=10000000-0000-0000-0000-000000000000&amp;amp;amp;CurrentUnixTime=1363964598&amp;amp;amp;Os=win&amp;lt;/Href&amp;gt;\n" +
            "\t&amp;lt;Info&amp;gt;\n" +
            "\t\t&amp;lt;SessionGuid&amp;gt;10000000-0000-0000-0000-000000000000&amp;lt;/SessionGuid&amp;gt;\n" +
            "\t&amp;lt;/Info&amp;gt;\n" +
            "&amp;lt;/InfoResultOfLicenseAgreementSession&amp;gt; &lt;/span&gt;\n" +
            "&lt;br/&gt;&lt;br/&gt;&lt;span class=\"meta\"&gt;variable defined: TopLevelCompanyApiToken=00000000-0000-0000-0000-000000000001&lt;/span&gt;\n" +
            "&lt;br/&gt;&lt;span class=\"meta\"&gt;variable defined: SubCompanyAToken=00000000-0000-0000-0000-000000000002&lt;/span&gt;\n" +
            "&lt;br/&gt;&lt;span class=\"meta\"&gt;variable defined: SubCompanyBToken=00000000-0000-0000-0000-000000000003&lt;/span&gt;\n" +
            "&lt;br/&gt;&lt;span class=\"meta\"&gt;variable defined: SubCompanyA1Token=00000000-0000-0000-0000-000000000004&lt;/span&gt;\n" +
            "&lt;br/&gt;&lt;span class=\"meta\"&gt;variable defined: SubCompanyB1Token=00000000-0000-0000-0000-000000000005&lt;/span&gt;\n" +
            "&lt;br/&gt;&lt;/div&gt;\n" +
            "&lt;/div&gt;\n" +
            "&lt;h1&gt;&lt;center&gt;Run&lt;/center&gt;&lt;/h1&gt;\n" +
            "&lt;br/&gt;&lt;br/&gt;&lt;b&gt;Given that the following company information is setup:&lt;/b&gt;&lt;br/&gt;&lt;div class=\"collapsible\"&gt;&lt;ul&gt;&lt;li&gt;&lt;a href='#' class='expandall'&gt;Expand All&lt;/a&gt;&lt;/li&gt;&lt;li&gt;&lt;a href='#' class='collapseall'&gt;Collapse All&lt;/a&gt;&lt;/li&gt;&lt;/ul&gt;\n" +
            "\t&lt;p class=\"title\"&gt;Included page: &lt;a href=\"AppsCloudhouseCom.ApiDocumentation.GetBaseCompanies\"&gt;.AppsCloudhouseCom.ApiDocumentation.GetBaseCompanies&lt;/a&gt; &lt;a href=\"AppsCloudhouseCom.ApiDocumentation.GetBaseCompanies?edit&amp;amp;redirectToReferer=true&amp;amp;redirectAction=\" class=\"edit\"&gt;(edit)&lt;/a&gt;&lt;/p&gt;\n" +
            "\t&lt;div&gt;&lt;div class=\"collapsible invisible\"&gt;&lt;ul&gt;&lt;li&gt;&lt;a href='#' class='expandall'&gt;Expand All&lt;/a&gt;&lt;/li&gt;&lt;li&gt;&lt;a href='#' class='collapseall'&gt;Collapse All&lt;/a&gt;&lt;/li&gt;&lt;/ul&gt;\n" +
            "\t&lt;p class=\"title\"&gt;Hide this section&lt;/p&gt;\n" +
            "\t&lt;div&gt;&lt;table&gt;\n" +
            "\t&lt;tr&gt;\n" +
            "\t\t&lt;td&gt;&lt;span class=\"fit_interpreter\"&gt;import&lt;/span&gt;&lt;/td&gt;\n" +
            "\t&lt;/tr&gt;\n" +
            "\t&lt;tr&gt;\n" +
            "\t\t&lt;td&gt;CloudhousePortalAcceptanceTests&lt;/td&gt;\n" +
            "\t&lt;/tr&gt;\n" +
            "&lt;/table&gt;\n" +
            "&lt;span class=\"meta\"&gt;variable defined: TopLevelCompanyApiToken=00000000-0000-0000-0000-000000000001&lt;/span&gt;\n" +
            "&lt;br/&gt;&lt;span class=\"meta\"&gt;variable defined: SubCompanyAToken=00000000-0000-0000-0000-000000000002&lt;/span&gt;\n" +
            "&lt;br/&gt;&lt;span class=\"meta\"&gt;variable defined: SubCompanyBToken=00000000-0000-0000-0000-000000000003&lt;/span&gt;\n" +
            "&lt;br/&gt;&lt;span class=\"meta\"&gt;variable defined: SubCompanyA1Token=00000000-0000-0000-0000-000000000004&lt;/span&gt;\n" +
            "&lt;br/&gt;&lt;span class=\"meta\"&gt;variable defined: SubCompanyB1Token=00000000-0000-0000-0000-000000000005&lt;/span&gt;\n" +
            "&lt;br/&gt;&lt;/div&gt;\n" +
            "&lt;/div&gt;\n" +
            "&lt;table&gt;\n" +
            "\t&lt;tr&gt;\n" +
            "\t\t&lt;td colspan=\"4\"&gt;&lt;span class=\"fit_interpreter\"&gt;Get Company Data&lt;/span&gt;&lt;/td&gt;\n" +
            "\t&lt;/tr&gt;\n" +
            "\t&lt;tr&gt;\n" +
            "\t\t&lt;td&gt;&lt;span class=\"fit_member\"&gt;CompanyId&lt;/span&gt;&lt;/td&gt;\n" +
            "\t\t&lt;td&gt;&lt;span class=\"fit_member\"&gt;ParentCompanyId&lt;/span&gt;&lt;/td&gt;\n" +
            "\t\t&lt;td&gt;&lt;span class=\"fit_member\"&gt;CompanyName&lt;/span&gt;&lt;/td&gt;\n" +
            "\t\t&lt;td&gt;&lt;span class=\"fit_member\"&gt;ApiToken&lt;/span&gt;&lt;/td&gt;\n" +
            "\t&lt;/tr&gt;\n" +
            "\t&lt;tr&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;1&lt;/td&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;0&lt;/td&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;TopLevelCompany&lt;/td&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;00000000-0000-0000-0000-000000000001&lt;/td&gt;\n" +
            "\t&lt;/tr&gt;\n" +
            "\t&lt;tr&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;2&lt;/td&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;1&lt;/td&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;SubCompanyA&lt;/td&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;00000000-0000-0000-0000-000000000002&lt;/td&gt;\n" +
            "\t&lt;/tr&gt;\n" +
            "\t&lt;tr&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;3&lt;/td&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;1&lt;/td&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;SubCompanyB&lt;/td&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;00000000-0000-0000-0000-000000000003&lt;/td&gt;\n" +
            "\t&lt;/tr&gt;\n" +
            "\t&lt;tr&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;4&lt;/td&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;2&lt;/td&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;SubCompanyA1&lt;/td&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;00000000-0000-0000-0000-000000000004&lt;/td&gt;\n" +
            "\t&lt;/tr&gt;\n" +
            "\t&lt;tr&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;5&lt;/td&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;3&lt;/td&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;SubCompanyB1&lt;/td&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;00000000-0000-0000-0000-000000000005&lt;/td&gt;\n" +
            "\t&lt;/tr&gt;\n" +
            "&lt;/table&gt;\n" +
            "&lt;/div&gt;\n" +
            "&lt;/div&gt;\n" +
            "&lt;br/&gt;&lt;b&gt;And that the following license agreements are setup:&lt;/b&gt;&lt;br/&gt;&lt;div class=\"collapsible\"&gt;&lt;ul&gt;&lt;li&gt;&lt;a href='#' class='expandall'&gt;Expand All&lt;/a&gt;&lt;/li&gt;&lt;li&gt;&lt;a href='#' class='collapseall'&gt;Collapse All&lt;/a&gt;&lt;/li&gt;&lt;/ul&gt;\n" +
            "\t&lt;p class=\"title\"&gt;Included page: &lt;a href=\"AppsCloudhouseCom.ApiDocumentation.GetBaseLicenseAgreements\"&gt;.AppsCloudhouseCom.ApiDocumentation.GetBaseLicenseAgreements&lt;/a&gt; &lt;a href=\"AppsCloudhouseCom.ApiDocumentation.GetBaseLicenseAgreements?edit&amp;amp;redirectToReferer=true&amp;amp;redirectAction=\" class=\"edit\"&gt;(edit)&lt;/a&gt;&lt;/p&gt;\n" +
            "\t&lt;div&gt;&lt;table&gt;\n" +
            "\t&lt;tr&gt;\n" +
            "\t\t&lt;td colspan=\"7\"&gt;&lt;span class=\"fit_interpreter\"&gt;Get License Agreements&lt;/span&gt;&lt;/td&gt;\n" +
            "\t&lt;/tr&gt;\n" +
            "\t&lt;tr&gt;\n" +
            "\t\t&lt;td&gt;&lt;span class=\"fit_member\"&gt;LicenseAgreementId&lt;/span&gt;&lt;/td&gt;\n" +
            "\t\t&lt;td&gt;&lt;span class=\"fit_member\"&gt;AppName&lt;/span&gt;&lt;/td&gt;\n" +
            "\t\t&lt;td&gt;&lt;span class=\"fit_member\"&gt;ProductDescription&lt;/span&gt;&lt;/td&gt;\n" +
            "\t\t&lt;td&gt;&lt;span class=\"fit_member\"&gt;LicenseType&lt;/span&gt;&lt;/td&gt;\n" +
            "\t\t&lt;td&gt;&lt;span class=\"fit_member\"&gt;ExpiryTime&lt;/span&gt;&lt;/td&gt;\n" +
            "\t\t&lt;td&gt;&lt;span class=\"fit_member\"&gt;TotalMaxLicenses&lt;/span&gt;&lt;/td&gt;\n" +
            "\t\t&lt;td&gt;&lt;span class=\"fit_member\"&gt;OfflineAllowanceInMinutes&lt;/span&gt;&lt;/td&gt;\n" +
            "\t&lt;/tr&gt;\n" +
            "\t&lt;tr&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;1&lt;/td&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;App1&lt;/td&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;App1CompanyAProduct1&lt;/td&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;PerUser&lt;/td&gt;\n" +
            "\t\t&lt;td&gt;&lt;span class=\"fit_grey\"&gt; null&lt;/span&gt;&lt;/td&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;100&lt;/td&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;60&lt;/td&gt;\n" +
            "\t&lt;/tr&gt;\n" +
            "\t&lt;tr&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;2&lt;/td&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;App1&lt;/td&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;App1CompanyA1Product1&lt;/td&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;PerUser&lt;/td&gt;\n" +
            "\t\t&lt;td&gt;&lt;span class=\"fit_grey\"&gt; null&lt;/span&gt;&lt;/td&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;75&lt;/td&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;60&lt;/td&gt;\n" +
            "\t&lt;/tr&gt;\n" +
            "\t&lt;tr&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;3&lt;/td&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;App2&lt;/td&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;App2CompanyBProduct1&lt;/td&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;PerUser&lt;/td&gt;\n" +
            "\t\t&lt;td&gt;&lt;span class=\"fit_grey\"&gt; null&lt;/span&gt;&lt;/td&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;700&lt;/td&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;60&lt;/td&gt;\n" +
            "\t&lt;/tr&gt;\n" +
            "&lt;/table&gt;\n" +
            "&lt;/div&gt;\n" +
            "&lt;/div&gt;\n" +
            "&lt;br/&gt;&lt;b&gt;And that the following user information is setup:&lt;/b&gt;&lt;br/&gt;&lt;div class=\"collapsible\"&gt;&lt;ul&gt;&lt;li&gt;&lt;a href='#' class='expandall'&gt;Expand All&lt;/a&gt;&lt;/li&gt;&lt;li&gt;&lt;a href='#' class='collapseall'&gt;Collapse All&lt;/a&gt;&lt;/li&gt;&lt;/ul&gt;\n" +
            "\t&lt;p class=\"title\"&gt;Included page: &lt;a href=\"AppsCloudhouseCom.ApiDocumentation.SetupUsers\"&gt;.AppsCloudhouseCom.ApiDocumentation.SetupUsers&lt;/a&gt; &lt;a href=\"AppsCloudhouseCom.ApiDocumentation.SetupUsers?edit&amp;amp;redirectToReferer=true&amp;amp;redirectAction=\" class=\"edit\"&gt;(edit)&lt;/a&gt;&lt;/p&gt;\n" +
            "\t&lt;div&gt;&lt;div class=\"collapsible invisible\"&gt;&lt;ul&gt;&lt;li&gt;&lt;a href='#' class='expandall'&gt;Expand All&lt;/a&gt;&lt;/li&gt;&lt;li&gt;&lt;a href='#' class='collapseall'&gt;Collapse All&lt;/a&gt;&lt;/li&gt;&lt;/ul&gt;\n" +
            "\t&lt;p class=\"title\"&gt;Hide this section&lt;/p&gt;\n" +
            "\t&lt;div&gt;&lt;table&gt;\n" +
            "\t&lt;tr&gt;\n" +
            "\t\t&lt;td&gt;&lt;span class=\"fit_interpreter\"&gt;import&lt;/span&gt;&lt;/td&gt;\n" +
            "\t&lt;/tr&gt;\n" +
            "\t&lt;tr&gt;\n" +
            "\t\t&lt;td&gt;CloudhousePortalAcceptanceTests&lt;/td&gt;\n" +
            "\t&lt;/tr&gt;\n" +
            "&lt;/table&gt;\n" +
            "&lt;span class=\"meta\"&gt;variable defined: TopLevelCompanyApiToken=00000000-0000-0000-0000-000000000001&lt;/span&gt;\n" +
            "&lt;br/&gt;&lt;span class=\"meta\"&gt;variable defined: SubCompanyAToken=00000000-0000-0000-0000-000000000002&lt;/span&gt;\n" +
            "&lt;br/&gt;&lt;span class=\"meta\"&gt;variable defined: SubCompanyBToken=00000000-0000-0000-0000-000000000003&lt;/span&gt;\n" +
            "&lt;br/&gt;&lt;span class=\"meta\"&gt;variable defined: SubCompanyA1Token=00000000-0000-0000-0000-000000000004&lt;/span&gt;\n" +
            "&lt;br/&gt;&lt;span class=\"meta\"&gt;variable defined: SubCompanyB1Token=00000000-0000-0000-0000-000000000005&lt;/span&gt;\n" +
            "&lt;br/&gt;&lt;/div&gt;\n" +
            "&lt;/div&gt;\n" +
            "&lt;table&gt;\n" +
            "\t&lt;tr&gt;\n" +
            "\t\t&lt;td colspan=\"5\"&gt;&lt;span class=\"fit_interpreter\"&gt;Get User Data&lt;/span&gt;&lt;/td&gt;\n" +
            "\t&lt;/tr&gt;\n" +
            "\t&lt;tr&gt;\n" +
            "\t\t&lt;td&gt;&lt;span class=\"fit_member\"&gt;UserId&lt;/span&gt;&lt;/td&gt;\n" +
            "\t\t&lt;td&gt;&lt;span class=\"fit_member\"&gt;Username&lt;/span&gt;&lt;/td&gt;\n" +
            "\t\t&lt;td&gt;&lt;span class=\"fit_member\"&gt;CompanyId&lt;/span&gt;&lt;/td&gt;\n" +
            "\t\t&lt;td&gt;&lt;span class=\"fit_member\"&gt;Email&lt;/span&gt;&lt;/td&gt;\n" +
            "\t\t&lt;td&gt;&lt;span class=\"fit_member\"&gt;Role&lt;/span&gt;&lt;/td&gt;\n" +
            "\t&lt;/tr&gt;\n" +
            "\t&lt;tr&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;1&lt;/td&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;SubCompanyAUser1&lt;/td&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;2&lt;/td&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;SubCompanyAUser1@SubCompanyA.com&lt;/td&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;User&lt;/td&gt;\n" +
            "\t&lt;/tr&gt;\n" +
            "&lt;/table&gt;\n" +
            "&lt;/div&gt;\n" +
            "&lt;/div&gt;\n" +
            "&lt;br/&gt;&lt;br/&gt;&lt;hr/&gt;\n" +
            "&lt;br/&gt;&lt;br/&gt;&lt;br/&gt;&lt;b&gt;A GET operation on the Get Run Link url for a license agreement resource return a link to run the app (this will be XML encoded so you will need to decode it to use it) and the session Guid.&lt;/b&gt;&lt;br/&gt;&lt;i&gt;URL Format: https://api.cloudhouse.com/v2/Companies/{CompanyId}/LicenseAgreements/{LicenseAgreementId}/GetRunLink.{Format}?ApiToken={ApiToken}&amp;UserId={UserId}&amp;CurrentUnixTime={CurrentUnixTime}&amp;Os={OsType}&lt;/i&gt;&lt;br/&gt;&lt;table&gt;\n" +
            "\t&lt;tr&gt;\n" +
            "\t\t&lt;td colspan=\"3\"&gt;&lt;span class=\"fit_interpreter\"&gt;Test Api&lt;/span&gt;&lt;/td&gt;\n" +
            "\t&lt;/tr&gt;\n" +
            "\t&lt;tr&gt;\n" +
            "\t\t&lt;td&gt;&lt;span class=\"fit_member\"&gt;Input URL&lt;/span&gt;&lt;/td&gt;\n" +
            "\t\t&lt;td&gt;&lt;span class=\"fit_member\"&gt;HTTP Method&lt;/span&gt;&lt;/td&gt;\n" +
            "\t\t&lt;td&gt;&lt;span class=\"fit_member\"&gt;Output XML?&lt;/span&gt;&lt;/td&gt;\n" +
            "\t&lt;/tr&gt;\n" +
            "\t&lt;tr&gt;\n" +
            "\t\t&lt;td&gt;https://api.cloudhouse.com/v2/Companies/2/LicenseAgreements/1/GetRunLink.xml?ApiToken=00000000-0000-0000-0000-000000000002&amp;amp;UserId=1&amp;amp;CurrentUnixTime=1363964598&lt;/td&gt;\n" +
            "\t\t&lt;td&gt;GET&lt;/td&gt;\n" +
            "\t\t&lt;td class=\"pass\"&gt;&amp;lt;?xml version=\"1.0\" encoding=\"utf-8\"?&amp;gt;&lt;br/&gt;&amp;lt;InfoResultOfLicenseAgreementSession xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"&amp;gt;&lt;br/&gt;\t&amp;lt;Result&amp;gt;&lt;br/&gt;\t\t&amp;lt;Success&amp;gt;true&amp;lt;/Success&amp;gt;&lt;br/&gt;\t\t&amp;lt;Message&amp;gt;Success. Link is only valid for: 90 minutes&amp;lt;/Message&amp;gt;&lt;br/&gt;\t&amp;lt;/Result&amp;gt;&lt;br/&gt;\t&amp;lt;Href&amp;gt;http://localhost:9990/v2/Companies/2/LicenseAgreements/1/Run.xml?SessionGuid=10000000-0000-0000-0000-000000000000&amp;amp;amp;CurrentUnixTime=1363964598&amp;amp;amp;Os=win&amp;lt;/Href&amp;gt;&lt;br/&gt;\t&amp;lt;Info&amp;gt;&lt;br/&gt;\t\t&amp;lt;SessionGuid&amp;gt;10000000-0000-0000-0000-000000000000&amp;lt;/SessionGuid&amp;gt;&lt;br/&gt;\t&amp;lt;/Info&amp;gt;&lt;br/&gt;&amp;lt;/InfoResultOfLicenseAgreementSession&amp;gt;&lt;/td&gt;\n" +
            "\t&lt;/tr&gt;\n" +
            "&lt;/table&gt;\n" +
            "&lt;br/&gt;&lt;br/&gt;&lt;br/&gt;&lt;hr/&gt;\n" +
            "&lt;br/&gt;&lt;br/&gt;&lt;br/&gt;&lt;b&gt;A GET operation on the Run url for a license agreement resource with a valid session guid will run the application.&lt;/b&gt;&lt;br/&gt;&lt;i&gt;URL Format: https://api.cloudhouse.com/v2/Companies/{CompanyId}/LicenseAgreements/{LicenseAgreementId}/Run.{Format}?ApiToken={ApiToken}&amp;SessionGuid={SessionGuid}&amp;CurrentUnixTime={CurrentUnixTime}&amp;Os={OsType}&lt;/i&gt;&lt;br/&gt;&lt;table&gt;\n" +
            "\t&lt;tr&gt;\n" +
            "\t\t&lt;td colspan=\"3\"&gt;&lt;span class=\"fit_interpreter\"&gt;Run app for license agreement&lt;/span&gt;&lt;/td&gt;\n" +
            "\t&lt;/tr&gt;\n" +
            "\t&lt;tr&gt;\n" +
            "\t\t&lt;td&gt;&lt;span class=\"fit_member\"&gt;Input URL&lt;/span&gt;&lt;/td&gt;\n" +
            "\t\t&lt;td&gt;&lt;span class=\"fit_member\"&gt;HTTP Method&lt;/span&gt;&lt;/td&gt;\n" +
            "\t\t&lt;td&gt;&lt;span class=\"fit_member\"&gt;App Started?&lt;/span&gt;&lt;/td&gt;\n" +
            "\t&lt;/tr&gt;\n" +
            "\t&lt;tr&gt;\n" +
            "\t\t&lt;td&gt;https://api.cloudhouse.com/v2/Companies/2/LicenseAgreements/1/Run.xml?SessionGuid=10000000-0000-0000-0000-000000000000&amp;amp;CurrentUnixTime=1363964598&lt;/td&gt;\n" +
            "\t\t&lt;td&gt;GET&lt;/td&gt;\n" +
            "\t\t&lt;td class=\"error\"&gt;True&lt;hr /&gt;&lt;pre&gt;&lt;div class=\"fit_stacktrace\"&gt;System.Reflection.TargetInvocationException: Exception has been thrown by the target of an invocation. ---&gt; System.Reflection.TargetInvocationException: Exception has been thrown by the target of an invocation. ---&gt; System.Exception: An error occurred running: 16ff25ec-de7e-409c-b519-8f3bd6db7aaa.exe \n" +
            "System.ComponentModel.Win32Exception (0x80004005): The specified executable is not a valid application for this OS platform.\n" +
            "   at System.Diagnostics.Process.StartWithShellExecuteEx(ProcessStartInfo startInfo)\n" +
            "   at System.Diagnostics.Process.Start()\n" +
            "   at System.Diagnostics.Process.Start(ProcessStartInfo startInfo)\n" +
            "   at CloudhousePortal.AcceptanceTests.RunAppForLicenseAgreement.CheckAppStarted(String appName) in c:\\BuildAgent\\work\\e27dee74d5e82c31\\src\\AppStore\\CloudhousePortalTest.AcceptanceTests\\RunAppForLicenseAgreement.cs:line 36\n" +
            "   at CloudhousePortal.AcceptanceTests.RunAppForLicenseAgreement.CheckAppStarted(String appName) in c:\\BuildAgent\\work\\e27dee74d5e82c31\\src\\AppStore\\CloudhousePortalTest.AcceptanceTests\\RunAppForLicenseAgreement.cs:line 41\n" +
            "   at CloudhousePortal.AcceptanceTests.RunAppForLicenseAgreement.AppStarted() in c:\\BuildAgent\\work\\e27dee74d5e82c31\\src\\AppStore\\CloudhousePortalTest.AcceptanceTests\\RunAppForLicenseAgreement.cs:line 29\n" +
            "   --- End of inner exception stack trace ---\n" +
            "   at fitSharp.Machine.Model.TypedValue.ThrowExceptionIfNotValid()\n" +
            "   at fitSharp.Fit.Operators.CompareDefault.Compare(TypedValue actualValue, Tree`1 expected)\n" +
            "   at fitSharp.Machine.Engine.ProcessorBase`2.&lt;&gt;c__DisplayClass3.&lt;&gt;c__DisplayClass5.&lt;Compare&gt;b__2(CompareOperator`1 o)\n" +
            "   at fitSharp.Machine.Engine.Operators`2.Do[O](CanDoOperation`1 canDoOperation, DoOperation`1 doOperation)\n" +
            "   at fitSharp.Machine.Engine.ProcessorBase`2.&lt;&gt;c__DisplayClass3.&lt;Compare&gt;b__0(OperationLogging logging)\n" +
            "   at fitSharp.Machine.Engine.ProcessorBase`2.DoLoggedOperation[R](String startMessage, Func`2 operation)\n" +
            "   at fitSharp.Machine.Engine.ProcessorBase`2.Compare(TypedValue instance, Tree`1 parameters)\n" +
            "   at fitSharp.Fit.Operators.CheckDefault.Check(CellOperationValue actualValue, Tree`1 expectedCell)\n" +
            "   --- End of inner exception stack trace ---\n" +
            "   at System.RuntimeMethodHandle.InvokeMethod(Object target, Object[] arguments, Signature sig, Boolean constructor)\n" +
            "   at System.Reflection.RuntimeMethodInfo.UnsafeInvokeInternal(Object obj, Object[] parameters, Object[] arguments)\n" +
            "   at System.Reflection.RuntimeMethodInfo.Invoke(Object obj, BindingFlags invokeAttr, Binder binder, Object[] parameters, CultureInfo culture)\n" +
            "   at System.RuntimeType.InvokeMember(String name, BindingFlags bindingFlags, Binder binder, Object target, Object[] providedArgs, ParameterModifier[] modifiers, CultureInfo culture, String[] namedParams)\n" +
            "   at fitSharp.Machine.Engine.MethodMember.TryInvoke(Object[] parameters)\n" +
            "   at fitSharp.Machine.Engine.ReflectionMember.Invoke(Object[] parameters)\n" +
            "   at fitSharp.Machine.Engine.ProcessorBase`2.Operate[O](Object[] parameters)\n" +
            "   at fitSharp.Fit.Engine.CellProcessorExtension.Check(CellProcessor processor, Object systemUnderTest, Tree`1 memberName, Tree`1 parameters, Tree`1 expectedCell)\n" +
            "   at fitSharp.Fit.Service.CheckBinding.Do(Tree`1 cell)\n" +
            "   at fitSharp.Fit.Service.Binding.Do(Tree`1 cell)\n" +
            "   at fit.ColumnFixture.DoCell(Parse cell, Int32 column)&lt;/div&gt;&lt;/pre&gt;&lt;/td&gt;\n" +
            "\t&lt;/tr&gt;\n" +
            "&lt;/table&gt;\n" +
            "</content>\n" +
            "    <relativePageName>LicenseAgreementsRun</relativePageName>\n" +
            "    \n" +
            "    \n" +
            "    \n" +
            "  </result>\n" +
            "  \n" +
            "    <finalCounts>\n" +
            "    <right>0</right>\n" +
            "    <wrong>0</wrong>\n" +
            "    <ignores>0</ignores>\n" +
            "    <exceptions>1</exceptions>\n" +
            "  </finalCounts>\n" +
            "  <totalRunTimeInMillis>780</totalRunTimeInMillis>\n" +
            "  \n" +
            "</testResults>\n" +
            "\n";
}