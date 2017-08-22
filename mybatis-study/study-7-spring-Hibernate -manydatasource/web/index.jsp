
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>11</title>
    <script src="/jquery.js"></script>
    <script>
        $(function () {
            var data={};
            data.name="111";
            data.age=1;
            data.id=1;
            var url = "/error";
            var config = {
                url: url,
                data: data,
                success: function (data) {
                    alert(data);
                }
            };
            $.ajax(config);
        });
    </script>
</head>
<body>
<div>
 111111
</div>
</body>
</html>