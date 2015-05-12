#Epiphan WUI test automation#

##[1] About##

This repository contains the building blocks of
a basic web UI test automation framework for use by Epiphan QA.

###[1.1] What it is###
* A framework for automating repetitive tests of the web interface on the grid,
  recorder pro, or pearl.
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

Other projects used include:
* [ReportNG](http://reportng.uncommons.org) - For imporved Test Reporting
* [Apache Commons Exec](http://commons.apache.org/exec/) - For process
  execution from within java

##[3] Setup##
1. Install Maven
2. Install Jenkins
