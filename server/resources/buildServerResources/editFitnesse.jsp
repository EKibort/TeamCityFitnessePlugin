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
            <label for="fitnessePort">Port: </label>
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
            <props:textProperty name="fitnesseTest" className="longField"/>
            <span class="error" id="error_fitnesseTest"></span>
            <span class="smallNote">TestSuite.SubSuite?suite,TestSuite.SubSuite?suite;TestSuite.SubSuite?suite,TestSuite.SubSuite?suite or TestSuite.SuperTest?test</span>
        </td>
    </tr>
</l:settingsGroup>
