#Epiphan WUI test automation#

##<a name="1-table-of-contents"></a>[1] Table of Contents##
* [[1] Table of Contents](#1-table-of-contents)
* [[2] About](#2-about)
  * [[2.1] Building Blocks](#21-building-blocks)
  * [[2.2] Details](#22-details)
* [[3] Setup](#3-setup)
  * [[3.1] Using Git with Jenkins](#31-using-git-with-jenkins)
    * [[3.1.1] Steps](#311-steps)
    * [[3.1.2] Notes](#312-notes)


##<a name="2-about"></a>[2] About##

This repository contains the building blocks of
a basic web UI test automation framework for use by Epiphan QA.  
This framework is meant to provide a simple base upon which to build automated
tests for the WUI shared by the VGA Grid, Recorder Pro, and Pearl.
Additionally, this framework is meant to overcome some problems with the
default Selenium Grid2 system.  
This is **not** a framework for testing **anything other than WUI**.

###<a name="21-building-blocks"></a>[2.1] Building Blocks###
The Cornerstones of the UI automation framework are:
* [Selenium](http://seleniumhq.org) - A  *Web User Interface* (WUI) automation
  framework which allows Java programs to mimic the interactions of a human
  with a web page. Selenium implements the W3C
  [WebDriver](http://www.w3.org/TR/webdriver) spec as it's API. Selenium also
  provides functionality called Selenium Grid2, which allows multiple machines
  to be networked together in order to run tests on multiple OS/browser
  combinations.
* [TestNG](http://testng.org) - A Java framework for unit and
  integration testing similar to JUnit. TestNG allows the configuration of
  tests into 'suites' and 'groups' which provides a great deal of flexibility
  with regards to which tests to run, and when. TestNG also supports the
  the passing of parameters to tests at runtime through an xml configuration
  file.
* [Apache Maven](http://maven.apache.org) - A flexible Java build system with
  configurable dependency management.
* [Jenkins CI](http://jenkins-ci.org) - A popular *Continuous Integration* (CI)
  system used to schedule automatic builds and to aggregate build and test
  reports.

Other components include:
* [ReportNG](http://reportng.uncommons.org) - Improves on TestNG's default
  HTML and XML reports.
* [Apache Commons Exec](http://commons.apache.org/exec/) - An Apache Commons
  library for process execution from within Java programs. Commons Exec allows
  Java programs to spawn processes, and to continuously monitor the output of
  those processes while performing other tasks.
* [GitHub](http://github.org) - A popular Git repository host.


###<a name="22-details"></a>[2.2] Details###

##<a name="3-setup"></a>[3] Setup##

###<a name="31-using-git-with-jenkins"></a>[3.1] Using Git with Jenkins###
Using Jenkins CI as a build manager allows us to run the latest version of the
entire test system against (a) target(s) at specified times.
Depending on the configuration of Jenkins, the tests can be run periodically
(for example, every day at midnight), or when triggered by certain events
(for example, on every commit to a Git repository containing the test code).
This setup is ideal if you want to run tests on a separate computer in order to
keep your development machine free for other tasks while tests are running.  
Additionally, Jenkins offers a convenient web based interface for use on
headless machines, and has a wide range of plugins available for such things as
test reporting, and integration with Confluence / JIRA.

####<a name="311-steps"></a>[3.1.1] Steps####
1.  Install the latest version of Maven [version 3.3.3 at 2015-05-12]
2.  Install the latest version of Git for your OS [version 1.9.1 at 2015-05-12]
3.  Install the latest version of Jenkins [version 1.613 at 2015-05-12]
4.  In Jenkins, under `Manage Jenkins`>`Manage Plugins`>`Available`
    select `Git Client Plugin` and click
    `Download now and install after restart`
5.  Create a `New Item` and give it a suitable name, select `Maven Project` as
    the project type and press `OK`
6.  By default, Jenkins keeps all build artifacts until the end of time.
    I recommend that you select `Discard Old Builds` (unless you have a very
    good reason for doing otherwise), setting an appropriate time to live, or
    number of builds to keep.
7.  Under `Source Code Management` select `Git`  
    The repostiory location is `git://github.com/epiharvey/fuzzy-shame`  
    Go to QA to get the repository cridentials.
8.  Run a build to ensure that everything is set up correctly.

####<a name="312-notes"></a>[3.1.2] Notes####
* When first setting up a Jenkins project, it may be necessary to point Jenkins
  to the local Maven installation you want to use.
* Feel free to play around with additional plugins for test reporting,
  Confluence/JIRA integration, etc... Jenkins can't push to Git, so you can't
  break anything important.
* If you don't want to compile the grid module on every build, point Jenkins
  to `/fuzzy-shame/tests/pom.xml` instead of `/fuzzy-shame/pom.xml`

###<a name="32-using-git-with-maven"></a>[3.2] Using Git with Maven###
If you do
