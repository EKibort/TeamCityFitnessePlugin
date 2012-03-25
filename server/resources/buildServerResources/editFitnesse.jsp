<%--
 * Copyright (c) 2008 Oliver Jones.  See LICENSE file for details.
--%>

<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<l:settingsGroup title="Fitnesse options">
 
  <tr>   
      <th><label for="fit.project" Fitnesse path: <span class="mandatoryAsterix" title="Mandatory field">*</span></label></th>
    <td>
        <props:textProperty name="fit.project" className="longField" />
    </td>
  </tr>
</l:settingsGroup>
