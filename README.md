# Messaging Platform Selenium E2E

End-to-end UI tests for a web messaging platform, written in Java with Selenium WebDriver, TestNG and Maven, using the Page Object Model.

## What it covers

- Login
- SMS and WhatsApp campaign creation
- Summary and Advanced reports for SMS, WhatsApp, RCS and Truecaller, in both the panel and API views
- Account MIS reports for the same four channels

The report tests walk through every "view by" option on a page and save each table as an HTML file with a screenshot.

## Project layout

```
src/main/java/com/messaging/platform
  base/         BasePage (waits, clicks, typing) and BaseTest (browser set-up)
  pages/        page objects, grouped by login, campaign, mis, users and components
  utils/        config, Excel and JSON readers, screenshots, HTML table export
src/test/java/com/messaging/platform
  dataprovider/ TestNG data providers
  tests/        test classes
```

## Prerequisites

- Java 21
- Maven 3.8 or later
- Chrome, Firefox or Edge

Selenium Manager downloads the matching browser driver on first run.

## Set-up

1. Clone the repository.

   ```bash
   git clone https://github.com/ajaajahmad/messaging-platform-selenium-e2e.git
   cd messaging-platform-selenium-e2e
   ```

2. Create the config file and fill in the application URL, browser and the list and template names used by the campaign tests.

   ```bash
   cp src/main/resources/config.properties.example src/main/resources/config.properties
   ```

3. Add the login data. Create `src/test/resources/testdata/userdata.xlsx` with a header row followed by one row per user, username in the first column and password in the second. The sheet name must match `excel_sheet_name` in the config file.

The config file and all test data files are gitignored, so credentials stay on your machine.

## Running

Run the default suite in `testng.xml`:

```bash
mvn test
```

Run every test class:

```bash
mvn test -DsuiteXmlFile=full_suite.xml
```

Run the SMS campaign suite:

```bash
mvn test -DsuiteXmlFile=sms_campaign.xml
```

Each test class opens its own browser, logs in and logs out. The tests run against a live application in a visible browser window, and the campaign test sends a real campaign to the configured list.

## Output

- `reports/` holds the exported HTML tables, one folder per report page
- `screenshots/` holds the screenshots
- `screenshots/failures/` holds a screenshot of the browser for each failed test
- `logs/automation.log` holds the Log4j output: the start and result of every test, plus each click, typed field and navigation. Typed text and login data are never logged.

## Licence

MIT. See [LICENSE](LICENSE).
