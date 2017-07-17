<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<section>
    <table border="1" cellpadding="8" cellspacing="0">
        <tr>
            <th>DateTime</th>
            <th>description</th>
            <th>calories</th>
        </tr>
        <c:forEach items="${mealWithExceededs}" var="mealWithExceeded">
            <jsp:useBean id="mealWithExceeded" type="ru.javawebinar.topjava.model.MealWithExceed"/>
            <c:set var="cleanedDateTime" value="${fn:replace(mealWithExceeded.dateTime, 'T', ' ')}"/>
            <tr bgcolor="${mealWithExceeded.exceed ? 'red' : 'white'}">
                <td>${cleanedDateTime}</td>
                <td>${mealWithExceeded.description}</td>
                <td>${mealWithExceeded.calories}</td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>