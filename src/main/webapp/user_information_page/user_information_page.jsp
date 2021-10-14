<jsp:useBean id="current_user" scope="session" type="entity.User" />

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Message</title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="keywords" content="main page"/>
    <meta name="description" content="main page"/>
    <link rel="shortcut icon" type="image/png" href="../user_information_page/images/user_information_page_favicon.png" />
    <link rel="stylesheet" type="text/css" href="../user_information_page/user_information_page_stylesheet.css" />
</head>
<body>
    <table>
        <tr>
            <td class="cell_with_name_of_properties">Name: </td>
            <td class="cell_with_value_of_properties"><%= current_user.getName() %></td>
        </tr>
        <tr>
            <td class="cell_with_name_of_properties">Surname: </td>
            <td class="cell_with_value_of_properties"><%= current_user.getSurname() %></td>
        </tr>
        <tr>
            <td class="cell_with_name_of_properties">Patronymic: </td>
            <td class="cell_with_value_of_properties"><%= current_user.getPatronymic() %></td>
        </tr>
        <tr>
            <td class="cell_with_name_of_properties">Email: </td>
            <td class="cell_with_value_of_properties"><%= current_user.getEmail()%></td>
        </tr>
        <tr>
            <td class="cell_with_name_of_properties">Role: </td>
            <td class="cell_with_value_of_properties"><%= current_user.getRole().toString().toLowerCase(java.util.Locale.ROOT)%></td>
        </tr>
        <tr>
            <td class="cell_with_name_of_properties">Your ip: </td>
            <td class="cell_with_value_of_properties"><%= request.getRemoteAddr()%></td>
        </tr>
        <tr>
            <td class="cell_with_name_of_properties">Current date: </td>
            <td class="cell_with_value_of_properties"><%= new java.util.Date()%></td>
        </tr>
    </table>
</body>
</html>