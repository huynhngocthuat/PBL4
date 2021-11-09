

//Cấu hình websocket
//connect: event khi user login thành công
function connect()
{
    var socket = new SockJS('/gkz-stomp-endpoint');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, onError);
}

//Sử lí các tin nhắn về các user đang connect vào Websocket tương ứng.
var idConversation; // Lấy từ giao diện.
function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/public/' + idConversation, function (chatMessage){
        var message = JSON.parse(chatMessage.body); // Đối tượng Json
    });
}

// Thông báo lỗi
function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

//send: event gửi tin nhắn
//messageInput: Trường tin nhắn nhập vào
function send() {
    var messageContent = messageInput.value.trim();

    if(messageContent && stompClient) {
        var chatMessage = {
            sender: username,                       // phone number người gửi
            content: messageInput.value,            // nội dung của tin nhắn
            idConversation: idConversation.value    // id của phòng mà user đang nhập vào
        };

        stompClient.send("/app/send", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
}

// Unsubcriber websocket(Khi người dùng chuyển qua khung cho chuyện với người dùng khác).
function disconnect() {
    if (this.stompClient != null) {
        this.stompClient.disconnect();
    }

    this.setConnected(false);
    console.log('Disconnected!');
}
