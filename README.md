#Epiphan WUI test automation#

##<a name="1-table-of-contents"></a>[1] Table of Contents##
* [[1] Table of Contents](#1-table-of-contents)
* [[2] About](#2_about)
* [[3] Building Blocks](#3-building-blocks)
* [[4] Setup](#4-setup)
  * [[4.1] Using Git with Jenkins](#41-using-git-with-jenkins)
    * [[4.1.1] Steps](#411-steps)


##<a name="2-about"></a>[2] About##

This repository contains the building blocks of
a basic web UI test automation framework for use by Epiphan QA.

###<a name="21-what-it-is"></a>[2.1] What it is###
* A framework for automating repetitive tests of the web interface on
  the VGA Grid, Recorder Pro, or Pearl.
* Capable of running tests against multiple browser/platform combinations
  without modifications to the test code.

###<a name="22-what-it-isnt"></a>[2.2] What it isn't###
* A framework for testing **anything other than wui**

##<a name="3-building-blocks"></a>[3] Building Blocks##
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

##<a name="4-setup"></a>[4] Setup##

###<a name="41-using-git-with-jenkins"></a>[4.1] Using Git with Jenkins###
Using Jenkins CI as a build manager allows us to run the latest version of the
entire test system against (a) target(s) at specified times.
Depending on the configuration of Jenkins, the tests can be run periodically
(for example, every day at midnight), or when triggered by certain events
(for example, on every commit to a Git repository containing the test code).

Additionally, Jenkins has a wide range of plugins available for such things as
test reporting, and integration with Confluence / JIRA.

####<a name="411-steps"></a>[4.1.1] Steps####
1.  Install the latest version of Maven [version 3.3.3, at 2015-05-12]
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

####<a name="412-notes"></a>[4.1.2] Notes####
* When first setting up a Jenkins project, it may be necessary to point Jenkins
  to the local Maven installation you want to use.
* Feel free to play around with additional plugins for test reporting,
  Confluence/JIRA integration, etc... Jenkins can't push to Git, so you can't
  break anything important.
