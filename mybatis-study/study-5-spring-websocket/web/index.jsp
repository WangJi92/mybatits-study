
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>11</title>
  <script src="jquery.js"></script>
  <script src="sockjs.min.js"></script>
  <script>
      $(function () {
          socket = new SockJS("/socket-connect"); //创建连接
          socket.onopen = function() {
              socket.send("你好世界")
          };
          socket.onmessage = function(e) {
              //有消息传递到了
              console.log('message', e.data);
              alert(e.data)
          };
          socket.onclose = function() {
              console.log('close');
          };
          $("#send").click(function () {
              if(socket !=undefined){
                  socket.send("message");
              }else{
                  console.log("socket is null")
              }
          });
      });

  </script>
</head>
<body>
<div>
  <input type="button" value="Start" id="send" />
</div>
</body>
</html>