
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>11</title>
  <script src="jquery.js"></script>
  <script src="sockjs.min.js"></script>
  <script src="stomp.min.js"></script>
  <script>
    var socket = new SockJS("/websocket");
    var stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        stompClient.subscribe('/topic/helloworld', function(data) {
            $("#ret").text(data.body);
        });
    });
    $(function () {
        $("#send").onclick = function () {
            stompClient.send("/app/hello",{},{
                msg : "广播推送"
            });
        };
    })

  </script>
</head>
<body>
<div>
  <input type="button" value="Start" id="send" />
</div>
</body>
</html>