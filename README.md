# _.o0 JDevLog 0o._


## Summary
JDevLog is a Java application that reads log messages from the history log of your Svn or Git repository and produces a Syndication feed file. You can upload this file to a Web server and subscribe with any RSS reader to have up-to-date info on your project.

## Requirements

  * Java 1.6/1.7 JRE/JDK

## Install
Installation is pretty straightforward:
  * Download a copy from the [downloads](https://github.com/petarov/jdevlog/downloads) page.
  * Extract the Jar file and copy it somewhere on your file system.
  * Run the jar file using the java.exe and

## Command line parameters

## Usage

* Make sure you have the [%JAVA_HOME%](http://wso2.org/project/wsas/java/2.0/docs/setting-java-home.html) environment variable setup on your system.
* Alternatively, you could also use the full path to the **java.exe** executable, e.g., E:\Java\jdk1.6\bin.

### Command line parameters

    usage: jdevlog.jar [options]
     -h,--help             Display command line parameters.
     -m,--maxlog <arg>     Amount of (latest) log messages to fetch.
     -o,--out <arg>        Destination where to write the RSS file, e.g.,
                           C:\Tests\scm.xml
     -p,--password <arg>   Password for authentication.
     -s,--source <arg>     SCM location, e.g., for SVN:
                           http://svn.apache.org/repos/asf/spamassassin
     -t,--type <arg>       [svn|git]
     -u,--username <arg>   Username for authentication.
     -v,--verbose          Additional logging messages.

E:\Tools\jdevlog>

### Examples

* Use the following on the command line to bring up help information.
    %JAVA_HOME%\bin\java jdevlog.jar -h

* Use the following to generate RSS from the last 100 messages in your Subversion repository

    %JAVA_HOME%\bin\java -jar jdevlog.jar -source https://svn.myserver.net/repos/dev -out /var/www/devlog/my.svn.prj.rss.xml -username "iamalive" -password imdead -maxlog 100

* Use the following to generate RSS from the last 100 messages in your Git repository

    %JAVA_HOME%\bin\java -jar jdevlog.jar -type git -source /projects/jdevlog -out /var/www/devlog/my.git.prj.rss.xml -maxlog 100

## Documentation

  * Read HISTORY for latest release changes.
  * Read LICENSE for licensing topics.
  * Read CREDITS for contributed code and libraries.


Have fun ;)