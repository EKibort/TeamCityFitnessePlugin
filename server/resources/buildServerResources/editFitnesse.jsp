<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<l:settingsGroup title="Fitnesse options">
    <tr>
        <th>
            <label for="fitnesseJarPath">Fitnesse: </label>
        </th>
        <td>
            <props:textProperty name="fitnesseJarPath" />
            <span class="error" id="error_fitnesseJarPath"></span>
            <span class="smallNote">c:\FITNESSE\fitnesse.jar</span>
        </td>
    </tr>
    <tr>
        <th>
            <label for="fitnessePort">Port (-p): </label>
        </th>
        <td>
            <props:textProperty name="fitnessePort" />
            <span class="error" id="error_fitnessePort"></span>
            <span class="smallNote">8080 or range like 8080-8090</span>
        </td>
    </tr>
    <tr>
        <th>
            <label for="fitnesseTest">Test or suite relative url </label>
        </th>
        <td>
            <props:textProperty name="fitnesseTest" className="longField" expandable="true" />
            <span class="error" id="error_fitnesseTest"></span>
            <span class="smallNote">List of suites or tests delimited by semicolon e.g. TestSuite.SubSuite1?suite;TestSuite.SubSuite2?suite or TestSuite.SuperTest?test</span>
        </td>
    </tr>
	<tr class="advancedSetting">
        <th>
            <label for="fitnesseRoot">Test root folder: </label>
        </th>
        <td>
            <props:textProperty name="fitnesseRoot" />
            <span class="error" id="error_fitnesseRoot"></span>
			<span class="smallNote">The directory in which FitNesse will start the SLIM or Fit test runners. (-d option)</span>
        </td>
    </tr>
	<tr class="advancedSetting">
        <th>
            <label for="fitnessePages">Page root folder: </label>
        </th>
        <td>
            <props:textProperty name="fitnessePages" />
            <span class="error" id="error_fitnessePages"></span>
			<span class="smallNote">The directory in which FitNesse looks for top level wiki pages. (-r option)</span>
        </td>
    </tr>
	<tr class="advancedSetting">
        <th>
            <label for="fitnesseConfig">Config file: </label>
        </th>
        <td>
            <props:textProperty name="fitnesseConfig" />
            <span class="error" id="error_fitnesseConfig"></span>
			<span class="smallNote">Load an alternative configuration file. (-f option)</span>
        </td>
    </tr>
    <tr>
        <th>
            <label for="fitnesseTestRunParallel">Parallel execution </label>
        </th>
        <td>
            <props:checkboxProperty name="fitnesseTestRunParallel" />
			<label for="fitnesseTestRunParallel">Run tests or suites in parallel</label>
            <span class="error" id="error_fitnesseTestRunParallel"></span>
        </td>
    </tr>
</l:settingsGroup>
