# Welcome to the LFRGS Test Framework from SDLC repository
 
# Overview
This project contains __samples__ based on Liferay GS Selenium Commons Framework. In this framework you will find modules with examples functional (both with BDD and TDD), integration tests and JS Test.

# Functional Tests
Functional tests are used to demonstrate tasks performed by the end user. In this case, web elements and actions are tested. No code will be directly tested.

##Functional Tests with BDD
[/test/functional-tests-BDD]
BDD (Behavior Driven Development) is an approach based on TDD. It uses a shared language, easier to understand by different stakeholders (with tech and non-tech knowledge).


### Pages
This package contains classes to identify elements on each page. It's based on Page objects pattern.

__1. LoginPage:__ It holds the locators of each element used to login and the methods related to this action, such as fillPasswordField.
   
__2. WelcomePage:__ It has a method to verify if the username is presented on welcome page.


### Steps

__1. Background steps definition__
Defines the "Background steps definition", necessary to be used for the future others steps.

__2. LoginStepDefinition__
Defines the steps necessary for a user to perform login. 

__1. SettingsStartingEndingSteps__
Defines the  actions needed to be performed before and after the executions of scenarios. 
e.g.: maximize the browser window before a scenario and take a screenshot if a scenario fails.

### Utils

__ 1. RunTest __
Defines that the tests are going to be run with Cucumber and the options related to it (e.g.: path to features file.).

### Resources

__ 1. Login.feature __
This file contains the description of a feature and the scenarios related to it
e.g.: for a feature 'login' we have two scenarios (positive and negative)


##Functional Tests with TDD
[/functional-tests-TDD]

### Pages
This package contains classes to identify elements on each page. It's based on Page objects pattern.

__LoginPage:__ It holds the locators of each element used to login and the methods related to this action, such as fillPasswordField.

__WelcomePage:__ It has a method to verify if the username is presented on welcome page.

__Tests__
This package is responsible for organizing the tests.

LoginTest: It has examples of positive and negative scenarios of login. e.g.: login with success/login with fail.


### Tests Suite
__1. RunAllTests:__ It is used to define test suites, e.g.: sanity test suite.
Test classes should be added on *@Suite.SuiteClasses(LoginTest.class)*

### Utils

__1. CommonMethods:__ It has methods that facilitate the interaction with the elements of a page. 
e.g.: wait until an element is visible to click on it.

__2. FunctionalTest:__ Methods to record test evidences, such as take a screenshot.

__3. Retry Test Execution:__ In some occasions tests may run too fast, resulting fails in tests that have not been executed yet (elements not loaded, for example). In this cases, tests that have failed should be executed again. 

# JS Unit Tests

## Jasmine
Jasmine is a framework to perform Javascript unit tests. There are two important concepts in Jasmine: Suite and Spec
__Spec__ is a test case from a suite. 
__Suite__ is a set of test cases used to verify the behavior of a JS code (e. g. a specific function). 

## Jest
Jest folder contains 'src' which contains the classes to be testes. Tests folder has the test codes.

## Mocha-Karma
Mocha-karma structure is similar to Jest. It contains 'src' which contains the classes to be testes. __tests__ folder has the test codes.

#Integration Tests
Integration tests are used to test the interaction of two or more pieces of code. It tests the code directly.
[/integration-tests]

### Api
__CreatePageByRestAPI:__ This class creates a new page on portal.

__DeletePageByRestAPI:__ It deletes a page on portal.

__GetCompanyByRestAPI:__ Defines the main instance (company)

__GetGroupByRestAPI:__ Define the sites related to the instance defined previously.

__IPageParametersByRestAPI:__ Interface used to mock the company and group.

### Tests
__RunTest:__ It contains methods to create a new page, a duplicated page and delete a page by Rest API.

### Utils
__CommonMethods:__ Methods that are common to other packages (e.g.: api and tests), such as: definition of the privacy of a page.

# Unit Tests
[test/unit-test] 
Unit tests verify and validate functionality of classes and methods in isolation by “mocking” external dependencies. One of the most widely-used tools for test driven development on the Java platform is JUnit. You can use the JUnit framework to write unit tests for Liferay Portal applications. 
JUnit integrates with build environments like Gradle. JUnit plugins are available in IDEs such as Eclipse, IntelliJ, and NetBeans. And of course, Liferay Workspace supports running JUnit tests.

## How to structure your Unit Test Packages

#### Mocking Classes
When a developer unit tests a component, the objective is to isolate that component as much as possible. Adding these components means adding pieces of the Liferay Platform to your test, thus not isolating the component.

### Tests
__RunTest:__ It contains classes and methods to test each part of component.

#### How to improve your unit test code
1.[F.I.R.S.T](https://dzone.com/articles/writing-your-first-unit-tests): Better way to code, such as: simple, clean, cleaner, robust, etc. 
2.[Java Code Coverage:](https://www.eclipse.org/community/eclipse_newsletter/2015/august/article1.php) It's a technique which provide a view of how the code is coverage.

### References
Check more practice informations on [Unit Test - Liferay Portal](https://dev.liferay.com/develop/tutorials/-/knowledge_base/7-0/unit-testing-with-junit).

# How to use

## BDD Functional test

### Requirement
1. The bundle must be started

### The magic happens...
1. `blade gw setupCucumberTestWithGoogleChrome` or `setupCucumberTestWithFirefox`- To create the Functional Tests properties and driver (GoogleChrome currently the driver is 2.41 and Firefox currently the driver is 0.20.1)
2. `blade gw runCucumberTest` - To execute the all BDD Functional Tests

## TDD Functional test

### Requirement
1. The bundle must be started

### The magic happens...
1. `blade gw setupFunctionalTest` or `setupFunctionalTestWithFirefox`- To create the Functional Tests properties and driver (GoogleChrome currently the driver is 2.41 and Firefox currently the driver is 0.20.1)
2. `blade gw runFunctionalTest` - To execute the all TDD Functional Tests
3. `blade gw functionalTestClass -PclassToBeTested=LoginTest` - To execute only one class of the TDD Functional Tests

## Integration test

### Requirement
1. The bundle must be started. (to that examples can be performed).

### The magic happens ...
1. `blade gw setupIntegrationTest` - To create the Integration Tests properties
2. `blade gw runTestIntegration` - To execute the all Integration Tests

## JavaScript Unit Tests

__USING JASMINE:__
1. Go to /modules/test/js-unit-tests/jasmine
2. Run npm i
3. Perform npm run test command to run all Java Script tests.

__USING KARMA MOCHA:__
1. Go to /modules/test/js-unit-tests/mocha-karma
2. Run npm i
3. Perform npm run test command to run

__USING JEST:__
1. Go to modules/test/js-unit-tests/jest
2. Run npm run clean to remove any trash folder and install dependencies.
3. Perform npm run test command to run

## Unit Test

### Running JUnit Tests

##### Command Line 
Gradle commands execute module unit tests:

1. `blade gw unitTest` - To run all Unit Tests created.
