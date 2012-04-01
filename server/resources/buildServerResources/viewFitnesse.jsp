<%--
 * Copyright (c) 2008 Oliver Jones.  See LICENSE file for details.
--%>

<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<div class="parameter">
    Fitnesse path:
    <strong><props:displayValue name="fit.project" emptyValue="not specified"/></strong>
</div>
