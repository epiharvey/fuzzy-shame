#Epiphan WUI test automation#

##[1] About##

This repository contains the building blocks of
a basic web UI test automation framework for use by Epiphan QA.

###[1.1] What it is###
* A framework for automating repetitive tests of the web interface on
  the VGA Grid, Recorder Pro, or Pearl.
* Capable of running tests against multiple browser/platform combinations
  without modifications to the test code.

###[1.2] What it isn't###
* A framework for testing **anything other than wui**

##[2] Building Blocks##
The Cornerstones of the UI automation framework are:
* [Selenium](http://seleniumhq.org) - For WUI interaction
* [TestNG](http://testng.org) - For test automation and reporting
* [Apache Maven](http://maven.apache.org) - As the build system
* [Jenkins CI](http://jenkins-ci.org) - For easy build scheduling and pretty
  test reports

Other components include:
* [ReportNG](http://reportng.uncommons.org) - For imporved Test Reporting
* [Apache Commons Exec](http://commons.apache.org/exec/) - For process
  execution from within java
* [GitHub](http://github.org) - For SCM, and to make the system resilient to
  multiple simultaneous users writing tests

##[3] Setup##

###[3.1] Using Jenkins###
Using Jenkins CI as a build manager allows us to run the latest version of the
entire test system against (a) target(s) at specified times.
Depending on the configuration of Jenkins, the tests can be run periodically
(for example, every day at midnight), or when triggered by certain events
(for example, on every commit to a Git repository containing the test code).

Additionally, Jenkins has a wide range of plugins available for such things as
test reporting, and integration with Confluence / JIRA.

The steps to set up Jenkins with our test automation framework are as follows:

1.  Install Maven
2.  Install Jenkins
3.  In Jenkins, under `Manage Jenkins`>`Manage Plugins`>`Available`
    select `Git Client Plugin` and click
    `Download now and install after restart`
4.  Create a `New Item` and give it a suitable name, select `Maven Project` as
    the project type and press `OK`
5.  By default, Jenkins keeps all build artifacts until the end of time.
    I recommend that you select `Discard Old Builds` unless you have a very
    good reason for doing otherwise, setting an appropriate time to live, or
    number of builds to keep.
