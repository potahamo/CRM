
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Search Customers</title>
    </head>
    <body>
        <%@include file="layout/header.jsp" %>
        <h1>Customers</h1>
        <form action="searchCustomers" method="POST">
            <select name="field" id="field">
                <option value="NAME">name</option>
                <option value="PHONE">phone</option>
                <option value="EMAIL">email</option>
                <c:forEach var="field" items="${fields}">
                    <option value="${field}">${field}</option>
                </c:forEach>
            </select>
            <input type="text" name="searchValue" />
            <input type="submit" value="Search" />
        </form>
        <c:forEach var="customer" items="${customers}">
            <div style="border: 1px brown solid; padding: 10px;">
                Id: ${customer.id}<br />
                Name: ${customer.name}<br />
                Gender: ${(customer.gender == 1)? "Female" : "Male"}<br />
                DOB: ${customer.dob}<br />
                Created: ${customer.created}<br />
                <c:forEach var="field" items="${customer.fields}">
                    <span style="text-transform: capitalize">${field.name}</span>: ${field.fieldValue} <a href="doDeleteField?cId=${customer.id}&fId=${field.id}" style="font-size: small"> (delete field)</a> <br />
                </c:forEach>

                <c:forEach var="contact" items="${customer.contacts}">
                    <c:if test ="${contact.phone != null}">
                        Tel: ${contact.phone}
                    </c:if>
                    <c:if test ="${contact.email != null}">
                        Email: ${contact.email}
                    </c:if>
                    <a href="doDeleteContact?cId=${contact.id}" style="font-size: small"> (delete contact)</a> <br />
                </c:forEach>

                <form action="doAddField" method="POST">
                    <input type="hidden" name="cId" value="${customer.id}">
                    <input type="text" placeholder="custom field" name="name"/>
                    <input type="text" placeholder="value" name="value"/>
                    <input type="submit" value="Add Field" />
                </form>
                <form action="doAddContact" method="POST">
                    <input type="hidden" name="cId" value="${customer.id}">
                    <select name="type" id="field">
                        <option value="PHONE">phone</option>
                        <option value="EMAIL">email</option>
                    </select>
                    <input type="text" placeholder="value" name="value"/>
                    <input type="submit" value="Add Contact" />
                </form><br />
                <form action="editCustomer" method="POST">
                    <input type="hidden" name="cId" value="${customer.id}">
                    <input type="submit" value="Edit Customer">
                </form>
                <form action="doDeleteCustomer"  method="POST">
                    <input type="hidden" name="cId" value="${customer.id}">
                    <input type="submit" value="Delete Customer">
                </form>
            </div>
        </c:forEach>
    </body>
</html>