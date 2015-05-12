#Epiphan WUI test automation#

##<a name="1-table-of-contents"></a>[1] Table of Contents##
* [[1] Table of Contents](#1-table-of-contents)
* [[2] About](#2_about)
  * [[2.1] Building Blocks](#21-building-blocks)
  * [[2.2] What it is](#22-what-it-is)
  * [[2.3] What it is not](#23-what-it-is-not)
* [[3] Setup](#3-setup)
  * [[3.1] Using Git with Jenkins](#31-using-git-with-jenkins)
    * [[3.1.1] Steps](#311-steps)
    * [[3.1.2] Notes](#312-notes)


##<a name="2-about"></a>[2] About##

This repository contains the building blocks of
a basic web UI test automation framework for use by Epiphan QA.
This framework is meant to provide a simple base upon which to build automated
tests for the WUI shared by the VGA Grid, Recorder Pro, and Pearl.

###<a name="21-building-blocks"></a>[2.1] Building Blocks###
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


###<a name="21-what-it-is"></a>[2.1] What it is###
* A framework for automating repetitive tests of the web interface on
  the VGA Grid, Recorder Pro, or Pearl.
* Capable of running tests against multiple browser/platform combinations
  without modifications to the test code.

###<a name="22-what-it-is-not"></a>[2.2] What it is not###
* A framework for testing **anything other than wui**

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
